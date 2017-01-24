package com.adec.firebasestorekeeper.Utility.RefListenerPackage;

import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Sohel on 1/16/2017.
 */

public class MyProductReferenceClass {

    private DatabaseReference productRef;
    private ProductReferenceListener listener;

    private TreeSet<String> myProductSet;


    public MyProductReferenceClass(String owner_id) {
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        productRef = myDatabaseReference.getProductReference(owner_id);

        myProductSet = new TreeSet<>();

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> productList = new ArrayList<String>();
                for(DataSnapshot x: dataSnapshot.getChildren()){
                    String product = x.getValue(String.class);

                    productList.add(product);



                }

                if(listener!=null){
                    listener.getProduct(productList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setProductReferenceListener(ProductReferenceListener listener){
        this.listener=listener;
    }



    public interface ProductReferenceListener{
        public void  getProduct(List<String> product);
    }
}
