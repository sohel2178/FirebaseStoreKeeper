package com.adec.firebasestorekeeper.Utility.RefListenerPackage;

import com.adec.firebasestorekeeper.Model.Store;
import com.adec.firebasestorekeeper.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Sohel on 1/8/2017.
 */

public class MyStoreReferenceClass {

    private DatabaseReference myRef;

    private StoreReferenceListener listener;
    private ValueEventListener valueEventListener;

    public MyStoreReferenceClass(DatabaseReference myRef) {
        this.myRef =myRef;

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot x: dataSnapshot.getChildren()){
                    Store store = x.getValue(Store.class);

                    if(listener!=null){
                        listener.getStore(store);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        myRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void setStoreReferenceListener(StoreReferenceListener listener){
        this.listener=listener;
    }

    public void removeListener(){
        myRef.removeEventListener(valueEventListener);
    }

    public interface StoreReferenceListener{
        public void getStore(Store store);
    }
}
