package com.adec.firebasestorekeeper.AppUtility;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Sohel on 1/14/2017.
 */

public class MyFirebaseUtils {

    public static boolean uploadUri(Uri uri, StorageReference reference){

        boolean success =false;

        UploadTask uploadTask =reference.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });


        return success;

    }
}
