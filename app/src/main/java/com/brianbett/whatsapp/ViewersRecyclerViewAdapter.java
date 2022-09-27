package com.brianbett.whatsapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brianbett.whatsapp.retrofit.StatusViewer;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ViewersRecyclerViewAdapter extends RecyclerView.Adapter<ViewersRecyclerViewAdapter.MyViewHolder> {

    private final List<StatusViewer> viewers;
    private final Context context;

    public ViewersRecyclerViewAdapter(Context context, List<StatusViewer> viewers) {
        this.viewers = viewers;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.single_status,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.time.setText(ConvertTimestamp.getStatusMoments(context,viewers.get(position).getViewedAt()));
        holder.username.setText(viewers.get(position).getViewerDetails().getPhoneNumber());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + viewers.get(position).getViewerDetails().getProfilePic());
        Task<Uri> uriTask = storageReference.getDownloadUrl();

        uriTask.addOnSuccessListener(uri1 -> {

            Glide.with(context).load(uri1).into(holder.profilePic);
        }).addOnFailureListener(e -> {
            Log.e("Exception",e.getMessage());
        });
    }

    @Override
    public int getItemCount() {
        return viewers.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username,time;
        ImageView profilePic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.image);
            username=itemView.findViewById(R.id.status_poster_name);
            time=itemView.findViewById(R.id.last_status_time);

        }
    }
}
