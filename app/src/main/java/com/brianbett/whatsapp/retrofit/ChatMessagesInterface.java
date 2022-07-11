package com.brianbett.whatsapp.retrofit;

import java.util.List;

public interface ChatMessagesInterface {
    void success(List<ChatMessage> messagesList);
    void failure(Throwable throwable);
}
