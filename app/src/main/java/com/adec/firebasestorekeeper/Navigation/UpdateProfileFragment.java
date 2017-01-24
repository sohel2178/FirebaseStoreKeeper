package com.adec.firebasestorekeeper.Navigation;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.DialogFragment.UploadImageDialog;
import com.adec.firebasestorekeeper.Interface.NavDrawerListener;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.MyFirebaseStorage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfileFragment extends Fragment implements View.OnClickListener{

    private static final int PICTURE_REQUEST=14;

    private MyEditText etName,etEmail,etContact,etAddress;
    private CircleImageView ivProfileImage;
    private IconTextView itvUpdate;
    private Button btnUpdate;

    private User currentUser;

    private OnSuccessListener mySuccessListener;

    private Bitmap imageBitmap;
    private Uri imageUri;

    private MyDatabaseReference myDatabaseReference;

    private String name,email,contact,address;

    private ProgressDialog dialog;

    private UserLocalStore userLocalStore;

    private NavDrawerListener navDrawerListener;


    public UpdateProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        myDatabaseReference = new MyDatabaseReference();

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");

        navDrawerListener= (NavDrawerListener) getActivity();



        mySuccessListener = new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                UploadTask.TaskSnapshot snapshot = (UploadTask.TaskSnapshot) o;
                String url = String.valueOf(snapshot.getDownloadUrl());

                currentUser.setImage_url(url);
                userLocalStore.setUser(currentUser);

                navDrawerListener.setProfileImage(url);

                myDatabaseReference.getUserRef().child(currentUser.getId()).child("image_url").setValue(url);

                if(currentUser.getUser_type()==1){
                    myDatabaseReference.getEmployeeReference(currentUser.getParent_id()).child(currentUser.getId()).child("image_url").setValue(url);
                }

                dialog.dismiss();

                getFragmentManager().beginTransaction().replace(R.id.main_container,new ProfileFragment())
                        .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).commit();


                Toast.makeText(getActivity(), "User Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        
        initView(view);
        setData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setData() {
        if(!currentUser.getImage_url().equals("")){
            Picasso.with(getActivity())
                    .load(currentUser.getImage_url())
                    .into(ivProfileImage);
        }else{
            ivProfileImage.setImageResource(R.drawable.placeholder);
        }

        etName.setText(currentUser.getName());
        etEmail.setText(currentUser.getEmail());
        etContact.setText(currentUser.getContact());
        etAddress.setText(currentUser.getAddress());
    }

    private void initView(View view) {
        etName = (MyEditText) view.findViewById(R.id.name);
        etEmail = (MyEditText) view.findViewById(R.id.email);
        etContact = (MyEditText) view.findViewById(R.id.contact);
        etAddress = (MyEditText) view.findViewById(R.id.address);

        ivProfileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        itvUpdate = (IconTextView) view.findViewById(R.id.camera);
        btnUpdate = (Button) view.findViewById(R.id.update);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        itvUpdate.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        getValueFormTExtField();

        switch (view.getId()){
            case R.id.camera:

                UploadImageDialog uploadImageDialog = new UploadImageDialog();
                uploadImageDialog.setTargetFragment(this,PICTURE_REQUEST);
                uploadImageDialog.show(getFragmentManager(), Constant.DEFAULAT_FRAGMENT_TAG);


                break;

            case R.id.update:
                dialog.show();
                //Todo update User at Local Data Store
                updateUserAtLocalStore();

                //Todo Upload image to the reference
                if(imageBitmap==null){
                    updateUserNode();
                    if(currentUser.getUser_type()==1){
                        updateEmployeeNode();
                    }

                    dialog.dismiss();
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new ProfileFragment())
                            .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).commit();

                }else{
                    uploadImage();

                    updateUserNode();
                    if(currentUser.getUser_type()==1){
                        updateEmployeeNode();
                    }
                }

                //Todo Update user at User Node.


                //Todo if UserType==0, update user node only else update employee node alse
                if(currentUser.getUser_type()==1){

                }







                break;

        }
    }

    private void getValueFormTExtField(){
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        contact = etContact.getText().toString().trim();
        address = etAddress.getText().toString().trim();
    }

    private void updateUserAtLocalStore(){
        currentUser.setName(name);
        currentUser.setEmail(email);
        currentUser.setContact(contact);
        currentUser.setAddress(address);

        userLocalStore.setUser(currentUser);

        navDrawerListener.setName(name);
        navDrawerListener.setEmail(email);


    }

    private void updateUserNode(){
        myDatabaseReference.getUserRef().child(currentUser.getId()).child("name").setValue(name);
        myDatabaseReference.getUserRef().child(currentUser.getId()).child("email").setValue(email);
        myDatabaseReference.getUserRef().child(currentUser.getId()).child("contact").setValue(contact);
        myDatabaseReference.getUserRef().child(currentUser.getId()).child("address").setValue(address);
    }

    private void updateEmployeeNode(){
        myDatabaseReference.getEmployeeReference(currentUser.getParent_id()).child(currentUser.getId()).child("name").setValue(name);
        myDatabaseReference.getEmployeeReference(currentUser.getParent_id()).child(currentUser.getId()).child("email").setValue(email);
        myDatabaseReference.getEmployeeReference(currentUser.getParent_id()).child(currentUser.getId()).child("contact").setValue(contact);
        myDatabaseReference.getEmployeeReference(currentUser.getParent_id()).child(currentUser.getId()).child("address").setValue(address);
    }


    private void uploadImage(){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        MyFirebaseStorage myFirebaseStorage = new MyFirebaseStorage();
        dialog.show();
        UploadTask uploadTask =myFirebaseStorage.getUserImageReference().child(currentUser.getId()).putBytes(data);
        uploadTask.addOnSuccessListener(mySuccessListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PICTURE_REQUEST:
                if(resultCode== Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String path = bundle.getString(Constant.URI);


                    File file = new File(path);

                    imageUri = Uri.fromFile(file);

                    imageBitmap = MyUtils.resizeBitmap(path,120,120);

                   /* Picasso.with(getActivity())
                            .load(imageUri)
                            .into(ivProfileImage);*/

                    ivProfileImage.setImageBitmap(imageBitmap);

                }
                break;
        }
    }
}
