package com.example.vitaminka.network;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

public class SessionManager implements TokenProvider {

    private static final String PREF_NAME  = "pharmacy_session";
    private static final String KEY_TOKEN  = "auth_token";

    private final SharedPreferences prefs;
    private static SessionManager instance;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /** Вызвать один раз в Application.onCreate() */
    public static void init(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SessionManager.init() не вызван");
        }
        return instance;
    }

    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public void clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply();
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    @Override
    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public int getUserId() {
        String token = getToken();
        if (token == null) return -1;
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return -1;
            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
            JSONObject json = new JSONObject(payload);
            return json.getInt("user_id");
        } catch (Exception e) {
            return -1;
        }
    }
}