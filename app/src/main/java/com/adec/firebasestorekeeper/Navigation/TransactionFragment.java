package com.adec.firebasestorekeeper.Navigation;


import android.app.ProgressDialog;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.adec.firebasestorekeeper.Adapter.TransactionAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyFloatingAction;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.DetailFragment.VoucherDetailFragment;
import com.adec.firebasestorekeeper.Fragments.AddMemo;
import com.adec.firebasestorekeeper.Fragments.AddVoucher;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyTransactionReferenceClass;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment implements MyFloatingAction.FloatingActionMenuListener,
        MyTransactionReferenceClass.TransactionReferenceListener,TransactionAdapter.TransactionListener{

    private ActionBar actionBar;
    private RecyclerView rvTransaction;

    private List<Transaction> transactionList;
    private TransactionAdapter adapter;

    private MyFloatingAction myFloatingAction;


    private User currentUser;

    private ProgressDialog dialog;

    private MyTransactionReferenceClass myTransactionReferenceClass;




    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        myTransactionReferenceClass = new MyTransactionReferenceClass(currentUser.getAssign_store_id());
        myTransactionReferenceClass.setTransactionReferenceListener(this);

        transactionList = new ArrayList<>();
        adapter = new TransactionAdapter(getActivity(),transactionList);
        adapter.setTransactionListener(this);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait while Loading Data...");
        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_transaction, container, false);
        
        initView(view);
        return view;
    }

    private void initView(View view) {
        //buildFloatingActionMenu();

        rvTransaction = (RecyclerView) view.findViewById(R.id.rv_transaction);
        rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTransaction.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        myFloatingAction= new MyFloatingAction(getActivity());
        myFloatingAction.setFloatingActionMenuListener(this);

        if(currentUser.getUser_type()==0){
            myFloatingAction.hide();
        }

        actionBar.setTitle("Transactions");
    }

    private void buildFloatingActionMenu(){
        ImageView icon = new ImageView(getActivity()); // Create an icon
        icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.plus,null));

        final FloatingActionButton actionButton = new FloatingActionButton.Builder(getActivity())
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
// repeat many times:
        ImageView itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.plus,null));

        ImageView itemIcon2 = new ImageView(getActivity());
        itemIcon2.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.plus,null));

        ImageView itemIcon3 = new ImageView(getActivity());
        itemIcon3.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.plus,null));

        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();

        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .build();



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                getFragmentManager().beginTransaction().replace(R.id.main_container,new AddVoucher())
                        .addToBackStack(null).commit();

                actionMenu.close(true);
                actionButton.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myFloatingAction.hide();
        myFloatingAction=null;
    }

    @Override
    public void buttonClick(int buttonNumber) {
        switch (buttonNumber){
            case 1:
                getFragmentManager().beginTransaction().replace(R.id.main_container,new AddVoucher())
                        .addToBackStack(null).commit();

                myFloatingAction.hide();
                break;

            case 2:
                getFragmentManager().beginTransaction().replace(R.id.main_container,new AddMemo())
                        .addToBackStack(null).commit();

                myFloatingAction.hide();
                break;

            case 3:
                break;
        }
    }




    @Override
    public void getTransactions(List<Transaction> transactions) {
        /*Collections.reverse(transactions);
        transactionList.addAll(transactions);
        adapter.notifyDataSetChanged();
        dialog.dismiss();*/

        //myTransactionReferenceClass.triggerChildEvent();
    }

    @Override
    public void addTransaction(Transaction transaction) {
        dialog.dismiss();
        adapter.addTransaction(transaction);
    }

    @Override
    public void onClickTransaction(int position) {
        Transaction transaction = transactionList.get(position);

        if(transaction.getType()==0){
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.TRANSACTION,transaction);
            VoucherDetailFragment voucherDetailFragment = new VoucherDetailFragment();
            voucherDetailFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.main_container,voucherDetailFragment)
                    .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).commit();

        }
    }
}
