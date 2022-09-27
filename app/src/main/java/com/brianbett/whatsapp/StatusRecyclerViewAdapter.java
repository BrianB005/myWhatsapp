package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.brianbett.whatsapp.retrofit.Contact;
import com.brianbett.whatsapp.retrofit.MyPreferences;
import com.brianbett.whatsapp.retrofit.RetrievedStatus;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.single_status,parent,false);

        return new MyViewHolder(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(position!=0) {
            holder.username.setText(statuses.get(position).getNestedUserDetails().getPhoneNumber());

//            Gson gson=new Gson();
//            String jsonObject=gson.toJson(statuses.get(position));
//            Log.d("status",jsonObject);

            holder.time.setText(ConvertTimestamp.getStatusMoments(context,statuses.get(position).getTimeCreated()));
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
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + statuses.get(position).getStatusImage());


                Task<Uri> uriTask = storageReference.getDownloadUrl();

                uriTask.addOnSuccessListener(uri1 -> {

                    Glide.with(context).load(uri1).into(holder.imageLastPosted);
                }).addOnFailureListener(e -> {

                    Toast.makeText(context, "Something went wrong loading data!Try again later.", Toast.LENGTH_SHORT).show();
                    Log.e("Exception",e.getMessage());
                });


            }

        }else {

            holder.username.setText("My status");
            if(statuses.get(position).getTimeCreated().equals("Tap here to add a status update!")){
                String profilePic= MyPreferences.getSavedItem(context,"profilePic");
                Glide.with(context).load(profilePic).into(holder.imageLastPosted);
                holder.time.setText(statuses.get(position).getTimeCreated());


            }else{
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
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + statuses.get(position).getStatusImage());


                    Task<Uri> uriTask = storageReference.getDownloadUrl();

                    uriTask.addOnSuccessListener(uri1 -> {

                        Glide.with(context).load(uri1).into(holder.imageLastPosted);
                    }).addOnFailureListener(e -> {

                        Toast.makeText(context, "Something went wrong loading data!Try again later.", Toast.LENGTH_SHORT).show();
                        Log.e("Exception",e.getMessage());
                    });

                }
                holder.time.setText(ConvertTimestamp.getStatusMoments(context,statuses.get(position).getTimeCreated()));

            }

        }



//        opening the status activity when a status is clicked
        holder.itemView.setOnClickListener(view1 -> {
            Intent intent1=new Intent(context,StatusActivity.class);
            if(position==0){
//                context.startActivity(intent);
                if(holder.time.getText().equals("Tap here to add a status update!")){

                    context.startActivity(new Intent(context,TypeStatusActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else {
                    context.startActivity(new Intent(context, MyStatusActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }

            }else {
                intent1.putExtra("userId", String.valueOf(statuses.get(position).getNestedUserDetails().getUserId()));
                intent1.putExtra("userName", String.valueOf(statuses.get(position).getNestedUserDetails().getPhoneNumber()));
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
