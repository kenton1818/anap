package com.example.lui_project.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;


public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Initialization tool class
     *
     * @param context
     */
    public static void init(@NonNull Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * get ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }
}