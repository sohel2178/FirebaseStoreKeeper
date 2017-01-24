package com.adec.firebasestorekeeper.Utility.RefListenerPackage;

import android.util.Log;

import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Sohel on 1/8/2017.
 */

public class MyUserReferenceClass {

    private DatabaseReference myRef;

    private UserReferenceListener listener;

    public MyUserReferenceClass() {
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        this.myRef = myDatabaseReference.getUserRef();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               for(DataSnapshot x: dataSnapshot.getChildren()){
                   User user = x.getValue(User.class);

                   if(listener!=null){
                       listener.getUser(user);
                   }


               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUserReferenceListener(UserReferenceListener listener){
        this.listener=listener;
    }

    public interface UserReferenceListener{
        public void getUser(User user);
    }
}
