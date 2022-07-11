package com.brianbett.whatsapp;

public class StarredMessage {
    private final String message;
    private final String time;
    private final boolean sent ;
    private final String chattingWith;

    public StarredMessage(String message, String time,boolean isSent,String sender_name) {
        this.message = message;
        this.time = time;
        this.sent=isSent;
        this.chattingWith=sender_name;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public boolean isSent() {
        return sent;
    }

    public String getChattingWith() {
        return chattingWith;
    }
}
