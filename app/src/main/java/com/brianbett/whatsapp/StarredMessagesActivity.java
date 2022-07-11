package com.brianbett.whatsapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class StarredMessagesActivity extends AppCompatActivity {
    ArrayList<StarredMessage> messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred_messages);

//        initializing the arraylist
        messages=new ArrayList<>();


//        creating messages arraylist
        StarredMessage chatMessage1=new StarredMessage("Hello there. I hope you had  a great night","10:50",true,"Clarence");
        StarredMessage chatMessage2=new StarredMessage("Hello. I indeed had a great night","10:52",false,"Audrey");
        StarredMessage chatMessage3=new StarredMessage("How about yours?Was it great?","10:53",false,"CLarence");
        StarredMessage chatMessage4=new StarredMessage("Mine was great too.","11:03",true,"Brian");
        StarredMessage chatMessage5=new StarredMessage("I slept like a baby in fact","11:03",true,"Dorian");
        StarredMessage chatMessage6=new StarredMessage("Will you have  any classes today","11:06",true,"Neil Arm");

//        messages.add(chatMessage1);messages.add(chatMessage2);messages.add(chatMessage3);messages.add(chatMessage4);
        messages.add(chatMessage5);messages.add(chatMessage6);


//        creating the recyclerView




        RecyclerView recyclerView = findViewById(R.id.starred_messages_recycler_view);
        StarredMessagesRecyclerViewAdapter adapter = new StarredMessagesRecyclerViewAdapter(messages, StarredMessagesActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(StarredMessagesActivity.this));
        //        hiding the recyclerView when it has no contents
        if(messages.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            findViewById(R.id.zero_starred_messages).setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.zero_starred_messages).setVisibility(View.GONE);
        }



    }
}