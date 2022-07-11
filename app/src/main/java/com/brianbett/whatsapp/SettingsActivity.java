package com.brianbett.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.User;
import com.brianbett.whatsapp.retrofit.UserInterface;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View view =findViewById(R.id.first_bar);
        TextView about,username;
        about=findViewById(R.id.my_profile_about);
        username=findViewById(R.id.my_profile_username);


        ImageView profilePic=findViewById(R.id.my_profile_pic);

        RetrofitHandler.getCurrentUser(getApplicationContext(), new UserInterface() {
            @Override
            public void success(User user) {
                about.setText(user.getAbout());

                username.setText(user.getName());
                profilePic.setImageBitmap(user.getProfileBitmap());
            }
            @Override
            public void failure(Throwable throwable) {

            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,MyProfileActivity.class));
            }
        });

//        starting the privacy security activity
        View accountView =findViewById(R.id.settings_account);
        accountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,PrivacySecurityActivity.class));
            }
        });
    }
}