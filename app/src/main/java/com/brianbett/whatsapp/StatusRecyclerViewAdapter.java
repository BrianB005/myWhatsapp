package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.brianbett.whatsapp.retrofit.Contact;
import com.brianbett.whatsapp.retrofit.ImageInterface;
import com.brianbett.whatsapp.retrofit.RetrievedStatus;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class StatusRecyclerViewAdapter extends RecyclerView.Adapter<StatusRecyclerViewAdapter.MyViewHolder>{
    private final Context context;
    private final ArrayList<RetrievedStatus> statuses;
    private final ArrayList<Contact> contacts;
    public StatusRecyclerViewAdapter(Context context, ArrayList<RetrievedStatus> statuses, ArrayList<Contact> contacts) {
        this.context=context;
        this.statuses=statuses;

        this.contacts=contacts;
    }

//    making mystatus appear at the top
//    @Override
//    public int getItemViewType(int position) {
//        int layout;
//        if(position==0){
//            layout=R.layout.my_status;
//        }
//        else{
//            layout=R.layout.single_status;
//        }
//        return layout;
//    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.single_status,parent,false);

        return new MyViewHolder(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        if (position==0){
//            holder.username.setText(statuses.get(0).getUsername());
//            holder.imageLastPosted.setImageResource(statuses.get(0).getLast_image());
//            holder.time.setText(statuses.get(0).getTime_posted());
//            holder.itemView.setLongClickable(false);
//        }


        if(statuses.get(position).isTyped()){
            holder.imageLastPosted.setVisibility(View.GONE);

            holder.typedStatus.setVisibility(View.VISIBLE);
            holder.typedStatus.setText(statuses.get(position).getTitle());
            Typeface typeface = ResourcesCompat.getFont(context,Integer.parseInt(statuses.get(position).getFont()));
            holder.typedStatus.setTypeface(typeface);
            holder.typedStatus.setBackgroundColor(Integer.parseInt(statuses.get(position).getBackgroundColor()));
        }else{
            holder.imageLastPosted.setVisibility(View.VISIBLE);
            holder.typedStatus.setVisibility(View.GONE);
            RetrofitHandler.fetchImage(statuses.get(position).getStatusImage(), new ImageInterface() {
                @Override
                public void success(Bitmap bitmap) {
                    holder.imageLastPosted.setImageBitmap(bitmap);
                }

                @Override
                public void failure(Throwable throwable) {

                }
            });


        }
        if(position!=0) {
            holder.username.setText(statuses.get(position).getNestedUserDetails().getPhoneNumber());

//            Gson gson=new Gson();
//            String jsonObject=gson.toJson(statuses.get(position));
//            Log.d("status",jsonObject);

        }else {
            holder.username.setText("My Status");
        }
        holder.time.setText(statuses.get(position).getTimeCreated());


//        opening the status activity when a status is clicked
        holder.itemView.setOnClickListener(view1 -> {
            Intent intent=new Intent(context,CameraActivity.class);
            Intent intent1=new Intent(context,StatusActivity.class);
            if(position==0){
//                context.startActivity(intent);
                context.startActivity(new Intent(context,MyStatusActivity.class));

            }else {
                intent1.putExtra("userId", String.valueOf(statuses.get(position).getNestedUserDetails().getUserId()));
                intent1.putExtra("userName", String.valueOf(statuses.get(position).getNestedUserDetails().getPhoneNumber()));
                context.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username,time,typedStatus;
        ImageView imageLastPosted;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
                imageLastPosted=itemView.findViewById(R.id.image);
                typedStatus=itemView.findViewById(R.id.typedStatus);
                username=itemView.findViewById(R.id.status_poster_name);
                time=itemView.findViewById(R.id.last_status_time);

        }
    }
}
