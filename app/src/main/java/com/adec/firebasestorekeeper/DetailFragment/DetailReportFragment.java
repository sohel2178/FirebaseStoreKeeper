package com.adec.firebasestorekeeper.DetailFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adec.firebasestorekeeper.Adapter.TransactionAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.Model.OwnerDataModel;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailReportFragment extends Fragment implements TransactionAdapter.TransactionListener {

    private ActionBar actionBar;

    private OwnerDataModel ownerDataModel;
    private int monthNo,year;
    private int week;
    private String dateStr;
    private int from;

    private List<Transaction> transactionList;
    private TransactionAdapter adapter;

    private RecyclerView rvTransaction;

    private Subscription subscription;

    String[] str = {"January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"};


    public DetailReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(getArguments()!= null){
            ownerDataModel = (OwnerDataModel) getArguments().getSerializable("data");
            from = getArguments().getInt("from");

            dateStr = getArguments().getString("date");
            week = getArguments().getInt("week_no");
            monthNo = getArguments().getInt("month_no");
            year = getArguments().getInt("year");

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_report, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvTransaction = (RecyclerView) view.findViewById(R.id.rv_transaction);
        rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    @Override
    public void onResume() {
        super.onResume();
        List<Transaction> tempList = ownerDataModel.getTransactionList();

        Log.d("TTTTT",tempList.size()+"");

        transactionList = new ArrayList<>();
        adapter = new TransactionAdapter(getActivity(),transactionList);
        adapter.setTransactionListener(this);
        rvTransaction.setAdapter(adapter);




        switch (from){
            case 1:
                actionBar.setTitle("Daily Sales/"+dateStr);
                subscription=Observable.from(tempList).filter(new Func1<Transaction, Boolean>() {
                    @Override
                    public Boolean call(Transaction transaction) {
                        return transaction.getType()==1 && transaction.getDate().equals(dateStr);
                    }
                }).subscribe(new Action1<Transaction>() {
                    @Override
                    public void call(Transaction transaction) {
                        Log.d("MMMMMMMM",transaction.getTotal()+"");
                        adapter.addTransaction(transaction);
                    }
                });
                break;

            case 2:
                actionBar.setTitle("Weekly Sales/"+week);
                subscription=Observable.from(tempList).filter(new Func1<Transaction, Boolean>() {
                    @Override
                    public Boolean call(Transaction transaction) {
                        return transaction.getType()==1 && MyUtils.getWeekNumber(transaction.getDate())==week
                                && MyUtils.getYear(transaction.getDate())==year;
                    }
                }).subscribe(new Action1<Transaction>() {
                    @Override
                    public void call(Transaction transaction) {
                        adapter.addTransaction(transaction);
                    }
                });
                break;

            case 3:
                actionBar.setTitle("Monthly Sales/"+str[monthNo]);
                subscription=Observable.from(tempList).filter(new Func1<Transaction, Boolean>() {
                    @Override
                    public Boolean call(Transaction transaction) {
                        return transaction.getType()==1 && MyUtils.getMonthNumber(transaction.getDate())==monthNo;
                    }
                }).subscribe(new Action1<Transaction>() {
                    @Override
                    public void call(Transaction transaction) {
                        Log.d("MMMMMMMM",transaction.getTotal()+"");
                        adapter.addTransaction(transaction);
                    }
                });
                break;

            case 4:
                actionBar.setTitle("Yearly Sales/"+year);
                subscription=Observable.from(tempList).filter(new Func1<Transaction, Boolean>() {
                    @Override
                    public Boolean call(Transaction transaction) {
                        return transaction.getType()==1 && MyUtils.getYear(transaction.getDate())==year;
                    }
                }).subscribe(new Action1<Transaction>() {
                    @Override
                    public void call(Transaction transaction) {
                        Log.d("MMMMMMMM",transaction.getTotal()+"");
                        adapter.addTransaction(transaction);
                    }
                });
                break;
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if(subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
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
