package com.adec.firebasestorekeeper.DetailFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adec.firebasestorekeeper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoucherDetailFragment extends Fragment {
    private ActionBar actionBar;


    public VoucherDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_voucher_detail, container, false);
        
        initView(view);
        return view;
    }

    private void initView(View view) {
    }


    @Override
    public void onResume() {
        super.onResume();
        actionBar.hide();
    }
}
