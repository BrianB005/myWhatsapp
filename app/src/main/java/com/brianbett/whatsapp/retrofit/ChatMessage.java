package com.brianbett.whatsapp.retrofit;

import com.google.gson.annotations.SerializedName;

public class ChatMessage {
    @SerializedName("title")
    private final String message;
    @SerializedName("createdAt")
    private final String time;
    @SerializedName("recipient")
    private final String recipient;


    public ChatMessage(String message, String time,String recipient) {
        this.message = message;
        this.time = time;
        this.recipient=recipient;

    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getRecipient() {
        return recipient;
    }

    public boolean isSent(String currentUserId){
        return !currentUserId.equals(recipient);
    }
}
