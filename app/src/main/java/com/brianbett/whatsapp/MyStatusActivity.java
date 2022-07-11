package com.brianbett.whatsapp;

import android.annotation.SuppressLint;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.brianbett.whatsapp.retrofit.RetrievedStatus;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.StatusesInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class MyStatusActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        statuses=new ArrayList<>();



        actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("My Status");
        actionBar.setSubtitle("Today 11:09");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_status);

//         initializing our  views.
        image = findViewById(R.id.image);
        typedStatus=findViewById(R.id.typedStatus);
        statusCaption=findViewById(R.id.status_caption);
//


        storiesProgressView = findViewById(R.id.stories);



        // on below line we are setting story duration for each story.
        storiesProgressView.setStoryDuration(3000L);

        // on below line we are calling a method for set
        // on story listener and passing context to it.
        storiesProgressView.setStoriesListener(this);


        RetrofitHandler.getMyStatuses(getApplicationContext(), new StatusesInterface() {
            @Override
            public void success(List<RetrievedStatus> statuses) {
                MyStatusActivity.statuses.addAll(statuses);
                handleStatus(MyStatusActivity.statuses.get(counter), MyStatusActivity.this, image, statusCaption, typedStatus, actionBar);
                storiesProgressView.setStoriesCount(MyStatusActivity.statuses.size());
                storiesProgressView.startStories(counter);

            }

            @Override
            public void failure(Throwable throwable) {

            }
        });




        storiesProgressView.setStoriesListener(this);




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
        handleStatus(MyStatusActivity.statuses.get(++counter), MyStatusActivity.this, image, statusCaption, typedStatus, actionBar);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        handleStatus(MyStatusActivity.statuses.get(--counter), MyStatusActivity.this, image, statusCaption, typedStatus, actionBar);
        
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

    private static void handleStatus(RetrievedStatus status, Context context, ImageView image, TextView statusCaption, TextView typedStatus, ActionBar actionBar){
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

        }
        actionBar.setSubtitle("10 minutes ago");

    }
}





















//package com.brianbett.whatsapp;
//
//import android.annotation.SuppressLint;
//
//import android.content.Context;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.res.ResourcesCompat;
//
//import com.brianbett.whatsapp.retrofit.RetrievedStatus;
//import com.brianbett.whatsapp.retrofit.RetrofitHandler;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import jp.shts.android.storiesprogressview.StoriesProgressView;
//
//public class MyStatusActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater layoutInflater=getMenuInflater();
//        layoutInflater.inflate(R.menu.single_status_action_bar,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//
//    // on below line we are creating a int array
//    // in which we are storing all our image ids.
//    private final int[] resources = new int[]{
//            R.drawable.image4,
//            R.drawable.image5,
//            R.drawable.image6,
//            R.drawable.image7,
//            R.drawable.image2,
//            R.drawable.image3,
//
//    };
//    private  static List<RetrievedStatus> statuses;
//    TextView typedStatus;
//    TextView statusCaption;
//    ActionBar actionBar;
//
//
//
//    // on below line we are creating variable for
//    // our press time and time limit to display a story.
//    long pressTime = 0L;
//    long limit = 500L;
//
//    // on below line we are creating variables for
//    // our progress bar view and image view .
//    private StoriesProgressView storiesProgressView;
//    private ImageView image;
//
//    // on below line we are creating a counter
//    // for keeping count of our stories.
//    private int counter = 0;
//
//    // on below line we are creating a new method for adding touch listener
//    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
//        @SuppressLint("ClickableViewAccessibility")
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            // inside on touch method we are
//            // getting action on below line.
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//
//                    // on action down when we press our screen
//                    // the story will pause for specific time.
//                    pressTime = System.currentTimeMillis();
//
//                    // on below line we are pausing our indicator.
//                    storiesProgressView.pause();
//                    return false;
//                case MotionEvent.ACTION_UP:
//
//                    // in action up case when user do not touches
//                    // screen this method will skip to next image.
//                    long now = System.currentTimeMillis();
//
//                    // on below line we are resuming our progress bar for status.
//                    storiesProgressView.resume();
//
//                    // on below line we are returning if the limit < now - presstime
//                    return limit < now - pressTime;
//            }
//            return false;
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        dealing with the action bar
//
//        actionBar=getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setTitle("Ivy Cherop");
//        actionBar.setSubtitle("Today 11:09");
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//
//        // inside in create method below line is use to make a full screen.
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_my_status);
//
//        // on below line we are initializing our variables.
//        storiesProgressView = findViewById(R.id.stories);
//
//
//
//        // on below line we are setting story duration for each story.
//        storiesProgressView.setStoryDuration(3000L);
//
//        // on below line we are calling a method for set
//        // on story listener and passing context to it.
//
//
//        // initializing our image view.
//        image = findViewById(R.id.image);
////        typedStatus=findViewById(R.id.typedStatus);
//        statusCaption=findViewById(R.id.status_caption);
//
//
//
//        storiesProgressView.setStoriesListener(MyStatusActivity.this);
//        storiesProgressView.setStoriesCount(resources.length);
//        storiesProgressView.startStories(counter);
//        // on below line we are setting image to our image view.
//        image.setImageResource(resources[counter]);
//
////        ExecutorService executor= Executors.newSingleThreadExecutor();
////
////        Runnable runnable= () -> {
////
////            handleStatus(MyStatusActivity.statuses.get(0), MyStatusActivity.this, image, statusCaption, typedStatus, actionBar);
////
////            storiesProgressView.setStoriesCount(MyStatusActivity.statuses.size());
////            storiesProgressView.startStories(counter);
////
////
////        };
////
////
////        executor.submit(()->{
////            MyStatusActivity.statuses= RetrofitHandler.getMyStatuses(MyStatusActivity.this);
////            runOnUiThread(runnable);
////            runnable.run();
////        });
////
//
//
//        // below is the view for going to the previous story.
//        // initializing our previous view.
//        View reverse = findViewById(R.id.reverse);
//
//        // adding on click listener for our reverse view.
//        reverse.setOnClickListener(v -> {
//            // inside on click we are
//            // reversing our progress view.
//            storiesProgressView.reverse();
//        });
//
//
//
//        // on below line we are calling a set on touch
//        // listener method to move towards previous image.
//        reverse.setOnTouchListener(onTouchListener);
//
//        // on below line we are initializing
//        // view to skip a specific story.
//        View skip = findViewById(R.id.skip);
//        skip.setOnClickListener(v -> {
//            // inside on click we are
//            // skipping the story progress view.
//            storiesProgressView.skip();
//        });
//        // on below line we are calling a set on touch
//        // listener method to move to next story.
//        skip.setOnTouchListener(onTouchListener);
//    }
//
//    @Override
//    public void onNext() {
//        // this method is called when we move
//        // to next progress view of story.
//        image.setImageResource(resources[++counter]);
////        handleStatus(MyStatusActivity.statuses.get(++counter),MyStatusActivity.this,image,statusCaption,typedStatus,actionBar);
//    }
//
//    @Override
//    public void onPrev() {
//
//        // this method id called when we move to previous story.
//        // on below line we are decreasing our counter
//        if ((counter - 1) < 0) return;
//
//        // on below line we are setting image to image view
//        image.setImageResource(resources[--counter]);
////        handleStatus(MyStatusActivity.statuses.get(--counter),MyStatusActivity.this,image,statusCaption,typedStatus,actionBar);
//    }
//
//    @Override
//    public void onComplete() {
//        // when the stories are completed this method is called.
//        // in this method we are moving back to initial main activity.
////        Intent i = new Intent(StatusActivity.this, MainActivity.class);
////        i.putExtra("open status","open status");
//
////        startActivity(i);
//
//
//        finish();
//    }
//
//    @Override
//    protected void onDestroy() {
//        // in on destroy method we are destroying
//        // our stories progress view.
//        storiesProgressView.destroy();
//        super.onDestroy();
//    }
//    private static void handleStatus(RetrievedStatus status, Context context, ImageView image, TextView statusCaption, TextView typedStatus, ActionBar actionBar){
//        if(status.isTyped()){
//            image.setVisibility(View.GONE);
//            statusCaption.setVisibility(View.GONE);
//            typedStatus.setVisibility(View.VISIBLE);
//            typedStatus.setText(status.getTitle());
//            Typeface typeface = ResourcesCompat.getFont(context,Integer.parseInt(status.getFont()));
//            typedStatus.setTypeface(typeface);
//            typedStatus.setBackgroundColor(Integer.parseInt(status.getBackgroundColor()));
//        }else{
//            image.setVisibility(View.VISIBLE);
//            statusCaption.setVisibility(View.VISIBLE);
//            typedStatus.setVisibility(View.GONE);
//
//        }
//        actionBar.setSubtitle("10 minutes ago");
//
//    }
//}
//




//package com.brianbett.whatsapp;
//
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.res.ResourcesCompat;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.database.DataSetObserver;
//import android.graphics.Typeface;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.Adapter;
//import android.widget.ImageView;
//import android.widget.ListAdapter;
//import android.widget.TextView;
//
//import com.brianbett.whatsapp.retrofit.RetrievedStatus;
//import com.brianbett.whatsapp.retrofit.RetrofitHandler;
//import com.brianbett.whatsapp.retrofit.StatusesInterface;
//
//import org.w3c.dom.Text;
//
//import java.util.List;
//import java.util.concurrent.Executor;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//import jp.shts.android.storiesprogressview.StoriesProgressView;
//
//public class MyStatusActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater layoutInflater=getMenuInflater();
//        layoutInflater.inflate(R.menu.single_status_action_bar,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    TextView typedStatus;
//    TextView statusCaption;
//    ActionBar actionBar;
//
//    static List<RetrievedStatus> statuses;
//
//
//    // on below line we are creating a int array
//    // in which we are storing all our image ids.
//    private final int[] resources = new int[]{
//            R.drawable.image4,
//            R.drawable.image5,
//            R.drawable.image6,
//            R.drawable.image7,
//            R.drawable.image2,
//            R.drawable.image3,
//
//    };
//
//
//
//    // on below line we are creating variable for
//    // our press time and time limit to display a story.
//    long pressTime = 0L;
//    long limit = 500L;
//
//    // on below line we are creating variables for
//    // our progress bar view and image view .
//    private StoriesProgressView storiesProgressView;
//    private ImageView image;
//
//    // on below line we are creating a counter
//    // for keeping count of our stories.
//    private int counter = 0;
//
//    // on below line we are creating a new method for adding touch listener
//    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
//        @SuppressLint("ClickableViewAccessibility")
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            // inside on touch method we are
//            // getting action on below line.
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//
//                    // on action down when we press our screen
//                    // the story will pause for specific time.
//                    pressTime = System.currentTimeMillis();
//
//                    // on below line we are pausing our indicator.
//                    storiesProgressView.pause();
//                    return false;
//                case MotionEvent.ACTION_UP:
//
//                    // in action up case when user do not touches
//                    // screen this method will skip to next image.
//                    long now = System.currentTimeMillis();
//
//                    // on below line we are resuming our progress bar for status.
//                    storiesProgressView.resume();
//
//                    // on below line we are returning if the limit < now - presstime
//                    return limit < now - pressTime;
//            }
//            return false;
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_status);
//
//
//        actionBar=getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setTitle("My Status");
//        actionBar.setSubtitle("Today 11:09");
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//
//
//        // inside in create method below line is use to make a full screen.
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_my_status);
//
//
//
//        //        views
//        typedStatus=findViewById(R.id.typedStatus);
//        statusCaption=findViewById(R.id.status_caption);
//        image = findViewById(R.id.image);
//
//        // on below line we are initializing our variables.
//        storiesProgressView = findViewById(R.id.stories);
//
//
////        storiesProgressView.setStoriesCount(resources.length);
//
//        // on below line we are setting story duration for each story.
//        storiesProgressView.setStoryDuration(3000L);
//
//        // on below line we are calling a method for set
//        // on story listener and passing context to it.
//
//
//
//
//
//        ExecutorService executor= Executors.newSingleThreadExecutor();
//
//        Runnable runnable= () -> {
//
//            handleStatus(MyStatusActivity.statuses.get(0), MyStatusActivity.this, image, statusCaption, typedStatus, actionBar);
//            storiesProgressView.setStoriesListener(MyStatusActivity.this);
//            storiesProgressView.setStoriesCount(MyStatusActivity.statuses.size());
//            storiesProgressView.startStories(counter);
//
//
//        };
//
//
//        executor.submit(()->{
//            MyStatusActivity.statuses=RetrofitHandler.getMyStatuses(MyStatusActivity.this);
//            runOnUiThread(runnable);
//            runnable.run();
//        });
//
//
//
//
//
//
//
//
//
//        // below is the view for going to the previous story.
//        // initializing our previous view.
//        View reverse = findViewById(R.id.reverse);
//
//        // adding on click listener for our reverse view.
//        reverse.setOnClickListener(v -> {
//            // inside on click we are
//            // reversing our progress view.
//            storiesProgressView.reverse();
//        });
//
//        // on below line we are calling a set on touch
//        // listener method to move towards previous image.
//        reverse.setOnTouchListener(onTouchListener);
//
//        // on below line we are initializing
//        // view to skip a specific story.
//        View skip = findViewById(R.id.skip);
//        skip.setOnClickListener(v -> {
//            // inside on click we are
//            // skipping the story progress view.
//            storiesProgressView.skip();
//        });
//        // on below line we are calling a set on touch
//        // listener method to move to next story.
//        skip.setOnTouchListener(onTouchListener);
//    }
//
//    @Override
//    public void onNext() {
//
//
//        handleStatus(MyStatusActivity.statuses.get(++counter),MyStatusActivity.this,image,statusCaption,typedStatus,actionBar);
//    }
//
//    @Override
//    public void onPrev() {
//
//        // this method id called when we move to previous story.
//        // on below line we are decreasing our counter
//        if ((counter - 1) < 0) return;
//
//        handleStatus(MyStatusActivity.statuses.get(++counter),MyStatusActivity.this,image,statusCaption,typedStatus,actionBar);
//    }
//
//    @Override
//    public void onComplete() {
////        finish();
//    }
//
//    @Override
//    protected void onDestroy() {
//        // in on destroy method we are destroying
//        // our stories progress view.
//        storiesProgressView.destroy();
//        super.onDestroy();
//    }
//
//
//    private static void handleStatus(RetrievedStatus status, Context context, ImageView image, TextView statusCaption, TextView typedStatus, ActionBar actionBar){
//        if(status.isTyped()){
//            image.setVisibility(View.GONE);
//            statusCaption.setVisibility(View.GONE);
//            typedStatus.setVisibility(View.VISIBLE);
//            typedStatus.setText(status.getTitle());
//            Typeface typeface = ResourcesCompat.getFont(context,Integer.parseInt(status.getFont()));
//            typedStatus.setTypeface(typeface);
//            typedStatus.setBackgroundColor(Integer.parseInt(status.getBackgroundColor()));
//        }else{
//            image.setVisibility(View.VISIBLE);
//            statusCaption.setVisibility(View.VISIBLE);
//            typedStatus.setVisibility(View.GONE);
//
//        }
//        actionBar.setSubtitle("10 minutes ago");
//
//    }
//
//
//
//
//}