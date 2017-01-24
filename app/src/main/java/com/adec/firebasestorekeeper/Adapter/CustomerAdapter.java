package com.adec.firebasestorekeeper.Adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.R;

import java.util.List;

/**
 * Created by Sohel on 1/11/2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerHolder> {

    private Context context;
    private List<Customer> customerList;
    private LayoutInflater inflater;
    private CustomerListener listener;



    public CustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CustomerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_customer,parent,false);

        CustomerHolder holder = new CustomerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomerHolder holder, int position) {

        Customer customer = customerList.get(position);

        holder.tvName.setText(customer.getName());
        holder.tvContact.setText(customer.getContact());
        holder.tvOpeningBalance.setText(String.valueOf(customer.getOpening_balance()));

        animateScale(holder.itemView);

    }

    public void addCustomer(Customer customer){
        customerList.add(customer);
        int position = customerList.indexOf(customer);
        notifyItemInserted(position);

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class CustomerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName,tvContact,tvOpeningBalance;
        public CustomerHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.name);
            tvContact = (TextView) itemView.findViewById(R.id.contact);
            tvOpeningBalance = (TextView) itemView.findViewById(R.id.opening_balance);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener!= null){
                listener.onItemClick(getAdapterPosition());
            }
        }
    }

    private void animateScale(View view){

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view,"scaleY",0,1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view,"scaleX",0,1);
        scaleX.setDuration(1000);
        scaleY.setDuration(1000);
        animatorSet.playTogether(scaleX,scaleY);

        animatorSet.start();


    }

    public void setCustomerListener(CustomerListener listener){
        this.listener = listener;
    }

    public interface CustomerListener{
        public void onItemClick(int position);
    }
}
