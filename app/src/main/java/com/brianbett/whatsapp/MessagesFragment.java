package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.brianbett.whatsapp.retrofit.ChatMessage;
import com.brianbett.whatsapp.retrofit.Contact;
import com.brianbett.whatsapp.retrofit.ImageInterface;
import com.brianbett.whatsapp.retrofit.Message;
import com.brianbett.whatsapp.retrofit.MessagesInterface;
import com.brianbett.whatsapp.retrofit.MyPreferences;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MessagesFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Contact> allContacts;
    ArrayList<Message> messages;
    RecyclerViewAdapter recyclerViewAdapter;


    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public  Context getMyContext(){
        return context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context=getContext();
        messages=new ArrayList<>();
        View rootView=inflater.inflate(R.layout.fragment_messages, container, false);
        //      making the foreground/overlay invisible
        FrameLayout frameLayout=rootView.findViewById(R.id.messages_container);
        frameLayout.getForeground().setAlpha(0);
        recyclerView=rootView.findViewById(R.id.recyclerView);
        //        setting up the recyclerview
        recyclerViewAdapter=new RecyclerViewAdapter(getContext(),messages,rootView);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        ensures that the action bar is visible
        setHasOptionsMenu(true);

        //    retrieving contacts from shared preferences

        SharedPreferences preferences= getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String contacts=preferences.getString("contacts",null);
        Type type= new TypeToken<ArrayList<Contact>>(){}.getType();
        Gson gson=new Gson();
        allContacts=gson.fromJson(contacts,type);


//        custom method to refresh feed
        refreshChats(allContacts,messages,recyclerViewAdapter,getContext(),recyclerView,rootView);


//        opening contacts page when start chat btn is clicked
        View openContacts=rootView.findViewById(R.id.open_contacts);
        openContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ContactsActivity.class));
            }
        });



//        configuring pusher

        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");
        options.setUseTLS(true);

        String myUserId= MyPreferences.getSavedItem(getContext(),"userId");

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


        Channel channel1 = pusher.subscribe(myUserId);

        channel1.bind("inserted", new SubscriptionEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(PusherEvent event) {
                Log.i("Pusher", "Received event with data: " + event.toString());

                String eventData = event.getData();
                refreshChats(allContacts,messages,recyclerViewAdapter,getMyContext(),recyclerView,rootView);


            }

        });


        return rootView;

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_action_bar,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {


        super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.newGroup:
//                startActivity(new Intent(getActivity(),StatusActivity.class));
                break;
            case R.id.newBroadcast:
//
//                startActivity(new Intent(getActivity(),RegisterActivityFour.class));
                break;
            case R.id.linkedDevices:
//
//                startActivity(new Intent(getActivity(),RegisterActivityOne.class));
                break;
            case R.id.starredMessages:
                startActivity(new Intent(getActivity(), StarredMessagesActivity.class));
                break;
            case R.id.settings:
                Intent intent=new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    private static  void refreshChats(ArrayList<Contact> allContacts,ArrayList<Message> messages,
                                      RecyclerViewAdapter recyclerViewAdapter,Context context,RecyclerView recyclerView,View rootView){

        RetrofitHandler.getAllChats(context, new MessagesInterface() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void success(List<Message> dbMessages) {
                messages.clear();
                for(Message message:dbMessages) {
                    //        getting names of those saved in the phone nook
                    assert  allContacts!=null;


                    RetrofitHandler.fetchImage(message.getRecipient().getProfilePic(), new ImageInterface() {
                        @Override
                        public void success(Bitmap bitmap) {
                            message.getRecipient().setImageBitmap(bitmap);

                        }

                        @Override
                        public void failure(Throwable throwable) {

                        }
                    });
                    Message message1=new Message(message.getMessage(),message.getRecipient().getPhoneNumber(),message.getRecipient().getUserId(), message.getTime(),message.getRecipient().getImageBitmap());
                    messages.add(message1);


//                    for (Contact contact:allContacts){
//                        if(contact.getPhoneNumber().equals(message.getRecipient().getPhoneNumber())){
//                            name=contact.getName();
//                            //                            Log.d("name1",name);
//                        }else{
//                            name=message.getRecipient().getPhoneNumber();
//                        }
//
//                        message1=new Message(message.getMessage(),name,message.getRecipient().getUserId(), message.getTime(),message.getRecipient().getImageBitmap());
//                    }
//
//                    messages.add(message1);
                }

                recyclerViewAdapter.notifyDataSetChanged();
                View zeroChats=rootView.findViewById(R.id.zero_chats);
                if(messages.isEmpty()){
                    zeroChats.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else{
                    zeroChats.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void failure(Throwable throwable) {
                Toast.makeText(context, throwable.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }















}