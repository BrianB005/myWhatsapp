package com.brianbett.whatsapp.retrofit;

import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

//public interface StatusesInterface extends StoriesProgressView.StoriesListener {
//    void success(List<RetrievedStatus> statuses);
//    void failure(Throwable throwable);
//}

public interface StatusesInterface  {
    void success(List<RetrievedStatus> statuses);
    void failure(Throwable throwable);
}
