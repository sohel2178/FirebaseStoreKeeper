package com.adec.firebasestorekeeper.Adapter.ReportAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adec.firebasestorekeeper.AppUtility.MyFloatingAction;
import com.adec.firebasestorekeeper.Model.Report.StoreModel;
import com.adec.firebasestorekeeper.R;

import java.util.List;

/**
 * Created by Sohel on 2/10/2017.
 */

public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportAdapter.DailyHolder>{

    private Context context;
    private List<StoreModel> storeModelList;
    private LayoutInflater inflater;
    private int height;
    private ReportListener listener;

    public DailyReportAdapter(Context context, List<StoreModel> storeModelList) {
        this.context = context;
        this.storeModelList = storeModelList;
        this.inflater = LayoutInflater.from(context);
        this.height=0;
    }

    @Override
    public DailyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_store_daily_report,parent, false);

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int tempheight = view.getMeasuredHeight();

        if(tempheight>height){
            height=tempheight;
            Log.d("BBBB",height+"");
            if(listener!= null){
                listener.getHeight(height);
            }
        }


        DailyHolder holder = new DailyHolder(view);
        return holder;
    }

    public int getHeight(){
       return this.height*storeModelList.size();
    }

    @Override
    public void onBindViewHolder(DailyHolder holder, int position) {

        StoreModel storeModel = storeModelList.get(position);


        holder.tvName.setText(storeModel.getStore_name());
        holder.tvSales.setText("Tk. "+String.valueOf(storeModel.getSales()));
        holder.tvDue.setText("Tk. "+String.valueOf(storeModel.getDue()));
        holder.tvExpense.setText("Tk. "+String.valueOf(storeModel.getExpense()));

        int hei=holder.itemView.getMeasuredHeight();



    }

    @Override
    public int getItemCount() {
        return storeModelList.size();
    }

    public class DailyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName,tvSales,tvDue,tvExpense;

        public DailyHolder(View itemView) {
            super(itemView);



            tvName = (TextView) itemView.findViewById(R.id.store_name);
            tvSales = (TextView) itemView.findViewById(R.id.sales);
            tvDue = (TextView) itemView.findViewById(R.id.due);
            tvExpense = (TextView) itemView.findViewById(R.id.expense);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener!= null){
                listener.onItemClick(getAdapterPosition());
            }
        }
    }

    public void setReportListener(ReportListener listener){
        this.listener =listener;
    }

    public interface ReportListener{
        public void getHeight(int height);
        public void onItemClick(int position);
    }


}
