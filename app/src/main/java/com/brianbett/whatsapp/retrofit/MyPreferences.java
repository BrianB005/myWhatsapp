package com.brianbett.whatsapp.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    private static SharedPreferences preferences;
    public  static String getSavedItem(Context context,String key){
        String savedValue;
        preferences=context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        savedValue=preferences.getString(key,null);
        return savedValue;
    }
    public static void saveItemToSP(Context context,String key,String value){
        preferences=context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();

        editor.putString(key,value).apply();

    }
}
