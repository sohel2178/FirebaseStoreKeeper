package com.adec.firebasestorekeeper.DetailFragment;


import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.adec.firebasestorekeeper.Adapter.TransactionAdapter;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.CustomListener.MyChangeListener;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.Model.OwnerDataModel;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomizeReportFragment extends Fragment implements View.OnClickListener {
    private static final String TAG="SOHEL";

    private ActionBar actionBar;

    private RadioButton rbToday,rbYesterday,rbDateRange,rbAllStore,rbSingleStore,rbAll,rbVoucher,rbMemo;
    private RadioGroup rgStore,rgTransaction,rgDate;
    private MyEditText etFirstDate,etLastDate;

    private LinearLayout llDateContainer,llMyContainer;
    private ImageView ivExpandCollapse;
    private MaterialSpinner spStore;
    private RecyclerView rvTransaction;
    private List<Transaction> adapterList;
    private TransactionAdapter adapter;

    private int selectedStore,selectedDate,selectedTransaction;
    private List<OwnerDataModel> ownerDataModelList;
    private List<Transaction> transactionList;

    private List<String> storeNames;

    private String today,yesterday;
    private Date firstDate,lastDate;

    private Subscription subscription;

    private RadioGroup.OnCheckedChangeListener changeListener= new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (group.getId()){
                case R.id.rg_date:
                    switch (checkedId){
                        case R.id.rad_today:
                            llDateContainer.setVisibility(View.GONE);
                            selectedDate=0;
                            getFilteredList(selectedStore,selectedDate,selectedTransaction);
                            break;

                        case R.id.rad_yesterday:
                            llDateContainer.setVisibility(View.GONE);
                            selectedDate=1;
                            getFilteredList(selectedStore,selectedDate,selectedTransaction);
                            break;

                        case R.id.rad_date_range:
                            llDateContainer.setVisibility(View.VISIBLE);
                            etFirstDate.setText(MyUtils.dateToString(firstDate));
                            etLastDate.setText(MyUtils.dateToString(lastDate));

                            selectedDate=2;
                            getFilteredList(selectedStore,selectedDate,selectedTransaction);
                            break;
                    }
                    break;

                case R.id.rg_store:
                    switch (checkedId){
                        case R.id.rad_all_store:
                            spStore.setVisibility(View.INVISIBLE);
                            selectedStore=0;
                            getFilteredList(selectedStore,selectedDate,selectedTransaction);
                            break;
                        case R.id.rad_single_store:
                            spStore.setVisibility(View.VISIBLE);
                            selectedStore=1;
                            getFilteredList(selectedStore,selectedDate,selectedTransaction);
                            break;
                    }
                    break;

                case R.id.rg_transaction:
                    switch (checkedId){
                        case R.id.rad_all:
                            selectedTransaction=0;
                            getFilteredList(selectedStore,selectedDate,selectedTransaction);
                            break;

                        case R.id.rad_voucher:
                            selectedTransaction=1;
                            getFilteredList(selectedStore,selectedDate,selectedTransaction);
                            break;

                        case R.id.rad_memo:
                            selectedTransaction=2;
                            getFilteredList(selectedStore,selectedDate,selectedTransaction);
                            break;
                    }
                    break;
            }
        }
    };


    public CustomizeReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(getArguments()!= null){
            ownerDataModelList = (List<OwnerDataModel>) getArguments().getSerializable("data");
            getStoreNames();
            getAllTransaction();
            Log.d(TAG,"ListSize: "+transactionList.size());

            List<Date> sortedDateList =MyUtils.getSortedDateList(transactionList);

            if(sortedDateList.size()>0){
                firstDate = sortedDateList.get(0);
                lastDate = sortedDateList.get(sortedDateList.size()-1);

                Log.d("GGGGG",firstDate.compareTo(firstDate)+"");
                Log.d("GGGGG",firstDate.compareTo(lastDate)+"");
                Log.d("GGGGG",lastDate.compareTo(firstDate)+"");
            }



            Log.d(TAG,"First Date "+firstDate);
            Log.d(TAG,"Last Date "+lastDate);
        }

        Date date = new Date();
        today = MyUtils.dateToString(date);
        yesterday =MyUtils.dateToString(MyUtils.incrementDate(-1,date));

        Log.d(TAG,"Today: "+today);
        Log.d(TAG,"Yesterday: "+yesterday);

        selectedDate=0;
        selectedStore=0;
        selectedTransaction=0;
    }

    private void getStoreNames() {
        storeNames = new ArrayList<>();
        for(OwnerDataModel x : ownerDataModelList){
            storeNames.add(x.getStore_name());

            Log.d(TAG,x.getStore_name());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_customize_report, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rgDate = (RadioGroup) view.findViewById(R.id.rg_date);
        rgStore = (RadioGroup) view.findViewById(R.id.rg_store);
        rgTransaction = (RadioGroup) view.findViewById(R.id.rg_transaction);

        rgDate.setOnCheckedChangeListener(changeListener);
        rgStore.setOnCheckedChangeListener(changeListener);
        rgTransaction.setOnCheckedChangeListener(changeListener);

        rbToday = (RadioButton) view.findViewById(R.id.rad_today);
        rbYesterday = (RadioButton) view.findViewById(R.id.rad_yesterday);
        rbDateRange = (RadioButton) view.findViewById(R.id.rad_date_range);
        rbAllStore = (RadioButton) view.findViewById(R.id.rad_all_store);
        rbSingleStore = (RadioButton) view.findViewById(R.id.rad_single_store);
        rbAll = (RadioButton) view.findViewById(R.id.rad_all);
        rbVoucher = (RadioButton) view.findViewById(R.id.rad_voucher);
        rbMemo = (RadioButton) view.findViewById(R.id.rad_memo);

        // Edit Text
        etFirstDate = (MyEditText) view.findViewById(R.id.first_date);
        etFirstDate.setMyChangeListener(new MyChangeListener() {
            @Override
            public void textChange(String changeText) {
                firstDate = MyUtils.getDateFromString(changeText);
                getFilteredList(selectedStore,selectedDate,selectedTransaction);
            }
        });
        etLastDate = (MyEditText) view.findViewById(R.id.last_date);
        etLastDate.setMyChangeListener(new MyChangeListener() {
            @Override
            public void textChange(String changeText) {
                lastDate = MyUtils.getDateFromString(changeText);
                getFilteredList(selectedStore,selectedDate,selectedTransaction);

            }
        });



        spStore = (MaterialSpinner) view.findViewById(R.id.sp_store);
        spStore.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                // Call the Method Again
                getFilteredList(selectedStore,selectedDate,selectedTransaction);
            }
        });


        llDateContainer = (LinearLayout) view.findViewById(R.id.date_container);
        llMyContainer = (LinearLayout) view.findViewById(R.id.my_container);

        ivExpandCollapse = (ImageView) view.findViewById(R.id.expand_collapse);

        rvTransaction = (RecyclerView) view.findViewById(R.id.rv_transaction);
        rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void getAllTransaction(){
        transactionList = new ArrayList<>();
        for(OwnerDataModel x: ownerDataModelList){

            List<Transaction> strTrans = x.getTransactionList();
            String stId = x.getStore_id();

            for(Transaction y : strTrans){
                y.setStore_id(stId);
                transactionList.add(y);
            }

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etFirstDate.setOnClickListener(this);
        etLastDate.setOnClickListener(this);
        ivExpandCollapse.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle("Customize Report");
        spStore.setItems(storeNames);

        adapterList = new ArrayList<>();
        adapter = new TransactionAdapter(getActivity(),adapterList);

        rvTransaction.setAdapter(adapter);

        getFilteredList(selectedStore,selectedDate,selectedTransaction);

    }


    private void getFilteredList(final int selectedStore, final int selectedDate, final int selectedTransaction){

        if(adapterList!= null){

            adapterList.clear();

            final List<Transaction> tempList= new ArrayList<>();
            Observable<Transaction> observable =Observable.from(transactionList)
                    .filter(new Func1<Transaction, Boolean>() {
                        @Override
                        public Boolean call(Transaction transaction) {
                            boolean bool=false;
                            switch (selectedStore){
                                case 0:
                                    bool=true;
                                    break;

                                case 1:
                                    bool =ownerDataModelList.get(spStore.getSelectedIndex()).getStore_id().equals(transaction.getStore_id());
                                    break;
                            }
                            return bool;
                        }
                    }).filter(new Func1<Transaction, Boolean>() {
                        @Override
                        public Boolean call(Transaction transaction) {
                            boolean bool = false;
                            switch (selectedTransaction){
                                case 0:
                                    bool = true;
                                    break;

                                case 1:
                                    bool=(transaction.getType()==0);
                                    break;

                                case 2:
                                    bool=(transaction.getType()==1);
                                    break;
                            }
                            return bool;
                        }
                    }).filter(new Func1<Transaction, Boolean>() {
                        @Override
                        public Boolean call(Transaction transaction) {
                            boolean bool = false;

                            switch (selectedDate){
                                case 0:
                                    bool=(transaction.getDate().equals(today));
                                    break;
                                case 1:
                                    bool=(transaction.getDate().equals(yesterday));
                                    break;
                                case 2:
                                    Date thisDate =MyUtils.getDateFromString(transaction.getDate());
                                    bool = thisDate.compareTo(firstDate)>=0 && thisDate.compareTo(lastDate)<=0;
                                    break;
                            }
                            return bool;
                        }
                    });

            subscription = observable.subscribe(new Action1<Transaction>() {
                @Override
                public void call(Transaction transaction) {
                    tempList.add(transaction);
                }
            });

            Collections.sort(tempList, new Comparator<Transaction>() {
                @Override
                public int compare(Transaction o1, Transaction o2) {
                    return (int) (o2.getInsert_date()-o1.getInsert_date());
                }
            });

            adapterList.addAll(tempList);
            adapter.notifyDataSetChanged();

        }


    }

    @Override
    public void onPause() {
        if(!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.first_date:
                MyUtils.showDialogAndSetTime(getActivity(),etFirstDate);
                break;
            case R.id.last_date:
                MyUtils.showDialogAndSetTime(getActivity(),etLastDate);
                break;

            case R.id.expand_collapse:
                Drawable collapse = ResourcesCompat.getDrawable(getResources(), R.drawable.collapse, null);

                if(ivExpandCollapse.getDrawable().getConstantState().equals(collapse.getConstantState())){
                    collapseView();
                }else{

                    expandView();
                }
                break;
        }
    }

    private void collapseView(){
        ivExpandCollapse.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.expand,null));
        llMyContainer.setVisibility(View.GONE);
    }

    private void expandView(){
        ivExpandCollapse.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.collapse,null));
        llMyContainer.setVisibility(View.VISIBLE);
    }


}
