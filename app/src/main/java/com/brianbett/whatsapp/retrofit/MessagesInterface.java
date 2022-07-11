package com.brianbett.whatsapp.retrofit;

import java.util.List;

public interface MessagesInterface {
    void success(List<Message> messages);
    void failure(Throwable throwable);
}
