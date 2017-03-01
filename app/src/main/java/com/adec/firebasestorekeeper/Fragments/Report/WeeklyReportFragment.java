package com.adec.firebasestorekeeper.Fragments.Report;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Set;
import java.util.TreeSet;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyReportFragment extends Fragment implements View.OnClickListener,
        DailyReportAdapter.ReportListener{

    private String dateStr;
    private int currentWeekNumber,currentYear;

    private List<OwnerDataModel> ownerDataModelList;

    private List<StoreModel> storeModels;
    private DailyReportAdapter adapter;

    // View Declaration;
    private ImageView ivPrev,ivNext;
    private RecyclerView rvStores;

    private MaterialSpinner spinnerYear,spinnerWeek;

    private List<Integer> yearList;
    private List<Integer> weekList;

    private int selectedYear,selectedWeek;

    private Subscription mySub;


    public WeeklyReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weekList = new ArrayList<>();
        yearList = new ArrayList<>();

        if(getArguments() != null){
            ownerDataModelList = (List<OwnerDataModel>) getArguments().getSerializable("data");
        }

        Date date = new Date();
        dateStr = MyUtils.dateToString(date);
        currentWeekNumber = MyUtils.getWeekNumber(dateStr);
        currentYear = MyUtils.getYear(dateStr);

        // selected year and selected Week initialized Here
        selectedYear = currentYear;
        selectedWeek = currentWeekNumber;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_report, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        spinnerYear = (MaterialSpinner) view.findViewById(R.id.year_spinner);
        spinnerWeek = (MaterialSpinner) view.findViewById(R.id.week_spinner);



        spinnerYear.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedYear = (int) item;
                processData();
            }
        });

        spinnerWeek.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedWeek = (int) item;
                processData();
            }
        });


        rvStores = (RecyclerView) view.findViewById(R.id.rv_store);
        RecyclerView.LayoutManager mamage = new LinearLayoutManager(getActivity());
        mamage.setAutoMeasureEnabled(true);
        rvStores.setLayoutManager(mamage);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                        spinnerYear.setItems(yearList);
                        // set Selected Year Here

                        // set Current Year Selectd initially
                        spinnerYear.setSelectedIndex(yearList.indexOf(currentYear));

                        weekList=getWeekList();
                        // Calculate WeekList for selected Year and set It
                        spinnerWeek.setItems(getWeekList());


                        spinnerWeek.setSelectedIndex(weekList.indexOf(currentWeekNumber));

                        // set the Cuurent Week Selected


                    }
                });

        processData();



    }


    private List<Integer> getWeekList(){
        TreeSet<Integer> weekset = new TreeSet<>();

        String firstDate = "01-01-"+selectedYear;
        Date fDate = MyUtils.getDateFromString(firstDate);
        String year = MyUtils.getYear(fDate);

        Date date = fDate;

        while (MyUtils.getYear(date).equals(year)){
            weekset.add(MyUtils.getWeekNumber(MyUtils.dateToString(date)));
            date= MyUtils.incrementDate(1,date);
        }
        return new ArrayList<>(weekset);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previous:

                break;
            case R.id.next:

                break;
        }
    }

    @Override
    public void onPause() {
        if(!mySub.isUnsubscribed()){
            mySub.unsubscribe();
        }
        super.onPause();
    }

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
                if(MyUtils.getWeekNumber(y.getDate())==selectedWeek && MyUtils.getYear(y.getDate())==selectedYear){

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
        bundle.putInt("week_no",selectedWeek);
        bundle.putInt("year",selectedYear);
        bundle.putInt("from",2);

        DetailReportFragment detailReportFragment = new DetailReportFragment();
        detailReportFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.sub_container,detailReportFragment).addToBackStack(null).commitAllowingStateLoss();

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
}
