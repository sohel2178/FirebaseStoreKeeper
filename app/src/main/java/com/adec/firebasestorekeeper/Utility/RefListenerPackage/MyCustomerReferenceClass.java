package com.adec.firebasestorekeeper.Utility.RefListenerPackage;

import android.util.Log;

import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Sohel on 1/8/2017.
 */

public class MyCustomerReferenceClass {

    private DatabaseReference myRef;
    private ValueEventListener valueEventListener;
    private CustomerReferenceListener listener;

    public MyCustomerReferenceClass(String owner_id) {
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        this.myRef = myDatabaseReference.getCustomerRef(owner_id);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot x: dataSnapshot.getChildren()){
                    Customer customer = x.getValue(Customer.class);

                    if(listener!=null){
                        listener.getCustomer(customer);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void removeListener(){
        myRef.removeEventListener(valueEventListener);
    }

    public void setCustomerReferenceListener(CustomerReferenceListener listener){
        this.listener=listener;
    }

    public interface CustomerReferenceListener{
        public void getCustomer(Customer customer);
    }
}
