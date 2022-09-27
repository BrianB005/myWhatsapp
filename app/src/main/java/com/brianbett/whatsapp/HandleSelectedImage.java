package com.brianbett.whatsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.brianbett.whatsapp.retrofit.Contact;
import com.brianbett.whatsapp.retrofit.ImageStatus;
import com.brianbett.whatsapp.retrofit.MyPreferences;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.UploadStatusSuccess;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class HandleSelectedImage extends AppCompatActivity {
    ImageView selectedImageView;
    AppCompatEditText captionInput;
    FloatingActionButton sendStatus;

    ArrayList<Contact> contactsList;
    ArrayList<String> allContacts;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_selected_image);



        selectedImageView=findViewById(R.id.image);
        captionInput=findViewById(R.id.status_caption_input);
        sendStatus=findViewById(R.id.send_status);
        progressBar=findViewById(R.id.progress_bar);
        Intent intent=getIntent();
        Uri selectedPhoto=Uri.parse(intent.getStringExtra("photo"));
        File file=new File(selectedPhoto.getPath());





        selectedImageView.setImageURI(selectedPhoto);



//        retrieving contacts
        String contacts = MyPreferences.getSavedItem(getApplicationContext(), "myContacts");
        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        Gson gson = new Gson();
        contactsList = gson.fromJson(contacts, type);


        allContacts = new ArrayList<>();
        if (contactsList == null) {
            return;
        } else {
            for (Contact contact : contactsList) {
                assert contact != null;
                allContacts.add(contact.getUserId());
            }
        }

        sendStatus.setOnClickListener(view->{
            sendStatus.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            String caption= Objects.requireNonNull(captionInput.getText()).toString();

           String filename=System
                   .currentTimeMillis()+"_"+file.getName();

            StorageReference imagesRef=FirebaseStorage.getInstance().getReference("images/"+filename);
            UploadTask uploadTask=imagesRef.putFile(selectedPhoto);

             uploadTask.continueWithTask(task -> {
                 if (!task.isSuccessful()) {
                     throw Objects.requireNonNull(task.getException());
                 }

                 // Continue with the task to get the download URL
                 return imagesRef.getDownloadUrl();
             }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ImageStatus imageStatus=new ImageStatus(allContacts,caption,filename);
                    RetrofitHandler.createImageStatus(getApplicationContext(), imageStatus, new UploadStatusSuccess() {
                        @Override
                        public void success() {
                            Toast.makeText(getApplicationContext(),"Upload was successful",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            finish();

                        }

                        @Override
                        public void failure(Throwable t) {
                            Toast.makeText(getApplicationContext(),"Oops!Something went wrong.Try again",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            sendStatus.setEnabled(true);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(),"Something went wrong.Try again",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

        });


    }
}