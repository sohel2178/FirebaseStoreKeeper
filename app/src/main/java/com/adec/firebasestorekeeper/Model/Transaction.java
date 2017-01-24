package com.adec.firebasestorekeeper.Model;


import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Sohel on 1/11/2017.
 */

public class Transaction implements Serializable{
    private String id;
    private String memo_voucher_id;
    private String date;
    private String expenditure_head;
    private int type;
    private String product_name;
    private int quantity;
    private double unit_price;
    private double total;
    private String customer_id;
    private String pay_to;
    private double payment_amount;
    private String sales_person_id;
    private String payment_method;
    private String remarks;
    private long insert_date;

    public Transaction() {
    }


    public Transaction(String id, String memo_voucher_id, String date, String expenditure_head, int type, String product_name, int quantity, double unit_price, double total, String customer_id, String pay_to, double payment_amount, String sales_person_id, String payment_method,  String remarks,long insert_date) {
        this.id = id;
        this.memo_voucher_id = memo_voucher_id;
        this.date = date;
        this.expenditure_head = expenditure_head;
        this.type = type;
        this.product_name = product_name;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.total = total;
        this.customer_id = customer_id;
        this.pay_to = pay_to;
        this.payment_amount = payment_amount;
        this.sales_person_id = sales_person_id;
        this.payment_method = payment_method;
        this.remarks = remarks;
        this.insert_date=insert_date;
    }

    public String getExpenditure_head() {
        return expenditure_head;
    }

    public void setExpenditure_head(String expenditure_head) {
        this.expenditure_head = expenditure_head;
    }

    public long getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(long insert_date) {
        this.insert_date = insert_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getPay_to() {
        return pay_to;
    }

    public void setPay_to(String pay_to) {
        this.pay_to = pay_to;
    }

    public double getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(double payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getSales_person_id() {
        return sales_person_id;
    }

    public void setSales_person_id(String sales_person_id) {
        this.sales_person_id = sales_person_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemo_voucher_id() {
        return memo_voucher_id;
    }

    public void setMemo_voucher_id(String memo_voucher_id) {
        this.memo_voucher_id = memo_voucher_id;
    }

    public String getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
