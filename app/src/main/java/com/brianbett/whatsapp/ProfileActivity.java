package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

        MaterialToolbar toolbar=findViewById(R.id.tool_bar);
        CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsing_tool_bar);

        AppBarLayout appBarLayout=findViewById(R.id.appBarLayout);





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

//                imageView.setImageBitmap(user.getProfileBitmap());
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

                ImageView profilePic=findViewById(R.id.profile_pic);
                StorageReference storageReference= FirebaseStorage.getInstance().getReference("images/"+user.getProfilePic());


                Task<Uri> uriTask=storageReference.getDownloadUrl();

                uriTask.addOnSuccessListener(uri1 -> {
                    Glide.with(getApplicationContext()).load(uri1).into(profilePic);
                    Intent intent1=new Intent(ProfileActivity.this,ViewImageActivity.class);
                    intent1.putExtra("image",uri1.toString());
                    intent1.putExtra("user",user.getPhoneNumber());
                    profilePic.setOnClickListener(view -> startActivity(intent1));

                    //                adding title to toolbar when collapsed
                    appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
                        if(verticalOffset==-collapsingToolbarLayout.getHeight()+toolbar.getHeight()){
                            toolbar.setTitle(username);
                        }else{
                            toolbar.setTitle("");
                        }
                    });
                });


                profilePic.setOnClickListener(view -> startActivity(new Intent(ProfileActivity.this,ViewImageActivity.class)));
            }

            @Override
            public void failure(Throwable throwable) {

            }
        });


    }
}