package com.adec.firebasestorekeeper.AppUtility;



import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;


/**
 * Created by Sohel on 10/8/2016.
 */

public class MyValueFormatter implements ValueFormatter {
    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
    }



    @Override
    public String getFormattedValue(float value) {
        return "Tk. "+mFormat.format(value);// e.g. append a dollar-sign
    }
}
