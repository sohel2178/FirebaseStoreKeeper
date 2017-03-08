package com.adec.firebasestorekeeper.Adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Model.Customer;
import com.adec.firebasestorekeeper.Model.PaymentAgainstOB;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Sohel on 1/11/2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerHolder> {

    private Context context;
    private List<Customer> customerList;
    private LayoutInflater inflater;
    private CustomerListener listener;

    private User currentUser;



    public CustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
        this.inflater = LayoutInflater.from(context);

        UserLocalStore userLocalStore = new UserLocalStore(context);
        currentUser = userLocalStore.getUser();

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
        //holder.tvOpeningBalance.setText(String.valueOf(customer.getOpening_balance()));

        if(currentUser.getUser_type()==0){
            holder.iv_transaction.setVisibility(View.GONE);
        }



        updateCutomerOpeningBalance(holder.tvOpeningBalance,customer);

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
        ImageView iv_transaction;
        public CustomerHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.name);
            tvContact = (TextView) itemView.findViewById(R.id.contact);
            tvOpeningBalance = (TextView) itemView.findViewById(R.id.opening_balance);
            iv_transaction = (ImageView) itemView.findViewById(R.id.iv_transaction);

            itemView.setOnClickListener(this);
            iv_transaction.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.iv_transaction){
                if(listener!= null){
                    listener.onTransactionClick(getAdapterPosition());
                }
            }
            if(view==itemView){
                if(listener!= null){
                    listener.onItemClick(getAdapterPosition());
                }
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

    private void updateCutomerOpeningBalance(final TextView textView, final Customer customer){
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        myDatabaseReference.getPaymentRefOB(customer.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double payment=0;
                for(DataSnapshot x : dataSnapshot.getChildren()){
                    PaymentAgainstOB paymentAgainstOB = x.getValue(PaymentAgainstOB.class);
                    payment = payment+paymentAgainstOB.getPayment();
                }

                double openingBal = customer.getOpening_balance()-payment;
                textView.setText(String.valueOf("Tk. "+openingBal));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setCustomerListener(CustomerListener listener){
        this.listener = listener;
    }

    public interface CustomerListener{
        public void onItemClick(int position);
        public void onTransactionClick(int position);
    }
}
