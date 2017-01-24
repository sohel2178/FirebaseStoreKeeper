package com.adec.firebasestorekeeper;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Navigation.AllCustomersFragment;
import com.adec.firebasestorekeeper.Navigation.AllManagerFragment;
import com.adec.firebasestorekeeper.Navigation.AllStoreFragment;
import com.adec.firebasestorekeeper.Navigation.Employees;
import com.adec.firebasestorekeeper.Navigation.HomeFragment;
import com.adec.firebasestorekeeper.Navigation.ProfileFragment;
import com.adec.firebasestorekeeper.Navigation.UpdateProfileFragment;
import com.adec.firebasestorekeeper.Navigation.TransactionFragment;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawer extends Fragment implements View.OnClickListener {

    private RelativeLayout rlHome,rlProfile,rlStores,rlViewAllManager,rlAllCustomers,rlEmployees,rlTransaction,rlLogout;


    public static final String PREF_NAME ="mypref";
    public static final String KEY_USER_LEARNED_DRAWERR="user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private CircleImageView profileImage;
    private TextView tvName,tvEmail;

    private View containerView;

    private UserLocalStore userLocalStore;

    private User user;






    public NavigationDrawer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        userLocalStore = new UserLocalStore(getActivity());
        user = userLocalStore.getUser();*/

        userLocalStore = new UserLocalStore(getActivity());
        //userLocalStore.clearUserData();
        user = userLocalStore.getUser();




        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWERR,"false"));

        // if saveInstanceState is not null its coming back from rotation
        if(savedInstanceState!=null){
            mFromSavedInstanceState=true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


        //Initialize View

        initView(view);

        if(user.getUser_type()==0){
            rlStores.setVisibility(View.VISIBLE);
            rlViewAllManager.setVisibility(View.VISIBLE);
        }

        if(user.getUser_type()==1){
            rlTransaction.setVisibility(View.VISIBLE);
        }


        if(user.getImage_url().equals("")){
            profileImage.setImageResource(R.drawable.placeholder);
        }else{
            Picasso.with(getActivity())
                    .load(user.getImage_url())
                    .into(profileImage);
        }

        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());









        return view;
    }

    private void initView(View view) {

        rlHome = (RelativeLayout) view.findViewById(R.id.home);
        rlProfile = (RelativeLayout) view.findViewById(R.id.profile);
        rlStores = (RelativeLayout) view.findViewById(R.id.all_store);
        rlViewAllManager = (RelativeLayout) view.findViewById(R.id.view_all_manager);

        rlAllCustomers = (RelativeLayout) view.findViewById(R.id.all_customers);
        rlEmployees = (RelativeLayout) view.findViewById(R.id.employees);
        rlTransaction = (RelativeLayout) view.findViewById(R.id.transaction);
        rlLogout = (RelativeLayout) view.findViewById(R.id.log_out);

        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        tvName = (TextView) view.findViewById(R.id.name);
        tvEmail = (TextView) view.findViewById(R.id.email);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rlHome.setOnClickListener(this);
        rlProfile.setOnClickListener(this);
        rlStores.setOnClickListener(this);
        rlViewAllManager.setOnClickListener(this);
        rlAllCustomers.setOnClickListener(this);
        rlEmployees.setOnClickListener(this);
        rlTransaction.setOnClickListener(this);
        rlLogout.setOnClickListener(this);



    }

    public void setUp(int fragmentId, DrawerLayout layout, final Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = layout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                //if user gonna not seen the drawer before thats mean the drawer is open for the first time

                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    // save it in sharedpreferences
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWERR,mUserLearnedDrawer+"");

                    getActivity().invalidateOptionsMenu();
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Log.d("Sohel","Offset "+slideOffset);
                /*if(slideOffset<0.4){
                    toolbar.setAlpha(1-slideOffset);
                }*/
            }
        };

        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String key, String prefValue){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,prefValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String key, String defaultValue){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return pref.getString(key,defaultValue);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                getFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment())
                        .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();
                break;

            case R.id.profile:


                mDrawerLayout.closeDrawer(Gravity.LEFT);
                getFragmentManager().beginTransaction().replace(R.id.main_container,new ProfileFragment())
                        .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();


                break;


            case R.id.all_store:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                getFragmentManager().beginTransaction().replace(R.id.main_container,new AllStoreFragment())
                        .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();
                break;


            case R.id.view_all_manager:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                getFragmentManager().beginTransaction().replace(R.id.main_container,new AllManagerFragment())
                        .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();
                break;


            case R.id.all_customers:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                getFragmentManager().beginTransaction().replace(R.id.main_container,new AllCustomersFragment())
                        .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();
                break;

            case R.id.employees:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                getFragmentManager().beginTransaction().replace(R.id.main_container,new Employees())
                        .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();
                break;

            case R.id.transaction:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                getFragmentManager().beginTransaction().replace(R.id.main_container,new TransactionFragment())
                        .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).commit();
                break;

            case R.id.log_out:
                userLocalStore.clearUserData();
                getActivity().finish();
                break;
        }
    }


    public void setUserName(String name){
        tvName.setText(name);
    }

    public void setUserImage(String url){
        Picasso.with(getActivity())
                .load(url)
                .into(profileImage);
    }

    public void setUserEmail(String email){
        tvEmail.setText(email);
    }
}
