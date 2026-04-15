package com.example.vitaminka.network;

import android.util.Base64;

import org.json.JSONObject;

public interface TokenProvider {
    /** Возвращает актуальный JWT-токен или null, если пользователь не вошёл. */
    String getToken();
    void saveToken(String token);
    void clearToken();
}