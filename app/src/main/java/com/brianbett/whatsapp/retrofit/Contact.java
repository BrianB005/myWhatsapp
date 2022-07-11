package com.brianbett.whatsapp.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class Contact {
    private final String phoneNumber;
    private final String about;
    @SerializedName("_id")
    private  final String userId;
    private String name;

    public Contact(String phoneNumber, String about,String userId) {
        this.phoneNumber = phoneNumber;
        this.about = about;
        this.userId=userId;
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
