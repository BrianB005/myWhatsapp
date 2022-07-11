package com.brianbett.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.google.android.material.button.MaterialButton;

public class RegisterActivityFour extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_four);

        Intent intent=getIntent();
        String phoneNumber=intent.getStringExtra("Phone number");
        View showPopup=findViewById(R.id.show_gallery_popup);
        MaterialButton nextBtn=findViewById(R.id.next_btn);
        EditText nameInput=findViewById(R.id.name_input);
        View progressbar =findViewById(R.id.progressbar);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=nameInput.getText().toString();

                if(name.equals("")){
                    Toast.makeText(getApplicationContext(),"Kindly fill the name field",Toast.LENGTH_SHORT).show();
                }else{
                    RetrofitHandler.registerUser(getApplicationContext(),progressbar,name,Integer.parseInt(phoneNumber));
                    startActivity(new Intent(RegisterActivityFour.this,MainActivity.class));
                    finish();


                }
            }
        });


//        setting up the popup window that show the open camera and gallery buttons
        showPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rootView.getForeground().setAlpha(100);
                View popupView=getLayoutInflater().inflate(R.layout.gallery_popup,null);
                PopupWindow popupWindow=new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,600,true);
                popupWindow.showAtLocation(view,Gravity.BOTTOM,0,0);
                View openCamera=popupView.findViewById(R.id.open_camera);
                View openGallery=popupView.findViewById(R.id.open_gallery);
                openCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(RegisterActivityFour.this,CameraActivity.class));
                        popupWindow.dismiss();

                    }
                });
                openGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(RegisterActivityFour.this,GalleryActivity.class));
                        popupWindow.dismiss();
                    }
                });



            }

        });


    }
}