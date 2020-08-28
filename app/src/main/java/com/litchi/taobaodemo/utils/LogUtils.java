package com.litchi.taobaodemo.utils;

import android.util.Log;

public class LogUtils {
    private static final int CUR_LEVEL = 1;
    private static final int DEBUG_LEVEL = 1;
    private static final int INFO_LEVEL = 2;
    private static final int WARNING_LEVEL = 3;
    private static final int ERROR_LEVEL = 4;

    public static void d(String TAG, String log) {
        if (CUR_LEVEL <= DEBUG_LEVEL) {
            Log.d(TAG, log);
        }
    }

    public static void i(String TAG, String log) {
        if (CUR_LEVEL <= INFO_LEVEL) {
            Log.i(TAG, log);
        }
    }

    public static void w(String TAG, String log) {
        if (CUR_LEVEL <= WARNING_LEVEL) {
            Log.w(TAG, log);
        }
    }

    public static void e(String TAG, String log) {
        if (CUR_LEVEL <= ERROR_LEVEL) {
            Log.e(TAG, log);
        }
    }
}
