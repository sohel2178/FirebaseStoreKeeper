package com.adec.firebasestorekeeper.Fragments.FirstPage;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Fragments.Report.DailyReportFragment;
import com.adec.firebasestorekeeper.Fragments.Report.MonthlyReportFragment;
import com.adec.firebasestorekeeper.Fragments.Report.WeeklyReportFragment;
import com.adec.firebasestorekeeper.Fragments.Report.YearlyReportFragment;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Interface.HomeListener;
import com.adec.firebasestorekeeper.Model.OwnerDataModel;
import com.adec.firebasestorekeeper.Model.Store;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.AllTransaction;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyStoreReferenceClass;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyTransactionReferenceClass;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment implements MyStoreReferenceClass.StoreReferenceListener,
        AllTransaction.MapTransactionListener{
    private HomeListener homeListener;

    private DatabaseReference transactionRef,storeRef;
    private AllTransaction allTransaction;
    private MyStoreReferenceClass myStoreReferenceClass;

    private User currentUser;

    private List<OwnerDataModel> ownerDataModelList;
    private int storeCounter;
    private List<Store> storeList;

    private ProgressDialog dialog;


    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait..");
        dialog.show();


    }

    public void setHomeListener(HomeListener homeListener){
        this.homeListener = homeListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        initView(view);
        return  view;
    }

    private void initView(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();

        ownerDataModelList = new ArrayList<>();
        storeList = new ArrayList<>();
        storeCounter =0;

        if(homeListener!=null){
            homeListener.getFragment(300);
        }

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();

        if(currentUser.getUser_type()==0){
            storeRef = myDatabaseReference.getStoreRef(currentUser.getId());
            myStoreReferenceClass = new MyStoreReferenceClass(storeRef);
            myStoreReferenceClass.setStoreReferenceListener(this);
        }

    }

    @Override
    public void onPause() {
        if(myStoreReferenceClass!= null){
            myStoreReferenceClass.removeListener();
        }

        if(allTransaction!= null){
            allTransaction.removeListener();
        }
        super.onPause();
    }

    @Override
    public void getStore(Store store) {
        storeList.add(store);
        allTransaction = new AllTransaction(store);
        allTransaction.setMapTransactionListener(this);
    }

    @Override
    public void getStoreTransaction(String storeName, List<Transaction> transactionList,String store_id) {
        Log.d("SOHELTEST",transactionList.size()+"");
        storeCounter++;
        OwnerDataModel ownerDataModel = new OwnerDataModel(storeName,transactionList,store_id);
        ownerDataModelList.add(ownerDataModel);

        if(storeCounter==storeList.size()){
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) ownerDataModelList);

            DailyReportFragment dailyReportFragment = new DailyReportFragment();
            dailyReportFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.daily_report,dailyReportFragment).commitAllowingStateLoss();

            WeeklyReportFragment weeklyReportFragment = new WeeklyReportFragment();
            weeklyReportFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.weekly_report,weeklyReportFragment).commitAllowingStateLoss();

            MonthlyReportFragment monthlyReportFragment = new MonthlyReportFragment();
            monthlyReportFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.monthly_report,monthlyReportFragment).commitAllowingStateLoss();

            YearlyReportFragment yearlyReportFragment = new YearlyReportFragment();
            yearlyReportFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.yearly_report,yearlyReportFragment).commitAllowingStateLoss();

            dialog.dismiss();

        }

    }
}
