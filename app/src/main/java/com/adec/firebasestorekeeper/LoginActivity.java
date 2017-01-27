package com.adec.firebasestorekeeper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.adec.firebasestorekeeper.Utility.RefListenerPackage.MyUserReferenceClass;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, MyUserReferenceClass.UserReferenceListener{


    //private int last_id;

    private List<User> userList;

    private UserLocalStore userLocalStore;

    // View for this Activity
    private EditText etUserName,etPassword;
    private Button btnLogin;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userLocalStore = new UserLocalStore(this);

        userList = new ArrayList<>();

        //last_id =0;

        MyUserReferenceClass myUserReferenceClass = new MyUserReferenceClass();
        myUserReferenceClass.setUserReferenceListener(this);





        initView();

    }

    private void initView() {
        etUserName = (EditText) findViewById(R.id.user_name);
        etPassword = (EditText) findViewById(R.id.password);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void getUser(User user) {
       /* last_id=user.getId();

        Log.d("lastId",String.valueOf(last_id));*/
        userList.add(user);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:



                String email = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    etUserName.requestFocus();
                    Toast.makeText(this, "Email is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    etPassword.requestFocus();
                    Toast.makeText(this, "Password Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(userList.size()==0){
                    Toast.makeText(this, "No User Loaded Yet", Toast.LENGTH_SHORT).show();
                    return;
                }

                while (userList.size()==0){
                    continue;
                }

                loginUser(email,password);
                break;
        }
    }

    private void loginUser(String email,String password) {
        User user = getLoginUser(email,password);


        if(user!=null){

            if(user.getUser_type()==1 && user.getAssign_store_id().equals("")){
                Toast.makeText(this, "You are not assign any store yet!!!", Toast.LENGTH_SHORT).show();
                return;
            }else{
                userLocalStore.setUserLoggedIn(true);
                userLocalStore.setUser(user);
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }


        }else{
            Toast.makeText(this, "Email or Password doesn't match", Toast.LENGTH_SHORT).show();
        }
    }

    private User getLoginUser(String email, String password) {
        User user = null;

        for(User x : userList){
            if(x.getEmail().equals(email) && x.getPassword().equals(password)){
                user=x;
                break;
            }
        }

        return user;
    }
}
