package com.adec.firebasestorekeeper.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.adec.firebasestorekeeper.Adapter.AutoCompleteAdapter.ProductAdapter;
import com.adec.firebasestorekeeper.Adapter.DialogFragmenAdapter.CustomerDialogAdapter;
import com.adec.firebasestorekeeper.Adapter.ImageAdapter;
import com.adec.firebasestorekeeper.Adapter.LayoutAdapter;
import com.adec.firebasestorekeeper.Adapter.SpinnerAdapter.SalesManSpinnerAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomView.MyAutoCompleteTextView;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.DialogFragment.CustomerDialogFragment;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Model.Memo;
import com.adec.firebasestorekeeper.Model.MyLayout;
import com.adec.firebasestorekeeper.Model.Product;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Model.Voucher;
import com.adec.firebasestorekeeper.Navigation.TransactionFragment;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.SqlDatabase.ProductDatabase;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.MyFirebaseStorage;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyCustomerReferenceClass;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyEmployeeReferenceClass;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyProductReferenceClass;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyUserReferenceClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.joanzapata.iconify.widget.IconTextView;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMemo extends Fragment implements View.OnClickListener,MyEmployeeReferenceClass.EmployeeReferenceListener,
        ImageAdapter.AttachmentListener{

    private static final int CUSTOMER_REQUEST_CODE=12;

    private static final int REQUEST_CAMERA = 0;

    private FragmentListener fragmentListener;


    private EditText etMemoNumber,etTotal,etPayment,etDate,etRemarks,etCustomer;
    private MaterialSpinner spSalesMan,spPaymentMethod;

    private IconTextView itvCalendar,itvAttach,itvCamera,itvSelectCustomer;

    private ImageView ivAddProduct;

    private RecyclerView rvAttachments,rvProduct;

    private List<MyLayout> myLayoutList;
    private LayoutAdapter layoutAdapter;
    private ImageAdapter attachmentAdapter;
    private List<Bitmap> bitmapList;

    private List<String> paymentMethods;
    private List<Product> productList;



    private Button btnAdd;


    private DatabaseReference transactionRef;
    private MyFirebaseStorage myFirebaseStorage;

    private ProgressDialog dialog;

    private User currentUser;

    private List<User> salesManList;
    private List<String> salesManName;


    private Customer selectedCustomer;

    private String transaction_id;

    private OnSuccessListener mySuccessListener;

    MyDatabaseReference myDatabaseReference;

    private int uploadIndicator;


    private String store_id;


    public AddMemo() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentListener = (FragmentListener) getActivity();
        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        store_id = currentUser.getAssign_store_id();

        myDatabaseReference = new MyDatabaseReference();
        transactionRef = myDatabaseReference.getTransactionRef(store_id);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");

        myFirebaseStorage = new MyFirebaseStorage();


        salesManList= new ArrayList<>();
        salesManName = new ArrayList<>();
        //salesManSpinnerAdapter = new SalesManSpinnerAdapter(getActivity(),R.layout.single_sales_man_spinner,salesManList);


        MyEmployeeReferenceClass myEmployeeReferenceClass = new MyEmployeeReferenceClass(currentUser.getParent_id());
        myEmployeeReferenceClass.setEmployeeReferenceListener(this);

        paymentMethods = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.payment_method))) ;

        myLayoutList = new ArrayList<>();
        layoutAdapter = new LayoutAdapter(getActivity(),myLayoutList);
        layoutAdapter.addItem(new MyLayout());
        layoutAdapter.setLayoutListener(new LayoutAdapter.LayoutListener() {
            @Override
            public void onCloseClick(int position) {
                layoutAdapter.removeItem(position);
            }
        });



        //initList();


        // Work for Image Recycler View
        bitmapList = new ArrayList<>();
        attachmentAdapter = new ImageAdapter(getContext(),bitmapList);
        attachmentAdapter.setAttacmentListener(this);

        uploadIndicator=0;

        // OnSuuccess Listener
        mySuccessListener = new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                UploadTask.TaskSnapshot snapshot = (UploadTask.TaskSnapshot) o;

                @SuppressWarnings("VisibleForTests")
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
    public void onResume() {
        super.onResume();



        if(fragmentListener!= null){
            fragmentListener.getFragment(62);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_memo, container, false);

        initView(view);

        Date date = new Date();
        String dateStr = MyUtils.dateToString(date);

        etDate.setText(dateStr);

        return view;
    }

    private void initView(View view) {
       /* acProductName = (AutoCompleteTextView) view.findViewById(R.id.product_name);
        acProductName.setThreshold(0);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line,productList);
        acProductName.setAdapter(adapter);*/

        // All EditTest init Here
        etMemoNumber = (EditText) view.findViewById(R.id.memo_no);
        etCustomer = (EditText) view.findViewById(R.id.customer);
        etTotal = (EditText) view.findViewById(R.id.total);
        etPayment = (EditText) view.findViewById(R.id.payment);
        spPaymentMethod = (MaterialSpinner) view.findViewById(R.id.payment_method);
        spPaymentMethod.setItems(paymentMethods);
        etDate = (EditText) view.findViewById(R.id.date);
        etRemarks = (EditText) view.findViewById(R.id.remarks);

        // Icon Text View Init Here
        itvCalendar = (IconTextView) view.findViewById(R.id.calendar);
        itvAttach = (IconTextView) view.findViewById(R.id.attachment);
        itvCamera = (IconTextView) view.findViewById(R.id.camera);
        itvSelectCustomer = (IconTextView) view.findViewById(R.id.select_customer);

        // Image Button
        ivAddProduct = (ImageView) view.findViewById(R.id.add_product);



        btnAdd = (Button) view.findViewById(R.id.add);

        // Spinner Init Here

        //spCustomer,spSalesMan,spPaymentMethod;


        spSalesMan = (MaterialSpinner) view.findViewById(R.id.sales_person);
        //spSalesMan.getBackground().setColorFilter(ResourcesCompat.getColor(getResources(),R.color.my_color,null), PorterDuff.Mode.SRC_ATOP);
        //spSalesMan.setAdapter(salesManSpinnerAdapter);

        // Attachment Recycler View
        int colCount = MyUtils.getScreenWidthInDp(getActivity())/110;
        rvAttachments = (RecyclerView) view.findViewById(R.id.rv_attachments);
        rvAttachments.setLayoutManager(new GridLayoutManager(getActivity(),colCount));
        //rvAttachments.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        rvAttachments.setAdapter(attachmentAdapter);

        rvProduct = (RecyclerView) view.findViewById(R.id.rv_product);
        rvProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProduct.setAdapter(layoutAdapter);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        itvAttach.setOnClickListener(this);
        itvCalendar.setOnClickListener(this);
        itvCamera.setOnClickListener(this);
        itvSelectCustomer.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        ivAddProduct.setOnClickListener(this);
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
            case R.id.select_customer:

                CustomerDialogFragment customerDialogFragment = new CustomerDialogFragment();
                customerDialogFragment.setTargetFragment(this,CUSTOMER_REQUEST_CODE);
                customerDialogFragment.show(getFragmentManager(), Constant.DEFAULAT_FRAGMENT_TAG);
                break;

            case R.id.add:
                MyUtils.hideKey(view);
                String date = etDate.getText().toString().trim();
                String memo_no = etMemoNumber.getText().toString().trim();
                /*String productName = acProductName.getText().toString().trim();
                String quantityStr= etQuantity.getText().toString().trim();
                String unitPriceStr = etUnitPrice.getText().toString();*/
                String totalStr = etTotal.getText().toString().trim();




                productList = layoutAdapter.getData();

                String payment = etPayment.getText().toString().trim();
                String paymentMethod = paymentMethods.get(spPaymentMethod.getSelectedIndex());

                // Todo later Sales Person Id
                String salesPersonId = salesManList.get(spSalesMan.getSelectedIndex()).getId();
                Log.d("HHHH",salesPersonId);
                String remarks = etRemarks.getText().toString().trim();


                if(TextUtils.isEmpty(memo_no)){
                    etMemoNumber.requestFocus();
                    Toast.makeText(getActivity(), "Memo is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }



                if(TextUtils.isEmpty(totalStr)){
                    etTotal.requestFocus();
                    Toast.makeText(getActivity(), "Total is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(payment)){
                    etPayment.requestFocus();
                    Toast.makeText(getActivity(), "Total is empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                String customerId = null;

                if(selectedCustomer != null){
                    customerId = selectedCustomer.getId();
                }else{
                    etCustomer.requestFocus();
                    Toast.makeText(getActivity(), "You should a Customer", Toast.LENGTH_SHORT).show();
                }

                double total = Double.parseDouble(totalStr);
                double paymentAmount = Double.parseDouble(payment);

                /*if(TextUtils.isEmpty(amount)){
                    etAmount.requestFocus();
                    Toast.makeText(getActivity(), "Amount is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Voucher voucher = new Voucher(voucher_no,date,head,payTo,Double.parseDouble(amount),paymentMethod,remarks);
                dialog.show();
                postDatatotheServer(voucher);*/

                if(customerId != null){
                    Memo memo = new Memo(memo_no,date,"",0,0,total,customerId,paymentAmount,salesPersonId,paymentMethod,remarks,store_id);
                    dialog.show();
                    postDatatotheServer(memo);
                }

                break;

            case R.id.add_product:
                layoutAdapter.addItem(new MyLayout());
                break;
        }

    }

    private void postDatatotheServer(Memo memo) {
        transactionRef.push().setValue(memo, new DatabaseReference.CompletionListener() {
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

                if(productList.size()>0){
                    uploadProduct();
                }



                if(bitmapList.size()==0){
                    dialog.dismiss();
                    getFragmentManager().popBackStack();

                    getFragmentManager().beginTransaction().replace(R.id.main_container,new TransactionFragment())
                            .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();
                }else{
                    // Upload Image If it has
                    uploadImage();
                }

            }
        });


    }

    private void uploadProduct(){
        DatabaseReference prodRef = myDatabaseReference.getProductReference(currentUser.getParent_id()).child(transaction_id);

        for(Product x: productList){
            prodRef.push().setValue(x);
        }
    }

    private void uploadImage(){

        for(int i=0;i<bitmapList.size();i++){
            // Resize for Upload


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapList.get(i).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = myFirebaseStorage.getMemoImageReference().child(transaction_id).child(String.valueOf(i)).putBytes(data);

            uploadTask.addOnSuccessListener(mySuccessListener);
        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CUSTOMER_REQUEST_CODE:

                if(resultCode== Activity.RESULT_OK){

                    Bundle bundle = data.getExtras();

                    selectedCustomer = (Customer) bundle.getSerializable(Constant.CUSTOMER);

                    etCustomer.setText(selectedCustomer.getName());



                }
                break;
        }
    }

    @Override
    public void onRemoveClick(int postion) {

        attachmentAdapter.removeImage(bitmapList.get(postion));

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                takePicture();

            } else {
                //Permission not granted
                //Toast.makeText(MainActivity.this,"You need to grant camera permission to use camera",Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void getEmployee(User user) {

        if(user.getUser_type()==2 && user.getAssign_store_id().equals(currentUser.getAssign_store_id())){
            salesManList.add(user);
            salesManName.add(user.getName());
            //salesManSpinnerAdapter.notifyDataSetChanged();

            spSalesMan.setItems(salesManName);
        }

    }


}
