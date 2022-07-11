package com.brianbett.whatsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.User;
import com.brianbett.whatsapp.retrofit.UserInterface;

import java.util.HashMap;
import java.util.List;

public class ChangeAboutRecyclerViewAdapter extends RecyclerView.Adapter<ChangeAboutRecyclerViewAdapter.MyViewHolder>{
    private final String [] abouts;
    private final Context context;
    private final TextView currentAbout;
    public ChangeAboutRecyclerViewAdapter(Context context, String [] abouts,TextView currentAbout) {
        this.context=context;
        this.abouts=abouts;
        this.currentAbout=currentAbout;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.single_about,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.single_about_text.setText(abouts[position]);

//        updating  about when an about is clicked
        holder.itemView.setOnClickListener(view -> {
            holder.progressUpdate.setVisibility(View.VISIBLE);
            HashMap<String,String>updatedAbout=new HashMap<>();
            updatedAbout.put("about",holder.single_about_text.getText().toString());
            RetrofitHandler.updateUser(updatedAbout, context, new UserInterface() {
                @Override
                public void success(User user) {
                    holder.progressUpdate.setVisibility(View.GONE);
                    currentAbout.setText(user.getAbout());
                    Toast.makeText(context, "About updated successfully", Toast.LENGTH_SHORT).show();

                }
                @Override
                public void failure(Throwable throwable) {
                    holder.progressUpdate.setVisibility(View.GONE);
                    Toast.makeText(context, throwable.getMessage(),Toast.LENGTH_SHORT).show();


                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return abouts.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView single_about_text;
        View progressUpdate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            single_about_text=itemView.findViewById(R.id.about_text);
            progressUpdate=itemView.findViewById(R.id.progress_update);
        }
    }
}
