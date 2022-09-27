package com.brianbett.whatsapp.retrofit;

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

    private final String profilePic;

    public Recipient getRecipient() {
        return recipient;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


//    private  final Bitmap imageSource;


    public Message(String message, String phoneNumber,String userId, String time,String profilePic) {
        this.message = message;
        this.time = time;
        this.phoneNumber=phoneNumber;
        this.userId=userId;
        this.profilePic=profilePic;
    }

    public String getProfilePic() {
        return profilePic;
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

//    public Bitmap getImageSource() {
//        return imageSource;
//    }
}
