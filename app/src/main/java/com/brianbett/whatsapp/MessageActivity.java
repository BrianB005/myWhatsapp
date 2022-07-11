package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brianbett.whatsapp.retrofit.ChatMessage;
import com.brianbett.whatsapp.retrofit.ChatMessagesInterface;
import com.brianbett.whatsapp.retrofit.MyPreferences;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;

import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity {
    TextView option_menu;
    ArrayList<ChatMessage> messages;
    RecyclerView recyclerView;
    MessagesRecyclerViewAdapter messagesRecyclerViewAdapter;

    String recipientId;
    String username;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        SharedPreferences preferences=getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        String currentUserId=preferences.getString("userId",null);


        //        initializing the arraylist
        messages=new ArrayList<>();

        recyclerView=findViewById(R.id.messagesRecyclerView);
        messagesRecyclerViewAdapter=new MessagesRecyclerViewAdapter(MessageActivity.this,messages,MessageActivity.this,currentUserId);
        recyclerView.setAdapter(messagesRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));



//        scrolling to the end of messages arraylist when the activity is created
        recyclerView.scrollToPosition(Objects.requireNonNull(recyclerView.getAdapter()).getItemCount()-1);

        //        getting the userId from intent
        Intent intent=getIntent();




       recipientId =intent.getStringExtra("userId");

        //        getting username from intent
       username=intent.getStringExtra("username");

        EditText textInput=findViewById(R.id.textInput);

        //        retrieving the messages and setting up the recycler view
        RetrofitHandler.getAllMessages(MessageActivity.this, recipientId, new ChatMessagesInterface() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void success(List<ChatMessage> messagesList) {
                for (ChatMessage message:messagesList){
                    messages.add(new ChatMessage(message.getMessage(),message.getTime(),message.getRecipient()));
//
                }
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                messagesRecyclerViewAdapter.notifyDataSetChanged();





                textInput.setOnFocusChangeListener((view, b) -> recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1));

            }
            @Override
            public void failure(Throwable throwable) {
                Log.d("Exceptions",throwable.getMessage());
            }
        });


        LayoutInflater layoutInflater=getLayoutInflater();
        @SuppressLint("InflateParams") View view=layoutInflater.inflate(R.layout.messages_toolbar,null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(view,new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        updating the username at the top of the activity
        TextView usernameView= findViewById(R.id.username);
        usernameView.setText(username==null?recipientId:username);
//
        Toolbar parent=(Toolbar) view.getParent();
        parent.setPadding(0,0,0,0);
        parent.setContentInsetsAbsolute(0,0);
//        opening camera
        findViewById(R.id.open_camera).setOnClickListener(view12 -> startActivity(new Intent(MessageActivity.this,CameraActivity.class)));
        
//        going back to MainActivity when the back button is clicked
        findViewById(R.id.back_btn).setOnClickListener(view1->super.finish());

//
        option_menu=findViewById(R.id.option_menu);




//        progress bar
        View progressBar=view.findViewById(R.id.progress_circular);




        option_menu.setOnClickListener(view13 -> {
            PopupMenu popupMenu=new PopupMenu(MessageActivity.this,option_menu);
            popupMenu.getMenuInflater().inflate(R.menu.message_action_bar,popupMenu.getMenu());
//                listening to click events
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                Toast.makeText(MessageActivity.this,"You selected "+menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            });
            popupMenu.show();
        });

//            opening the profile page when the username is clicked

        usernameView.setOnClickListener(view14 -> {
            Intent intent1 =new Intent(MessageActivity.this,ProfileActivity.class);
            intent1.putExtra("user",recipientId);
            intent1.putExtra("username",username);
            startActivity(intent1);
        });



        //        making the send icon visible when the user starts typing

        ImageView sendBtn=findViewById(R.id.sendBtn);
        ImageView voiceBtn=findViewById(R.id.voice_btn);




        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(String.valueOf(textInput.getText()).equals("")){
                    sendBtn.setVisibility(View.GONE);
                    voiceBtn.setVisibility(View.VISIBLE);
                }else {
                    sendBtn.setVisibility(View.VISIBLE);
                    voiceBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        sending messages
        sendBtn.setOnClickListener(view15 -> {
            String message=String.valueOf(textInput.getText());
            RetrofitHandler.createMessage(getApplicationContext(),message,recipientId,progressBar);
            textInput.setText("");

        });


        String myUserId= MyPreferences.getSavedItem(MessageActivity.this,"userId");

//        HttpAuthorizer httpAuthorizer=new HttpAuthorizer("https://my-whatsapp-api-backend.herokuapp.com/api/v1/pusher/user-auth");
//        HashMap<String,String> headers=new HashMap<>();
//        headers.put("Authorization","Bearer "+token);
//        httpAuthorizer.setHeaders(headers);
//        PusherOptions options = new PusherOptions();
//        options.setCluster("mt1");
////        options.setAuthorizer(httpAuthorizer);
//        options.setUseTLS(true);
//
//

        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");
        options.setUseTLS(true);

        Pusher pusher = new Pusher("36b67a35099f920fa4e2", options);

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

        Channel channel1 = pusher.subscribe(recipientId);

        channel1.bind("inserted",
                event -> {
                    Log.i("Pusher", "Received event with data: " + event.toString());

                    String eventData=event.getData();
                    String message;
                    String time;
                    String recipient;
                    ChatMessage chatMessage;
                    try {
                        JSONObject jsonObject=new JSONObject(eventData);
                        message=jsonObject.getString("title");
                        time=jsonObject.getString("createdAt");
                        recipient= jsonObject.getString("recipient");
                        chatMessage=new ChatMessage(message,time,recipient);
                        runOnUiThread(() -> {
                            messages.add(chatMessage);
                            messagesRecyclerViewAdapter.notifyDataSetChanged();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                });

        Channel channel2 = pusher.subscribe(myUserId);

        channel2.bind("inserted", event -> {
            Log.i("Pusher", "Received event with data: " + event.toString());

            String eventData=event.getData();
            String message;
            String time;
            String recipient;

            ChatMessage chatMessage;
            try {
                JSONObject jsonObject=new JSONObject(eventData);
                message=jsonObject.getString("title");
                time=jsonObject.getString("createdAt");
                recipient= jsonObject.getString("recipient");
                chatMessage=new ChatMessage(message,time,recipient);
                runOnUiThread(() -> {
                    messages.add(chatMessage);
                    messagesRecyclerViewAdapter.notifyDataSetChanged();
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }


        });
    }





}