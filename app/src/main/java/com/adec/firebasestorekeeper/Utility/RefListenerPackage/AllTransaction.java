package com.adec.firebasestorekeeper.Utility.RefListenerPackage;

import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.Model.Store;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sohel on 2/8/2017.
 */

public class AllTransaction {
    private DatabaseReference transactionRef;

    private ValueEventListener valueEventListener;
    private Store store;

    private MapTransactionListener listener;

    private List<Transaction> transactionList;


    public AllTransaction(final Store store) {
        this.store = store;
        transactionList = new ArrayList<>();

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        this.transactionRef= myDatabaseReference.getTransactionRef(store.getId());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot x : dataSnapshot.getChildren()){
                    Transaction transaction = x.getValue(Transaction.class);

                    transactionList.add(transaction);

                    /*if(transaction.getDate().equals(MyUtils.dateToString(new Date())) && transaction.getType()==1){
                        transactionList.add(transaction);
                    }*/
                }

                if(listener!= null){
                    listener.getStoreTransaction(store.getName(),transactionList,store.getId());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        transactionRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void removeListener(){
        transactionRef.removeEventListener(valueEventListener);
    }

    public void setMapTransactionListener(MapTransactionListener listener){
        this.listener = listener;
    }

    public interface MapTransactionListener{
        public void getStoreTransaction(String storeName, List<Transaction> transactionList,String store_id);
    }
}
