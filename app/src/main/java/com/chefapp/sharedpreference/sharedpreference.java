package com.chefapp.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedpreference {
    private static Context mcontext;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    public static final String PREFERENCE = "User";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String USERNAME_KEY = "username";
    private static final String EMAIL_KEY = "email";
    private static final String NAME_KEY = "name";

    public sharedpreference(Context mcontex) {
        this.mcontext = mcontex;
        pref = mcontext.getSharedPreferences(PREFERENCE, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public static void setSharedpreference(Context context, String name, String value) {
        mcontext = context;
        SharedPreferences settings = mcontext.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(name, value);
        editor.apply();
    }

    public static String getSharedprefrences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }

    public static void setLoggedInUsername(Context context, String username) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(USERNAME_KEY, username);
        editor.apply();
    }

    public static String getLoggedInUsername(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(USERNAME_KEY, "");
    }

    public static void setLoggedInEmail(Context context, String email) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(EMAIL_KEY, email);
        editor.apply();
    }

    public static String getLoggedInEmail(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(EMAIL_KEY, "");
    }

    public static void setLoggedInName(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(NAME_KEY, name);
        editor.apply();
    }

    public static String getLoggedInName(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(NAME_KEY, "");
    }

    public static void removepreference(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        settings.edit().remove(name).apply();
    }

    public static void clearPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        settings.edit().clear().apply();
    }
}
