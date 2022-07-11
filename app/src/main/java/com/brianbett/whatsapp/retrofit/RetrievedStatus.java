package com.brianbett.whatsapp.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetrievedStatus{
    private String caption;
    private String statusImage;

    private String sender;

    private   String backgroundColor;
    private  String font;
    private  boolean isTyped;
    private  String title;

    @SerializedName("createdAt")
    private String timeCreated;

    public String getCaption() {
        return caption;
    }

    public String getStatusImage() {
        return statusImage;
    }

    public String getSender() {
        return sender;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getFont() {
        return font;
    }

    public boolean isTyped() {
        return isTyped;
    }

    public String getTitle() {
        return title;
    }

    public String getTimeCreated() {
        return timeCreated;
    }
}
