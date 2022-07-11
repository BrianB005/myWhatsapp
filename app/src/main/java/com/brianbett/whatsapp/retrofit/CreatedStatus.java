package com.brianbett.whatsapp.retrofit;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CreatedStatus {
    @SerializedName("targetAudience")
    private final ArrayList<String> contacts;


    public CreatedStatus( ArrayList<String> contacts) {
        this.contacts = contacts;

    }

    public ArrayList<String> getContacts() {
        return contacts;
    }


}
