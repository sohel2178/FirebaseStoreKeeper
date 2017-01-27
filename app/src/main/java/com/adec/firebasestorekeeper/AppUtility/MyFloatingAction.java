package com.adec.firebasestorekeeper.AppUtility;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;

import com.adec.firebasestorekeeper.Fragments.AddVoucher;
import com.adec.firebasestorekeeper.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

/**
 * Created by Sohel on 1/12/2017.
 */

public class MyFloatingAction {

    private Activity activity;
    private android.app.FragmentManager manager;

    FloatingActionButton actionButton;
    FloatingActionMenu actionMenu;

    private FloatingActionMenuListener listener;

    public MyFloatingAction(Activity activity) {
        this.activity = activity;
        manager=activity.getFragmentManager();

        ImageView icon = new ImageView(activity); // Create an icon
        icon.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.doller,null));

        actionButton = new FloatingActionButton.Builder(activity)
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);
// repeat many times:
        ImageView itemIcon = new ImageView(activity);
        itemIcon.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(),R.drawable.plus,null));

        ImageView itemIcon2 = new ImageView(activity);
        itemIcon2.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(),R.drawable.plus,null));

        ImageView itemIcon3 = new ImageView(activity);
        itemIcon3.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(),R.drawable.plus,null));

        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();

        actionMenu = new FloatingActionMenu.Builder(activity)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .build();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.buttonClick(1);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.buttonClick(2);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.buttonClick(3);
                }
            }
        });
    }

    public void setFloatingActionMenuListener(FloatingActionMenuListener listener){
        this.listener =listener;
    }



    public void hide(){
        actionMenu.close(true);
        actionButton.setVisibility(View.GONE);
    }

    public void show(){
        actionButton.setVisibility(View.VISIBLE);
    }


    public interface FloatingActionMenuListener{
        public void buttonClick(int buttonNumber);
    }
}
