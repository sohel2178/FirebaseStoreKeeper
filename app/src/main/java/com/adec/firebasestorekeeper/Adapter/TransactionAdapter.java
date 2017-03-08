package com.adec.firebasestorekeeper.Adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adec.firebasestorekeeper.AppUtility.MyUtils;
import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Model.Transaction;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sohel on 1/15/2017.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private Context context;
    private List<Transaction> transactionList;
    private LayoutInflater inflater;

    private TransactionListener listener;

    private ValueEventListener valueEventListener;
    private DatabaseReference attachmentRef;

    private MyDatabaseReference myDatabaseReference;

    private User currentUser;


    public TransactionAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
        this.inflater = LayoutInflater.from(context);

        myDatabaseReference = new MyDatabaseReference();

        UserLocalStore userLocalStore = new UserLocalStore(context);
        currentUser = userLocalStore.getUser();


    }

    public void clearAll(){
        transactionList.clear();
        notifyDataSetChanged();
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_transaction,parent,false);

        TransactionHolder holder = new TransactionHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TransactionHolder holder, int position) {

        Transaction transaction = transactionList.get(position);

        holder.tvDate.setText(MyUtils.getDateText(transaction.getInsert_date()));
        holder.tvVoucherMemoId.setText(transaction.getMemo_voucher_id());

        if(transaction.getType()==0){
            holder.tvPaymentAmountOrTotal.setText(String.valueOf(transaction.getPayment_amount()));
            holder.tvPaymentAmountOrTotal.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.red,null));

            holder.tvPayToOrCustomer.setText(transaction.getPay_to());
        }else{
            holder.tvPaymentAmountOrTotal.setText(String.valueOf(transaction.getTotal()));
            holder.tvPaymentAmountOrTotal.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.dark_green,null));

            holder.tvPayToOrCustomer.setText(transaction.getProduct_name());
        }

        attachmentRef = myDatabaseReference.getAttachmentRererence(transaction.getId());
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot x : dataSnapshot.getChildren()){
                    String url = x.getValue(String.class);
                    Picasso.with(context).load(url).into(holder.ivImage);
                    break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        attachmentRef.addListenerForSingleValueEvent(valueEventListener);

        // update Store Name
        updateStoreName(holder.tvStroreName,transaction.getStore_id());

        // Update Due
        if(transaction.getType()==0){
            holder.tvDue.setVisibility(View.GONE);
        }



    }

    public void addTransaction(Transaction transaction){
        transactionList.add(0,transaction);
        notifyItemInserted(0);
    }

    private void updateStoreName(final TextView textView, String store_id){
        String ownerId=null;

        if(currentUser.getUser_type()==0){
           ownerId= currentUser.getId();
        }else if(currentUser.getUser_type()==1){
            ownerId = currentUser.getParent_id();
        }
        myDatabaseReference.getStoreRef(ownerId).child(store_id).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.getValue(String.class);
                        textView.setText(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class TransactionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvDate,tvVoucherMemoId,tvPaymentAmountOrTotal,tvPayToOrCustomer,tvStroreName,tvDue;
        ImageView ivImage;

        public TransactionHolder(View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.date);
            tvPayToOrCustomer = (TextView) itemView.findViewById(R.id.pay_to_or_customer);
            tvPaymentAmountOrTotal = (TextView) itemView.findViewById(R.id.payment_amount_or_total);
            tvVoucherMemoId = (TextView) itemView.findViewById(R.id.voucher_memo_id);
            tvStroreName = (TextView) itemView.findViewById(R.id.store_name);
            tvDue = (TextView) itemView.findViewById(R.id.due);

            ivImage = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(listener!= null){
                listener.onClickTransaction(getAdapterPosition());
            }

        }
    }

    public void removeAttachmentListener(){

        if(attachmentRef != null && valueEventListener!=null){
            attachmentRef.removeEventListener(valueEventListener);
        }

    }




    public void setTransactionListener(TransactionListener listener){
        this.listener = listener;
    }


    public interface TransactionListener{
        public void onClickTransaction(int position);
    }
}
