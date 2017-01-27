package com.adec.firebasestorekeeper.Navigation;


import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.Toast;

import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.Model.Store;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyStoreReferenceClass;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddStoreFragment extends Fragment implements View.OnClickListener{

    private MyEditText etStoreName,etAddress,etContact;
    private Button btnAdd;

    private UserLocalStore userLocalStore;
    private DatabaseReference storeRef;

    private ProgressDialog dialog;

    private ActionBar actionBar;


    public AddStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        userLocalStore= new UserLocalStore(getActivity());
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();


        //Get Logged in User From Local Database
        User user=userLocalStore.getUser();

        //Instantiate Store Reference Class for Getting all data from the reference
        storeRef = myDatabaseReference.getStoreRef(user.getId());

        // Progress Dialog Init Here
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_store, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        etStoreName = (MyEditText) view.findViewById(R.id.store_name);
        etAddress = (MyEditText) view.findViewById(R.id.store_address);
        etContact = (MyEditText) view.findViewById(R.id.store_contact);

        btnAdd = (Button) view.findViewById(R.id.add);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle(Constant.ADD_STORE);
    }

    @Override
    public void onClick(View view) {
        MyUtils.hideKey(view);
        switch (view.getId()){
            case R.id.add:
                String storeName= etStoreName.getText().toString().trim();
                String storeAddress= etAddress.getText().toString().trim();
                String storeContact= etContact.getText().toString().trim();

                if(TextUtils.isEmpty(storeName)){
                    etStoreName.requestFocus();
                    Toast.makeText(getActivity(), "Outlet Name is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(storeAddress)){
                    etAddress.requestFocus();
                    Toast.makeText(getActivity(), "Address is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(storeContact)){
                    etContact.requestFocus();
                    Toast.makeText(getActivity(), "Contact is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }



                Store store = new Store(storeName,storeAddress,storeContact);
                addStoreToTheReference(storeRef,store);


                dialog.show();

                break;
        }
    }

    private void addStoreToTheReference(DatabaseReference reference, Store store){


        reference.push().setValue(store, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String id = databaseReference.getKey();

                updateStoreAtId(id);

            }
        });



    }


    private void updateStoreAtId(String id){
        storeRef.child(id).child("id").setValue(id);

        dialog.hide();

        getFragmentManager().popBackStack();

        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.main_container,new AllStoreFragment()).commit();
    }




}
