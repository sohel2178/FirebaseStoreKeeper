package com.adec.firebasestorekeeper.Fragments.Customer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adec.firebasestorekeeper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerTransactionFragment extends Fragment {


    public CustomerTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_transaction, container, false);
    }

}
