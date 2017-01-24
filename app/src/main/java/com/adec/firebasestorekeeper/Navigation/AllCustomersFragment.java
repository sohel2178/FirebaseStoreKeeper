package com.adec.firebasestorekeeper.Navigation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adec.firebasestorekeeper.Adapter.CustomerAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.DetailFragment.CustomerDetailFragment;
import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyCustomerReferenceClass;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllCustomersFragment extends Fragment implements MyCustomerReferenceClass.CustomerReferenceListener,
        View.OnClickListener,CustomerAdapter.CustomerListener{

    private ActionBar actionBar;

    private RecyclerView rvCustomers;

    private List<Customer> customerList;
    private CustomerAdapter adapter;

    private FloatingActionButton fabAdd;

    private User currentUser;

    private MyCustomerReferenceClass myCustomerReferenceClass;


    public AllCustomersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        customerList = new ArrayList<>();
        adapter = new CustomerAdapter(getActivity(),customerList);
        adapter.setCustomerListener(this);

        // Listen All Customer from the Database Reference
        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        // Set Customer Reference as per User Type
        if(currentUser.getUser_type()==0){
            myCustomerReferenceClass = new MyCustomerReferenceClass(currentUser.getId());
        }else if(currentUser.getUser_type()==1){
            myCustomerReferenceClass = new MyCustomerReferenceClass(currentUser.getParent_id());
        }
        myCustomerReferenceClass.setCustomerReferenceListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_customers, container, false);
        
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvCustomers = (RecyclerView) view.findViewById(R.id.rvCustomers);
        rvCustomers.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCustomers.setAdapter(adapter);

        fabAdd = (FloatingActionButton) view.findViewById(R.id.fab_add);

        if(currentUser.getUser_type()==0){
            fabAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle(Constant.ALL_CUSTOMERS);
        actionBar.show();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fabAdd.setOnClickListener(this);
    }

    @Override
    public void getCustomer(Customer customer) {

        adapter.addCustomer(customer);

    }

    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction().replace(R.id.main_container,new AddCustomerFragment())
                .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).addToBackStack(Constant.ALL_CUSTOMERFRAGMENT).commit();
    }

    @Override
    public void onItemClick(int position) {
        Customer customer = customerList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.CUSTOMER,customer);

        CustomerDetailFragment customerDetailFragment = new CustomerDetailFragment();
        customerDetailFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.main_container,customerDetailFragment)
                .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null).commit();
    }
}
