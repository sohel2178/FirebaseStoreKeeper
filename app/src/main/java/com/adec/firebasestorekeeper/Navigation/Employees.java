package com.adec.firebasestorekeeper.Navigation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyEmployeeReferenceClass;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyUserReferenceClass;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Employees extends Fragment implements
        View.OnClickListener,MyEmployeeReferenceClass.EmployeeReferenceListener,
        ManagerAdapter.ManagerListener{

    private FragmentListener fragmentListener;

    private RecyclerView rvEmployee;
    private FloatingActionButton btnAdd;
    private List<User> userList;
    private ManagerAdapter adapter;

    private String store_id;
    private User currentUser;

    private MyEmployeeReferenceClass myEmployeeReferenceClass;


    public Employees() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        fragmentListener = (FragmentListener) getActivity();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_employees, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvEmployee= (RecyclerView) view.findViewById(R.id.rvEmployees);
        rvEmployee.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnAdd = (FloatingActionButton) view.findViewById(R.id.fab_add);

        if(currentUser.getUser_type()==0){
            btnAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(fragmentListener!= null){
            fragmentListener.getFragment(5);
        }


        if(currentUser.getUser_type()==0){
            myEmployeeReferenceClass = new MyEmployeeReferenceClass(currentUser.getId());

        }else if(currentUser.getUser_type()==1){
            myEmployeeReferenceClass = new MyEmployeeReferenceClass(currentUser.getParent_id());
        }

        myEmployeeReferenceClass.setEmployeeReferenceListener(this);

        userList = new ArrayList<>();
        adapter = new ManagerAdapter(getActivity(),userList);
        adapter.setManagerListener(this);

        // Set Adapter Here
        rvEmployee.setAdapter(adapter);


    }

    @Override
    public void onPause() {
        // Remove Reference
        myEmployeeReferenceClass.removeReference();
        super.onPause();
    }

    /* @Override
    public void getUser(User user) {

        if(!user.getId().equals(currentUser.getId())){
            if(user.getAssign_store_id().equals(store_id)){
                adapter.addManager(user);
            }
        }



    }*/

    @Override
    public void onClick(View view) {
       /* Bundle bundle = new Bundle();
        AddManagerFragment addManagerFragment = new AddManagerFragment();
        addManagerFragment.setArguments(bundle);*/

        getFragmentManager().beginTransaction().replace(R.id.main_container,new AddManagerFragment())
                .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null).commit();
    }

    @Override
    public void getEmployee(User user) {
        if(currentUser.getUser_type()==0){
            if(user.getUser_type()==2){
                adapter.addManager(user);
            }

        }else if(currentUser.getUser_type()==1){

            if(!user.getId().equals(currentUser.getId())){
                if(user.getAssign_store_id().equals(currentUser.getAssign_store_id())){
                    adapter.addManager(user);
                }
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        User employee = userList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER,employee);

        EmployeeDetailFragment employeeDetailFragment = new EmployeeDetailFragment();
        employeeDetailFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.main_container,employeeDetailFragment)
                .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null).commit();
    }
}
