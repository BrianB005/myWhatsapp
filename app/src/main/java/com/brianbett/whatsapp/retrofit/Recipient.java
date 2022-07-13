package com.brianbett.whatsapp.retrofit;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Recipient {
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("_id")
    private String userId;

    @SerializedName("profilePic")
    private String userProfilePic;

    private Bitmap imageBitmap;

    public String getProfilePic() {
        return userProfilePic;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap=imageBitmap;
    }

    public String getUserId() {
        return userId;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
