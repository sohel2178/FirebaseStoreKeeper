package com.adec.firebasestorekeeper.DialogFragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.adec.firebasestorekeeper.Adapter.DialogFragmenAdapter.CustomerDialogAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyCustomerReferenceClass;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerDialogFragment extends DialogFragment implements MyCustomerReferenceClass.CustomerReferenceListener,
        CustomerDialogAdapter.CustomerDialogAdapterListener{

    private EditText etSearch;
    private RecyclerView rvCustomers;

    private ProgressDialog dataLoaderIndicator;

    private User currentUser;

    private List<Customer> customerList;
    private List<Customer> customerHolderList;
    private CustomerDialogAdapter adapter;

    private MyCustomerReferenceClass myCustomerReferenceClass;


    public CustomerDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        customerList = new ArrayList<>();
        customerHolderList = new ArrayList<>();
        adapter = new CustomerDialogAdapter(getActivity(),customerList);
        adapter.setCustomerDialogAdapterListener(this);

        if(currentUser.getUser_type()==0){
            myCustomerReferenceClass = new MyCustomerReferenceClass(currentUser.getId());
        }else if(currentUser.getUser_type()==1){
            myCustomerReferenceClass = new MyCustomerReferenceClass(currentUser.getParent_id());
        }
        myCustomerReferenceClass.setCustomerReferenceListener(this);


        dataLoaderIndicator = new ProgressDialog(getActivity());
        dataLoaderIndicator.setMessage("Please Wait while Loading Data...");
        dataLoaderIndicator.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_customer_dialog, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        rvCustomers = (RecyclerView) view.findViewById(R.id.rv_customers);
        rvCustomers.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCustomers.setAdapter(adapter);

        etSearch = (EditText) view.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String serachText = s.toString().trim();

                if(!TextUtils.isEmpty(serachText)){
                    upDateAdapter(serachText);
                }else {
                    customerList.clear();
                    customerList.addAll(customerHolderList);
                    adapter.notifyDataSetChanged();
                }

                if(s.toString().trim().equals(""))

                Log.d("Test", String.valueOf(s.toString().trim()));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void getCustomer(Customer customer) {
        customerHolderList.add(customer);
        customerList.add(customer);
        adapter.notifyDataSetChanged();

        Log.d("MYTEST",customerList.size()+"");

        dataLoaderIndicator.dismiss();
    }


    private void upDateAdapter(String search){
        List<Customer> tempList = new ArrayList<>();

        for(Customer x : customerHolderList){
            if(x.getName().toLowerCase().startsWith(search.toLowerCase())){
                tempList.add(x);
            }
        }

        customerList.clear();
        customerList.addAll(tempList);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(int position) {
        Customer customer = customerList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.CUSTOMER,customer);;

        Intent intent = new Intent();
        intent.putExtras(bundle);

        getTargetFragment().onActivityResult(12, Activity.RESULT_OK,intent);
        Log.d("TTTTT",customer.getName());
        getDialog().dismiss();






    }
}
