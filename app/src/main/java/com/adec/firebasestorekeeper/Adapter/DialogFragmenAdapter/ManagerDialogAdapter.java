package com.adec.firebasestorekeeper.Adapter.DialogFragmenAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adec.firebasestorekeeper.Model.User;
import com.adec.firebasestorekeeper.R;

import java.util.List;

/**
 * Created by Sohel on 1/16/2017.
 */

public class ManagerDialogAdapter extends RecyclerView.Adapter<ManagerDialogAdapter.ManagerDialogHolder> {

    private Context context;
    private List<User> managerList;
    private LayoutInflater inflater;

    private ManagerDialogListener listener;

    private int selectedPos;

    public ManagerDialogAdapter(Context context, List<User> managerList) {
        this.context = context;
        this.managerList = managerList;
        this.inflater = LayoutInflater.from(context);
        selectedPos =-1;
    }

    @Override
    public ManagerDialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_manager_dialog_holder,parent,false);

        ManagerDialogHolder holder = new ManagerDialogHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(ManagerDialogHolder holder, int position) {

        User manager = managerList.get(position);

        holder.tvName.setText(manager.getName());
        holder.tvContact.setText(manager.getContact());

        if(selectedPos==position){
            holder.radio.setImageResource(R.drawable.radio_select);
        }else{
            holder.radio.setImageResource(R.drawable.radio_deselect);
        }



    }

    public void setManagerDialogListener(ManagerDialogListener listener){
        this.listener =listener;
    }

    public void addManager(User user){
        managerList.add(user);
        int pos = managerList.indexOf(user);

        notifyItemInserted(pos);
    }

    @Override
    public int getItemCount() {
        return managerList.size();
    }

    class ManagerDialogHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView radio;
        TextView tvName,tvContact;

        public ManagerDialogHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.name);
            tvContact = (TextView) itemView.findViewById(R.id.contact);
            radio = (ImageView) itemView.findViewById(R.id.radio);

            radio.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectedPos=getAdapterPosition();
            notifyDataSetChanged();

            if(selectedPos!=-1){
                listener.selectedManager(selectedPos);
            }
        }
    }

    public interface ManagerDialogListener{
        public void selectedManager(int position);
    }
}
