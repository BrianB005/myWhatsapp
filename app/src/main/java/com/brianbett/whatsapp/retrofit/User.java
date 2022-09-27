package com.brianbett.whatsapp.retrofit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("username")
    private final String name;
    private  final int phoneNumber;
    @SerializedName("_id")
    private   String userId;
    private String about;
    private String profilePic;



    public String getProfilePic() {
        return profilePic;
    }

    public User(String name, int phoneNumber){
        this.name=name;
        this.phoneNumber=phoneNumber;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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



//    public UserDetails getUserDetails(){
//        return userDetails;
//    }
}
