package com.adec.firebasestorekeeper.Utility;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Sohel on 1/8/2017.
 */

public class MyDatabaseReference {
    private static final String USER_REF="user";
    private static final String STORE_REF="stores";
    private static final String CUSTOMER_REF="customers";
    private static final String TRANSACTION_REF="transaction";
    private static final String ATTACHMENT_REF="attachments";
    private static final String OB_ATTACHMENT="Opening Balance Attachments";
    private static final String PAYMENT_ATTACHMENT="Payment Attachments";
    private static final String EMPLOYEE_REF="employees";
    private static final String PRODUCT_REF="products";
    private static final String HEAD_REF="Head";
    private static final String PAYMENT_REF="Payment";
    private static final String DUE_PAYMENT_REF="Due Payment";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mainRef;


    public MyDatabaseReference() {

        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.mainRef = firebaseDatabase.getReference();

    }


    public DatabaseReference getUserRef(){
        DatabaseReference userRef = mainRef.child(USER_REF);
        return userRef;
    }

    public DatabaseReference getStoreRef(String userId){
        DatabaseReference storeRef = mainRef.child(STORE_REF).child(String.valueOf(userId));
        return storeRef;
    }

    public DatabaseReference getCustomerRef(String owner_id){
        DatabaseReference customerRef = mainRef.child(CUSTOMER_REF).child(owner_id);
        return customerRef;
    }

    public DatabaseReference getTransactionRef(String store_id){
        DatabaseReference transactionRef = mainRef.child(TRANSACTION_REF).child(store_id);
        return transactionRef;
    }

    public DatabaseReference getAttachmentRererence(String transaction_id){
        DatabaseReference attachmentRef = mainRef.child(ATTACHMENT_REF).child(transaction_id);
        return attachmentRef;
    }

    public DatabaseReference getEmployeeReference(String owner_id){
        DatabaseReference employeeRef = mainRef.child(EMPLOYEE_REF).child(owner_id);
        return employeeRef;
    }

    public DatabaseReference getProductReference(String owner_id){
        DatabaseReference productRef = mainRef.child(PRODUCT_REF).child(owner_id);
        return productRef;
    }

    public DatabaseReference getHeadReference(String owner_id){
        DatabaseReference headRef = mainRef.child(HEAD_REF).child(owner_id);
        return headRef;
    }

    public DatabaseReference getPaymentRefAgainstMemo(String transaction_id){
        DatabaseReference paymentRef = mainRef.child(PAYMENT_REF).child(transaction_id);
        return paymentRef;
    }

    public DatabaseReference getPaymentRefOB(String customer_id){
        DatabaseReference paymentRef = mainRef.child(DUE_PAYMENT_REF).child(customer_id);
        return paymentRef;
    }

    public DatabaseReference getPaymentOBAttachmentRef(String customer_id){
        DatabaseReference paymentRef = mainRef.child(OB_ATTACHMENT).child(customer_id);
        return paymentRef;
    }

    public DatabaseReference getPaymentMemoAttachmentRef(String paymentId){
        DatabaseReference paymentRef = mainRef.child(PAYMENT_ATTACHMENT).child(paymentId);
        return paymentRef;
    }




}
