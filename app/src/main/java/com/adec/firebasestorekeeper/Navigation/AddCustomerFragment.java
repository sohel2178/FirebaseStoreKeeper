package com.adec.firebasestorekeeper.Navigation;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCustomerFragment extends Fragment implements View.OnClickListener{
    private FragmentListener fragmentListener;

    private MyEditText etName,etEmail,etAddress,etContact,etOpeningBalance;

    private Button btnAdd;

    private UserLocalStore userLocalStore;
    private String owner_id;

    private DatabaseReference customer_ref;

    private ProgressDialog dialog;



    public AddCustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentListener = (FragmentListener) getActivity();
        userLocalStore = new UserLocalStore(getActivity());
        owner_id = userLocalStore.getUser().getParent_id();

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        customer_ref=myDatabaseReference.getCustomerRef(owner_id);

        // progress Dialog init Here
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_customer, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        etName = (MyEditText) view.findViewById(R.id.name);
        etEmail = (MyEditText) view.findViewById(R.id.email);
        etAddress = (MyEditText) view.findViewById(R.id.address);
        etContact = (MyEditText) view.findViewById(R.id.contact);
        etOpeningBalance = (MyEditText) view.findViewById(R.id.opening_balance);

        btnAdd= (Button) view.findViewById(R.id.add);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentListener.getFragment(41);
    }

    @Override
    public void onClick(View view) {

        MyUtils.hideKey(view);

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String openingBalance = etOpeningBalance.getText().toString().trim();

        double openBal = Double.parseDouble(openingBalance);

        if(TextUtils.isEmpty(name)){
            etName.requestFocus();
            Toast.makeText(getActivity(), "Name Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(contact)){
            etContact.requestFocus();
            Toast.makeText(getActivity(), "Contact Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }


        Customer customer = new Customer(name,email,address,contact,openBal);
        dialog.show();
        // Add the Customer to database
        addCustomer(customer);
    }


    private void addCustomer(Customer customer){
        customer_ref.push().setValue(customer, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String id = databaseReference.getKey();

                updateCustomerId(id);
            }
        });
    }


    private void updateCustomerId(String id){
        customer_ref.child(id).child("id").setValue(id);
        dialog.dismiss();

        getFragmentManager().popBackStack();

    }
}
