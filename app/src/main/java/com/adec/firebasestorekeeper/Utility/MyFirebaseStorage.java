package com.adec.firebasestorekeeper.Utility;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Sohel on 1/14/2017.
 */

public class MyFirebaseStorage {

    private static final String MY_STORAGE="gs://sohelfcm.appspot.com";
    private static final String USER="user";
    private static final String MEMO="memo";
    private static final String VOUCHER="voucher";
    private static final String STORE="store";

    private StorageReference mainRef;

    public MyFirebaseStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        mainRef = storage.getReferenceFromUrl(MY_STORAGE);
    }

    public StorageReference getUserImageReference(){
        return mainRef.child(USER);
    }

    public StorageReference getMemoImageReference(){
        return mainRef.child(MEMO);
    }

    public StorageReference getVoucherImageReference(){
        return mainRef.child(VOUCHER);
    }

    public StorageReference getStoreImageReference(){
        return mainRef.child(STORE);
    }

}
