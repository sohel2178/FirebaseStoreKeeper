package com.adec.firebasestorekeeper.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.adec.firebasestorekeeper.R;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sohel on 1/14/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {

    private Context context;
    private List<Bitmap> bitmapList;
    private LayoutInflater inflater;

    private AttachmentListener listener;

    public ImageAdapter(Context context, List<Bitmap> bitmapList) {
        this.context = context;
        this.bitmapList = bitmapList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setAttacmentListener(AttachmentListener listener){
        this.listener=listener;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_image_holder,parent,false);

        ImageHolder holder = new ImageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {

        Bitmap bitmap = bitmapList.get(position);

        holder.imageView.setImageBitmap(bitmap);

    }


    public void addImage(Bitmap bitmap){
        bitmapList.add(bitmap);
        int position = bitmapList.indexOf(bitmap);
        notifyItemInserted(position);
    }

    public void removeImage(Bitmap bitmap){
        int position = bitmapList.indexOf(bitmap);
        bitmapList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        IconTextView tvClose;

        public ImageHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image);
            tvClose = (IconTextView) itemView.findViewById(R.id.btn_close);

            tvClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(listener!= null){
                listener.onRemoveClick(getAdapterPosition());
            }

        }
    }

    public interface AttachmentListener{
        public void onRemoveClick(int postion);
    }
}
