package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.brianbett.whatsapp.retrofit.MyPreferences;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.User;
import com.brianbett.whatsapp.retrofit.UserInterface;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class MyProfileActivity extends AppCompatActivity {

    Pusher pusher;
    String myUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


//        activity for opening  gallery/camera

        ActivityResultLauncher<Intent> launcher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        Uri uri = result.getData().getData();
                        ((ImageView)findViewById(R.id.my_profile_profilePic)).setImageURI(uri);

                        RetrofitHandler.uploadFile(uri,getApplicationContext());

                        // Use the uri to load the image
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
//
                        Log.d("Error",ImagePicker.Companion.getError(result.getData()));
                    }
                });





//        opening gallery/camera so as to change profile pic

        findViewById(R.id.changeProfilePic).setOnClickListener(view-> ImagePicker.Companion.with(MyProfileActivity.this)
                .crop()
                .cropOval()
                .provider(ImageProvider.BOTH)
                .createIntentFromDialog(new Function1() {
                    public Object invoke(Object var1) {
                        this.invoke((Intent) var1);
                        return Unit.INSTANCE;
                    }

                    public void invoke(@NotNull Intent it) {
                        Intrinsics.checkNotNullParameter(it, "it");
                        launcher.launch(it);
                    }
                })
        );




        View edit_name=findViewById(R.id.edit_name);
        View edit_about=findViewById(R.id.edit_about);
        ImageView profilePicImageView=findViewById(R.id.my_profile_profilePic);
        TextView about,username,myPhoneNumber;
        about=findViewById(R.id.my_profile_about);
        username=findViewById(R.id.my_profile_username);
        myPhoneNumber=findViewById(R.id.my_profile_phoneNumber);
        RetrofitHandler.getCurrentUser(getApplicationContext(), new UserInterface() {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(User user) {
                about.setText(user.getAbout());
                myPhoneNumber.setText("+254 "+user.getPhoneNumber());
                username.setText(user.getName());
                profilePicImageView.setImageBitmap(user.getProfileBitmap());

                MyPreferences.saveItemToSP(getApplicationContext(),"userId",user.getUserId());
                edit_name.setOnClickListener(view->{
                    @SuppressLint("InflateParams") View popupView=getLayoutInflater().inflate(R.layout.change_name_popup,null);
                    PopupWindow popupWindow=new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,true);
                    popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);

                    AppCompatEditText nameInput=popupView.findViewById(R.id.name_edit_input);
                    nameInput.setText(user.getName());

                    TextView cancelPopup,saveChanges;
                    cancelPopup=popupView.findViewById(R.id.cancel_name_popup);
                    cancelPopup.setOnClickListener(view1->popupWindow.dismiss());
                    saveChanges=popupView.findViewById(R.id.save_name_changes);

                    saveChanges.setOnClickListener(view2-> {
                        popupView.findViewById(R.id.progress_update).setVisibility(View.VISIBLE);
                        String updatedName = Objects.requireNonNull(nameInput.getText()).toString();
                        if (updatedName.equals("")) {
                            Toast.makeText(getApplicationContext(), "You must provide a name!", Toast.LENGTH_LONG).show();
                        } else{
                            HashMap<String, String> nameUpdate = new HashMap<>();
                            nameUpdate.put("username", updatedName);
                            RetrofitHandler.updateUser(nameUpdate, getApplicationContext(), new UserInterface() {
                                @Override
                                public void success(User user) {
                                    popupView.findViewById(R.id.progress_update).setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();
                                    username.setText(user.getName());
                                    popupWindow.dismiss();
                                }

                                @Override
                                public void failure(Throwable throwable) {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                    popupView.findViewById(R.id.progress_update).setVisibility(View.GONE);

                                }
                            });
                    }
                    });

                });

                edit_about.setOnClickListener(view->{
                    Intent intent=new Intent(MyProfileActivity.this,ChangeAboutActivity.class);
                    intent.putExtra("about",user.getAbout());
                    startActivity(intent);
                });
            }
            @Override
            public void failure(Throwable throwable) {

            }
        });





//        connecting to pusher
        myUserId=MyPreferences.getSavedItem(getApplicationContext(),"userId");

        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");
        options.setUseTLS(true);

       pusher = new Pusher("36b67a35099f920fa4e2", options);
        updateUser(pusher,myUserId);

    }

    @Override
    protected void onPause() {
        super.onPause();
        myUserId=MyPreferences.getSavedItem(getApplicationContext(),"userId");

        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");
        options.setUseTLS(true);

        pusher = new Pusher("36b67a35099f920fa4e2", options);
        updateUser(pusher,myUserId);


    }

    private void updateUser(Pusher pusher, String myUserId){
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

        channel.bind("updated", event -> Log.i("Pusher", "Received event with data: " + event.toString()));


    }
//    private Runnable activityResultRunnable;
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        activityResultRunnable=new Runnable() {
//            @Override
//            public void run() {
//                Uri uri=data.getData();
////                RetrofitHandler.uploadFile(uri,getApplicationContext());
//            }
//        };
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        if(activityResultRunnable!=null){
//            activityResultRunnable.run();
//            activityResultRunnable=null;
//        }
//    }
}