package com.adec.firebasestorekeeper.Model.Report;

/**
 * Created by Sohel on 2/10/2017.
 */

public class StoreModel {

    private String store_id;
    private String store_name;
    private double sales;
    private double due;
    private double expense;


    public StoreModel(String store_id,String store_name, double sales, double due, double expense) {
        this.store_id = store_id;
        this.store_name = store_name;
        this.sales = sales;
        this.due = due;
        this.expense = expense;
    }


    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public double getDue() {
        return due;
    }

    public void setDue(double due) {
        this.due = due;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }
}
