package com.brianbett.whatsapp;

import java.util.ArrayList;

public class SingleStatus {
    private  final int last_image;
    private final String username;
    private final String time_posted;
    private final long phoneNumber;
    private  boolean isMine;

    public SingleStatus(int last_image, String username,long phoneNumber, String time_posted) {
        this.last_image = last_image;
        this.username = username;
        this.time_posted = time_posted;
        this.phoneNumber=phoneNumber;
    }

    public boolean isMine() {
        return isMine;
    }
    public void setMine(boolean mine) {
        this.isMine = mine;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public int getLast_image() {
        return last_image;
    }

    public String getUsername() {
        return username;
    }

    public String getTime_posted() {
        return time_posted;
    }
}
