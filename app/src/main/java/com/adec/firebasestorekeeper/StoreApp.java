package com.adec.firebasestorekeeper;

import android.app.Activity;
import android.app.Application;

import com.adec.firebasestorekeeper.AppUtility.MyFloatingAction;

/**
 * Created by Sohel on 1/27/2017.
 */

public class StoreApp extends Application {

    public static MyFloatingAction myFloatingAction;

    @Override
    public void onCreate() {
        super.onCreate();

        myFloatingAction = new MyFloatingAction((Activity) getApplicationContext());
    }
}
