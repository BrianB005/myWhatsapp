package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.brianbett.whatsapp.retrofit.ChatMessage;

import java.util.ArrayList;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<ChatMessage> messages;
    ActionBar actionBar;
    Activity parentActivity;
    String currentUserId;


    //    constructor

    public MessagesRecyclerViewAdapter(Context myContext, ArrayList<ChatMessage> messageArrayList, Activity activity,String currentUserID) {
        context=myContext;
        messages=messageArrayList;
        this.parentActivity=activity;
        this.currentUserId=currentUserID;
    }


//    method for checking if message's status is sent or received .
//    this helps in creating the corresponding viewHolders and also binding
//    we return the corresponding layouts resource files then inflate them

    @Override
    public int getItemViewType(int position) {

        if(messages.get(position).isSent(currentUserId)){
            return R.layout.outgoing_message;
        }else {
            return R.layout.incoming_message;
        }
    }





    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);

        actionBar=((AppCompatActivity)parentActivity).getSupportActionBar();

        View view=null;

//        the viewType contains the corresponding layout
        view=inflater.inflate(viewType,parent,false);
//        showing popup when a message is long pressed
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                actionBar.setDisplayHomeAsUpEnabled(true);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View popUpView = layoutInflater.inflate(R.layout.message_popup, null);
                PopupWindow popupWindow = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, 200,true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAtLocation(view,Gravity.TOP,0,0);
                popUpView.findViewById(R.id.dismiss_popup).setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                return true;
            }
        });
        return  new MyViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.message.setText(messages.get(position).getMessage());

        holder.time.setText(ConvertTimestamp.getTime(messages.get(position).getTime()));


    }

    @Override
    public int getItemCount() {

        return messages.isEmpty()?0:messages.size();
    }

//    creating separate holders for outgoing and incoming texts
    protected static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView message,time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            message=itemView.findViewById(R.id.text_content);
            time=itemView.findViewById(R.id.text_time);

        }
    }

}
