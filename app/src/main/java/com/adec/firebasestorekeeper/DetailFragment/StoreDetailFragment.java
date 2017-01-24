package com.adec.firebasestorekeeper.DetailFragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.DialogFragment.UploadImageDialog;
import com.adec.firebasestorekeeper.Model.Store;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Navigation.AllStoreFragment;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.MyFirebaseStorage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreDetailFragment extends Fragment implements View.OnClickListener{
    private static final int PICTURE_REQUEST=14;
    private ActionBar actionBar;

    private ImageView ivClose,ivTick,ivImage;
    private TextView tvChangeImage;
    private MyEditText etName,etContact,etAddress;

    private Bitmap bitmap;

    private Store currentStore;

    private DatabaseReference myStoreRef;

    private User currentUser;

    private OnSuccessListener mySuccessListener = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            UploadTask.TaskSnapshot snapshot = (UploadTask.TaskSnapshot) o;
            String url = String.valueOf(snapshot.getDownloadUrl());
            doCommonTask(currentStore.getName(),currentStore.getContact(),currentStore.getAddress(),url);

            getFragmentManager().popBackStack();
            getFragmentManager().beginTransaction().replace(R.id.main_container,new AllStoreFragment())
                    .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).commit();


        }
    };


    public StoreDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(getArguments()!=null){
            currentStore = (Store) getArguments().getSerializable(Constant.STORE);
        }

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_store_detail, container, false);
        initView(view);

        if(currentStore!=null){
            setText();
        }

        MyUtils.hideKey(view);
        return view;
    }

    private void setText() {
        etName.setText(currentStore.getName());
        etContact.setText(currentStore.getContact());
        etAddress.setText(currentStore.getAddress());

        if(currentStore.getStore_image()!=null){
            if(!currentStore.getStore_image().equals("")){
                Picasso.with(getActivity())
                        .load(currentStore.getStore_image())
                        .into(ivImage);
            }
        }
    }

    private void initView(View view) {
        ivClose = (ImageView) view.findViewById(R.id.close);
        ivTick = (ImageView) view.findViewById(R.id.tick);
        ivImage = (ImageView) view.findViewById(R.id.image);

        tvChangeImage = (TextView) view.findViewById(R.id.change_image);
        etName = (MyEditText) view.findViewById(R.id.store_name);
        etContact = (MyEditText) view.findViewById(R.id.store_contact);
        etAddress = (MyEditText) view.findViewById(R.id.store_address);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvChangeImage.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivTick.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.hide();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        actionBar.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_image:
                UploadImageDialog uploadImageDialog = new UploadImageDialog();
                uploadImageDialog.setTargetFragment(this,PICTURE_REQUEST);
                uploadImageDialog.show(getFragmentManager(), Constant.DEFAULAT_FRAGMENT_TAG);
                break;

            case R.id.close:
                getFragmentManager().popBackStack();
                break;

            case R.id.tick:
                MyUtils.hideKey(v);
                
                updateStoreOnly();
                if(bitmap!=null){
                    updateImage();
                }
                break;
        }
    }

    private void updateImage(){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        MyFirebaseStorage myFirebaseStorage = new MyFirebaseStorage();

        UploadTask uploadTask =myFirebaseStorage.getStoreImageReference().child(currentUser.getId()).child(currentStore.getId()).putBytes(data);
        uploadTask.addOnSuccessListener(mySuccessListener);

    }

    private void updateStoreOnly(){
        String storeName= etName.getText().toString().trim();
        String storeContact= etContact.getText().toString().trim();
        String storeAddress= etAddress.getText().toString().trim();

        doCommonTask(storeName,storeContact,storeAddress,"");

        if(bitmap==null){
            getFragmentManager().popBackStack();
            getFragmentManager().beginTransaction().replace(R.id.main_container,new AllStoreFragment())
                    .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).commit();
        }
    }

    private void doCommonTask(String name,String contact, String address, String url){
        currentStore.setName(name);
        currentStore.setContact(contact);
        currentStore.setAddress(address);
        currentStore.setStore_image(url);

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        myDatabaseReference.getStoreRef(currentUser.getId()).child(currentStore.getId()).setValue(currentStore);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PICTURE_REQUEST:
                if(resultCode== Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();

                    String path = bundle.getString(Constant.URI);

                    bitmap = MyUtils.resizeBitmap(path,150,150);

                    ivImage.setImageBitmap(bitmap);
                }
                break;
        }
    }
}
