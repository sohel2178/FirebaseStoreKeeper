package com.adec.firebasestorekeeper.Navigation;


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

import com.adec.firebasestorekeeper.Adapter.ManagerAdapter;
import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.DetailFragment.EmployeeDetailFragment;
import com.adec.firebasestorekeeper.DetailFragment.ManagerDetailFragment;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyEmployeeReferenceClass;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyUserReferenceClass;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllManagerFragment extends Fragment implements MyEmployeeReferenceClass.EmployeeReferenceListener,
        View.OnClickListener,ManagerAdapter.ManagerListener{

    private RecyclerView rvManagers;
    private List<User> userList;
    private ManagerAdapter adapter;

    private String currentUserID;

    private FloatingActionButton fabAdd;

    private FragmentListener fragmentListener;

    private MyEmployeeReferenceClass employeeReferenceClass;




    public AllManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentListener = (FragmentListener) getActivity();

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUserID = userLocalStore.getUser().getId();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_manager, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        rvManagers = (RecyclerView) view.findViewById(R.id.rvManager);
        rvManagers.setLayoutManager(new LinearLayoutManager(getActivity()));


        fabAdd = (FloatingActionButton) view.findViewById(R.id.fab_add);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fabAdd.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(fragmentListener!= null){
            fragmentListener.getFragment(3);
        }

        employeeReferenceClass = new MyEmployeeReferenceClass(currentUserID);
        employeeReferenceClass.setEmployeeReferenceListener(this);

        userList = new ArrayList<>();
        adapter = new ManagerAdapter(getActivity(),userList);
        adapter.setManagerListener(this);

        // set Adapter OnResume
        rvManagers.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        employeeReferenceClass.removeReference();
    }

    /* @Override
    public void getUser(User user) {
        if(user.getParent_id().equals(currentUserID)){
            adapter.addManager(user);


        }
        Log.d("TGTGTG","1212");
    }*/

    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction().replace(R.id.main_container,new AddManagerFragment())
                .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null).commit();
    }

    @Override
    public void onItemClick(int position) {
        User manager = userList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER,manager);

        ManagerDetailFragment managerDetailFragment = new ManagerDetailFragment();
        managerDetailFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.main_container,managerDetailFragment).addToBackStack(null).commit();
    }

    @Override
    public void getEmployee(User user) {
        if(user.getUser_type()==1){
            adapter.addManager(user);
        }
    }
}
