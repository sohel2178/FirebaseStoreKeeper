package com.adec.firebasestorekeeper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adec.firebasestorekeeper.Model.PaymentAgainstMemo;
import com.adec.firebasestorekeeper.R;

import java.util.List;

/**
 * Created by Sohel on 3/1/2017.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentHolder>{
    private Context context;
    private List<PaymentAgainstMemo> paymentAgainstMemoList;
    private LayoutInflater inflater;

    public PaymentAdapter(Context context, List<PaymentAgainstMemo> paymentAgainstMemoList) {
        this.context = context;
        this.paymentAgainstMemoList = paymentAgainstMemoList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PaymentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_payment,parent,false);
        PaymentHolder holder = new PaymentHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PaymentHolder holder, int position) {
        PaymentAgainstMemo paymentAgainstMemo = paymentAgainstMemoList.get(position);

        holder.tvDate.setText(paymentAgainstMemo.getDate());
        holder.tvPayment.setText("Tk. "+paymentAgainstMemo.getPayment());

    }

    public void addPayment(PaymentAgainstMemo paymentAgainstMemo){
        paymentAgainstMemoList.add(paymentAgainstMemo);
        int position = paymentAgainstMemoList.indexOf(paymentAgainstMemo);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return paymentAgainstMemoList.size();
    }

    public class PaymentHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvPayment;

        public PaymentHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.date);
            tvPayment = (TextView) itemView.findViewById(R.id.payment_amount);
        }
    }
}
