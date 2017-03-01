package com.adec.firebasestorekeeper.Fragments.FirstPage;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adec.firebasestorekeeper.Adapter.TransactionModelAdapter;
import com.adec.firebasestorekeeper.AppUtility.MyChartUtil;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.DetailFragment.CustomizeReportFragment;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Interface.HomeListener;
import com.adec.firebasestorekeeper.Model.OwnerDataModel;
import com.adec.firebasestorekeeper.Model.Store;
import com.adec.firebasestorekeeper.Model.StoreTransactionModel;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Navigation.HomeFragment;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.AllTransaction;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyStoreReferenceClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment implements MyStoreReferenceClass.StoreReferenceListener,
        AllTransaction.MapTransactionListener{

    private HomeListener homeListener;

    private TextView tvDate,tvSales,tvSalesCount,tvStoreCount;
    private RecyclerView rvStores;
    private LinearLayout chartContainer;
    private RelativeLayout rlTopSection;
    String today;
    private int salesCount;
    private double totalSum;

    private DatabaseReference storeRef;
    private User currentUser;

    private MyStoreReferenceClass myStoreReferenceClass;
    private List<Store> storeList;

    private List<StoreTransactionModel> storeTransactionModelList;
    private TransactionModelAdapter adapter;

    private AllTransaction allTransaction;

    private MyChartUtil myChartUtil;

    private int storeCounterForChart;

    private List<OwnerDataModel> ownerDataModelList;



    public DashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Date date = new Date();
        today = MyUtils.dateToString(date);

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

    }

    public void setHomeListener( HomeListener homeListener){
        this.homeListener =homeListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvDate = (TextView) view.findViewById(R.id.date);
        tvDate.setText(today);
        tvSales = (TextView) view.findViewById(R.id.sales);
        tvSalesCount = (TextView) view.findViewById(R.id.sales_count);
        tvStoreCount = (TextView) view.findViewById(R.id.store_count);

        rvStores = (RecyclerView) view.findViewById(R.id.rv_store);
        rvStores.setLayoutManager(new LinearLayoutManager(getActivity()));

        chartContainer = (LinearLayout) view.findViewById(R.id.chart_container);
        // Top section Click
        rlTopSection = (RelativeLayout) view.findViewById(R.id.top_section);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rlTopSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if(ownerDataModelList!=null && ownerDataModelList.size()>0){
                    bundle.putSerializable("data", (Serializable) ownerDataModelList);
                    CustomizeReportFragment customizeReportFragment = new CustomizeReportFragment();
                    customizeReportFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.sub_container,customizeReportFragment)
                            .addToBackStack(null).commitAllowingStateLoss();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("ResumeCall","Resume Call DashBoadr Fragment");
        salesCount =0;
        totalSum =0;

        if(homeListener!= null){
            homeListener.getFragment(100);

        }


        ownerDataModelList = new ArrayList<>();
        storeList = new ArrayList<>();

        storeTransactionModelList = new ArrayList<>();
        adapter = new TransactionModelAdapter(getActivity(),storeTransactionModelList);
        rvStores.setAdapter(adapter);

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        storeRef = myDatabaseReference.getStoreRef(currentUser.getId());

        myStoreReferenceClass = new MyStoreReferenceClass(storeRef);
        myStoreReferenceClass.setStoreReferenceListener(this);

        myChartUtil = new MyChartUtil(getActivity());
        storeCounterForChart=0;





    }

    @Override
    public void onPause() {
        if(myStoreReferenceClass != null){
            myStoreReferenceClass.removeListener();
        }


        if(allTransaction!= null){
            allTransaction.removeListener();
        }

        super.onPause();
    }

    @Override
    public void getStore(Store store) {
        // get All Store Here
        storeList.add(store);
        tvStoreCount.setText(storeList.size()+" Outlets");

        allTransaction = new AllTransaction(store);
        allTransaction.setMapTransactionListener(this);
    }

    @Override
    public void getStoreTransaction(String storeName, List<Transaction> transactionList,String store_id) {
        Log.d("Bala",storeName+ "  "+transactionList.size());

        OwnerDataModel model = new OwnerDataModel(storeName,transactionList,store_id);
        ownerDataModelList.add(model);

        storeCounterForChart++;

        // get the Correct List
        List<Transaction> tempList = MyUtils.getList(transactionList);

        int count = tempList.size();
        // increment sales Count Here
        salesCount = salesCount+count;
        tvSalesCount.setText("Sales: "+salesCount);

        double sum = getSum(tempList);
        totalSum = totalSum+sum;
        tvSales.setText("Tk "+totalSum);
        StoreTransactionModel storeTransactionModel = new StoreTransactionModel(storeName,count,sum);
        adapter.addModel(storeTransactionModel);

        if(storeCounterForChart == storeList.size()){

            chartContainer.addView(myChartUtil.getBarChart(storeTransactionModelList));
        }


        // Todo Here
    }



    // get the sum of transaction in aparticular store
    private double getSum(List<Transaction> transactionList){
        double sum = 0;

        for(Transaction x: transactionList){
            sum = sum+x.getTotal();
        }

        return sum;
    }
}
