package com.brianbett.whatsapp.retrofit;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    private final String name;
    private  final int phoneNumber;
    @SerializedName("_id")
    private   String userId;
    private String about;

    private String profilePic;

    private Bitmap profileBitmap;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Bitmap getProfileBitmap() {
        return profileBitmap;
    }

    public void setProfileBitmap(Bitmap profileBitmap) {
        this.profileBitmap = profileBitmap;
    }

    public User(String name, int phoneNumber){
        this.name=name;
        this.phoneNumber=phoneNumber;
    }

    public String getAbout() {
        return about;
    }

    public String getUserId() {
        return userId;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }
    public String getName() {
        return name;
    }
    public String token;
    public String getToken() {
        return token;
    }


//    public UserDetails getUserDetails(){
//        return userDetails;
//    }
}
