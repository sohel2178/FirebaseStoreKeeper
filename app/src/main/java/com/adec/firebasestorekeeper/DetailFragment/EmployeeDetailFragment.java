package com.adec.firebasestorekeeper.DetailFragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.DialogFragment.UploadImageDialog;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Navigation.AllManagerFragment;
import com.adec.firebasestorekeeper.Navigation.AllStoreFragment;
import com.adec.firebasestorekeeper.Navigation.Employees;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.MyFirebaseStorage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeDetailFragment extends Fragment implements View.OnClickListener{
    private static final int PICTURE_REQUEST=14;

    private ActionBar actionBar;

    private User currentEmployee;
    private User currentUser;

    private ImageView ivClose,ivTick;
    private CircleImageView image;
    private TextView tvChangeImage;

    private MyEditText etName,etEmail,etContact,etAddress,etSalary,etReferredBy;

    private Bitmap imageBitmap;


    private OnSuccessListener mySuccessListener = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            UploadTask.TaskSnapshot snapshot = (UploadTask.TaskSnapshot) o;
            String url = String.valueOf(snapshot.getDownloadUrl());

            MyDatabaseReference myDatabaseReference = new MyDatabaseReference();

            if(currentUser.getUser_type()==0){
                myDatabaseReference.getEmployeeReference(currentUser.getId()).child(currentEmployee.getId()).child("image_url").setValue(url);
            }else if(currentUser.getUser_type()==1){
                myDatabaseReference.getEmployeeReference(currentUser.getParent_id()).child(currentEmployee.getId()).child("image_url").setValue(url);
            }

            getFragmentManager().popBackStack();

            getFragmentManager().beginTransaction().replace(R.id.main_container,new Employees())
                    .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).commit();




        }
    };


    public EmployeeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        if(getArguments()!= null){
            currentEmployee = (User) getArguments().getSerializable(Constant.USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_employee_detail, container, false);
        initView(view);

        /*if(currentEmployee.getUser_type()==1){
            etName.setFocusable(false);
            etEmail.setFocusable(false);
            etContact.setFocusable(false);
            etAddress.setFocusable(false);
            etReferredBy.setFocusable(false);

            tvChangeImage.setVisibility(View.GONE);
        }*/

        if(currentEmployee!= null){
            setData();
        }
        return view;
    }

    private void initView(View view) {

        ivClose = (ImageView) view.findViewById(R.id.close);
        ivTick = (ImageView) view.findViewById(R.id.tick);

        image = (CircleImageView) view.findViewById(R.id.image);

        tvChangeImage = (TextView) view.findViewById(R.id.change_image);

        etName = (MyEditText) view.findViewById(R.id.name);
        etEmail = (MyEditText) view.findViewById(R.id.email);
        etContact = (MyEditText) view.findViewById(R.id.contact);
        etAddress = (MyEditText) view.findViewById(R.id.address);
        etSalary = (MyEditText) view.findViewById(R.id.salary);
        etReferredBy = (MyEditText) view.findViewById(R.id.referred_by);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ivClose.setOnClickListener(this);
        ivTick.setOnClickListener(this);
        tvChangeImage.setOnClickListener(this);
    }

    private void setData(){

        etName.setText(currentEmployee.getName());
        etEmail.setText(currentEmployee.getEmail());
        etContact.setText(currentEmployee.getContact());
        etAddress.setText(currentEmployee.getAddress());
        etSalary.setText(currentEmployee.getSalary());
        etReferredBy.setText(currentEmployee.getReferred_by());

        if(!currentEmployee.getImage_url().equals("")){
            Picasso.with(getActivity())
                    .load(currentEmployee.getImage_url())
                    .into(image);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.hide();
    }

    @Override
    public void onClick(View v) {
        MyUtils.hideKey(v);

        switch (v.getId()){
            
            case R.id.close:
                getFragmentManager().popBackStack();
                break;

            case R.id.change_image:
                UploadImageDialog uploadImageDialog = new UploadImageDialog();
                uploadImageDialog.setTargetFragment(this,PICTURE_REQUEST);
                uploadImageDialog.show(getFragmentManager(), Constant.DEFAULAT_FRAGMENT_TAG);

                break;

            case R.id.tick:
                
                updateEmployeeOnly();

                if(imageBitmap!= null){
                    updateImage();
                }
                break;
        }

    }

    private void updateImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        MyFirebaseStorage myFirebaseStorage = new MyFirebaseStorage();

        UploadTask uploadTask = null;

        if(currentUser.getUser_type()==0){
            uploadTask =myFirebaseStorage.getEmployeesReference().child(currentUser.getId()).child(currentEmployee.getId()).putBytes(data);
        }else if(currentUser.getUser_type()==1){
            uploadTask =myFirebaseStorage.getEmployeesReference().child(currentUser.getParent_id()).child(currentEmployee.getId()).putBytes(data);
        }

        uploadTask.addOnSuccessListener(mySuccessListener);
    }

    private void updateEmployeeOnly() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String salary = etSalary.getText().toString().trim();
        String referred_by = etReferredBy.getText().toString().trim();

        if(!MyUtils.validateEmail(email)){
            etEmail.requestFocus();
            Toast.makeText(getActivity(), "Email not valid", Toast.LENGTH_SHORT).show();
            return;
        }

        doCommonTask(name,email,contact,address,salary,referred_by);

        if(imageBitmap==null){

            if(currentEmployee.getUser_type()==1){
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction().replace(R.id.main_container,new AllManagerFragment())
                        .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).commit();

            }else if(currentEmployee.getUser_type()==2){
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction().replace(R.id.main_container,new Employees())
                        .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).commit();
            }


        }


    }

    private void doCommonTask(String name,String email,String contact, String address, String salary, String referred_by){
        currentEmployee.setName(name);
        currentEmployee.setEmail(email);
        currentEmployee.setContact(contact);
        currentEmployee.setAddress(address);
        currentEmployee.setSalary(salary);
        currentEmployee.setReferred_by(referred_by);

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();

        if(currentUser.getUser_type()==0){
            myDatabaseReference.getEmployeeReference(currentUser.getId()).child(currentEmployee.getId()).setValue(currentEmployee);
        }else if(currentUser.getUser_type()==1){
            myDatabaseReference.getEmployeeReference(currentUser.getParent_id()).child(currentEmployee.getId()).setValue(currentEmployee);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PICTURE_REQUEST:
                if(resultCode== Activity.RESULT_OK){

                    Bundle bundle = data.getExtras();
                    String path = bundle.getString(Constant.URI);
                    imageBitmap = MyUtils.resizeBitmap(path,150,150);
                    image.setImageBitmap(imageBitmap);
                }

                break;
        }
    }
}
