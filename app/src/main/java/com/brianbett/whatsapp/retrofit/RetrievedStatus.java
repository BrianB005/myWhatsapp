package com.brianbett.whatsapp.retrofit;

import com.google.gson.annotations.SerializedName;

public class RetrievedStatus{
    private String caption;
    private String statusImage;
    @SerializedName("_id")
    private String statusId;
    @SerializedName("sender")
    private Recipient nestedUserDetails;

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

    public Recipient getNestedUserDetails() {
        return nestedUserDetails;
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

    public String getStatusId() {
        return statusId;
    }


}
