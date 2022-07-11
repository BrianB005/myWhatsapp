package com.brianbett.whatsapp.retrofit;

import java.util.List;

public interface ContactsInterface {
    void success(List<Contact> contacts);
    void failure(Throwable throwable);
}
