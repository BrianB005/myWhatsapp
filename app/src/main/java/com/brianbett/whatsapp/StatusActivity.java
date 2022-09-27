package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.brianbett.whatsapp.retrofit.RetrievedStatus;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.StatusesInterface;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StatusActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {
    View loadingStatus;

    public static void setStatuses(List<RetrievedStatus> statuses) {
        StatusActivity.statuses = statuses;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater layoutInflater=getMenuInflater();
        layoutInflater.inflate(R.menu.single_status_action_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }






    long pressTime = 0L;
    long limit = 500L;

    private StoriesProgressView storiesProgressView;
    private ImageView image;


    private int counter = 0;

    // on below line we are creating a new method for adding touch listener
    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    pressTime = System.currentTimeMillis();


                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:


                    long now = System.currentTimeMillis();


                    storiesProgressView.resume();


                    return limit < now - pressTime;
            }
            return false;
        }
    };

    private  static List<RetrievedStatus> statuses;
    TextView typedStatus;
    TextView statusCaption;
    ActionBar actionBar;

    String contactId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        contactId=intent.getStringExtra("userId");
        String userName=intent.getStringExtra("userName");
        actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(userName);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_status);

//         initializing our  views.
        image = findViewById(R.id.image);
        typedStatus=findViewById(R.id.typedStatus);
        statusCaption=findViewById(R.id.status_caption);
        storiesProgressView = findViewById(R.id.stories);
        loadingStatus=findViewById(R.id.status_loading);


        RetrofitHandler.getAContactStatuses(getApplicationContext(), contactId, new StatusesInterface() {
            @Override
            public void success(List<RetrievedStatus> statuses) {
                StatusActivity.setStatuses(statuses);
                handleStatus(statuses.get(counter), StatusActivity.this,loadingStatus, image, statusCaption, typedStatus, actionBar);
                storiesProgressView.setStoriesCount(statuses.size());

                storiesProgressView.setStoryDuration(3000L);
                storiesProgressView.setStoriesListener(StatusActivity.this);
                storiesProgressView.startStories(counter);

            }

            @Override
            public void failure(Throwable throwable) {
                Log.d("Exception! ",throwable.getMessage());

            }
        });


        View reverse = findViewById(R.id.reverse);


        reverse.setOnClickListener(v -> {

            storiesProgressView.reverse();
        });


        reverse.setOnTouchListener(onTouchListener);


        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(v -> {

            storiesProgressView.skip();
        });

        skip.setOnTouchListener(onTouchListener);


    }



    @Override
    public void onNext() {
        handleStatus(statuses.get(++counter), StatusActivity.this,loadingStatus, image, statusCaption, typedStatus, actionBar);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        handleStatus(statuses.get(--counter), StatusActivity.this,loadingStatus, image, statusCaption, typedStatus, actionBar);

    }

    @Override
    public void onComplete() {
        // when the stories are completed this method is called.
        // in this method we are moving back to initial main activity.
//        Intent i = new Intent(StatusActivity.this, MainActivity.class);
//        i.putExtra("open status","open status");

//        startActivity(i);


        finish();
    }

    @Override
    protected void onDestroy() {
        // in on destroy method we are destroying
        // our stories progress view.
        storiesProgressView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private static void handleStatus(RetrievedStatus status, Context context,View loadingStatus ,ImageView image, TextView statusCaption, TextView typedStatus, ActionBar actionBar){
        if(status.isTyped()){
            image.setVisibility(View.GONE);
            statusCaption.setVisibility(View.GONE);
            typedStatus.setVisibility(View.VISIBLE);
            typedStatus.setText(status.getTitle());
            Typeface typeface = ResourcesCompat.getFont(context,Integer.parseInt(status.getFont()));
            typedStatus.setTypeface(typeface);
            typedStatus.setBackgroundColor(Integer.parseInt(status.getBackgroundColor()));
        }else{
            image.setVisibility(View.VISIBLE);
            statusCaption.setVisibility(View.VISIBLE);
            typedStatus.setVisibility(View.GONE);
            loadingStatus.setVisibility(View.GONE);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + status.getStatusImage());


            Task<Uri> uriTask = storageReference.getDownloadUrl();

            uriTask.addOnSuccessListener(uri1 -> {

                Glide.with(context).load(uri1).into(image);
                loadingStatus.setVisibility(View.GONE);
            }).addOnFailureListener(e -> {

                loadingStatus.setVisibility(View.GONE);
                Toast.makeText(context, "Something went wrong loading data!Try again later.", Toast.LENGTH_SHORT).show();
                Log.e("Exception",e.getMessage());
            });
        }
        RetrofitHandler.viewStatus(context.getApplicationContext(), status.getStatusId());
        actionBar.setSubtitle(ConvertTimestamp.getStatusMoments(context,status.getTimeCreated()));

    }



}









