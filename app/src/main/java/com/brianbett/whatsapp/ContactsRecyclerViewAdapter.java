package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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

import com.brianbett.whatsapp.retrofit.Contact;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<Contact> contacts;
    View rootView;
    FrameLayout parentLayout;
    Activity activity;
    public ContactsRecyclerViewAdapter(Context context, ArrayList<Contact> contacts, Activity activity) {
        this.contacts=contacts;
        this.context=context;
        this.activity=activity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        rootView=layoutInflater.inflate(R.layout.single_contact,parent,false);

        parentLayout=activity.findViewById(R.id.contacts_container);
        parentLayout.getForeground().setAlpha(0);

//      starting the new ChatActivity when a contact is clicked


        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(contacts.get(position).getName());
        holder.about.setText(contacts.get(position).getAbout());
//        holder.profile_pic.setImageResource(contacts.get(position).getProfile_image());
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context,MessageActivity.class);
            intent.putExtra("userId",contacts.get(holder.getAdapterPosition()).getUserId());
            intent.putExtra("username",contacts.get(holder.getAdapterPosition()).getName());

            activity.startActivity(intent);
        });

        StorageReference storageReference= FirebaseStorage.getInstance().getReference("images/"+contacts.get(holder.getAdapterPosition()).getProfilePic());


        Task<Uri> uriTask=storageReference.getDownloadUrl();

        uriTask.addOnSuccessListener(uri1 -> {
            Glide.with(context).load(uri1).into(holder.profile_pic);
            holder.profile_pic.setOnClickListener(view -> {
                LayoutInflater layoutInflater=LayoutInflater.from(context);
                @SuppressLint("InflateParams") View popUpView=layoutInflater.inflate(R.layout.profile_popup,null);
                ImageView profilePic=popUpView.findViewById(R.id.profilePicPopUp);
                TextView profile_name=popUpView.findViewById(R.id.popup_user_name);
                profile_name.setText(contacts.get(holder.getAdapterPosition()).getName());
//                profilePic.setImageResource(contacts.get(holder.getAdapterPosition()).getProfile_image());
//
                Glide.with(context).load(uri1).into(profilePic);
                PopupWindow popupWindow=new PopupWindow(popUpView, 800,800,true);
                popupWindow.showAsDropDown(view,200,-view.getHeight()-50, Gravity.TOP);
//              blurring the background when popup is active
                parentLayout.getForeground().setAlpha(160);
                Log.d("userId",contacts.get(holder.getAdapterPosition()).getUserId());

//                removing the modal overlay on popup dismissal
                popupWindow.setOnDismissListener(() -> parentLayout.getForeground().setAlpha(0));
//                subscribing to click events on the popup window elements


                popUpView.findViewById(R.id.pop_up_open_profile).setOnClickListener(view13 -> {
                    Intent intent =new Intent(context,ProfileActivity.class);
//                    User user=new User(contacts.get(holder.getAdapterPosition()).getName(),Integer.parseInt(contacts.get(holder.getAdapterPosition()).getPhoneNumber()));
//                    user.setUserId(contacts.get(holder.getAdapterPosition()).getUserId());
//                    user.setAbout(contacts.get(holder.getAdapterPosition()).getAbout());
//                    user.setProfilePic(contacts.get(holder.getAdapterPosition()).getProfilePic());
                    intent.putExtra("user",contacts.get(holder.getAdapterPosition()).getUserId());
                    intent.putExtra("username",contacts.get(holder.getAdapterPosition()).getName());
                    context.startActivity(intent);
                });
                popUpView.findViewById(R.id.profilePicPopUp).setOnClickListener(view12 -> {
                    Intent intent=new Intent(context,ViewImageActivity.class);
                    intent.putExtra("user",contacts.get(holder.getAdapterPosition()).getName());
                    intent.putExtra("image",uri1.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
                popUpView.findViewById(R.id.popup_message).setOnClickListener(view1 -> {
                    Intent intent=new Intent(context,MessageActivity.class);
                    intent.putExtra("userId",contacts.get(holder.getAdapterPosition()).getUserId());
                    intent.putExtra("username",contacts.get(holder.getAdapterPosition()).getName());

                    activity.startActivity(intent);
                });
                popUpView.findViewById(R.id.popup_call).setOnClickListener(view14 -> {
                    LayoutInflater layoutInflater1 =LayoutInflater.from(context);
                    @SuppressLint("InflateParams") View popUpView1 = layoutInflater1.inflate(R.layout.profile_popup_call,null);
                    PopupWindow popupWindow1 =new PopupWindow(popUpView1, WindowManager.LayoutParams.MATCH_PARENT,350,true);
//                        popupWindow.showAsDropDown(view,0,0,Gravity.START);
                    popupWindow1.showAsDropDown(view14);


                });
                popUpView.findViewById(R.id.popup_video_call).setOnClickListener(view15 -> {
                    LayoutInflater layoutInflater12 =LayoutInflater.from(context);
                    @SuppressLint("InflateParams") View popUpView12 = layoutInflater12.inflate(R.layout.profile_popup_videocall,null);
                    PopupWindow popupWindow12 =new PopupWindow(popUpView12, WindowManager.LayoutParams.MATCH_PARENT,350,true);
                    popupWindow12.showAsDropDown(view15,0,0,Gravity.START);
                });


            });
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,about;
        ImageView profile_pic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.username);
            about=itemView.findViewById(R.id.user_about);
            profile_pic=itemView.findViewById(R.id.profile_pic);

        }
    }
}
