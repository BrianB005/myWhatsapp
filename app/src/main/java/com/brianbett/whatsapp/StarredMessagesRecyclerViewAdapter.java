package com.brianbett.whatsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StarredMessagesRecyclerViewAdapter extends RecyclerView.Adapter<StarredMessagesRecyclerViewAdapter.MyViewHolder> {
    ArrayList<StarredMessage> messages;
    Context context;

    public StarredMessagesRecyclerViewAdapter(ArrayList<StarredMessage> messages, Context myContext) {
        this.messages=messages;
        this.context=myContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

//       since we have made getItemViewType return a layout,we pass viewType to the inflater so that the correct layout is inflated
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(viewType,parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(messages.get(position).getChattingWith());
        holder.message.setText(messages.get(position).getMessage());
        holder.date.setText(messages.get(position).getTime());




    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

//    returning the respective layouts based on a condition so that they can be inflated
    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).isSent()){
            return R.layout.outgoing_starred_message;
        }else{
            return R.layout.incoming_starred_message;
        }
    }
    //    inner class that extends view Holder

    protected class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,date,message;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.starredMessageSender);
            date=itemView.findViewById(R.id.starredMessageTime);
            message=itemView.findViewById(R.id.starredMessage);
        }
    }
}
