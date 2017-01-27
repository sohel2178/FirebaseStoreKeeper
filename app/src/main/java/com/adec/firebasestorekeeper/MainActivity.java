package com.adec.firebasestorekeeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;

import com.adec.firebasestorekeeper.AppUtility.Constant;
import com.adec.firebasestorekeeper.AppUtility.MyAlert;
import com.adec.firebasestorekeeper.AppUtility.MyFloatingAction;
import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Fragments.AddMemo;
import com.adec.firebasestorekeeper.Fragments.AddVoucher;
import com.adec.firebasestorekeeper.Interface.FragmentListener;
import com.adec.firebasestorekeeper.Interface.NavDrawerListener;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Navigation.HomeFragment;
import com.adec.firebasestorekeeper.Navigation.TransactionFragment;
import com.adec.firebasestorekeeper.Navigation.UpdateProfileFragment;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyUserReferenceClass;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity implements MyUserReferenceClass.UserReferenceListener,NavDrawerListener,
        FragmentListener{

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private FragmentManager manager;

    private UserLocalStore userLocalStore;

    private NavigationDrawer drawerFragment;

    private MyFloatingAction myFloatingAction;




    DatabaseReference userRef;

    MyDatabaseReference myDatabaseReference;

    private int last_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify
                .with(new FontAwesomeModule());

        setContentView(R.layout.activity_main);

        userLocalStore = new UserLocalStore(this);
        myDatabaseReference = new MyDatabaseReference();










        if(userLocalStore.getUserLoggedIn()){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();


            // Set Navigation Drawer
            setUpNavigationDrawer();


            myFloatingAction = new MyFloatingAction(this);

            myFloatingAction.setFloatingActionMenuListener(new MyFloatingAction.FloatingActionMenuListener() {
                @Override
                public void buttonClick(int buttonNumber) {

                    switch (buttonNumber){
                        case 1:
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new AddVoucher())
                                    .addToBackStack(null).commit();

                            break;

                        case 2:
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new AddMemo())
                                    .addToBackStack(null).commit();

                            break;

                        case 3:
                            break;
                    }

                }
            });







           /* MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
            userRef=myDatabaseReference.getUserRef();
            User user = new User("Sohel Ahmed","sohel.ahmed2178@gmail.com","01741-948496",1);

            addDataToReference(userRef,user);*/

        }else{
            Intent intent = new Intent(this,LoginActivity.class);
            finish();
            startActivity(intent);
        }




       /* MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        userRef=myDatabaseReference.getUserRef();
        User user = new User("Sohel Ahmed","sohel.ahmed2178@gmail.com","01741-948496",1);

        addDataToReference(userRef,user);*/









    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void addDataToReference(DatabaseReference reference, User user){
        reference.push().setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d("UserId",databaseReference.getKey());

                String id = databaseReference.getKey();

                updateChild(id);


            }
        });
    }


    private void updateChild(String id){
        userRef.child(id).child("id").setValue(id);

        addNewNodeForUser(id);
    }

    private void addNewNodeForUser(String id) {

    }


    private void setUpNavigationDrawer(){
        manager = getSupportFragmentManager();

        //Toolbar Code
        toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);

        //Drawer Layout Code
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);



        drawerFragment =
                (NavigationDrawer) manager.findFragmentById(R.id.fragment_navigation_drawer);


        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, toolbar);
        getSupportActionBar().setTitle(Constant.HOME);

        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void getUser(User user) {
        //last_id=user.getId();
    }

    @Override
    public void onBackPressed() {
        /*for(int entry = 0; entry < getFragmentManager().getBackStackEntryCount(); entry++){
            Log.i("Sohel", "Found fragment: " + getFragmentManager().getBackStackEntryAt(entry).getId());
        }*/

        int backCount =getSupportFragmentManager().getBackStackEntryCount();

        if(backCount==0){
            MyAlert alert = new MyAlert(this,"","Click No to stay in the App");
            alert.setMyDialogClick(new MyAlert.MyDialogClick() {
                @Override
                public void positiveClick(DialogInterface dialog) {
                    dialog.dismiss();

                    MainActivity.super.onBackPressed();
                    //return;
                }
            });
        }else{
            super.onBackPressed();
        }



    }

    @Override
    public void setName(String name) {
       drawerFragment.setUserName(name);
    }

    @Override
    public void setEmail(String email) {
        drawerFragment.setUserEmail(email);
    }

    @Override
    public void setProfileImage(String url) {
        drawerFragment.setUserImage(url);
    }

    @Override
    public void getFragment(int number) {
        Log.d("HHHH",number+"");

        if(number==10){

            myFloatingAction.show();
        }else{

            myFloatingAction.hide();





        }
    }


}
