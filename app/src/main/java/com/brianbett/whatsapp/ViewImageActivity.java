package com.brianbett.whatsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);


        Intent intent=getIntent();
        String username=intent.getStringExtra("user");
        assert  getSupportActionBar()!=null;
        getSupportActionBar().setTitle(username);
        Uri uri=Uri.parse(intent.getStringExtra("image"));
        Glide.with(getApplicationContext()).load(intent.getStringExtra("image")).into( ((ImageView)findViewById(R.id.image)));

        ((ImageView)findViewById(R.id.image)).setImageURI(uri);
    }
}