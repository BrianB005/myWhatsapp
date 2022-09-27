package com.brianbett.whatsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.brianbett.whatsapp.retrofit.MyPreferences;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.User;
import com.brianbett.whatsapp.retrofit.UserInterface;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

public class SettingsActivity extends AppCompatActivity {

    String myUserId;
    Pusher pusher;
    ImageView profilePic;
    TextView about,username;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view =findViewById(R.id.first_bar);

        about=findViewById(R.id.my_profile_about);
        username=findViewById(R.id.my_profile_username);


       profilePic=findViewById(R.id.my_profile_pic);

        RetrofitHandler.getCurrentUser(getApplicationContext(), new UserInterface() {
            @Override
            public void success(User user) {
                Log.d("User",user.getProfilePic());
                about.setText(user.getAbout());
                StorageReference storageReference= FirebaseStorage.getInstance().getReference("images/"+user.getProfilePic());
                Task<Uri> uriTask=storageReference.getDownloadUrl();
                uriTask.addOnSuccessListener(uri1 -> {
                    Glide.with(getApplicationContext()).load(uri1).into(profilePic);
                });
                username.setText(user.getName());
                view.setOnClickListener(view1 -> {
                    Intent intent=new Intent(SettingsActivity.this,MyProfileActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                });

            }
            @Override
            public void failure(Throwable throwable) {

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
        //        connecting to pusher
        myUserId = MyPreferences.getSavedItem(getApplicationContext(), "userId");

        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");
        options.setUseTLS(true);


        pusher = new Pusher("36b67a35099f920fa4e2", options);
        updateUser(pusher, myUserId);

    }

    @Override
    protected void onPause() {
        super.onPause();
        myUserId = MyPreferences.getSavedItem(getApplicationContext(), "userId");

        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");
        options.setUseTLS(true);

        pusher = new Pusher("36b67a35099f920fa4e2", options);
        updateUser(pusher, myUserId);


    }

    private void updateUser(Pusher pusher, String myUserId) {
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe(myUserId);

        channel.bind("updated", event -> {
            RetrofitHandler.getCurrentUser(getApplicationContext(), new UserInterface() {
                @Override
                public void success(User user) {
                    Log.d("User",user.getProfilePic());
                    about.setText(user.getAbout());
                    StorageReference storageReference= FirebaseStorage.getInstance().getReference("images/"+user.getProfilePic());
                    Task<Uri> uriTask=storageReference.getDownloadUrl();
                    uriTask.addOnSuccessListener(uri1 -> {
                        Glide.with(getApplicationContext()).load(uri1).into(profilePic);
                    });
                    username.setText(user.getName());
                    view.setOnClickListener(view1 -> {
                        Intent intent=new Intent(SettingsActivity.this,MyProfileActivity.class);
                        intent.putExtra("user",user);
                        startActivity(intent);
                    });

                }
                @Override
                public void failure(Throwable throwable) {

                }
            });
        });


    }
}