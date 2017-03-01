package com.adec.firebasestorekeeper.Fragments.Report;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.adec.firebasestorekeeper.Adapter.ReportAdapter.DailyReportAdapter;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.DetailFragment.DetailReportFragment;
import com.adec.firebasestorekeeper.Model.OwnerDataModel;
import com.adec.firebasestorekeeper.Model.Report.StoreModel;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyReportFragment extends Fragment implements DailyReportAdapter.ReportListener{

    private String dateStr;

    private List<OwnerDataModel> ownerDataModelList;

    private List<StoreModel> storeModels;
    private DailyReportAdapter adapter;

    // View Declaration;
    private RecyclerView rvStores;
    private int selectedMonth,selectedYear;

    private List<Integer> yearList;

    private MaterialSpinner spYear,spMonth;

    private List<String> monthList =Arrays.asList("Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec");

    private Subscription mySub;


    public MonthlyReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Date date = new Date();
        dateStr = MyUtils.dateToString(date);

        selectedMonth = MyUtils.getMonthNumber(dateStr);
        selectedYear = MyUtils.getYear(dateStr);


        if(getArguments() != null){
            ownerDataModelList = (List<OwnerDataModel>) getArguments().getSerializable("data");

            //test();
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monthly_report, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {

        spYear = (MaterialSpinner) view.findViewById(R.id.year_spinner);
        spMonth = (MaterialSpinner) view.findViewById(R.id.month_spinner);
        spMonth.setItems(monthList);
        spMonth.setSelectedIndex(selectedMonth);

        rvStores = (RecyclerView) view.findViewById(R.id.rv_store);
        rvStores.setLayoutManager(new LinearLayoutManager(getActivity()));
        //rvStores.setAdapter(adapter);

        spYear.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedYear = (int) item;
                processData();
            }
        });

        spMonth.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedMonth = position;
                processData();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        storeModels = new ArrayList<>();
        adapter = new DailyReportAdapter(getActivity(),storeModels);
        adapter.setReportListener(this);
        rvStores.setAdapter(adapter);

        Observable<TreeSet<Integer>> yearObservable = Observable.create(new Observable.OnSubscribe<TreeSet<Integer>>() {
            @Override
            public void call(Subscriber<? super TreeSet<Integer>> subscriber) {
                TreeSet<Integer> data = getYearSet();
                subscriber.onNext(data); // Emit the contents of the URL
                subscriber.onCompleted();
            }
        });

        mySub =yearObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TreeSet<Integer>>() {
                    @Override
                    public void call(TreeSet<Integer> integers) {
                        yearList = new ArrayList<Integer> (integers);
                        spYear.setItems(yearList);
                        // set Selected Year Here

                        // set Current Year Selectd initially
                        spYear.setSelectedIndex(yearList.indexOf(selectedYear));

                    }
                });

        processData();

    }

    @Override
    public void onPause() {
        if(!mySub.isUnsubscribed()){
            mySub.unsubscribe();
        }
        super.onPause();
    }

    private TreeSet<Integer> getYearSet(){
        TreeSet<Integer> set = new TreeSet<>();
        for(OwnerDataModel x : ownerDataModelList){
            List<Transaction> tempList = x.getTransactionList();

            for(Transaction y :tempList){
                set.add(MyUtils.getYear(y.getDate()));
            }
        }

        return set;
    }


   /* private void doIt(){
        List<StoreModel> rashinList = processData(currentMonth);
        storeModels.clear();
        storeModels.addAll(rashinList);
        adapter.notifyDataSetChanged();

    }*/

    @Override
    public void getHeight(int height) {
        ViewGroup.LayoutParams params=rvStores.getLayoutParams();
        params.height=height*storeModels.size();
        rvStores.setLayoutParams(params);

    }

    @Override
    public void onItemClick(int position) {

        OwnerDataModel model =ownerDataModelList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable("data",model);
        bundle.putInt("month_no",selectedMonth);
        bundle.putInt("year",selectedYear);
        bundle.putInt("from",3);

        DetailReportFragment detailReportFragment = new DetailReportFragment();
        detailReportFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.sub_container,detailReportFragment).addToBackStack(null).commitAllowingStateLoss();


    }

   /* private List<StoreModel> processData(int monthNo){

        List<StoreModel> storeModels = new ArrayList<>();
        for(OwnerDataModel x: ownerDataModelList){

            List<Transaction> transactionList = x.getTransactionList();

            double sales=0;
            double due=0;
            double expense=0;
            String name = x.getStore_name();
            String id = x.getStore_id();

            for(Transaction y: transactionList){
                if(MyUtils.getMonthNumber(y.getDate())==monthNo){

                    if(y.getType()==1){
                        sales = sales+y.getTotal();
                        due = due+(y.getTotal()-y.getPayment_amount());
                    }else {
                        expense = expense+y.getPayment_amount();
                    }

                }
            }

            StoreModel storeModel = new StoreModel(id,name,sales,due,expense);

            storeModels.add(storeModel);

           *//* StoreTransactionModel storeTransactionModel = new StoreTransactionModel(name,count,sales);

            storeTransactionModelList.add(storeTransactionModel);*//*
        }

        return storeModels;

    }*/

    private void processData(){

        List<StoreModel> storeModels = new ArrayList<>();
        for(OwnerDataModel x: ownerDataModelList){

            List<Transaction> transactionList = x.getTransactionList();

            double sales=0;
            double due=0;
            double expense=0;
            String name = x.getStore_name();
            String id = x.getStore_id();

            for(Transaction y: transactionList){
                if(MyUtils.getMonthNumber(y.getDate())==selectedMonth && MyUtils.getYear(y.getDate())==selectedYear){

                    if(y.getType()==1){
                        sales = sales+y.getTotal();
                        due = due+(y.getTotal()-y.getPayment_amount());
                    }else {
                        expense = expense+y.getPayment_amount();
                    }

                }
            }

            StoreModel storeModel = new StoreModel(id,name,sales,due,expense);

            storeModels.add(storeModel);

            this.storeModels.clear();
            this.storeModels.addAll(storeModels);
            adapter.notifyDataSetChanged();

        }



    }

   /* public void test(){
        for(OwnerDataModel x: ownerDataModelList){
            List<Transaction> tranList = x.getTransactionList();

            Observable<Transaction> tranObs = Observable.from(tranList);
            tranObs.filter(new Func1<Transaction, Boolean>() {
                @Override
                public Boolean call(Transaction transaction) {
                    return  transaction.getTotal()<100;
                }
            });

            tranObs.subscribe(new Action1<Transaction>() {
                @Override
                public void call(Transaction transaction) {
                    Log.d("RashiBaba",transaction.getTotal()+"");
                }
            });
        }
    }*/
}
