package com.brianbett.whatsapp.retrofit;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TypedStatus extends  CreatedStatus {
    private final  String backgroundColor;
    private final String font;
    private final boolean isTyped;
    private final String title;

    public TypedStatus(ArrayList<String> contacts, String title, String font, String backgroundColor) {
        super(contacts);
        this.isTyped=true;
        this.font=font;
        this.title=title;
        this.backgroundColor=backgroundColor;

    }


    public String getTitle() {
        return title;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getFont() {
        return font;
    }

    public boolean isTyped() {
        return isTyped;
    }
}
