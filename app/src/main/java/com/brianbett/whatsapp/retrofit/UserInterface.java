package com.brianbett.whatsapp.retrofit;

import java.util.List;

public interface UserInterface {
    void success(User user);
    void failure(Throwable throwable);
}
