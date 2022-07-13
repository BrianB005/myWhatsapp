package com.brianbett.whatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class HandleSelectedImage extends AppCompatActivity {

    ImageView selectedImageView;
    AppCompatEditText captionInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_selected_image);


        selectedImageView=findViewById(R.id.image);
        captionInput=findViewById(R.id.status_caption_input);


        Intent intent=getIntent();
        Uri selectedPhoto=Uri.parse(intent.getStringExtra("photo"));

        selectedImageView.setImageURI(selectedPhoto);


    }
}