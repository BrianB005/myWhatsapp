package com.brianbett.whatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.User;
import com.brianbett.whatsapp.retrofit.UserInterface;

import java.util.HashMap;

public class ChangeAboutActivity extends AppCompatActivity {
    String [] abouts;
    RecyclerView recyclerView;
    ChangeAboutRecyclerViewAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_about);

        Intent intent=getIntent();
        String about=intent.getStringExtra("about");
        TextView currentAbout=findViewById(R.id.current_about);
        currentAbout.setText(about);

        abouts=new String[]{"Available","Busy","At school","At the movies","At work","Battery about to die","Can't talk, Whatsapp only","In a meeting","At the gym","Urgent calls only","Sleeping"};

        recyclerView=findViewById(R.id.about_recycler_view);
        recyclerViewAdapter=new ChangeAboutRecyclerViewAdapter(ChangeAboutActivity.this,abouts,currentAbout);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChangeAboutActivity.this));

        View change_about=findViewById(R.id.show_about_popup);

        change_about.setOnClickListener(view->{
            View popupView=getLayoutInflater().inflate(R.layout.change_about_popup,null);
            View progressBar=popupView.findViewById(R.id.progress_update);
            AppCompatEditText aboutInput=popupView.findViewById(R.id.about_edit_input);
            aboutInput.setText(about);
            PopupWindow popupWindow=new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,true);



            popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);

            TextView cancelPopup,saveChanges;
            cancelPopup=popupView.findViewById(R.id.cancel_about_popup);
            cancelPopup.setOnClickListener(view1->popupWindow.dismiss());
            saveChanges=popupView.findViewById(R.id.save_about_changes);


            saveChanges.setOnClickListener(view2-> {
                String updatedAbout= aboutInput.getText().toString();
                if (updatedAbout.equals("")) {
                    Toast.makeText(getApplicationContext(), "About cannot be empty!", Toast.LENGTH_LONG).show();
                } else{
                    popupView.findViewById(R.id.progress_update).setVisibility(View.VISIBLE);
                    HashMap<String, String> nameUpdate = new HashMap<>();
                    nameUpdate.put("about", updatedAbout);
                    RetrofitHandler.updateUser(nameUpdate, getApplicationContext(), new UserInterface() {
                        @Override
                        public void success(User user) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "About updated successfully", Toast.LENGTH_SHORT).show();
                            currentAbout.setText(user.getAbout());
                            popupWindow.dismiss();
                        }

                        @Override
                        public void failure(Throwable throwable) {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    });
                }
            });




        });
    }
}