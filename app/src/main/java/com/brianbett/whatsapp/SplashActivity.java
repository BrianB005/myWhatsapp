package com.brianbett.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    SharedPreferences preferences;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences=this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        token=preferences.getString("token",null);
        if(token!=null) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        else{
            startActivity(new Intent(SplashActivity.this, RegisterActivityOne.class));
        }
        finish();
    }
}