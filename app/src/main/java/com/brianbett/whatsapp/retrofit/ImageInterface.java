package com.brianbett.whatsapp.retrofit;

import android.graphics.Bitmap;

public interface ImageInterface {
    void success(Bitmap bitmap);
    void failure(Throwable throwable);
}
