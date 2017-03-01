package com.adec.firebasestorekeeper.DetailFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Navigation.AllCustomersFragment;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerDetailFragment extends Fragment implements View.OnClickListener{
    private ActionBar actionBar;
    
    private Customer currentCustomer;

    private ImageView ivClose,ivTick;

    private MyEditText etName,etEmail,etContact,etAddress;

    private User currentUser;


    public CustomerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if(getArguments()!=null){
            currentCustomer = (Customer) getArguments().getSerializable(Constant.CUSTOMER);
        }

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_customer_detail, container, false);
        initView(view);
        
        if(currentCustomer!= null){
            setData();
        }

        MyUtils.hideKey(view);
        return view;
    }

    private void setData() {
        etName.setText(currentCustomer.getName());
        etEmail.setText(currentCustomer.getEmail());
        etContact.setText(currentCustomer.getContact());
        etAddress.setText(currentCustomer.getAddress());
    }

    private void initView(View view) {
        ivClose = (ImageView) view.findViewById(R.id.close);
        ivTick = (ImageView) view.findViewById(R.id.tick);

        etName = (MyEditText) view.findViewById(R.id.name);
        etEmail = (MyEditText) view.findViewById(R.id.email);
        etContact = (MyEditText) view.findViewById(R.id.contact);
        etAddress = (MyEditText) view.findViewById(R.id.address);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
//        actionBar.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                getFragmentManager().popBackStack();
                break;
            case R.id.tick:
                MyUtils.hideKey(v);

                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String contact = etContact.getText().toString().trim();
                String address = etAddress.getText().toString().trim();

                if(!MyUtils.validateEmail(email)){
                    etEmail.requestFocus();
                    Toast.makeText(getActivity(), "Email is not Valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateCustomer(name,email,contact,address);
                break;
        }
    }

    private void updateCustomer(String name, String email, String contact, String address) {

        currentCustomer.setName(name);
        currentCustomer.setEmail(email);
        currentCustomer.setContact(contact);
        currentCustomer.setAddress(address);

        String id = null;

        if(currentUser.getUser_type()==0){
            id= currentUser.getId();
        }else if(currentUser.getUser_type()==1){
            id = currentUser.getParent_id();
        }

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        myDatabaseReference.getCustomerRef(id).child(currentCustomer.getId()).setValue(currentCustomer, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(getActivity(), "Customer Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
