package com.brianbett.whatsapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.brianbett.whatsapp.retrofit.MyRetrievedStatus;

import java.util.List;

public class MyViewModel extends ViewModel {

    private MutableLiveData<List<MyRetrievedStatus>> myStatuses;
    private MutableLiveData<List<MyRetrievedStatus>> myFriendsStatuses;

    public MutableLiveData<List<MyRetrievedStatus>> getMyStatuses() {
       if(myStatuses==null) {
           myStatuses=new MutableLiveData<>();
       }
        return myStatuses;
    }
    public MutableLiveData<List<MyRetrievedStatus>> getMyFriendsStatuses() {
        if(myFriendsStatuses==null) {
            myFriendsStatuses=new MutableLiveData<>();
        }
        return myFriendsStatuses;
    }





}
