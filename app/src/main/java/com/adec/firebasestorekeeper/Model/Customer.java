package com.adec.firebasestorekeeper.Model;

import java.io.Serializable;

/**
 * Created by Sohel on 1/10/2017.
 */

public class Customer implements Serializable {

    private String id;
    private String name;
    private String email;
    private String address;
    private String contact;
    private double opening_balance;


    public Customer(String id, String name, String email, String address, String contact, double opening_balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.opening_balance = opening_balance;
    }

    public Customer(String name, String email, String address, String contact, double opening_balance) {
        this.id="";
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.opening_balance = opening_balance;
    }

    public Customer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public double getOpening_balance() {
        return opening_balance;
    }

    public void setOpening_balance(double opening_balance) {
        this.opening_balance = opening_balance;
    }
}
