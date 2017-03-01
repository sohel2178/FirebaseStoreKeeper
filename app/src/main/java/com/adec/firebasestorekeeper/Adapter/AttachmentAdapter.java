package com.adec.firebasestorekeeper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.adec.firebasestorekeeper.R;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sohel on 2/11/2017.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ImagesHolder> {
    private Context context;
    private List<String> attachmentList;
    private LayoutInflater inflater;
    private int height;

    private MyAttachmentListener listener;



    public AttachmentAdapter(Context context, List<String> attachmentList) {
        this.context = context;
        this.attachmentList = attachmentList;
        this.inflater = LayoutInflater.from(context);
        height=0;
    }

    public void setMyAttachmentListener(MyAttachmentListener listener){
        this.listener = listener;
    }

    @Override
    public ImagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_image_holder,parent,false);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int tempheight = view.getMeasuredHeight();

        if(tempheight>height){
            height=tempheight;
            Log.d("BBBB",height+"");
            if(listener!= null){
                listener.getHeight(height);
            }
        }

        ImagesHolder holder = new ImagesHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImagesHolder holder, int position) {
        String url = attachmentList.get(position);

        Picasso.with(context)
                .load(url)
                .into(holder.ivImage);
    }

    public void add(String url){
        attachmentList.add(url);
        int position = attachmentList.indexOf(url);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return attachmentList.size();
    }

    public class ImagesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivImage;
        IconTextView ivClose;

        public ImagesHolder(View itemView) {

            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.image);
            ivClose = (IconTextView) itemView.findViewById(R.id.btn_close);
            ivClose.setVisibility(View.GONE);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(listener!= null){
                listener.onItemClick(getAdapterPosition());
            }

        }
    }

    public interface MyAttachmentListener{
        public void getHeight(int height);
        public void onItemClick(int position);
    }
}
