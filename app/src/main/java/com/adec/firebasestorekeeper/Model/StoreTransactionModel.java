package com.adec.firebasestorekeeper.Model;

/**
 * Created by Sohel on 2/8/2017.
 */

public class StoreTransactionModel {

    private String storeName;
    private int transaction_count;
    private double sales;


    public StoreTransactionModel(String storeName, int transaction_count, double sales) {
        this.storeName = storeName;
        this.transaction_count = transaction_count;
        this.sales = sales;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getTransaction_count() {
        return transaction_count;
    }

    public void setTransaction_count(int transaction_count) {
        this.transaction_count = transaction_count;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }
}
