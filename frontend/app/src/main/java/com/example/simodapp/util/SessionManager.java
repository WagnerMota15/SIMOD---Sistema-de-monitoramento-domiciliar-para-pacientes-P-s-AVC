package com.example.simodapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("SIMOD_SESSION", Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit().putString("TOKEN", token).apply();
    }

    public String getToken() {
        return prefs.getString("TOKEN", null);
    }
}

