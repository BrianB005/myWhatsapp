package com.brianbett.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Random;

public class RegisterActivityThree extends AppCompatActivity {


    @SuppressLint("StaticFieldLeak")
    private static TextView countdown;
    @SuppressLint("StaticFieldLeak")
    private static TextView resend_text_view;
    @SuppressLint("StaticFieldLeak")
    private  static View resendOtp;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_three);
        countdown=findViewById(R.id.countdown);
        Intent intent=getIntent();
        String otp=intent.getStringExtra("OTP");
        String phoneNumber=intent.getStringExtra("Phone number");
        TextView subtitle=findViewById(R.id.otp_prompt);
        resendOtp=findViewById(R.id.resend_otp);
        resend_text_view=findViewById(R.id.resend_sms);
        View wrongNumber=findViewById(R.id.wrong_number);
        EditText otpInput =findViewById(R.id.otp_input);
        MaterialButton verifyOtpBtn=findViewById(R.id.verify_btn);
//        showing the countdown
        showCountdown();

//        setting the text to display at the top
        subtitle.setText("Kindly enter the 6 digit code sent to +254 "+phoneNumber);
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTP(phoneNumber,getOTP());
                showCountdown();
                Toast.makeText(RegisterActivityThree.this,"Verification code sent to "+phoneNumber,Toast.LENGTH_SHORT).show();
            }
        });

//        starting the enter phone number activity when the user clicks on "wrong number view
        wrongNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivityThree.this,RegisterActivityTwo.class));
            }
        });

//        verifying the otp
        verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputtedOtp=otpInput.getText().toString();
                if(inputtedOtp.equals("")){
//                    Log.d("Otp12","Hello");
                    Toast.makeText(RegisterActivityThree.this,"The verification field is empty!",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (inputtedOtp.equals(otp)) {
                        Intent intent1 = new Intent(RegisterActivityThree.this, RegisterActivityFour.class);
                        intent1.putExtra("Phone number", phoneNumber);
                        startActivity(intent1);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivityThree.this, "Wrong verification code!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
//    resending OTP
    private static void sendOTP(String mobileNo,String otp){
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(mobileNo,null,"Your 6-digit verification code is " +
                ""+otp+" Enter this code to verify your phone Number!.Please do not share this code with anyone!",null,null);

    }
    private static String getOTP(){
        return new DecimalFormat("000000").format(new Random().nextInt(999999));

    }
    private static void showCountdown(){
        //    initializing the counter variabl

        resendOtp.setEnabled(false);
        resendOtp.setBackgroundColor(Color.DKGRAY);
        countdown.setVisibility(View.VISIBLE);
        new CountDownTimer(30000,1000){
            int counter=0;
            @Override
            public void onTick(long l) {
                int countdownTimer=30-counter;
                countdown.setText(String.valueOf(countdownTimer));
                counter++;
            }


            @Override
            public void onFinish() {
                resendOtp.setEnabled(true);
                resendOtp.setBackgroundColor(Color.BLACK);
                countdown.setVisibility(View.GONE);
                resend_text_view.setTextColor(Color.WHITE);
            }

        }.start();
    }
}