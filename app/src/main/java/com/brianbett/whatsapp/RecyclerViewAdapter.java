package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brianbett.whatsapp.retrofit.Message;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
   private final ArrayList<Message> messagesArrayList;
    private  Context myContext;
    LayoutInflater layoutInflater;

    View rootView;
    public RecyclerViewAdapter(Context context , ArrayList<Message>messages,View view) {
        this.messagesArrayList=messages;
//        this.myContext=context;
        this.rootView=view;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         layoutInflater= LayoutInflater.from(parent.getContext());
         myContext= parent.getContext();







        View view= layoutInflater.inflate(R.layout.message,parent,false);
//        showing the popup to delete m,archive,pin/unpin,mute chat.etc.
        view.setOnLongClickListener(view1 -> {
            LayoutInflater layoutInflater1 = LayoutInflater.from(myContext);
            @SuppressLint("InflateParams") View popUpView = layoutInflater1.inflate(R.layout.message_popup, null);
            PopupWindow popupWindow = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, 200,true);
            popupWindow.setOutsideTouchable(true);

            popupWindow.showAtLocation(view1,Gravity.TOP,0,0);

            popUpView.findViewById(R.id.dismiss_popup).setOnClickListener(view11 -> popupWindow.dismiss());
            return true;
        });
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



//       updating the views with their corresponding contents

//        String time= String.format(messagesArrayList.get(position).getTime(), DateTimeFormatter.ofPattern("hh:mm"));



        holder.name.setText(messagesArrayList.get(position).getPhoneNumber());
        holder.date.setText(ConvertTimestamp.getStatusMoments(myContext,messagesArrayList.get(position).getTime()));
        holder.message.setText(messagesArrayList.get(position).getMessage());

        StorageReference storageReference= FirebaseStorage.getInstance().getReference("images/"+messagesArrayList.get(position).getProfilePic());


        Task<Uri> uriTask=storageReference.getDownloadUrl();

        uriTask.addOnSuccessListener(uri1 -> {
            Glide.with(myContext).load(uri1).into(holder.imageView);
            holder.imageView.setOnClickListener(view -> {
                LayoutInflater layoutInflater=LayoutInflater.from(myContext);
                @SuppressLint("InflateParams") View popUpView=layoutInflater.inflate(R.layout.profile_popup,null);
                ImageView profilePic=popUpView.findViewById(R.id.profilePicPopUp);
                TextView profile_name=popUpView.findViewById(R.id.popup_user_name);
                profile_name.setText(messagesArrayList.get(holder.getAdapterPosition()).getPhoneNumber());
                Glide.with(myContext).load(uri1).into(profilePic);
//
                PopupWindow popupWindow=new PopupWindow(popUpView, 800,800,true);
                popupWindow.showAsDropDown(view,200,-view.getHeight()-50,Gravity.TOP);
//              blurring the background when popup is active
                FrameLayout frameLayout=rootView.findViewById(R.id.messages_container);
                frameLayout.getForeground().setAlpha(160);

//                removing the modal overlay on popup dismissal
                popupWindow.setOnDismissListener(() -> frameLayout.getForeground().setAlpha(0));
//                subscribing to click events on the popup window elements
                popUpView.findViewById(R.id.pop_up_open_profile).setOnClickListener(view1 -> {
                    Intent intent=new Intent(myContext,ProfileActivity.class);
                    intent.putExtra("user",messagesArrayList.get(holder.getAdapterPosition()).getUserId());
                    intent.putExtra("username",messagesArrayList.get(holder.getAdapterPosition()).getPhoneNumber());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myContext.startActivity(intent);
                });
                popUpView.findViewById(R.id.profilePicPopUp).setOnClickListener(view12 -> {
                    Intent intent=new Intent(myContext,ViewImageActivity.class);
                    intent.putExtra("user",messagesArrayList.get(holder.getAdapterPosition()).getPhoneNumber());
                    intent.putExtra("image",uri1.toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myContext.startActivity(intent);
                });
                popUpView.findViewById(R.id.popup_message).setOnClickListener(view14 -> {
                    Intent intent=new Intent(myContext,MessageActivity.class);
                    intent.putExtra("username",messagesArrayList.get(holder.getAdapterPosition()).getPhoneNumber());
                    intent.putExtra("userId",messagesArrayList.get(holder.getAdapterPosition()).getUserId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myContext.startActivity(intent);
                });
                popUpView.findViewById(R.id.popup_call).setOnClickListener(view13 -> {
                    LayoutInflater layoutInflater1 =LayoutInflater.from(myContext);
                    @SuppressLint("InflateParams") View popUpView1 = layoutInflater1.inflate(R.layout.profile_popup_call,null);
                    PopupWindow popupWindow1 =new PopupWindow(popUpView1, WindowManager.LayoutParams.MATCH_PARENT,350,true);
//                        popupWindow.showAsDropDown(view,0,0,Gravity.START);
                    popupWindow1.showAsDropDown(view13);
                });
                popUpView.findViewById(R.id.popup_video_call).setOnClickListener(view15 -> {
                    LayoutInflater layoutInflater12 =LayoutInflater.from(myContext);
                    @SuppressLint("InflateParams") View popUpView12 = layoutInflater12.inflate(R.layout.profile_popup_videocall,null);
                    PopupWindow popupWindow12 =new PopupWindow(popUpView12, WindowManager.LayoutParams.MATCH_PARENT,350,true);
                    popupWindow12.showAsDropDown(view15,0,0,Gravity.START);
                });

            });
            //        listening to onclick events and opening new activities
            holder.itemView.setOnClickListener(view -> {
                Intent intent=new Intent(myContext,MessageActivity.class);
                intent.putExtra("username",messagesArrayList.get(holder.getAdapterPosition()).getPhoneNumber());
                intent.putExtra("userId",messagesArrayList.get(holder.getAdapterPosition()).getUserId());
                intent.putExtra("profilePic",uri1.toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myContext.startActivity(intent);
            });

        });

        holder.imageView.setClipToOutline(true);

//        listening to onclick events and opening new activities
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(myContext,MessageActivity.class);
            intent.putExtra("username",messagesArrayList.get(holder.getAdapterPosition()).getPhoneNumber());
            intent.putExtra("userId",messagesArrayList.get(holder.getAdapterPosition()).getUserId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    //    inner class that extends viewHolder
    protected static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name,date,message;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);


        imageView=itemView.findViewById(R.id.profilePic);
        name=itemView.findViewById(R.id.name);
        date=itemView.findViewById(R.id.date);
        message=itemView.findViewById(R.id.message);
    }

}
}
