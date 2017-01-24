package com.adec.firebasestorekeeper.CustomView;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.adec.firebasestorekeeper.CustomListener.MyChangeListener;
import com.adec.firebasestorekeeper.R;


/**
 * Created by Sohel on 9/28/2016.
 */
public class MyTextView extends TextView {
    private Context context;

    private MyChangeListener listener;
    public MyTextView(Context context) {
        super(context);
        this.context =context;
    }

    public void setMyChangeListener(MyChangeListener listener){
        this.listener =listener;
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setPadding(20,5,5,5);
        this.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.black,null));


    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);

        if(listener!= null){
            listener.textChange(this.getText().toString());
        }
    }
}
