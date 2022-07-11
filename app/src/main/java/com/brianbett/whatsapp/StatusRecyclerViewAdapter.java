package com.brianbett.whatsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatusRecyclerViewAdapter extends RecyclerView.Adapter<StatusRecyclerViewAdapter.MyViewHolder>{
    private final Context context;
    private ArrayList<SingleStatus> statuses;
    public StatusRecyclerViewAdapter(Context context, ArrayList<SingleStatus> statuses) {
        this.context=context;
        this.statuses=statuses;
    }

//    making mystatus appear at the top
    @Override
    public int getItemViewType(int position) {
        int layout;
        if(position==0){
            layout=R.layout.my_status;
        }
        else{
            layout=R.layout.single_status;
        }
        return layout;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.single_status,parent,false);

        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position==0){
            holder.username.setText(statuses.get(0).getUsername());
            holder.imageLastPosted.setImageResource(statuses.get(0).getLast_image());
            holder.time.setText(statuses.get(0).getTime_posted());
            holder.itemView.setLongClickable(false);
        }

        holder.username.setText(statuses.get(position).getUsername());
        holder.imageLastPosted.setImageResource(statuses.get(position).getLast_image());
        holder.time.setText(statuses.get(position).getTime_posted());
//        opening the status activity when a status is clicked
        holder.itemView.setOnClickListener(view1 -> {
            Intent intent=new Intent(context,CameraActivity.class);
            Intent intent1=new Intent(context,StatusActivity.class);
            if(position==0){
//                context.startActivity(intent);
                context.startActivity(new Intent(context,MyStatusActivity.class));

            }else {
                intent1.putExtra("Username", String.valueOf(statuses.get(position).getPhoneNumber()));
                context.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username,time;
        ImageView imageLastPosted;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
                imageLastPosted=itemView.findViewById(R.id.last_status_thumbnail);
                username=itemView.findViewById(R.id.status_poster_name);
                time=itemView.findViewById(R.id.last_status_time);

        }
    }
}
