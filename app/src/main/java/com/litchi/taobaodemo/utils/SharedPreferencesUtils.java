package com.litchi.taobaodemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.litchi.taobaodemo.base.BaseApplication;

public class SharedPreferencesUtils {

    private static SharedPreferencesUtils sharedPreferencesUtils;
    private static String SHARED_PREFERENCES_NAME = "cache_shared_preference";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private SharedPreferencesUtils() {
        sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static SharedPreferencesUtils getInstance() {
        if (sharedPreferencesUtils == null) {
            sharedPreferencesUtils = new SharedPreferencesUtils();
        }
        return sharedPreferencesUtils;
    }

    public void saveObj(String key, Object obj) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, gson.toJson(obj));
        edit.apply();
    }

    public <T> T getObj(String key, TypeToken<T> typeToken) {
        String json = sharedPreferences.getString(key, null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, typeToken.getType());
    }

    public void delete(String key) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(key);
        edit.apply();
    }
}
