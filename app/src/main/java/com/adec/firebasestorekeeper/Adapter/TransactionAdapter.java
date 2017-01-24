package com.adec.firebasestorekeeper.Adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.R;

import java.util.List;

/**
 * Created by Sohel on 1/15/2017.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private Context context;
    private List<Transaction> transactionList;
    private LayoutInflater inflater;

    private TransactionListener listener;


    public TransactionAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_transaction,parent,false);

        TransactionHolder holder = new TransactionHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {

        Transaction transaction = transactionList.get(position);

        holder.tvDate.setText(MyUtils.getDateText(transaction.getInsert_date()));
        holder.tvVoucherMemoId.setText(transaction.getMemo_voucher_id());

        if(transaction.getType()==0){
            holder.tvPaymentAmountOrTotal.setText(String.valueOf(transaction.getPayment_amount()));
            holder.tvPaymentAmountOrTotal.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.delete,null));

            holder.tvPayToOrCustomer.setText(transaction.getPay_to());
        }else{
            holder.tvPaymentAmountOrTotal.setText(String.valueOf(transaction.getTotal()));
            holder.tvPaymentAmountOrTotal.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.my_color,null));

            holder.tvPayToOrCustomer.setText(transaction.getProduct_name());
        }

    }

    public void addTransaction(Transaction transaction){
        transactionList.add(0,transaction);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class TransactionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvDate,tvVoucherMemoId,tvPaymentAmountOrTotal,tvPayToOrCustomer;

        public TransactionHolder(View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.date);
            tvPayToOrCustomer = (TextView) itemView.findViewById(R.id.pay_to_or_customer);
            tvPaymentAmountOrTotal = (TextView) itemView.findViewById(R.id.payment_amount_or_total);
            tvVoucherMemoId = (TextView) itemView.findViewById(R.id.voucher_memo_id);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(listener!= null){
                listener.onClickTransaction(getAdapterPosition());
            }

        }
    }


    public void setTransactionListener(TransactionListener listener){
        this.listener = listener;
    }


    public interface TransactionListener{
        public void onClickTransaction(int position);
    }
}
