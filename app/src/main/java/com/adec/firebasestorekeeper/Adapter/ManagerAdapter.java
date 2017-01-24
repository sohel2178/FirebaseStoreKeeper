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

import com.adec.firebasestorekeeper.AppUtility.UserLocalStore;
import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;
import com.adec.firebasestorekeeper.Utility.MyDatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sohel on 1/10/2017.
 */

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.ManagerHolder> {

    private Context context;
    private List<User> userList;
    private LayoutInflater inflater;
    private ManagerListener listener;

    private User currentUser;


    public ManagerAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.inflater = LayoutInflater.from(context);

        UserLocalStore userLocalStore = new UserLocalStore(context);
        currentUser = userLocalStore.getUser();
    }

    @Override
    public ManagerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_manager,parent,false);

        ManagerHolder holder = new ManagerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ManagerHolder holder, int position) {

        User user = userList.get(position);

        holder.tvName.setText(user.getName());
        holder.tvContact.setText(user.getContact());


        if(!user.getImage_url().equals("")){
            Picasso.with(context)
                    .load(user.getImage_url())
                    .into(holder.image);
        }

        if(!user.getAssign_store_id().equals("")){
            MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
            myDatabaseReference.getStoreRef(currentUser.getId()).child(user.getAssign_store_id())
                    .child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    holder.tvStoreName.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        animateScale(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void addManager(User manager){
        userList.add(manager);
        int position = userList.indexOf(manager);
        notifyItemInserted(position);

    }

    public class ManagerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName,tvContact,tvStoreName;
        CircleImageView image;


        public ManagerHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.name);
            tvContact = (TextView) itemView.findViewById(R.id.contact);
            tvStoreName = (TextView) itemView.findViewById(R.id.store_name);
            image = (CircleImageView) itemView.findViewById(R.id.image);

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
        scaleX.setDuration(700);
        scaleY.setDuration(700);
        animatorSet.playTogether(scaleX,scaleY);

        animatorSet.start();


    }

    public void setManagerListener(ManagerListener listener){
        this.listener = listener;
    }

    public interface ManagerListener{
        public void onItemClick(int position);
    }
}
