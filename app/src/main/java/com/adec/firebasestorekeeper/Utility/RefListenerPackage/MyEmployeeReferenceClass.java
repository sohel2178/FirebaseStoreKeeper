package com.adec.firebasestorekeeper.Utility.RefListenerPackage;

import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Sohel on 1/16/2017.
 */

public class MyEmployeeReferenceClass {

    private DatabaseReference employeeRef;
    private EmployeeReferenceListener listener;

    private ValueEventListener valueEventListener;


    public MyEmployeeReferenceClass(String ownerId) {
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();

        employeeRef = myDatabaseReference.getEmployeeReference(ownerId);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot x: dataSnapshot.getChildren()){
                    User user = x.getValue(User.class);

                    if(listener!=null){
                        listener.getEmployee(user);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        employeeRef.addListenerForSingleValueEvent(valueEventListener);

       /* employeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot x: dataSnapshot.getChildren()){
                    User user = x.getValue(User.class);

                    if(listener!=null){
                        listener.getEmployee(user);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    public void setEmployeeReferenceListener(EmployeeReferenceListener listener){
        this.listener=listener;
    }

    public void removeReference(){
        employeeRef.removeEventListener(valueEventListener);
    }

    public interface EmployeeReferenceListener{
        public void getEmployee(User user);
    }
}
