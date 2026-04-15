package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class AuthRequest {

    @SerializedName("Login")
    private final String login;

    @SerializedName("Password")
    private final String password;

    public AuthRequest(String login, String password) {
        this.login    = login;
        this.password = password;
    }
}