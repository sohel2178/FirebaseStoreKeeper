package com.adec.firebasestorekeeper.Model;

import java.io.Serializable;

/**
 * Created by Sohel on 1/9/2017.
 */

public class Store implements Serializable{

    private String id;
    private String name;
    private String address;
    private String contact;
    private String assign_manager_id;
    private String store_image;


    public Store() {
    }


    public Store(String id, String name, String address, String contact, String assign_manager_id,String store_image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.assign_manager_id = assign_manager_id;
        this.store_image=store_image;
    }


    public Store(String id, String name, String address, String contact, String assign_manager_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.assign_manager_id = assign_manager_id;
    }

    public Store(String name, String address, String contact) {
        this.id="";
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.assign_manager_id="";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssign_manager_id() {
        return assign_manager_id;
    }

    public void setAssign_manager_id(String assign_manager_id) {
        this.assign_manager_id = assign_manager_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }
}
