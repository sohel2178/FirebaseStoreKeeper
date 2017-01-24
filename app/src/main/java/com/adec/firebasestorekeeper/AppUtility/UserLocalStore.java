package com.adec.firebasestorekeeper.AppUtility;

import android.content.Context;
import android.content.SharedPreferences;

import com.adec.firebasestorekeeper.Model.User;


/**
 * Created by Sohel on 4/12/2016.
 */
public class UserLocalStore {

    private static final String SP_NAME ="userDetails";

    private static final String USER_ID="USER_ID";
    private static final String USER_NAME="USER_NAME";
    private static final String USER_EMAIL="USER_EMAIL";
    private static final String USER_PASSWORD="USER_PASSWORD";
    private static final String USER_ADDRESS="USER_ADDRESS";
    private static final String USER_CONTACT="USER_CONTACT";
    private static final String USER_IMAGE_URL="USER_IMAGE_URL";
    private static final String USER_NID="USER_NID";
    private static final String USER_REFERRED_BY="USER_REFERRED_BY";
    private static final String USER_SALARY="USER_SALARY";
    private static final String USER_PARENT_ID="USER_PARENT_ID";
    private static final String USER_USER_TYPE="USER_USER_TYPE";
    private static final String USER_ASSIGN_STORE_ID="USER_ASSIGN_STORE_ID";

    private static final String CURRENT_STORE_ID="CURRENT_STORE_ID";
    private static final String LIMIT="LIMIT";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }


    // StoreUserData Method



    public  void setUserLoggedIn(boolean loggedin){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedin);
        spEditor.commit();

    }

    public boolean getUserLoggedIn(){
        return userLocalDatabase.getBoolean("loggedIn",false);
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }



    public void setProjectName(String userName){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("projectname",userName);
        spEditor.apply();
    }

    public String getProjectName(){
        return userLocalDatabase.getString("projectname","");
    }

    public void setCurrentStoreId(String storeId){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString(CURRENT_STORE_ID,storeId);
        spEditor.apply();
    }

    public String getCurrentStoreId(){
        return userLocalDatabase.getString(CURRENT_STORE_ID,"");
    }


    public void setUser(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();

        spEditor.putString(USER_ID,user.getId());
        spEditor.putString(USER_NAME,user.getName());
        spEditor.putString(USER_EMAIL,user.getEmail());
        spEditor.putString(USER_PASSWORD,user.getPassword());
        spEditor.putString(USER_ADDRESS,user.getAddress());
        spEditor.putString(USER_CONTACT,user.getContact());
        spEditor.putString(USER_IMAGE_URL,user.getImage_url());
        spEditor.putString(USER_NID,user.getNid());
        spEditor.putString(USER_REFERRED_BY,user.getReferred_by());
        spEditor.putString(USER_SALARY,user.getSalary());
        spEditor.putString(USER_PARENT_ID,user.getParent_id());
        spEditor.putInt(USER_USER_TYPE,user.getUser_type());
        spEditor.putString(USER_ASSIGN_STORE_ID,user.getAssign_store_id());

        spEditor.apply();

    }


    public User getUser(){

        String id = userLocalDatabase.getString(USER_ID,"");
        String name = userLocalDatabase.getString(USER_NAME,"");
        String email = userLocalDatabase.getString(USER_EMAIL,"");
        String password = userLocalDatabase.getString(USER_PASSWORD,"");
        String address = userLocalDatabase.getString(USER_ADDRESS,"");
        String contact = userLocalDatabase.getString(USER_CONTACT,"");
        String image_url = userLocalDatabase.getString(USER_IMAGE_URL,"");
        String nid = userLocalDatabase.getString(USER_NID,"");
        String referred_by = userLocalDatabase.getString(USER_REFERRED_BY,null);
        String salary = userLocalDatabase.getString(USER_SALARY,"");
        String parent_id = userLocalDatabase.getString(USER_PARENT_ID,"");
        int type = userLocalDatabase.getInt(USER_USER_TYPE,-1);
        String assign_store_id = userLocalDatabase.getString(USER_ASSIGN_STORE_ID,"");

        User user = new User(id,name,email,password,address,contact,image_url,nid,referred_by,salary,parent_id,type,assign_store_id);

        return user;

    }

    public void setLimit(int limit){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putInt(LIMIT,limit);
        spEditor.apply();
    }


    public int getLimit(){
        return userLocalDatabase.getInt(LIMIT,0) ;
    }


}
