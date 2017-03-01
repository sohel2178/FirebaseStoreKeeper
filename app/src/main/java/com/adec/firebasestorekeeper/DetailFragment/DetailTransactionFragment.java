package com.adec.firebasestorekeeper.DetailFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.adec.firebasestorekeeper.Adapter.AttachmentAdapter;
import com.adec.firebasestorekeeper.Adapter.ImageAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailTransactionFragment extends Fragment implements AttachmentAdapter.MyAttachmentListener,View.OnClickListener{

    private MyEditText etDate,etId,etHead,etProductName,etQuantity,etUnitPrice,etTotal,
        etCustomer,etPayTo,etPayment,etSalesMan,etPaymentMethod,etStoreName,etRemarks;

    private RelativeLayout rlProductName,rlQuantity,rlUnitPrice,rlTotal,rlCustomer,rlSalesMan,
                rlPayTo,rlPayment,rlHead;

    private ImageView ivClose;

    private RecyclerView rvAttachments;
    private AttachmentAdapter attachmentAdapter;
    private List<String> urlList;

    private Transaction transaction;

    private User currentUser;

    private FragmentListener fragmentListener;

    private int colCount;


    public DetailTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser= userLocalStore.getUser();

        fragmentListener = (FragmentListener) getActivity();

        if(getArguments()!= null){
            transaction = (Transaction) getArguments().getSerializable(Constant.TRANSACTION);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_detail_transaction, container, false);
        initView(view);

        if(transaction.getType()==0){
            rlProductName.setVisibility(View.GONE);
            rlQuantity.setVisibility(View.GONE);
            rlUnitPrice.setVisibility(View.GONE);
            rlTotal.setVisibility(View.GONE);
            rlCustomer.setVisibility(View.GONE);
            rlSalesMan.setVisibility(View.GONE);
        }else if(transaction.getType()==1){
            rlPayTo.setVisibility(View.GONE);
            rlPayment.setVisibility(View.GONE);
            rlHead.setVisibility(View.GONE);

        }

        setData();
        return view;
    }

    private void setData() {
        etDate.setText(transaction.getDate());
        etId.setText(transaction.getMemo_voucher_id());
        if(transaction.getType()==0){
            etHead.setText(transaction.getExpenditure_head());
            etPayTo.setText(transaction.getPay_to());
            etPayment.setText(String.valueOf(transaction.getPayment_amount()));
        }else if(transaction.getType()==1){
            etProductName.setText(transaction.getProduct_name());
            etQuantity.setText(String.valueOf(transaction.getQuantity()));
            etUnitPrice.setText(String.valueOf(transaction.getUnit_price()));
            etTotal.setText(String.valueOf(transaction.getTotal()));
        }
        etPaymentMethod.setText(transaction.getPayment_method());
        etRemarks.setText(transaction.getRemarks());
    }

    private void initView(View view) {

        colCount = MyUtils.getScreenWidthInDp(getActivity())/110;
        Log.d("DDD",colCount+"");
        rvAttachments = (RecyclerView) view.findViewById(R.id.rv_attachments);
        rvAttachments.setLayoutManager(new GridLayoutManager(getActivity(),colCount));
        //rvAttachments.setAdapter(attachmentAdapter);

        /*,,,,,,,
                ,,,,,,;*/

        etDate = (MyEditText) view.findViewById(R.id.date);
        etId = (MyEditText) view.findViewById(R.id.voucher_memo_id);
        etHead = (MyEditText) view.findViewById(R.id.exp_head);
        etProductName = (MyEditText) view.findViewById(R.id.product_name);
        etQuantity = (MyEditText) view.findViewById(R.id.quantity);
        etUnitPrice = (MyEditText) view.findViewById(R.id.unit_price);
        etTotal = (MyEditText) view.findViewById(R.id.total);
        etCustomer = (MyEditText) view.findViewById(R.id.customer);
        etPayTo = (MyEditText) view.findViewById(R.id.pay_to);
        etPayment = (MyEditText) view.findViewById(R.id.payment);
        etSalesMan = (MyEditText) view.findViewById(R.id.sales_person);
        etPaymentMethod = (MyEditText) view.findViewById(R.id.payment_method);
        etStoreName = (MyEditText) view.findViewById(R.id.store_name);
        etRemarks = (MyEditText) view.findViewById(R.id.remarks);



        rlProductName = (RelativeLayout) view.findViewById(R.id.rl_product_name);
        rlQuantity = (RelativeLayout) view.findViewById(R.id.rl_quantity);
        rlUnitPrice = (RelativeLayout) view.findViewById(R.id.rl_unit_price);
        rlTotal = (RelativeLayout) view.findViewById(R.id.rl_total);
        rlCustomer = (RelativeLayout) view.findViewById(R.id.rl_customer);
        rlSalesMan = (RelativeLayout) view.findViewById(R.id.rl_sales_man);
        rlPayTo = (RelativeLayout) view.findViewById(R.id.rl_pay_to);
        rlPayment = (RelativeLayout) view.findViewById(R.id.rl_payment);
        rlHead = (RelativeLayout) view.findViewById(R.id.rl_head);

        ivClose = (ImageView) view.findViewById(R.id.close);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ivClose.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        urlList = new ArrayList<>();
        attachmentAdapter = new AttachmentAdapter(getActivity(),urlList);
        attachmentAdapter.setMyAttachmentListener(this);

        rvAttachments.setAdapter(attachmentAdapter);

        fragmentListener.getFragment(63);

        String owner_id=null;
        if(currentUser.getUser_type()==0){
            owner_id= currentUser.getId();
        }else if(currentUser.getUser_type()==1){
            owner_id= currentUser.getParent_id();
        }




        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        myDatabaseReference.getCustomerRef(owner_id).child(transaction.getCustomer_id()).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.getValue(String.class);
                        etCustomer.setText(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        if(transaction.getType()==1 ){

            myDatabaseReference.getEmployeeReference(owner_id).child(transaction.getSales_person_id()).child("name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.getValue(String.class);
                            etSalesMan.setText(name);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }

        // Store Id

        if(transaction.getStore_id()!= null){
            myDatabaseReference.getStoreRef(owner_id).child(transaction.getStore_id()).child("name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.getValue(String.class);
                            etStoreName.setText(name);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }



        // for Attachment
        myDatabaseReference.getAttachmentRererence(transaction.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot x : dataSnapshot.getChildren()){
                            String url = x.getValue(String.class);
                            attachmentAdapter.add(url);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




    }

    @Override
    public void getHeight(int height) {
        ViewGroup.LayoutParams params=rvAttachments.getLayoutParams();
        params.height=height*getNumberOfRows();
        rvAttachments.setLayoutParams(params);
    }

    @Override
    public void onItemClick(int position) {
        String url = urlList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.URI,url);

        ImageDetailFragment imageDetailFragment = new ImageDetailFragment();
        imageDetailFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.main_container,imageDetailFragment)
                .addToBackStack(null).commitAllowingStateLoss();

    }

    private int getNumberOfRows(){
        int a = urlList.size()/colCount;
        double b = (double) urlList.size()/(double) colCount;
        if(a<b){
            return a+1;
        }else{
           return a;
        }
    }

    @Override
    public void onClick(View v) {
        getFragmentManager().popBackStack();
    }
}
