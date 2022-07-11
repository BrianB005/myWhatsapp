package com.brianbett.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivityTwo extends AppCompatActivity {
    MaterialButton getPhoneNumber;
    EditText phoneNumberInput;
    FrameLayout frameLayout;
    String otp;
    String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

//        making the activity full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        frameLayout=findViewById(R.id.register_activity_two);
        frameLayout.getForeground().setAlpha(0);

        getPhoneNumber=findViewById(R.id.submit_phoneNumber);

        getPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBtnClick(view);
            }
        });

    }
    private static boolean matchPhone(String phoneNumber){
        boolean feedback=false;
        String regex="^7([0-9]{8})$";
        Pattern pattern1=Pattern.compile(regex);
        Matcher matcher= pattern1.matcher(phoneNumber);
        if(matcher.matches()){
            feedback=true;
        }
        return feedback;

    }
    private static void sendOTP(String mobileNo,String otp){
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(mobileNo,null,"Your 6-digit verification code is " +
                ""+otp+" Enter this code to verify your phone Number!.Please do not share this code with anyone!",null,null);

    }
    private static String getOTP(){
//        return new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9);
        return new DecimalFormat("000000").format(new Random().nextInt(999999));

    }
    @SuppressLint("SetTextI18n")
    private void handleBtnClick(View view){
        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        phoneNumber = phoneNumberInput.getText().toString();
        if (phoneNumber.equals("")){
            Toast.makeText(RegisterActivityTwo.this, "The phone number field is empty", Toast.LENGTH_LONG).show();
        }else if(!matchPhone(phoneNumber)){
            Toast.makeText(RegisterActivityTwo.this, "Invalid phone number", Toast.LENGTH_LONG).show();
        }
        else {
            LayoutInflater layoutInflater = LayoutInflater.from(RegisterActivityTwo.this);
            View popUpView = layoutInflater.inflate(R.layout.confirm_phone_number_popup, null);
            TextView inputtedMobileNo=popUpView.findViewById(R.id.inputted_mobile_no);
            inputtedMobileNo.setText("+254 "+ phoneNumber);
            PopupWindow popupWindow = new PopupWindow(popUpView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,true);
            popupWindow.showAtLocation(view,Gravity.BOTTOM,0,0);
            frameLayout.getForeground().setAlpha(110);
            //    closing the overlay when the popup is closed
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    frameLayout.getForeground().setAlpha(0);
                }
            });
            MaterialButton editBtn = popUpView.findViewById(R.id.edit_phone_number);
            MaterialButton cancelBtn = popUpView.findViewById(R.id.cancel_popup);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    otp = getOTP();
                    if (ContextCompat.checkSelfPermission(RegisterActivityTwo.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RegisterActivityTwo.this, new String[]{Manifest.permission.SEND_SMS}, 200);
                    } else {
                        Intent intent = new Intent(RegisterActivityTwo.this, RegisterActivityThree.class);
                        intent.putExtra("OTP", otp);
                        intent.putExtra("Phone number",phoneNumber);
                        sendOTP(phoneNumber, otp);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(RegisterActivityTwo.this, RegisterActivityThree.class);
            intent.putExtra("OTP", otp);
            intent.putExtra("Phone number",phoneNumber);
            sendOTP(phoneNumber, otp);
            startActivity(intent);
            finish();
        }
    }
}