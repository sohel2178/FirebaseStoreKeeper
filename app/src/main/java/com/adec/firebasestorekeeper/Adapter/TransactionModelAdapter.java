package com.adec.firebasestorekeeper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adec.firebasestorekeeper.Model.StoreTransactionModel;
import com.adec.firebasestorekeeper.R;

import java.util.List;

/**
 * Created by Sohel on 2/8/2017.
 */

public class TransactionModelAdapter extends RecyclerView.Adapter<TransactionModelAdapter.ModelStoreHolder> {

    private Context context;
    private List<StoreTransactionModel> storeTransactionModelList;
    private LayoutInflater inflater;

    public TransactionModelAdapter(Context context, List<StoreTransactionModel> storeTransactionModelList) {
        this.context = context;
        this.storeTransactionModelList = storeTransactionModelList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ModelStoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_store_model,parent,false);

        ModelStoreHolder holder = new ModelStoreHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ModelStoreHolder holder, int position) {

        StoreTransactionModel model = storeTransactionModelList.get(position);

        holder.tvStoreName.setText(model.getStoreName());
        holder.tvSalesCount.setText(model.getTransaction_count()+" Sales");
        holder.tvSales.setText("Tk. "+model.getSales());

    }

    @Override
    public int getItemCount() {
        return storeTransactionModelList.size();
    }

    public void addModel(StoreTransactionModel model){
        storeTransactionModelList.add(model);
        int pos = storeTransactionModelList.indexOf(model);
        notifyItemInserted(pos);
    }


    public class ModelStoreHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvStoreName,tvSalesCount,tvSales;

        public ModelStoreHolder(View itemView) {
            super(itemView);

            tvStoreName = (TextView) itemView.findViewById(R.id.store_name);
            tvSalesCount = (TextView) itemView.findViewById(R.id.sales_count);
            tvSales = (TextView) itemView.findViewById(R.id.sales);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
