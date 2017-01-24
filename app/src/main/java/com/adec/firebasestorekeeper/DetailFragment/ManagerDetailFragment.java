package com.adec.firebasestorekeeper.DetailFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerDetailFragment extends Fragment {

    private ActionBar actionBar;
    private User manager;

    private User currentUser;


    public ManagerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            manager = (User) getArguments().getSerializable(Constant.USER);
        }

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_manager_detail, container, false);
        initView(view);

        if(manager!=null){
            setData();
        }
        return view;
    }

    private void setData() {

    }

    private void initView(View view) {
    }

}
