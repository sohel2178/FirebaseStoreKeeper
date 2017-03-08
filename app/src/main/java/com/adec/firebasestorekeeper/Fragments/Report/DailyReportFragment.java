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
import android.widget.TextView;

import com.adec.firebasestorekeeper.Adapter.ReportAdapter.DailyReportAdapter;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.CustomListener.MyChangeListener;
import com.adec.firebasestorekeeper.CustomView.MyTextView;
import com.adec.firebasestorekeeper.DetailFragment.DetailReportFragment;
import com.adec.firebasestorekeeper.Model.OwnerDataModel;
import com.adec.firebasestorekeeper.Model.Report.StoreModel;
import com.adec.firebasestorekeeper.Model.StoreTransactionModel;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.R;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyReportFragment extends Fragment implements View.OnClickListener,MyChangeListener,DailyReportAdapter.ReportListener{

    private MyTextView tvDate;
    private RecyclerView rvStores;
    private IconTextView itvDate;

    private String dateStr;

    private List<OwnerDataModel> ownerDataModelList;

    private List<StoreModel> storeModelList;
    private DailyReportAdapter adapter;


    public DailyReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            ownerDataModelList = (List<OwnerDataModel>) getArguments().getSerializable("data");
        }

        storeModelList = new ArrayList<>();
        adapter = new DailyReportAdapter(getActivity(),storeModelList);
        adapter.setReportListener(this);

        Date date = new Date();
        dateStr = MyUtils.dateToString(date);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_report, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvDate = (MyTextView) view.findViewById(R.id.date);
        tvDate.setMyChangeListener(this);
        tvDate.setText(dateStr);

        rvStores = (RecyclerView) view.findViewById(R.id.rv_store);
        rvStores.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvStores.setAdapter(adapter);

        itvDate = (IconTextView) view.findViewById(R.id.my_calendar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        itvDate.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();




    }

    @Override
    public void onClick(View v) {
        MyUtils.showDialogAndSetTime(getActivity(),tvDate);
    }

    @Override
    public void textChange(String changeText) {
        Log.d("Sohel",changeText);
        dateStr =changeText;

        if(ownerDataModelList!= null){

            List<StoreModel> sohelList = getList(changeText);

            storeModelList.clear();
            storeModelList.addAll(sohelList);
            adapter.notifyDataSetChanged();
            /*Log.d("Sohel",sohelList.get(1).getStore_name()+" "+sohelList.get(1).getSales()+" "+
                sohelList.get(1).getDue());*/
        }

    }

    private List<StoreModel> getList(String dateStr){
        List<StoreModel> storeModels = new ArrayList<>();
        for(OwnerDataModel x: ownerDataModelList){

            List<Transaction> transactionList = x.getTransactionList();

            double sales=0;
            double due=0;
            double expense=0;
            String name = x.getStore_name();
            String id = x.getStore_id();

            for(Transaction y: transactionList){
                if(y.getDate().equals(dateStr)){

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

           /* StoreTransactionModel storeTransactionModel = new StoreTransactionModel(name,count,sales);

            storeTransactionModelList.add(storeTransactionModel);*/
        }

        return storeModels;
    }

    @Override
    public void getHeight(int height) {

    }

    @Override
    public void onItemClick(int position) {
        OwnerDataModel model =ownerDataModelList.get(position);

        Log.d("SSSS",model.getStore_name()+"");

        Bundle bundle = new Bundle();
        bundle.putSerializable("data",model);
        bundle.putString("date",dateStr);
        bundle.putInt("from",1);

        DetailReportFragment detailReportFragment = new DetailReportFragment();
        detailReportFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.sub_container,detailReportFragment).addToBackStack(null).commitAllowingStateLoss();

    }
}
