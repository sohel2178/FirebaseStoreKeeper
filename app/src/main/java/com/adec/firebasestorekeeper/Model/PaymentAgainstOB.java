package com.adec.firebasestorekeeper.Model;

import java.io.Serializable;

/**
 * Created by Sohel on 2/27/2017.
 */

public class PaymentAgainstOB implements Serializable {
    private String id;
    private String date;
    private double payment;
    private String paymentMethod;
    private long insertDate;
    private String remarks;

    public PaymentAgainstOB() {
    }

    public PaymentAgainstOB(String id, String date, double payment, String paymentMethod,String remarks) {
        this.id = id;
        this.date = date;
        this.payment = payment;
        this.paymentMethod = paymentMethod;
        this.insertDate = System.currentTimeMillis();
        this.remarks =remarks;
    }



    public PaymentAgainstOB(String date, double payment, String paymentMethod, String remarks) {
        this("",date,payment,paymentMethod,remarks);

    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public long getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(long insertDate) {
        this.insertDate = insertDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
