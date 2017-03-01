package com.adec.firebasestorekeeper.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sohel on 2/10/2017.
 */

public class OwnerDataModel implements Serializable {

    private String store_name;
    private String store_id;
    private List<Transaction> transactionList;

    public OwnerDataModel(String store_name, List<Transaction> transactionList,String store_id) {
        this.store_name = store_name;
        this.transactionList = transactionList;
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }
}
