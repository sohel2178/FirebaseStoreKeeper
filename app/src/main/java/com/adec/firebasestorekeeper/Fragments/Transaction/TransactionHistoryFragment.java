package com.adec.firebasestorekeeper.Fragments.Transaction;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adec.firebasestorekeeper.Adapter.PaymentAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.Model.PaymentAgainstMemo;
import com.adec.firebasestorekeeper.Model.PaymentAgainstOB;
import com.adec.firebasestorekeeper.Model.Transaction;
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
public class TransactionHistoryFragment extends Fragment {

    private TextView tvAmount,tvInitialPayment;
    private ActionBar actionBar;
    private Transaction transaction;

    private RecyclerView rvPayment;

    private List<PaymentAgainstMemo> paymentAgainstMemoList;
    private PaymentAdapter adapter;




    public TransactionHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            transaction = (Transaction) getArguments().getSerializable(Constant.TRANSACTION);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_transaction_history, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvAmount = (TextView) view.findViewById(R.id.tran_amount);
        tvInitialPayment = (TextView) view.findViewById(R.id.initial_payment);

        tvAmount.setText("Tk. "+transaction.getTotal());
        tvInitialPayment.setText("Tk. "+transaction.getPayment_amount());

        rvPayment = (RecyclerView) view.findViewById(R.id.rv_payment);
        rvPayment.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onResume() {
        super.onResume();

        paymentAgainstMemoList = new ArrayList<>();
        adapter = new PaymentAdapter(getActivity(),paymentAgainstMemoList);
        rvPayment.setAdapter(adapter);
        loadData();
    }

    private void loadData(){
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        myDatabaseReference.getPaymentRefAgainstMemo(transaction.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot x: dataSnapshot.getChildren()){
                            PaymentAgainstMemo paymentAgainstMemo = x.getValue(PaymentAgainstMemo.class);
                            adapter.addPayment(paymentAgainstMemo);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
