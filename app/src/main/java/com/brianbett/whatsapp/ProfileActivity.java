package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.User;
import com.brianbett.whatsapp.retrofit.UserInterface;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        View messageUser,callUser,videoCallUser;
        messageUser=findViewById(R.id.message_user);
        callUser=findViewById(R.id.call_user);
        videoCallUser=findViewById(R.id.video_call_user);
        TextView usernameView,phoneNumberView,blockView,reportView;
        ImageView imageView=findViewById(R.id.profile_pic);
        usernameView=findViewById(R.id.username);
        phoneNumberView=findViewById(R.id.phoneNumber);
        blockView=findViewById(R.id.block);
        reportView=findViewById(R.id.report);


        Intent intent=getIntent();
        String userId=intent.getStringExtra("user");
        String username=intent.getStringExtra("username");

        RetrofitHandler.getUser(getApplicationContext(), userId, new UserInterface() {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(User user) {

                if(username.equals(String.valueOf(user.getPhoneNumber()))){
                    usernameView.setText(user.getName());
                }
                else{
                    usernameView.setText(username);
                }
                phoneNumberView.setText("+254 "+user.getPhoneNumber());
                blockView.setText("Block "+username);
                reportView.setText("Report "+username);

                imageView.setImageBitmap(user.getProfileBitmap());
                messageUser.setOnClickListener(view -> {
                    Intent intent1=new Intent(ProfileActivity.this,MessageActivity.class);
                    intent1.putExtra("username",username);
                    intent1.putExtra("userId",userId);
                    startActivity(intent1);


                });
                callUser.setOnClickListener(view -> {
                    LayoutInflater layoutInflater=getLayoutInflater();
                    @SuppressLint("InflateParams") View popUpView=layoutInflater.inflate(R.layout.profile_popup_call,null);
                    PopupWindow popupWindow=new PopupWindow(popUpView, WindowManager.LayoutParams.MATCH_PARENT ,400,true);
                    popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL,0,0);
//                popupWindow.showAsDropDown(view);
                });
                videoCallUser.setOnClickListener(view -> {
                    LayoutInflater layoutInflater=getLayoutInflater();
                    @SuppressLint("InflateParams") View popUpView=layoutInflater.inflate(R.layout.profile_popup_videocall,null);
                    PopupWindow popupWindow=new PopupWindow(popUpView, WindowManager.LayoutParams.MATCH_PARENT,400,true);
                    popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL,0,0);
                });

                View openImage=findViewById(R.id.profile_pic);

                openImage.setOnClickListener(view -> startActivity(new Intent(ProfileActivity.this,ViewImageActivity.class)));
            }

            @Override
            public void failure(Throwable throwable) {

            }
        });


    }
}