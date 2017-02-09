package com.rajat.gopetting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by priyanka on 2/9/2017.
 */

public class MyPrefernces {
    private static final String IS_LOGGED_IN = "is_loggedIn";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String AVATAR_URL = "avatar_url";
    private static final String TOTAL = "total";
    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor editor;
    private Context mContext;


    MyPrefernces(Context context) {
        mContext = context;
        mSharedPreference = context.getSharedPreferences(String.valueOf(R.string.my_prefs_key), Context.MODE_PRIVATE);
        editor = mSharedPreference.edit();
    }

    public void addTotal(int total) {
        editor.putInt(TOTAL, total);
        editor.commit();
    }

    public int getTotal() {
        return mSharedPreference.getInt(TOTAL, -1);
    }

    public void addName(String name) {
        editor.putString(NAME, name);
        editor.commit();
    }

    public void setLogIn(boolean value) {
        editor.putBoolean(IS_LOGGED_IN, value);
        editor.commit();
    }

    public boolean checkLogIN() {
        return mSharedPreference.getBoolean(IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
}
