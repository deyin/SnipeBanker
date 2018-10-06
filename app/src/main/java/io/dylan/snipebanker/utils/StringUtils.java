package io.dylan.snipebanker.utils;

import android.support.annotation.NonNull;
import android.util.Log;

public final class StringUtils {

    private static final String TAG = StringUtils.class.getName();

    public static int parseToInt(@NonNull String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            Log.e(TAG, "parseToInt with str: " + str, e);
            return 0;
        }
    }

    public static Double parseToDouble(@NonNull String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            Log.e(TAG, "parseToInt with str: " + str, e);
            return 0d;
        }
    }
}
