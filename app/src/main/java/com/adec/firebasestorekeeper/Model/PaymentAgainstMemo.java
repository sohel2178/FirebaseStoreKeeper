package com.adec.firebasestorekeeper.Model;

import java.io.Serializable;

/**
 * Created by Sohel on 3/1/2017.
 */

public class PaymentAgainstMemo implements Serializable {
    private String id;
    private String date;
    private double payment;
    private String paymentMethod;
    private String transactionId;
    private long insertDate;
    private String remarks;

    public PaymentAgainstMemo() {
    }

    public PaymentAgainstMemo(String id, String date, double payment, String paymentMethod,String transactionId,String remarks) {
        this.id = id;
        this.date = date;
        this.payment = payment;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.insertDate = System.currentTimeMillis();
        this.remarks =remarks;
    }

    public PaymentAgainstMemo(String date, double payment, String paymentMethod,String transactionId, String remarks){
        this("",date,payment,paymentMethod,transactionId,remarks);
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(long insertDate) {
        this.insertDate = insertDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
