package com.adec.firebasestorekeeper.Utility.RefListenerPackage;

import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Sohel on 1/15/2017.
 */

public class MyAttachmentReferenceClass {

    private DatabaseReference myRef;
    private String transaction_id;

    private AttachmentReferenceListener listener;

    public MyAttachmentReferenceClass(String transaction_id) {
        this.transaction_id = transaction_id;
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();

        myRef = myDatabaseReference.getAttachmentRererence(transaction_id);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot x: dataSnapshot.getChildren()){
                    String url = x.getValue(String.class);

                    if(listener!=null){
                        listener.getUrl(url);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface AttachmentReferenceListener{
        public void getUrl(String url);
    }
}
