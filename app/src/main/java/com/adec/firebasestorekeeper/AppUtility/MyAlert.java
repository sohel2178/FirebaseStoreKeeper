package com.adec.firebasestorekeeper.AppUtility;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Sohel on 1/25/2017.
 */

public class MyAlert {

    private Context context;
    private String title;
    private String message;

    private MyDialogClick myDialogClick;

    public MyAlert(Context context, String title, String message) {
        this.context = context;
        this.title = title;
        this.message = message;

        AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
        aBuilder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(myDialogClick!= null){
                            myDialogClick.positiveClick(dialog);
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = aBuilder.create();
        alertDialog.show();
    }

    public interface MyDialogClick{
        public void positiveClick(DialogInterface dialog);
        //public void negativeClick(DialogInterface dialog);
    }

    public void setMyDialogClick(MyDialogClick myDialogClick){
        this.myDialogClick=myDialogClick;
    }
}
