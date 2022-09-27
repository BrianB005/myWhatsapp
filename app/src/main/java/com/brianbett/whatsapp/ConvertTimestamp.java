package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ConvertTimestamp {
    private static Date convertToDate(String mongodbTimeStamp){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsedDate = null;
        try {
            parsedDate=simpleDateFormat.parse(mongodbTimeStamp);
        }catch (ParseException e) {
            Log.e("Parse exception", e.getMessage());
        }
        return parsedDate;
    }
    static String getTime( String mongoDBTimestamp){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        Date date=convertToDate(mongoDBTimestamp);
        return dateFormat.format(date);
    }
    static String getMomentsAgo(Context context,String mongoDBTimestamp){
        Date date=convertToDate(mongoDBTimestamp);
        return DateUtils.getRelativeTimeSpanString(context,date.getTime()).toString();
//        return DateUtils.getRelativeDateTimeString(context,date.getTime(),DateUtils.MINUTE_IN_MILLIS,DateUtils.DAY_IN_MILLIS,0).toString();
    }
    static String getStatusMoments(Context context,String mongoDBTimestamp){
        Date date=convertToDate(mongoDBTimestamp);
        long numberOfMillisInAnHour=60*60*1000;
        long numberOfMillisInADay=24*60*60*1000;
        if(DateUtils.isToday(date.getTime())){
            if(System.currentTimeMillis()-date.getTime()<=numberOfMillisInAnHour){
                return DateUtils.getRelativeTimeSpanString(date.getTime(),
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_SHOW_DATE).toString();
            }else{
                return "Today "+getTime(mongoDBTimestamp);
            }

        }else if(System.currentTimeMillis()-date.getTime()<numberOfMillisInADay){
            return "Yesterday "+getTime(mongoDBTimestamp);
        }else{
            return getMomentsAgo(context,mongoDBTimestamp);
        }


    }
}
