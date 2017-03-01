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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class YearlyReportFragment extends Fragment implements View.OnClickListener,DailyReportAdapter.ReportListener{

    private String dateStr;
    private int currentYear;

    private List<OwnerDataModel> ownerDataModelList;

    private List<StoreModel> storeModels;
    private DailyReportAdapter adapter;

    // View Declaration;
    private ImageView ivPrev,ivNext;
    private RecyclerView rvStores;


    public YearlyReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            ownerDataModelList = (List<OwnerDataModel>) getArguments().getSerializable("data");
        }

        Date date = new Date();
        dateStr = MyUtils.dateToString(date);
        currentYear = MyUtils.getYear(dateStr);

        storeModels = new ArrayList<>();
        adapter = new DailyReportAdapter(getActivity(),storeModels);
        adapter.setReportListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yearly_report, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        ivPrev = (ImageView) view.findViewById(R.id.previous);
        ivNext = (ImageView) view.findViewById(R.id.next);

        rvStores = (RecyclerView) view.findViewById(R.id.rv_store);
        rvStores.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvStores.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        doIt();

    }

    private void doIt(){
        List<StoreModel> rashinList = processData(currentYear);
        storeModels.clear();
        storeModels.addAll(rashinList);
        adapter.notifyDataSetChanged();

    }

    private List<StoreModel> processData(int year){

        List<StoreModel> storeModels = new ArrayList<>();
        for(OwnerDataModel x: ownerDataModelList){

            List<Transaction> transactionList = x.getTransactionList();

            double sales=0;
            double due=0;
            double expense=0;
            String name = x.getStore_name();
            String id = x.getStore_id();

            for(Transaction y: transactionList){
                if(MyUtils.getYear(y.getDate())==year){

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
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.previous:
                currentYear--;
                break;
            case R.id.next:
                currentYear++;
                break;
        }
        doIt();

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
        bundle.putInt("year",currentYear);
        bundle.putInt("from",4);

        DetailReportFragment detailReportFragment = new DetailReportFragment();
        detailReportFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.sub_container,detailReportFragment).addToBackStack(null).commitAllowingStateLoss();

    }
}
