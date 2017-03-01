package com.adec.firebasestorekeeper.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adec.firebasestorekeeper.Adapter.ImageAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomListener.MyChangeListener;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.DialogFragment.CustomerDialogFragment;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Model.PaymentAgainstMemo;
import com.adec.firebasestorekeeper.Model.PaymentAgainstOB;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Navigation.TransactionFragment;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.MyFirebaseStorage;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyCustomerReferenceClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
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
public class PaymentFragment extends Fragment implements View.OnClickListener,
        ImageAdapter.AttachmentListener{
    private static final int CUSTOMER_REQUEST_CODE=12;
    private static final int REQUEST_CAMERA = 0;
    private User currentUser;
    private RadioGroup radioGroup;
    private RadioButton radMemo,radOpeningBalance;

    private FragmentListener fragmentListener;

    private RelativeLayout rlSelectCustomer,rlmemo;

    private IconTextView itvSelect,itvDone,itvCamera,itvAttachment;
    private TextView tvDue;
    private MyEditText etCustomer,etDate,etPayment,etRemarks,etMemoNo;
    private MaterialSpinner sppaymentMethod;
    private Button btnPayment;

    private Customer selectedCustomer;

    private double dueAmount;

    private RecyclerView rvAttachments;
    private ImageAdapter attachmentAdapter;
    private List<Bitmap> bitmapList;
    private int uploadIndicator;

    private int radioSelectedIndicator;

    private ProgressDialog dialog;

    private OnSuccessListener mySuccessListener;

    private String paymentAgainstOBKey;
    private String paymentAgainstMemoKey;
    private Transaction memoTransaction;





    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentListener = (FragmentListener) getActivity();
        dueAmount=0;
        radioSelectedIndicator=0;
        uploadIndicator=0;

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        bitmapList = new ArrayList<>();
        attachmentAdapter = new ImageAdapter(getContext(),bitmapList);
        attachmentAdapter.setAttacmentListener(this);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");

        mySuccessListener = new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                UploadTask.TaskSnapshot snapshot = (UploadTask.TaskSnapshot) o;

                String url = String.valueOf(snapshot.getDownloadUrl());

                if(paymentAgainstOBKey!=null){
                    MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
                    myDatabaseReference.getPaymentOBAttachmentRef(paymentAgainstOBKey).push().setValue(url);
                }

                if(paymentAgainstMemoKey!=null){
                    MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
                    myDatabaseReference.getPaymentMemoAttachmentRef(paymentAgainstMemoKey).push().setValue(url);
                }


                uploadIndicator++;


                if(uploadIndicator==bitmapList.size()){
                    dialog.dismiss();

                    getFragmentManager().popBackStack();

                }

            }
        };


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        initView(view);
        return  view;
    }

    private void initView(View view) {
        radMemo = (RadioButton) view.findViewById(R.id.rad_memo);
        radOpeningBalance = (RadioButton) view.findViewById(R.id.rad_opening_balance);
        radioGroup = (RadioGroup) view.findViewById(R.id.radGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rad_memo:
                        radioSelectedIndicator=1;
                        rlSelectCustomer.setVisibility(View.GONE);
                        rlmemo.setVisibility(View.VISIBLE);
                        break;

                    case R.id.rad_opening_balance:
                        radioSelectedIndicator=0;
                        rlSelectCustomer.setVisibility(View.VISIBLE);
                        rlmemo.setVisibility(View.GONE);
                        break;
                }
            }
        });

        rlSelectCustomer = (RelativeLayout) view.findViewById(R.id.rl_select_customer);
        rlmemo = (RelativeLayout) view.findViewById(R.id.rl_memo_no);

        itvSelect = (IconTextView) view.findViewById(R.id.select_customer);
        itvDone = (IconTextView) view.findViewById(R.id.done);
        itvCamera = (IconTextView) view.findViewById(R.id.camera);
        itvAttachment = (IconTextView) view.findViewById(R.id.attachment);

        etCustomer = (MyEditText) view.findViewById(R.id.customer);
        etCustomer.setMyChangeListener(new MyChangeListener() {
            @Override
            public void textChange(String changeText) {
                Log.d("OPENINGBALANCE",selectedCustomer.getOpening_balance()+"");
            }
        });

        etDate = (MyEditText) view.findViewById(R.id.date);
        Date date = new Date();
        String dateStr = MyUtils.dateToString(date);
        etDate.setText(dateStr);

        etPayment = (MyEditText) view.findViewById(R.id.payment);
        etRemarks = (MyEditText) view.findViewById(R.id.remarks);
        etMemoNo = (MyEditText) view.findViewById(R.id.memo_no);

        sppaymentMethod = (MaterialSpinner) view.findViewById(R.id.payment_method);
        sppaymentMethod.setItems(getResources().getStringArray(R.array.payment_method));

        tvDue = (TextView) view.findViewById(R.id.due);

        // Attachment Recycler View
        int colCount = MyUtils.getScreenWidthInDp(getActivity())/110;
        rvAttachments = (RecyclerView) view.findViewById(R.id.rv_attachments);
        rvAttachments.setLayoutManager(new GridLayoutManager(getActivity(),colCount));
        //rvAttachments.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        rvAttachments.setAdapter(attachmentAdapter);

        btnPayment = (Button) view.findViewById(R.id.btn_payment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        itvSelect.setOnClickListener(this);
        itvDone.setOnClickListener(this);
        itvCamera.setOnClickListener(this);
        itvAttachment.setOnClickListener(this);
        etDate.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(fragmentListener!= null){
            fragmentListener.getFragment(64);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_customer:
                CustomerDialogFragment customerDialogFragment = new CustomerDialogFragment();
                customerDialogFragment.setTargetFragment(this,CUSTOMER_REQUEST_CODE);
                customerDialogFragment.show(getFragmentManager(), Constant.DEFAULAT_FRAGMENT_TAG);
                break;
            case R.id.done:

                String memoNumber = etMemoNo.getText().toString().trim();
                if(TextUtils.isEmpty(memoNumber)){
                    etMemoNo.requestFocus();
                    Toast.makeText(getActivity(), "Please Enter Memo Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.show();
                getTransaction(memoNumber);


                break;

            case R.id.date:
                MyUtils.showDialogAndSetTime(getActivity(),etDate);
                break;

            case R.id.btn_payment:

                String payment = etPayment.getText().toString().trim();
                String customerName = etCustomer.getText().toString().trim();

                if(TextUtils.isEmpty(payment)){
                    etPayment.requestFocus();
                    Toast.makeText(getActivity(), "Payment is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(radioSelectedIndicator==0){
                    if(TextUtils.isEmpty(customerName)){
                        etCustomer.requestFocus();
                        Toast.makeText(getActivity(), "Please Select a Customer", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }



                double paymentDoub = Double.parseDouble(payment);
                String[] paymentMethods= getResources().getStringArray(R.array.payment_method);
                String paymentMethod = paymentMethods[sppaymentMethod.getSelectedIndex()];
                String dateStr = etDate.getText().toString().trim();
                String remarks = etRemarks.getText().toString().trim();

                if(radioSelectedIndicator==0){

                    if(selectedCustomer!= null){
                        PaymentAgainstOB paymentAgainstOB = new PaymentAgainstOB(dateStr,paymentDoub,paymentMethod,remarks);

                        dialog.show();
                        postDatatotheServer(paymentAgainstOB);
                    }

                }else{
                    if(memoTransaction!= null){
                        PaymentAgainstMemo paymentAgainstMemo = new PaymentAgainstMemo(dateStr,paymentDoub,paymentMethod,memoTransaction.getId(),remarks);
                        dialog.show();
                        postDatatotheServer(paymentAgainstMemo);
                    }
                }
                break;

            case R.id.camera:
                checkCameraPermission();
                break;

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
        }
    }

    private void postDatatotheServer(PaymentAgainstOB paymentAgainstOB) {
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        DatabaseReference ref = myDatabaseReference.getPaymentRefOB(selectedCustomer.getId());
        ref.push().setValue(paymentAgainstOB, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                paymentAgainstOBKey = databaseReference.getKey();
                updateKey();
            }
        });
    }

    private void postDatatotheServer(PaymentAgainstMemo paymentAgainstMemo) {
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        DatabaseReference ref = myDatabaseReference.getPaymentRefAgainstMemo(memoTransaction.getId());
        ref.push().setValue(paymentAgainstMemo, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                paymentAgainstMemoKey = databaseReference.getKey();
                updateMemoPaymentKey();
            }
        });
    }

    private void updateKey(){
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        DatabaseReference ref = myDatabaseReference.getPaymentRefOB(selectedCustomer.getId());

        ref.child(paymentAgainstOBKey).child("id").setValue(paymentAgainstOBKey, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(bitmapList.size()==0){
                    dialog.dismiss();
                    getFragmentManager().popBackStack();
                }else{
                    // Upload Image If it has
                    uploadImage();
                }
            }
        });


    }

    private void updateMemoPaymentKey(){
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        DatabaseReference ref = myDatabaseReference.getPaymentRefAgainstMemo(memoTransaction.getId());

        ref.child(paymentAgainstMemoKey).child("id").setValue(paymentAgainstMemoKey, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(bitmapList.size()==0){
                    dialog.dismiss();
                    getFragmentManager().popBackStack();
                }else{
                    // Upload Image If it has
                    uploadImageMemoPaymentRef();
                }
            }
        });


    }



    private void uploadImage(){

        MyFirebaseStorage myFirebaseStorage = new MyFirebaseStorage();

        for(int i=0;i<bitmapList.size();i++){
            // Resize for Upload


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapList.get(i).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = myFirebaseStorage.getPaymentAgainstOB().child(paymentAgainstOBKey).child(String.valueOf(i)).putBytes(data);

            uploadTask.addOnSuccessListener(mySuccessListener);
        }

    }

    private void uploadImageMemoPaymentRef(){

        MyFirebaseStorage myFirebaseStorage = new MyFirebaseStorage();

        for(int i=0;i<bitmapList.size();i++){
            // Resize for Upload


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapList.get(i).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = myFirebaseStorage.getPaymentAgainstMemo().child(paymentAgainstMemoKey).child(String.valueOf(i)).putBytes(data);

            uploadTask.addOnSuccessListener(mySuccessListener);
        }

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

    void takePicture() {
        /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);*/

        RxImagePicker.with(getActivity()).requestImage(Sources.CAMERA).subscribe(new Action1<Uri>() {
            @Override
            public void call(Uri uri) {
                attachmentAdapter.addImage(MyUtils.uriToScaledBitmap(getActivity(),uri,150,150));
            }
        });
    }

    private void requestCameraPermission() {
        // Camera permission has not been granted yet. Request it directly.
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CUSTOMER_REQUEST_CODE:

                if(resultCode== Activity.RESULT_OK){

                    Bundle bundle = data.getExtras();

                    selectedCustomer = (Customer) bundle.getSerializable(Constant.CUSTOMER);

                    getPaymentAgainstOpeningBalance(selectedCustomer.getId());

                    etCustomer.setText(selectedCustomer.getName());



                }
                break;
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    private void getPaymentAgainstOpeningBalance(String custome_id){

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();

        myDatabaseReference.getPaymentRefOB(custome_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double payment =0;
                        for(DataSnapshot x : dataSnapshot.getChildren()){
                            PaymentAgainstOB paymentAgainstOB = x.getValue(PaymentAgainstOB.class);
                            payment = payment+paymentAgainstOB.getPayment();
                        }
                        tvDue.setText(String.valueOf(selectedCustomer.getOpening_balance()-payment));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    @Override
    public void onRemoveClick(int postion) {
        attachmentAdapter.removeImage(bitmapList.get(postion));

    }

    private void getTransaction(final String memoNumber){

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        myDatabaseReference.getTransactionRef(currentUser.getAssign_store_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot x: dataSnapshot.getChildren()){
                    Transaction transaction = x.getValue(Transaction.class);
                    if(transaction.getType()==1 && transaction.getMemo_voucher_id().equals(memoNumber)){
                        memoTransaction = transaction;
                        break;
                    }
                }


                if(memoTransaction==null){
                    tvDue.setText("Memo not Found in Database");
                    dialog.dismiss();
                }else{
                    double initialDue = memoTransaction.getTotal()-memoTransaction.getPayment_amount();
                    if(initialDue>0){
                        calculateDue(initialDue);
                    }else{
                        dueAmount= initialDue;
                        dialog.dismiss();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void calculateDue(final double initialDue){
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        myDatabaseReference.getPaymentRefAgainstMemo(memoTransaction.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double payment =0;
                for(DataSnapshot x: dataSnapshot.getChildren()){
                    PaymentAgainstMemo paymentAgainstMemo = x.getValue(PaymentAgainstMemo.class);
                    payment = payment+paymentAgainstMemo.getPayment();
                }

                dueAmount = initialDue-payment;
                tvDue.setText("Tk. "+dueAmount);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
