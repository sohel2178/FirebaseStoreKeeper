package com.adec.firebasestorekeeper.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.adec.firebasestorekeeper.Adapter.ImageAdapter;
import com.adec.firebasestorekeeper.AppUtility.MarshMallowPermission;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.Model.Voucher;
import com.adec.firebasestorekeeper.Navigation.AllCustomersFragment;
import com.adec.firebasestorekeeper.Navigation.TransactionFragment;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.MyFirebaseStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;
import com.joanzapata.iconify.widget.IconTextView;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddVoucher extends Fragment implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback,
        ImageAdapter.AttachmentListener{
    private ActionBar actionBar;

    private static final int REQUEST_CAMERA = 0;

    private MyEditText etVoucherNumber,etHead,etPayto,etAmount,etPaymentMethod,etDate,etRemarks;
    private IconTextView itvCalendar,itvAttach,itvCamera;

    private RecyclerView rvAttachments;
    private List<Bitmap> bitmapList;
    private ImageAdapter attachmentAdapter;

    private Button btnAdd;




    private DatabaseReference transactionRef;

    private ProgressDialog dialog;

    MyFirebaseStorage myFirebaseStorage;

    private int uploadIndicator;


    private OnSuccessListener mySuccessListener;

    private List<String> downloadedUrlList;

    private String transaction_id;




    public AddVoucher() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();


        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        String store_id = userLocalStore.getUser().getAssign_store_id();

        final MyDatabaseReference myDatabaseReference = new MyDatabaseReference();

        transactionRef = myDatabaseReference.getTransactionRef(store_id);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");

         myFirebaseStorage = new MyFirebaseStorage();
        Log.d("TEST",myFirebaseStorage.getMemoImageReference().getBucket());


        // Work for Image Recycler View
        bitmapList = new ArrayList<>();
        attachmentAdapter = new ImageAdapter(getContext(),bitmapList);
        attachmentAdapter.setAttacmentListener(this);


        //
        downloadedUrlList = new ArrayList<>();

        uploadIndicator=0;

        mySuccessListener = new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                UploadTask.TaskSnapshot snapshot = (UploadTask.TaskSnapshot) o;

                String url = String.valueOf(snapshot.getDownloadUrl());

                if(transaction_id!=null){
                    myDatabaseReference.getAttachmentRererence(transaction_id).push().setValue(url);
                }


                uploadIndicator++;

                Log.d("YYYYY",uploadIndicator+"");

                if(uploadIndicator==bitmapList.size()){
                    dialog.dismiss();

                    getFragmentManager().popBackStack();
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new TransactionFragment())
                            .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();
                }

            }
        };




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_voucher, container, false);


        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle("Voucher Entry Form");
    }

    private void initView(View view) {
        etVoucherNumber = (MyEditText) view.findViewById(R.id.voucher_id);
        etHead = (MyEditText) view.findViewById(R.id.exp_head);
        etPayto = (MyEditText) view.findViewById(R.id.pay_to);
        etAmount = (MyEditText) view.findViewById(R.id.amount);
        etPaymentMethod = (MyEditText) view.findViewById(R.id.payment_method);
        etDate = (MyEditText) view.findViewById(R.id.date);
        etRemarks = (MyEditText) view.findViewById(R.id.remarks);

        itvCalendar = (IconTextView) view.findViewById(R.id.calendar);
        itvAttach = (IconTextView) view.findViewById(R.id.attachment);
        itvCamera = (IconTextView) view.findViewById(R.id.camera);

        // Attachment Recycler View
        int colCount = MyUtils.getScreenWidthInDp(getActivity())/110;
        rvAttachments = (RecyclerView) view.findViewById(R.id.rv_attachments);
        rvAttachments.setLayoutManager(new GridLayoutManager(getActivity(),colCount));
        rvAttachments.setAdapter(attachmentAdapter);


        btnAdd = (Button) view.findViewById(R.id.add);

        Date date = new Date();
        String dateStr = MyUtils.dateToString(date);
        etDate.setText(dateStr);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        itvAttach.setOnClickListener(this);
        itvCalendar.setOnClickListener(this);
        itvCamera.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.attachment:

                if(isStoragePermissionGranted()){
                   /* Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,REQUEST_CODE_PICKER);*/

                    RxImagePicker.with(getActivity()).requestMultipleImages().subscribe(new Action1<List<Uri>>() {
                        @Override
                        public void call(List<Uri> uris) {

                            for (Uri x: uris){
                                attachmentAdapter.addImage(MyUtils.uriToScaledBitmap(getActivity(),x,150,150));
                            }

                        }
                    });


                }

                break;

            case R.id.camera:

                checkCameraPermission();

                /*RxImagePicker.with(getActivity()).requestImage(Sources.CAMERA).subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        //Get image by uri using one of image loading libraries. I use Glide in sample app.
                    }
                });*/





                break;

            case R.id.calendar:
                MyUtils.showDialogAndSetTime(getActivity(),etDate);
                break;

            case R.id.add:

                MyUtils.hideKey(view);

                String date = etDate.getText().toString().trim();
                String voucher_no = etVoucherNumber.getText().toString().trim();
                String head = etHead.getText().toString().trim();
                String payTo = etPayto.getText().toString().trim();
                String amount = etAmount.getText().toString().trim();
                String paymentMethod = etPaymentMethod.getText().toString().trim();
                String remarks = etRemarks.getText().toString().trim();


                if(TextUtils.isEmpty(voucher_no)){
                    etVoucherNumber.requestFocus();
                    Toast.makeText(getActivity(), "Voucher is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(amount)){
                    etAmount.requestFocus();
                    Toast.makeText(getActivity(), "Amount is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Voucher voucher = new Voucher(voucher_no,date,head,payTo,Double.parseDouble(amount),paymentMethod,remarks);
                dialog.show();
                postDatatotheServer(voucher);



                break;
        }
    }

    private void postDatatotheServer(Voucher voucher) {
        transactionRef.push().setValue(voucher, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                transaction_id =databaseReference.getKey();

                updateKey();
            }
        });
    }

    private void updateKey(){
        transactionRef.child(transaction_id).child("id").setValue(transaction_id, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if(bitmapList.size()==0){
                    dialog.dismiss();
                    getFragmentManager().popBackStack();

                    getFragmentManager().beginTransaction().replace(R.id.main_container,new TransactionFragment())
                            .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();
                }else{
                    uploadImage();
                }

                /*if(ivVoucher.getDrawable()==null){
                    // If No Attachment in the Voucher the Finish Job Here
                    dialog.dismiss();
                    getFragmentManager().popBackStack();

                    getFragmentManager().beginTransaction().replace(R.id.main_container,new TransactionFragment())
                            .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();
                }else{
                    // Upload Image in the Firebase Storage
                    uploadImage(id);
                }*/


            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


    }



    private void uploadImage(){
       /* ivVoucher.setDrawingCacheEnabled(true);
        ivVoucher.buildDrawingCache();
        Bitmap bitmap = ivVoucher.getDrawingCache(true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = myFirebaseStorage.getVoucherImageReference().child(voucher_id).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                String image_url = String.valueOf(downloadUrl);

                updateImageUrl(voucher_id,image_url);
            }
        });*/

        for(int i=0;i<bitmapList.size();i++){
            Log.d("YYYYY","Sohel");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapList.get(i).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = myFirebaseStorage.getVoucherImageReference().child(transaction_id).child(String.valueOf(i)).putBytes(data);

            uploadTask.addOnSuccessListener(mySuccessListener);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i("SOHEL", "Received response for Camera permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Camera permission has been granted, preview can be displayed

                takePicture();

            } else {
                //Permission not granted
                //Toast.makeText(MainActivity.this,"You need to grant camera permission to use camera",Toast.LENGTH_LONG).show();
            }

        }
    }

    private void takePicture() {
        /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);*/

        RxImagePicker.with(getActivity()).requestImage(Sources.CAMERA).subscribe(new Action1<Uri>() {
            @Override
            public void call(Uri uri) {
                attachmentAdapter.addImage(MyUtils.uriToScaledBitmap(getActivity(),uri,150,150));
            }
        });
    }

    void checkCameraPermission() {
        boolean isGranted;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            requestCameraPermission();


        } else {

            takePicture();

        }
    }

    private void requestCameraPermission() {


        // Camera permission has not been granted yet. Request it directly.
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA);
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("SOHEL","Permission is granted");
                return true;
            } else {

                Log.v("SOHEL","Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("SOHEL","Permission is granted");
            return true;
        }
    }


    // Attachment Remove Listener
    @Override
    public void onRemoveClick(int postion) {
        Bitmap bitmap = bitmapList.get(postion);
        attachmentAdapter.removeImage(bitmap);
    }
}
