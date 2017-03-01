package com.adec.firebasestorekeeper.Navigation;


import android.app.ProgressDialog;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adec.firebasestorekeeper.Adapter.TransactionAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyFloatingAction;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.DetailFragment.DetailTransactionFragment;
import com.adec.firebasestorekeeper.DetailFragment.VoucherDetailFragment;
import com.adec.firebasestorekeeper.Fragments.AddMemo;
import com.adec.firebasestorekeeper.Fragments.AddVoucher;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyTransactionReferenceClass;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment implements
        MyTransactionReferenceClass.TransactionReferenceListener,TransactionAdapter.TransactionListener,
        View.OnClickListener{

    private RecyclerView rvTransaction;

    private List<Transaction> allList;
    private List<Transaction> transactionList;
    private TransactionAdapter adapter;

    private TextView tvAll,tvVoucher,tvMemo;

    private int selectedButton=0;

   // private MyFloatingAction myFloatingAction;



    private User currentUser;


    private MyTransactionReferenceClass myTransactionReferenceClass;

    private FragmentListener fragmentListener;




    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!MyUtils.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            //return;
        }

        fragmentListener = (FragmentListener) getActivity();

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_transaction, container, false);
        
        initView(view);
        return view;
    }

    private void initView(View view) {
        //buildFloatingActionMenu();

        rvTransaction = (RecyclerView) view.findViewById(R.id.rv_transaction);
        rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));

        tvAll= (TextView) view.findViewById(R.id.all);
        tvVoucher= (TextView) view.findViewById(R.id.voucher);
        tvMemo= (TextView) view.findViewById(R.id.memo);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvAll.setOnClickListener(this);
        tvVoucher.setOnClickListener(this);
        tvMemo.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
       /* myFloatingAction= new MyFloatingAction(getActivity());
        myFloatingAction.setFloatingActionMenuListener(this);*/

        if(fragmentListener!= null){
            fragmentListener.getFragment(6);
        }

        allList= new ArrayList<>();


        myTransactionReferenceClass = new MyTransactionReferenceClass(currentUser.getAssign_store_id());
        myTransactionReferenceClass.setTransactionReferenceListener(this);

        transactionList = new ArrayList<>();
        adapter = new TransactionAdapter(getActivity(),transactionList);
        adapter.setTransactionListener(this);
        // Set Adapter in OnResume
        rvTransaction.setAdapter(adapter);

        setBackground(selectedButton);


    }

    @Override
    public void onPause() {
        adapter.removeAttachmentListener();
        myTransactionReferenceClass.removeListener();
        super.onPause();

    }

    private void buildFloatingActionMenu(){
        ImageView icon = new ImageView(getActivity()); // Create an icon
        icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.plus,null));

        final FloatingActionButton actionButton = new FloatingActionButton.Builder(getActivity())
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
// repeat many times:
        ImageView itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.plus,null));

        ImageView itemIcon2 = new ImageView(getActivity());
        itemIcon2.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.plus,null));

        ImageView itemIcon3 = new ImageView(getActivity());
        itemIcon3.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.plus,null));

        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();

        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .build();



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                getFragmentManager().beginTransaction().replace(R.id.main_container,new AddVoucher())
                        .addToBackStack(null).commit();

                actionMenu.close(true);
                actionButton.setVisibility(View.GONE);

            }
        });
    }

    private void setBackground(int selectedButton){
        setDefaultColor();

        switch (selectedButton){
            case 0:
                tvAll.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDark,null));
                break;
            case 1:
                tvVoucher.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDark,null));
                break;
            case 2:
                tvMemo.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDark,null));
                break;
        }
    }

    private void setDefaultColor(){
        tvAll.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null));
        tvVoucher.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null));
        tvMemo.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null));
    }







    @Override
    public void getTransactions(List<Transaction> transactions) {
        /*Collections.reverse(transactions);
        transactionList.addAll(transactions);
        adapter.notifyDataSetChanged();
        dialog.dismiss();*/

        //myTransactionReferenceClass.triggerChildEvent();
    }

    @Override
    public void addTransaction(Transaction transaction) {
        allList.add(transaction);
        adapter.addTransaction(transaction);
    }

    @Override
    public void onClickTransaction(int position) {
        Transaction transaction = transactionList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TRANSACTION,transaction);
        DetailTransactionFragment detailTransactionFragment = new DetailTransactionFragment();
        detailTransactionFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.main_container,detailTransactionFragment)
                .addToBackStack(null).commitAllowingStateLoss();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all:
                selectedButton=0;

                break;
            case R.id.voucher:
                selectedButton=1;
                break;
            case R.id.memo:
                selectedButton=2;
                break;
        }
        setBackground(selectedButton);
        doThat(selectedButton);
    }

    private void doThat(final int selectedButton){
        transactionList.clear();
        Subscription subscription = Observable.from(allList)
                .filter(new Func1<Transaction, Boolean>() {
                    @Override
                    public Boolean call(Transaction transaction) {
                        boolean bool= false;
                        switch (selectedButton){
                            case 0:
                                bool=true;
                                break;

                            case 1:
                                bool= (transaction.getType()==0);
                                break;

                            case 2:
                                bool= (transaction.getType()==1);
                                break;
                        }

                        return bool;

                    }
                }).subscribe(new Action1<Transaction>() {
                    @Override
                    public void call(Transaction transaction) {
                        Log.d("SOHEL",transaction.getMemo_voucher_id());
                        transactionList.add(transaction);
                        Collections.reverse(transactionList);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
