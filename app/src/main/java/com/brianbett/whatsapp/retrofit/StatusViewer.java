package com.brianbett.whatsapp.retrofit;

import com.google.gson.annotations.SerializedName;

public class StatusViewer {


   @SerializedName("viewer")
   StatusViewerDetails viewerDetails;
    @SerializedName("createdAt")
    private String viewedAt;

    public StatusViewerDetails getViewerDetails() {
        return viewerDetails;
    }

    public String getViewedAt() {
        return viewedAt;
    }
}
