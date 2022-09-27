package com.brianbett.whatsapp.retrofit;

import java.util.List;

public interface MyStatusesInterface {
    void success(List<MyRetrievedStatus> statuses);
    void failure(Throwable throwable);
    void errorExists();
}
