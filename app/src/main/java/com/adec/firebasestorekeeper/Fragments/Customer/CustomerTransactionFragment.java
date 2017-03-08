package com.adec.firebasestorekeeper.Fragments.Customer;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adec.firebasestorekeeper.Adapter.TransactionAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.DetailFragment.DetailTransactionFragment;
import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerTransactionFragment extends Fragment implements TransactionAdapter.TransactionListener {

    private ActionBar actionBar;

    private Customer customer;
    private User currentUser;

    private List<Transaction> transactionList;
    private TransactionAdapter adapter;

    private RecyclerView rvTransaction;


    public CustomerTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_transaction, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvTransaction = (RecyclerView) view.findViewById(R.id.rv_transaction);
        rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getArguments() != null){
            customer = (Customer) getArguments().getSerializable(Constant.CUSTOMER);

        }
        Log.d("YYYY","HHHHH");
        actionBar.show();
        actionBar.setTitle(customer.getName()+"/Transaction");
        transactionList = new ArrayList<>();
        adapter = new TransactionAdapter(getActivity(),transactionList);
        adapter.setTransactionListener(this);
        rvTransaction.setAdapter(adapter);


        // Get Transaction Data
        getCustomerTransaction(customer);


    }

    private void getCustomerTransaction(final Customer customer){
        UserLocalStore userLocalStore =new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();


        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        if(currentUser.getUser_type()==1){
            myDatabaseReference.getTransactionRef(currentUser.getAssign_store_id())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot x: dataSnapshot.getChildren()){
                                Transaction transac = x.getValue(Transaction.class);

                                if(transac.getCustomer_id().equals(customer.getId())){
                                    adapter.addTransaction(transac);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    }

    @Override
    public void onClickTransaction(int position) {
        Transaction transaction = transactionList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TRANSACTION,transaction);
        DetailTransactionFragment detailTransactionFragment = new DetailTransactionFragment();
        detailTransactionFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.main_container,detailTransactionFragment)
                .addToBackStack(null).commitAllowingStateLoss();
    }
}
