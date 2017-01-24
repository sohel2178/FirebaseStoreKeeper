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
import android.widget.TextView;
import android.widget.Toast;

import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddManagerFragment extends Fragment implements View.OnClickListener{

    private MyEditText etName,etEmail,etAddress,etContact,etNid,etReferredBy,etSalary;
    private Button btnAdd;
    private TextView tvTitle;


    private DatabaseReference userRef;
    private DatabaseReference employeeRef;

    private ProgressDialog dialog;

    private ActionBar actionBar;

    private String title;

    private User currentUser;


    public AddManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            title= getArguments().getString(Constant.TITLE);
        }

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        userRef = myDatabaseReference.getUserRef();

        if(currentUser.getUser_type()==0){
            employeeRef = myDatabaseReference.getEmployeeReference(currentUser.getId());
        }else if(currentUser.getUser_type()==1){
            employeeRef = myDatabaseReference.getEmployeeReference(currentUser.getParent_id());
        }


        // progress Dialog init Here
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_manager, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {

        etName = (MyEditText) view.findViewById(R.id.name);
        etEmail = (MyEditText) view.findViewById(R.id.email);
        etAddress = (MyEditText) view.findViewById(R.id.address);
        etContact = (MyEditText) view.findViewById(R.id.contact);
        etNid = (MyEditText) view.findViewById(R.id.nid);
        etReferredBy = (MyEditText) view.findViewById(R.id.referred_by);
        etSalary = (MyEditText) view.findViewById(R.id.salary);

        tvTitle = (TextView) view.findViewById(R.id.title);

        if(currentUser.getUser_type()==1){
            tvTitle.setText("Sales Man Creation Form");
        }

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
        if(currentUser.getUser_type()==0){
            actionBar.setTitle(Constant.CREATE_MANAGER);

        }else if(currentUser.getUser_type()==1){
            actionBar.setTitle("Create Sales Man");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:

                MyUtils.hideKey(view);

                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String contact = etContact.getText().toString().trim();
                String nid = etNid.getText().toString().trim();
                String referred_by = etReferredBy.getText().toString().trim();
                String salary = etSalary.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    etName.requestFocus();
                    Toast.makeText(getActivity(), "Name Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(TextUtils.isEmpty(email)){
                    etEmail.requestFocus();
                    Toast.makeText(getActivity(), "Email Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(!MyUtils.validateEmail(email)){
                        //Log.d("Test","Test");
                        etEmail.requestFocus();
                        Toast.makeText(getActivity(), "Please Enter an Valid Email Address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                int user_type = currentUser.getUser_type()+1;
                String parent_id = currentUser.getId();

                User user = null;

                if(currentUser.getUser_type()==1){
                    user = new User(name,email,address,contact,nid,referred_by,salary,parent_id,user_type,currentUser.getAssign_store_id());

                }else{
                    user = new User(name,email,address,contact,nid,referred_by,salary,parent_id,user_type);
                }



                dialog.show();
                addUserToTheReference(user);

                break;
        }
    }

    private void addUserToTheReference(final User user){
        employeeRef.push().setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String id = databaseReference.getKey();
                updateChild(id);

                if(currentUser.getUser_type()==0){
                    user.setId(id);

                    userRef.child(id).setValue(user);
                }


            }
        });
    }

    private void updateChild(String id){
        employeeRef.child(id).child("id").setValue(id);
        dialog.hide();

        getFragmentManager().popBackStack();

        // Commit Transaction based on Employee
        if(getArguments()!=null){
            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                    .replace(R.id.main_container,new Employees()).commit();
        }else {
            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                    .replace(R.id.main_container,new AllManagerFragment()).commit();
        }


    }
}
