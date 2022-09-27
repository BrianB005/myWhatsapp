package com.brianbett.whatsapp.retrofit;

import com.google.gson.annotations.SerializedName;

public class Contact {
    private final String phoneNumber;
    private final String about;
    @SerializedName("_id")
    private  final String userId;
    private String name;
    private final String profilePic;

    public Contact(String phoneNumber, String about,String userId,String profilePic) {
        this.phoneNumber = phoneNumber;
        this.about = about;
        this.userId=userId;
        this.profilePic=profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAbout(){
        return about;
    }


}
