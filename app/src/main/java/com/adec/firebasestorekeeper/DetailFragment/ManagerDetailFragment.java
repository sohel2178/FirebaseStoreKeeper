package com.adec.firebasestorekeeper.DetailFragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.CustomView.MyEditText;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Navigation.AllManagerFragment;
import com.adec.firebasestorekeeper.Navigation.Employees;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerDetailFragment extends Fragment implements View.OnClickListener{

    private ActionBar actionBar;



    private ImageView ivClose,ivTick;
    private CircleImageView image;
    private MyEditText etName,etEmail,etContact,etAddress,etSalary,etReferredBy;

    private User manager;
    private User currentUser;


    public ManagerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

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

        etName.setText(manager.getName());
        etEmail.setText(manager.getEmail());
        etContact.setText(manager.getContact());
        etAddress.setText(manager.getAddress());
        etSalary.setText(manager.getSalary());
        etReferredBy.setText(manager.getReferred_by());

        if(!manager.getImage_url().equals("")){
            Picasso.with(getActivity())
                    .load(manager.getImage_url())
                    .into(image);
        }

    }

    private void initView(View view) {
        ivClose = (ImageView) view.findViewById(R.id.close);
        ivTick = (ImageView) view.findViewById(R.id.tick);
        image = (CircleImageView) view.findViewById(R.id.image);

        etName = (MyEditText) view.findViewById(R.id.name);
        etEmail = (MyEditText) view.findViewById(R.id.email);
        etContact = (MyEditText) view.findViewById(R.id.contact);
        etAddress = (MyEditText) view.findViewById(R.id.address);
        etSalary = (MyEditText) view.findViewById(R.id.salary);
        etReferredBy = (MyEditText) view.findViewById(R.id.referred_by);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ivClose.setOnClickListener(this);
        ivTick.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                getFragmentManager().popBackStack();
                break;

            case R.id.tick:
                updateEmployeeOnly();
                break;
        }
    }

    private void updateEmployeeOnly() {
        String salary = etSalary.getText().toString().trim();

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        myDatabaseReference.getEmployeeReference(currentUser.getId()).child(manager.getId()).child("salary").setValue(salary);

        getFragmentManager().beginTransaction().replace(R.id.main_container,new AllManagerFragment())
                .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).commit();
    }
}
