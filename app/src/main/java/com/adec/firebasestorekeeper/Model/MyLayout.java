package com.adec.firebasestorekeeper.Model;

import android.widget.EditText;

/**
 * Created by Sohel on 3/7/2017.
 */

public class MyLayout {

    private EditText etName;
    private EditText etQuantity;
    private EditText etAmount;

    public MyLayout() {
    }


    public MyLayout(EditText etName, EditText etQuantity, EditText etAmount) {
        this.etName = etName;
        this.etQuantity = etQuantity;
        this.etAmount = etAmount;
    }

    public EditText getEtName() {
        return etName;
    }

    public void setEtName(EditText etName) {
        this.etName = etName;
    }

    public EditText getEtQuantity() {
        return etQuantity;
    }

    public void setEtQuantity(EditText etQuantity) {
        this.etQuantity = etQuantity;
    }

    public EditText getEtAmount() {
        return etAmount;
    }

    public void setEtAmount(EditText etAmount) {
        this.etAmount = etAmount;
    }
}
