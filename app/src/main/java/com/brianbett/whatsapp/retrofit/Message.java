package com.brianbett.whatsapp.retrofit;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;


public class Message {
    @SerializedName("title")
    private final String message;
    @SerializedName("createdAt")
    private final String time;
    private Recipient recipient;
    private final String phoneNumber;
    @SerializedName("_id")
    private final String userId;

    public Recipient getRecipient() {
        return recipient;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    private  final Bitmap imageSource;


    public Message(String message, String phoneNumber,String userId, String time,Bitmap imageSource) {
        this.message = message;
        this.time = time;
        this.phoneNumber=phoneNumber;
        this.userId=userId;
        this.imageSource=imageSource;
    }

    public String getMessage() {
        return message;
    }

//    public String getName() {
//        return name;
//    }

    public String getTime() {
        return time;
    }

    public Bitmap getImageSource() {
        return imageSource;
    }
}
