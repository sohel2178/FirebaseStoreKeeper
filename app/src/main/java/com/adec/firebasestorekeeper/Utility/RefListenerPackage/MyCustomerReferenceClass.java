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


    private CustomerReferenceListener listener;

    public MyCustomerReferenceClass(String owner_id) {
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        this.myRef = myDatabaseReference.getCustomerRef(owner_id);

        Log.d("GGGG",String.valueOf(myRef));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               // Log.d("TTTT",String.valueOf(dataSnapshot.getValue()));

               for(DataSnapshot x: dataSnapshot.getChildren()){
                   Customer customer = x.getValue(Customer.class);

                   if(listener!=null){
                       listener.getCustomer(customer);
                   }

               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.d("TTTT",databaseError.getMessage());

            }
        });
    }

    public void setCustomerReferenceListener(CustomerReferenceListener listener){
        this.listener=listener;
    }

    public interface CustomerReferenceListener{
        public void getCustomer(Customer customer);
    }
}
