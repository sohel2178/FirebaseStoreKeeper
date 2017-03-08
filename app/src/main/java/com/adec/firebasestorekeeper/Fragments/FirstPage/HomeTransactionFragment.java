package com.adec.firebasestorekeeper.Fragments.FirstPage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adec.firebasestorekeeper.Adapter.TransactionAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.DetailFragment.DetailTransactionFragment;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Interface.HomeListener;
import com.adec.firebasestorekeeper.Model.Store;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.AllTransaction;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyStoreReferenceClass;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTransactionFragment extends Fragment implements MyStoreReferenceClass.StoreReferenceListener,
        AllTransaction.MapTransactionListener,View.OnClickListener,TransactionAdapter.TransactionListener{
    private HomeListener homeListener;

    private List<Transaction> transactionList;
    private List<Transaction> allList;
    private List<Transaction> tempList;
    private TransactionAdapter adapter;
    private RecyclerView rvTransaction;

    private TextView tvAll,tvVoucher,tvMemo;
    private int selectedButton=0;

    private User currentUser;

    private AllTransaction allTransaction;

    private DatabaseReference storeRef;

    private int storeCounter;

    private List<Store> storeList;


    public HomeTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_transaction, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvTransaction = (RecyclerView) view.findViewById(R.id.rv_transaction);
        rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));

        tvAll= (TextView) view.findViewById(R.id.all);
        tvVoucher= (TextView) view.findViewById(R.id.voucher);
        tvMemo= (TextView) view.findViewById(R.id.memo);
    }

    public void setHomeListener(HomeListener homeListener){
        this.homeListener = homeListener;
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
        storeCounter =0;

        if(homeListener!= null){
            homeListener.getFragment(200);
        }


        transactionList = new ArrayList<>();
        allList = new ArrayList<>();
        tempList = new ArrayList<>();
        adapter = new TransactionAdapter(getActivity(),transactionList);
        adapter.setTransactionListener(this);

        storeList = new ArrayList<>();

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        storeRef= myDatabaseReference.getStoreRef(currentUser.getId());
        MyStoreReferenceClass myStoreReferenceClass = new MyStoreReferenceClass(storeRef);
        myStoreReferenceClass.setStoreReferenceListener(this);

        rvTransaction.setAdapter(adapter);

        setBackground(selectedButton);
    }

    @Override
    public void getStore(Store store) {
        storeList.add(store);
        allTransaction = new AllTransaction(store);
        allTransaction.setMapTransactionListener(this);
    }

    @Override
    public void getStoreTransaction(String storeName, List<Transaction> transactionList,String store_id) {
        storeCounter++;
        this.tempList.addAll(transactionList);
        allList.addAll(transactionList);

        if(storeCounter== storeList.size()){
            Collections.sort(tempList, new Comparator<Transaction>() {
                @Override
                public int compare(Transaction o1, Transaction o2) {
                    return (int) (o2.getInsert_date()-o1.getInsert_date());
                }
            });
            this.transactionList.addAll(tempList);
            adapter.notifyDataSetChanged();
        }

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
                        Collections.sort(transactionList, new Comparator<Transaction>() {
                            @Override
                            public int compare(Transaction o1, Transaction o2) {
                                return (int) (o2.getInsert_date()-o1.getInsert_date());
                            }
                        });
                        adapter.notifyDataSetChanged();
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
    public void onClickTransaction(int position) {
        Transaction transaction = transactionList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TRANSACTION,transaction);
        DetailTransactionFragment detailTransactionFragment = new DetailTransactionFragment();
        detailTransactionFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.main_container,detailTransactionFragment)
                .addToBackStack(null).commitAllowingStateLoss();
    }
}
