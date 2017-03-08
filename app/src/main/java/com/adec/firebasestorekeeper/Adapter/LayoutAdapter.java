package com.adec.firebasestorekeeper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


import com.adec.firebasestorekeeper.Model.MyLayout;
import com.adec.firebasestorekeeper.Model.Product;
import com.adec.firebasestorekeeper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohel on 3/7/2017.
 */

public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.LayoutHolder> {

    private Context context;
    private List<MyLayout> myLayoutList;
    private LayoutInflater inflater;

    private LayoutListener listener;

    public LayoutAdapter(Context context, List<MyLayout> myLayoutList) {
        this.context = context;
        this.myLayoutList = myLayoutList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public LayoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_layout,parent,false);

        LayoutHolder holder = new LayoutHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(LayoutHolder holder, int position) {

        MyLayout layout = myLayoutList.get(position);
        layout.setEtName(holder.etName);
        layout.setEtAmount(holder.etAmount);
        layout.setEtQuantity(holder.etQuantity);

    }

    public void addItem(MyLayout layout){
        myLayoutList.add(layout);
        int pos = myLayoutList.indexOf(layout);
        notifyItemInserted(pos);
    }

    public List<Product> getData(){
        List<Product> retList = new ArrayList<>();

        for(MyLayout x: myLayoutList){

            String name =x.getEtName().getText().toString().trim();
            String quantity =x.getEtQuantity().getText().toString().trim();
            String amount =x.getEtAmount().getText().toString().trim();

            if(name.equals("") || quantity.equals("") || amount.equals("")){


            }else{
                Product product = new Product(name,Integer.parseInt(quantity),Double.parseDouble(amount));
                retList.add(product);
            }

        }

        return retList;
    }


    public void removeItem(int position){
        myLayoutList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return myLayoutList.size();
    }

    public class LayoutHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        EditText etName,etQuantity,etAmount;
        ImageView btnClose;

        public LayoutHolder(View itemView) {
            super(itemView);

            etName = (EditText) itemView.findViewById(R.id.et_name);
            etQuantity = (EditText) itemView.findViewById(R.id.et_quantity);
            etAmount = (EditText) itemView.findViewById(R.id.et_amount);
            btnClose = (ImageView) itemView.findViewById(R.id.close);

            btnClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(listener != null){
                listener.onCloseClick(getAdapterPosition());
            }

        }
    }

    public void setLayoutListener(LayoutListener listener){
        this.listener = listener;
    }

    public interface LayoutListener{
        public void onCloseClick(int position);
    }
}
