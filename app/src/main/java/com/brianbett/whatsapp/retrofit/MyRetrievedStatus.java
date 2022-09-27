package com.brianbett.whatsapp.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyRetrievedStatus extends RetrievedStatus{
    @SerializedName("viewers")
    private List<StatusViewer> statusViewerList;

    public List<StatusViewer> getStatusViewerList() {
        return statusViewerList;
    }
}
