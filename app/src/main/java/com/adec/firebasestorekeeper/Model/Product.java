package com.adec.firebasestorekeeper.Model;

/**
 * Created by Sohel on 1/12/2017.
 */

public class Product {

    private String name;
    private int quantity;
    private double amount;


    public Product() {
    }


    public Product(String name, int quantity, double amount) {
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
