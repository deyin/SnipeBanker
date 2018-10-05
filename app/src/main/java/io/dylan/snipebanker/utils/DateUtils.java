package io.dylan.snipebanker.utils;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    @NonNull
    public static Date getDaysBefore(@NonNull Date date, int days) {
        return new Date(date.getTime() - days * 24 * 60 * 60 * 1000);
    }

    @NonNull
    public static Date getDaysAfter(@NonNull Date date, int days) {
        return new Date(date.getTime() + days * 24 * 60 * 60 * 1000);
    }

    @NonNull
    public static Date getHoursBefore(@NonNull Date date, int hours) {
        return new Date(date.getTime() - hours * 60 * 60 * 1000);
    }

    @NonNull
    public static Date getHoursAfter(@NonNull Date date, int hours) {
        return new Date(date.getTime() + hours * 60 * 60 * 1000);
    }


}
