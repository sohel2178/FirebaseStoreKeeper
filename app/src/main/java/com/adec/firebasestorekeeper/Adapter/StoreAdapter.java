package com.adec.firebasestorekeeper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Model.Store;
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

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreHolder> {

    private StoreListener listener;

    private Context context;
    private List<Store> storeList;
    private LayoutInflater inflater;
    private User currentUser;
    private DatabaseReference employeeRef;

    public StoreAdapter(Context context, List<Store> storeList) {
        this.context = context;
        this.storeList = storeList;
        this.inflater = LayoutInflater.from(context);

        UserLocalStore userLocalStore = new UserLocalStore(context);
        currentUser = userLocalStore.getUser();

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        employeeRef=myDatabaseReference.getEmployeeReference(currentUser.getId());
    }

    @Override
    public StoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_store,parent,false);

        StoreHolder holder = new StoreHolder(view);
        return  holder;
    }

    public void setStoreListener(StoreListener listener){
        this.listener =listener;
    }

    public void addStore(Store store){
        storeList.add(store);
        int pos  = storeList.indexOf(store);
        notifyItemInserted(pos);
    }

    @Override
    public void onBindViewHolder(final StoreHolder holder, int position) {

        Store store = storeList.get(position);

        holder.tvStoreName.setText(store.getName());
        holder.tvContact.setText(store.getContact());

        if(store.getStore_image()!=null){
            if(!store.getStore_image().equals("")){
                Picasso.with(context)
                        .load(store.getStore_image())
                        .into(holder.ivStoreImage);
            }
        }

        if(store.getAssign_manager_id().equals("")){

        }else{
            holder.btnAssign.setVisibility(View.GONE);
            User manager = null;
            employeeRef.child(store.getAssign_manager_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User manager = dataSnapshot.getValue(User.class);
                    holder.tvManagerName.setText(manager.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }


    class StoreHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvStoreName,tvContact,tvManagerName;
        ImageView ivStoreImage;
        Button btnAssign;

        public StoreHolder(View itemView) {
            super(itemView);

            tvStoreName = (TextView) itemView.findViewById(R.id.store_name);
            tvContact = (TextView) itemView.findViewById(R.id.store_contact);
            tvManagerName = (TextView) itemView.findViewById(R.id.manager_name);
            ivStoreImage = (ImageView) itemView.findViewById(R.id.image);
            btnAssign = (Button) itemView.findViewById(R.id.assign);

            btnAssign.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view==btnAssign){
                if(listener!=null){
                    listener.onAssignClick(getAdapterPosition());
                }
            }

            if(view==itemView){
                if(listener!=null){
                    listener.onItemClick(getAdapterPosition());
                }
            }



        }
    }


    public interface StoreListener{
        public void onAssignClick(int position);
        public void onItemClick(int position);
    }
}
