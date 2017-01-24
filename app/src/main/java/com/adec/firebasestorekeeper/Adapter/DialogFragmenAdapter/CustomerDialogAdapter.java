package com.adec.firebasestorekeeper.Adapter.DialogFragmenAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.R;

import java.util.List;

/**
 * Created by Sohel on 1/14/2017.
 */

public class CustomerDialogAdapter extends RecyclerView.Adapter<CustomerDialogAdapter.CustomerDialogHolder> {

    private Context context;
    private List<Customer> customerList;
    private LayoutInflater inflater;

    private CustomerDialogAdapterListener listener;

    public CustomerDialogAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
        this.inflater= LayoutInflater.from(context);
    }

    @Override
    public CustomerDialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_customer_dialog_item,parent,false);

        CustomerDialogHolder holder = new CustomerDialogHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomerDialogHolder holder, int position) {

        Customer customer = customerList.get(position);

        holder.tvName.setText(customer.getName());
        holder.tvContact.setText(customer.getContact());

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }


    class CustomerDialogHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName,tvContact;

        public CustomerDialogHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.name);
            tvContact = (TextView) itemView.findViewById(R.id.contact);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(listener!=null){
                listener.onItemClick(getAdapterPosition());
            }

        }
    }

    public void setCustomerDialogAdapterListener(CustomerDialogAdapterListener listener){
        this.listener=listener;
    }


    public interface CustomerDialogAdapterListener{
        public void onItemClick(int position);
    }

}
