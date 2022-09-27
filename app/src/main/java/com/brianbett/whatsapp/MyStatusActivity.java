package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.brianbett.whatsapp.retrofit.MyRetrievedStatus;
import com.brianbett.whatsapp.retrofit.MyStatusesInterface;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.StatusViewer;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class MyStatusActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    public static void setStatuses(List<MyRetrievedStatus> statuses) {
        MyStatusActivity.statuses = statuses;
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

    private  static List<MyRetrievedStatus> statuses;
    TextView typedStatus;
    TextView statusCaption;
    ActionBar actionBar;

    MyViewModel myViewModel;
    View loadingStatus;
    TextView showViewers;
    PopupWindow popupWindow;
    TextView hideViewers;
    RecyclerView recyclerView;
    List<StatusViewer> viewersList;
    ViewersRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("My Status");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_status);

//         initializing our  views.
        image = findViewById(R.id.image);
        typedStatus=findViewById(R.id.typedStatus);
        statusCaption=findViewById(R.id.status_caption);
        loadingStatus=findViewById(R.id.status_loading);
        showViewers=findViewById(R.id.viewers);

        viewersList=new ArrayList<>();
        recyclerViewAdapter=new ViewersRecyclerViewAdapter(getApplicationContext(),viewersList);




//
        storiesProgressView = findViewById(R.id.stories);
        View popupView=getLayoutInflater().inflate(R.layout.status_viewers,null);
        popupWindow=new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);

        recyclerView=popupView.findViewById(R.id.viewers_recycler_view);
        hideViewers=popupView.findViewById(R.id.hide_viewers);
        recyclerView.setAdapter(recyclerViewAdapter);






        RetrofitHandler.getMyStatuses(getApplicationContext(), new MyStatusesInterface() {
            @Override
            public void success(List<MyRetrievedStatus> statuses) {
                myViewModel.getMyStatuses().setValue(statuses);

            }
            @Override
            public void failure(Throwable throwable) {

            }
            @Override
            public void errorExists(){

            }
        });

        myViewModel=new ViewModelProvider(this).get(MyViewModel.class);



        final Observer<List<MyRetrievedStatus>> statusesObserver = myStatuses -> {
            assert myStatuses != null;
            MyStatusActivity.setStatuses(myStatuses);
            handleStatus(statuses.get(counter), MyStatusActivity.this, image,showViewers, statusCaption, typedStatus, actionBar,loadingStatus,popupWindow);
            storiesProgressView.setStoriesCount(statuses.size());

            storiesProgressView.setStoryDuration(3000L);
            storiesProgressView.setStoriesListener(this);
            storiesProgressView.startStories(counter);

        };

        myViewModel.getMyStatuses().observe(this,statusesObserver);



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
        handleStatus(statuses.get(++counter), MyStatusActivity.this, image,showViewers, statusCaption, typedStatus, actionBar, loadingStatus,popupWindow);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        handleStatus(statuses.get(--counter), MyStatusActivity.this, image,showViewers ,statusCaption, typedStatus, actionBar, loadingStatus,popupWindow);
        
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

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    private  void handleStatus(MyRetrievedStatus status, Context context, ImageView image, TextView statusViewers, TextView statusCaption, TextView typedStatus, ActionBar actionBar, View loadingStatus, PopupWindow popupWindow) {
        if (status.isTyped()) {
            image.setVisibility(View.GONE);
            statusCaption.setVisibility(View.GONE);
            typedStatus.setVisibility(View.VISIBLE);
            typedStatus.setText(status.getTitle());
            Typeface typeface = ResourcesCompat.getFont(context, Integer.parseInt(status.getFont()));
            typedStatus.setTypeface(typeface);
            typedStatus.setBackgroundColor(Integer.parseInt(status.getBackgroundColor()));
        } else {

            loadingStatus.setVisibility(View.VISIBLE);

            image.setVisibility(View.VISIBLE);
            statusCaption.setVisibility(View.VISIBLE);
            typedStatus.setVisibility(View.GONE);
            if(!status.getCaption().isEmpty()){
                statusCaption.setVisibility(View.VISIBLE);
                statusCaption.setText(status.getCaption());
            }else{
                statusCaption.setVisibility(View.GONE);
            }
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + status.getStatusImage());


            Task<Uri> uriTask = storageReference.getDownloadUrl();

            uriTask.addOnSuccessListener(uri1 -> {
                loadingStatus.setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load(uri1).into(image);
            }).addOnFailureListener(e -> {
                loadingStatus.setVisibility(View.GONE);
                Toast.makeText(context, "Something went wrong loading data!Try again later.", Toast.LENGTH_SHORT).show();
                Log.e("Exception",e.getMessage());
            });


        }

        hideViewers.setOnClickListener((view)->popupWindow.dismiss());
        statusViewers.setText("Viewers ("+ status.getStatusViewerList().size() +")");
        statusViewers.setOnClickListener(view -> {
            if(!status.getStatusViewerList().isEmpty()) {

                this.popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
                this.hideViewers.setText("Viewers ("+ status.getStatusViewerList().size() +")");
                this.viewersList.addAll(status.getStatusViewerList());
                this.recyclerViewAdapter.notifyDataSetChanged();
                this.storiesProgressView.pause();
                popupWindow.setOnDismissListener(() -> {
                    storiesProgressView.resume();
                    viewersList=new ArrayList<>();
                });


            }

        });
        actionBar.setSubtitle(ConvertTimestamp.getStatusMoments(context,status.getTimeCreated()));

    }
}









