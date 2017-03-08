package com.adec.firebasestorekeeper.Navigation;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adec.firebasestorekeeper.Adapter.StoreAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.DetailFragment.StoreDetailFragment;
import com.adec.firebasestorekeeper.DialogFragment.ManagerDialogFragment;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Model.Store;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyStoreReferenceClass;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllStoreFragment extends Fragment implements MyStoreReferenceClass.StoreReferenceListener,
        View.OnClickListener,StoreAdapter.StoreListener{


    private static final int DIALOG_REQUEST_CODE=13;
    private int counter=0;

    private FragmentListener fragmentListener;

    private TextView tvOutlets;

    private RecyclerView rvStore;
    private List<Store> storeList;
    private StoreAdapter adapter;

    private FloatingActionButton fabAdd;

    private ProgressDialog dialog;

    private User selectedManager;
    private String selected_store_id;

    DatabaseReference storeRef;
    MyDatabaseReference myDatabaseReference;

    User user;


    public AllStoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentListener = (FragmentListener) getActivity();

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        user = userLocalStore.getUser();
        myDatabaseReference= new MyDatabaseReference();
        storeRef = myDatabaseReference.getStoreRef(user.getId());

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait while Loading Data...");
        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_store, container, false);

        initView(view);

        return  view;
    }

    private void initView(View view) {
        tvOutlets = (TextView) view.findViewById(R.id.outlets);
        rvStore = (RecyclerView) view.findViewById(R.id.rvStores);
        rvStore.setLayoutManager(new LinearLayoutManager(getActivity()));

        fabAdd = (FloatingActionButton) view.findViewById(R.id.fab_add);

    }

    @Override
    public void onResume() {
        super.onResume();

        counter=0;

        if(fragmentListener!= null){
            fragmentListener.getFragment(2);
        }

        // Data Listener
        MyStoreReferenceClass myStoreReferenceClass= new MyStoreReferenceClass(storeRef);
        myStoreReferenceClass.setStoreReferenceListener(this);
        storeList = new ArrayList<>();
        adapter = new StoreAdapter(getActivity(),storeList);
        adapter.setStoreListener(this);

        rvStore.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fabAdd.setOnClickListener(this);
    }

    @Override
    public void getStore(Store store) {
        adapter.addStore(store);
        // Increment Counter
        counter++;
        tvOutlets.setText("Outlets "+counter);

        Log.d("Store",store.getName());
        dialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction().replace(R.id.main_container,new AddStoreFragment())
                .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null).commit();
    }

    @Override
    public void onAssignClick(int position) {
        // Show Manager Dialog Fragment
        selected_store_id = storeList.get(position).getId();
        ManagerDialogFragment managerDialogFragment = new ManagerDialogFragment();
        managerDialogFragment.setTargetFragment(this,DIALOG_REQUEST_CODE);
        managerDialogFragment.show(getFragmentManager(), Constant.DEFAULAT_FRAGMENT_TAG);

    }

    @Override
    public void onItemClick(int position) {

        Store store = storeList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.STORE,store);

        StoreDetailFragment storeDetailFragment = new StoreDetailFragment();
        storeDetailFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.main_container,storeDetailFragment).addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).commit();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case DIALOG_REQUEST_CODE:
                if(resultCode== Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();

                    selectedManager = (User) bundle.getSerializable(Constant.MANAGER);

                    if(selectedManager!= null){
                        // Update User Ref
                        updateUserInUserRef();

                        // Update Employee Ref

                        updateUserInEmployeeRef();

                        // Update Store Ref

                        updateStoreRef();

                        // update adapter

                        updateAdapter();
                    }



                }
                break;
        }
    }

    private void updateUserInUserRef(){
        myDatabaseReference.getUserRef().child(selectedManager.getId()).child("assign_store_id").setValue(selected_store_id);
    }

    private void updateUserInEmployeeRef(){
        myDatabaseReference.getEmployeeReference(user.getId()).child(selectedManager.getId()).child("assign_store_id").setValue(selected_store_id);
    }

    private void updateStoreRef(){
        myDatabaseReference.getStoreRef(user.getId()).child(selected_store_id).child("assign_manager_id").setValue(selectedManager.getId());
    }

    private void updateAdapter(){

        for(Store x: storeList){
            if(x.getId().equals(selected_store_id)){
                x.setAssign_manager_id(selectedManager.getId());

                adapter.notifyDataSetChanged();
            }
        }
    }
}
