package com.brianbett.whatsapp.retrofit;

import java.util.ArrayList;

public class ImageStatus extends CreatedStatus{

    private final String caption;
    private final String statusImage;
    public ImageStatus(ArrayList<String> contacts, String caption,String statusImage){
        super(contacts);

        this.statusImage=statusImage;
        this.caption=caption;
    }

    public String getCaption() {
        return caption;
    }

    public String getStatusImage() {
        return statusImage;
    }
}
