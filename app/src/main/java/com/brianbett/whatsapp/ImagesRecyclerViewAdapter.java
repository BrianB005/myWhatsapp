package com.brianbett.whatsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImagesRecyclerViewAdapter.MyViewHolder>{
    List<String> myImages;
    List<Bitmap> thumbnails;
    Context context;
    public ImagesRecyclerViewAdapter(Context context,List<String> images) {
        this.context=context;
        this.myImages=images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.image,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageURI(Uri.parse(myImages.get(position)));
//        holder.imageView.setImageBitmap(thumbnails.get(position));

    }

    @Override
    public int getItemCount() {
        return myImages.size();
//        return thumbnails.size();
    }



    protected static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView=itemView.findViewById(R.id.image);

        }
    }
}
