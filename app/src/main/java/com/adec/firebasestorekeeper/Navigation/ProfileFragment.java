package com.adec.firebasestorekeeper.Navigation;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Generics.GenericClass;
import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    private TextView tvName,tvEmail,tvContact,tvAddress;
    private TextView txtName,txtEmail,txtContact,txtAddress;
    private CircleImageView ivProfileImage;
    private Button btnEdit;

    private User currentUser;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserLocalStore userLocalStore = new UserLocalStore(getActivity());
        currentUser = userLocalStore.getUser();

        GenericClass<User> obj = new GenericClass<User>(currentUser);

        User user = obj.getob();

        Log.d("ggggg",user.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        setValue();
        return view;
    }

    private void initView(View view) {
        tvName = (TextView) view.findViewById(R.id.name);
        tvEmail = (TextView) view.findViewById(R.id.email);
        tvContact = (TextView) view.findViewById(R.id.contact);
        tvAddress = (TextView) view.findViewById(R.id.address);

        txtName = (TextView) view.findViewById(R.id.txt_name);
        txtEmail = (TextView) view.findViewById(R.id.txt_email);
        txtContact = (TextView) view.findViewById(R.id.txt_contact);
        txtAddress = (TextView) view.findViewById(R.id.txt_address);


        ivProfileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        btnEdit = (Button) view.findViewById(R.id.edit);
    }

    private void setValue(){
        if(!currentUser.getImage_url().equals("")){
            Picasso.with(getActivity())
                    .load(currentUser.getImage_url())
                    .into(ivProfileImage);
        }else{
            ivProfileImage.setImageResource(R.drawable.placeholder);
        }

        tvName.setText(currentUser.getName());
        tvEmail.setText(currentUser.getEmail());
        tvContact.setText(currentUser.getContact());
        tvAddress.setText(currentUser.getAddress());

        MyUtils.underLineText(txtName);
        MyUtils.underLineText(txtEmail);
        MyUtils.underLineText(txtContact);
        MyUtils.underLineText(txtAddress);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction().replace(R.id.main_container,new UpdateProfileFragment())
                .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();
    }


    public static void underLineText(TextView textView){
        textView.setPaintFlags(textView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }
}
