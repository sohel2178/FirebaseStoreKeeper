package com.adec.firebasestorekeeper.Model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Sohel on 1/15/2017.
 */

public class Memo extends Transaction implements Serializable {



    public Memo(String id, String memo_voucher_id, String date, String product_name, int quantity, double unit_price, double total, String customer_id, double payment_amount, String sales_person_id, String payment_method, String remarks) {
        super(id, memo_voucher_id, date, "", 1, product_name, quantity, unit_price, total, customer_id, "", payment_amount, sales_person_id, payment_method, remarks,System.currentTimeMillis());
    }

    public Memo( String memo_voucher_id, String date, String product_name, int quantity, double unit_price, double total, String customer_id, double payment_amount, String sales_person_id, String payment_method, String remarks) {
        super("", memo_voucher_id, date, "", 1, product_name, quantity, unit_price, total, customer_id, "", payment_amount, sales_person_id, payment_method, remarks,System.currentTimeMillis());
    }

    // A Empty Constructor
    public Memo(){

    }
}
