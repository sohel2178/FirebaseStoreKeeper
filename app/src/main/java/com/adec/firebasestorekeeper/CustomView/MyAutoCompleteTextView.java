package com.adec.firebasestorekeeper.CustomView;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.adec.firebasestorekeeper.R;


/**
 * Created by Sohel on 9/29/2016.
 */
public class MyAutoCompleteTextView extends AutoCompleteTextView implements View.OnFocusChangeListener {
    private Context context;
    public MyAutoCompleteTextView(Context context) {
        super(context);

    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.bottom_border_default,null));
        this.setOnFocusChangeListener(this);
        this.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.edit_text_text_color,null));
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(view.hasFocus()){
            view.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.bottom_border_selected,null));
        }else{
            view.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.bottom_border_default,null));
        }
    }
}
