package com.adec.firebasestorekeeper.Utility.RefListenerPackage;

import android.util.Log;

import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohel on 1/15/2017.
 */

public class MyTransactionReferenceClass {

    private DatabaseReference myRef;
    private String store_id;

    private ChildEventListener childEventListener;

    private TransactionReferenceListener listener;
    private List<Transaction> transactionList;

    public MyTransactionReferenceClass(String store_id) {
        this.store_id = store_id;
        transactionList = new ArrayList<>();

        MyDatabaseReference myDatabaseReference= new MyDatabaseReference();
        myRef = myDatabaseReference.getTransactionRef(store_id);

        /*myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot x: dataSnapshot.getChildren()){
                    Transaction transaction = x.getValue(Transaction.class);
                    transactionList.add(transaction);

                }

                *//*if(listener!=null){
                    listener.getTransactions(transactionList);
                }*//*
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        triggerChildEvent();



    }

    public void setTransactionReferenceListener(TransactionReferenceListener listener){
        this.listener=listener;
    }


    public void triggerChildEvent(){

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Transaction transaction = dataSnapshot.getValue(Transaction.class);
                if(listener!= null){
                    listener.addTransaction(transaction);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myRef.addChildEventListener(childEventListener);
    }

    public void removeListener(){
        myRef.removeEventListener(childEventListener);
    }

    /*private boolean isExist(Transaction transaction){
        boolean exist = false;
        for(Transaction x: transactionList){
            if(x.getMemo_voucher_id().equals())
        }
    }*/

    public interface TransactionReferenceListener{
        public void getTransactions(List<Transaction> transactions);
        public void addTransaction(Transaction transaction);
    }
}
