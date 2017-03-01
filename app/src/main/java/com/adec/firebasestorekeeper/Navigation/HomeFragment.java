package com.adec.firebasestorekeeper.Navigation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.Fragments.FirstPage.DashBoardFragment;
import com.adec.firebasestorekeeper.Fragments.FirstPage.HomeTransactionFragment;
import com.adec.firebasestorekeeper.Fragments.FirstPage.NotificationFragment;
import com.adec.firebasestorekeeper.Fragments.FirstPage.ReportFragment;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Interface.HomeListener;
import com.adec.firebasestorekeeper.R;
import com.joanzapata.iconify.widget.IconTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, HomeListener{

    private FragmentListener fragmentListener;
    private ActionBar actionBar;


    public IconTextView tvDashBoard,tvTransaction,tvNotification,tvReport;


    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        fragmentListener = (FragmentListener) getActivity();

        DashBoardFragment dashBoardFragment = new DashBoardFragment();
        dashBoardFragment.setHomeListener(this);
        getFragmentManager().beginTransaction().replace(R.id.sub_container,dashBoardFragment).commit();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);





        return  view;
    }

    private void initView(View view) {

        tvDashBoard = (IconTextView) view.findViewById(R.id.dashboard);
        tvTransaction = (IconTextView) view.findViewById(R.id.transaction);
        tvNotification = (IconTextView) view.findViewById(R.id.notification);
        tvReport = (IconTextView) view.findViewById(R.id.report);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvDashBoard.setOnClickListener(this);
        tvTransaction.setOnClickListener(this);
        tvNotification.setOnClickListener(this);
        tvReport.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();




        Log.d("ResumeCall","Resume Call Home Fragment");



        /*setDefaultColor();
        if(getFragmentManager().findFragmentById(R.id.sub_container) instanceof DashBoardFragment){
            tvDashBoard.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
        }else if(getFragmentManager().findFragmentById(R.id.sub_container) instanceof HomeTransactionFragment){
            tvTransaction.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
        }else if(getFragmentManager().findFragmentById(R.id.sub_container) instanceof ReportFragment){
            tvReport.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
        }else if(getFragmentManager().findFragmentById(R.id.sub_container) instanceof NotificationFragment){
            tvNotification.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
        }*/

        if(fragmentListener!= null){
            fragmentListener.getFragment(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dashboard:
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                dashBoardFragment.setHomeListener(this);
                getFragmentManager().beginTransaction().replace(R.id.sub_container,dashBoardFragment).addToBackStack(null).commit();
                /*setDefaultColor();
                tvDashBoard.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));*/
                break;

            case R.id.transaction:
                HomeTransactionFragment homeTransactionFragment = new HomeTransactionFragment();
                homeTransactionFragment.setHomeListener(this);
                getFragmentManager().beginTransaction().replace(R.id.sub_container,homeTransactionFragment).addToBackStack(null).commit();
                /*setDefaultColor();
                tvTransaction.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));*/
                break;

            case R.id.notification:
                NotificationFragment notificationFragment = new NotificationFragment();
                notificationFragment.setHomeListener(this);
                getFragmentManager().beginTransaction().replace(R.id.sub_container,notificationFragment).addToBackStack(null).commit();
                /*setDefaultColor();
                tvNotification.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));*/
                break;

            case R.id.report:
                ReportFragment reportFragment = new ReportFragment();
                reportFragment.setHomeListener(this);
                getFragmentManager().beginTransaction().replace(R.id.sub_container,reportFragment).addToBackStack(null).commit();
                /*setDefaultColor();
                tvReport.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));*/
                break;
        }
    }

    public void changeBackGround(int number){
        setDefaultColor();

        switch (number){
            case 100:
                tvDashBoard.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                break;

            case 200:
                tvTransaction.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                break;
            case 400:
                tvNotification.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                break;
            case 300:
                tvReport.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                break;
        }

    }



    private void setDefaultColor(){
        if(tvDashBoard!= null){
            tvDashBoard.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        }
        if(tvTransaction!= null){
            tvTransaction.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        }
        if(tvNotification!= null){
            tvNotification.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

        }
        if(tvReport!= null){
            tvReport.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

        }
    }

    @Override
    public void getFragment(int fragNumber) {

        // Check the view is null or not
        if(getView()!= null){
            switch (fragNumber){
                case 100:
                    actionBar.setTitle("Dashboard");
                    //tvDashBoard.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDark,null));
                    changeBackGround(100);
                    break;
                case 200:
                    actionBar.setTitle("Transaction");
                    changeBackGround(200);
                    break;
                case 300:
                    actionBar.setTitle("Report");
                    changeBackGround(300);
                    break;
                case 400:
                    actionBar.setTitle("Notification");
                    changeBackGround(400);
                    break;
        }


        }
    }
}
