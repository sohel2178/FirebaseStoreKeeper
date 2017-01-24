package com.adec.firebasestorekeeper.Adapter.SpinnerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adec.firebasestorekeeper.Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohel on 1/14/2017.
 */

public class SalesManSpinnerAdapter extends ArrayAdapter {

    private Context context;
    private List<User> userList;

    public SalesManSpinnerAdapter(Context context, int resource, List<User> userList) {
        super(context, resource, userList);
        this.context = context;
        this.userList = userList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(userList.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(userList.get(position).getName());
        return label;
    }
}
