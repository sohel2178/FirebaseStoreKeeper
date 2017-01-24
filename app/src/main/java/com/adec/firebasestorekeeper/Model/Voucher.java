package com.adec.firebasestorekeeper.Model;

import java.sql.Timestamp;

/**
 * Created by Sohel on 1/11/2017.
 */

public class Voucher extends Transaction {


    public Voucher(String id, String memo_voucher_id, String date, String expenditure_head, String pay_to, double payment_amount,  String payment_method,  String remarks) {
        super(id, memo_voucher_id, date, expenditure_head, 0, "", 0, 0, 0, "", pay_to, payment_amount, "", payment_method, remarks,System.currentTimeMillis());
    }

    public Voucher(String memo_voucher_id, String date, String expenditure_head, String pay_to, double payment_amount,  String payment_method, String remarks) {
        super("", memo_voucher_id, date, expenditure_head, 0, "", 0, 0, 0, "", pay_to, payment_amount, "", payment_method,  remarks,System.currentTimeMillis());
    }

    public Voucher(){

    }
}
