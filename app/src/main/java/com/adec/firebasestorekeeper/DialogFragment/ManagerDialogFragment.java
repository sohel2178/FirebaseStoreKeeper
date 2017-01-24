package com.adec.firebasestorekeeper.DialogFragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.adec.firebasestorekeeper.Adapter.DialogFragmenAdapter.ManagerDialogAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyEmployeeReferenceClass;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyUserReferenceClass;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerDialogFragment extends DialogFragment implements MyEmployeeReferenceClass.EmployeeReferenceListener,
        ManagerDialogAdapter.ManagerDialogListener,View.OnClickListener{

    private RecyclerView recyclerView;

    private ImageView btnCancel,btnOk;

    private List<User> managerList;
    private ManagerDialogAdapter adapter;

    private User currentUser;

    private User selectedManager;


    public ManagerDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        MyEmployeeReferenceClass myEmployeeReferenceClass = new MyEmployeeReferenceClass(currentUser.getId());
        myEmployeeReferenceClass.setEmployeeReferenceListener(this);

        managerList = new ArrayList<>();
        adapter = new ManagerDialogAdapter(getActivity(),managerList);
        adapter.setManagerDialogListener(this);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_manager_dialog, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        btnCancel = (ImageView) view.findViewById(R.id.cancel);
        btnOk = (ImageView) view.findViewById(R.id.ok);
    }

    @Override
    public void getEmployee(User user) {
        if(user.getUser_type()==1 && user.getAssign_store_id().equals("")){
            adapter.addManager(user);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void selectedManager(int position) {
        selectedManager = managerList.get(position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                break;

            case R.id.ok:

                if(selectedManager!= null){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.MANAGER,selectedManager);;

                    Intent intent = new Intent();
                    intent.putExtras(bundle);

                    getTargetFragment().onActivityResult(13, Activity.RESULT_OK,intent);
                    getDialog().dismiss();
                }
                break;
        }
    }
}
