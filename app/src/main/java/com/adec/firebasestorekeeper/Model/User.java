package com.adec.firebasestorekeeper.Model;

import java.io.Serializable;

/**
 * Created by Sohel on 1/8/2017.
 */

public class User implements Serializable {

    private String id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String contact;
    private String image_url;
    private String nid;
    private String referred_by;
    private String salary;
    private String parent_id;
    private int user_type;
    private String assign_store_id;


    public User() {
    }


    public User(String name, String email, String address, String contact, String nid, String referred_by, String salary) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.nid = nid;
        this.referred_by = referred_by;
        this.salary = salary;
    }

    public User(String name, String email, String contact, int user_type) {
        this.id="";
        this.name = name;
        this.email = email;
        this.password="123456";
        this.address = "";
        this.contact = contact;
        this.user_type = user_type;
        this.image_url = "";
        this.nid = "";
        this.referred_by = "";
        this.salary = "";
        this.parent_id = "";
        this.assign_store_id = "";
    }

    public User(String id, String name, String email, String password, String address, String contact, String image_url, String nid, String referred_by, String salary, String parent_id, int user_type, String assign_store_id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.contact = contact;
        this.image_url = image_url;
        this.nid = nid;
        this.referred_by = referred_by;
        this.salary = salary;
        this.parent_id = parent_id;
        this.user_type = user_type;
        this.assign_store_id = assign_store_id;
    }

    public User(String name, String email, String address, String contact, String nid, String referred_by, String salary, String parent_id, int user_type) {
        this.id="";
        this.name = name;
        this.email = email;
        this.password="123456";
        this.address = address;
        this.contact = contact;
        this.user_type = user_type;
        this.image_url = "";
        this.nid = nid;
        this.referred_by = referred_by;
        this.salary = salary;
        this.parent_id = parent_id;
        this.assign_store_id = "";
    }

    public User(String name, String email, String address, String contact, String nid, String referred_by, String salary, String parent_id, int user_type,String assign_store_id){
        this(name,email,address,contact,nid,referred_by,salary,parent_id,user_type);
        this.assign_store_id=assign_store_id;
    }

    public String getAssign_store_id() {
        return assign_store_id;
    }

    public void setAssign_store_id(String assign_store_id) {
        this.assign_store_id = assign_store_id;
    }

   /* public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getReferred_by() {
        return referred_by;
    }

    public void setReferred_by(String referred_by) {
        this.referred_by = referred_by;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
}
