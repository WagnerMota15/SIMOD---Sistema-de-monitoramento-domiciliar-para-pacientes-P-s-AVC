package com.example.simodapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String NAME = "session_manager";
    private static final String TOKEN = "token";
    private SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {

        preferences.edit().putString(TOKEN, token).apply();
    }

    public String getToken() {

        return preferences.getString(TOKEN, null);
    }

    public boolean isLogged(){
        return getToken()!=null;
    }

    public void clear(){
        preferences.edit().clear().apply();
    }

}

