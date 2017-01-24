package com.adec.firebasestorekeeper.Generics;

import android.util.Log;

/**
 * Created by Sohel on 1/18/2017.
 */

public class GenericClass<T> {

    T ob;

    public GenericClass(T ob) {
        this.ob = ob;
    }

    public T getob() {
        return ob;
    }

    public void showType() {
        Log.d("Generic","Type of T is " + ob.getClass().getName());
    }
}
