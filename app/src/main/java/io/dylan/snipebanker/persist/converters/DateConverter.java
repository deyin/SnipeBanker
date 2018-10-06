package io.dylan.snipebanker.persist.converters;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date dateFromYmdHmsString(@NonNull String value) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
        } catch (Exception e) {
            Log.e(DateConverter.class.getName(), "dateFromYmdHmsString: ", e);
        }
        return new Date(); // avoid NullPointerException
    }

    public static Date dateFromslashYmdHmString(@NonNull String value) {
        try {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(value);
        } catch (Exception e) {
            Log.e(DateConverter.class.getName(), "dateFromYmdHmsString: ", e);
        }
        return new Date(); // avoid NullPointerException
    }


    @TypeConverter
    public static String dateToYmdHmsString(@NonNull Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Date dateFromYmdString(@NonNull String value) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(value);
        } catch (ParseException e) {
            Log.e(DateConverter.class.getName(), "dateFromYmdString: ", e);
        }
        return new Date(); // avoid NullPointerException
    }

    public static String dateToYmdString(@NonNull Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String dateToHmsString(@NonNull Date date) {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    public static String dateToHmString(@NonNull Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }

}