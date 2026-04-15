package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("token")
    private String token;

    @SerializedName("TokenType")
    private String tokenType;

    public String getToken()     { return token; }
    public String getTokenType() { return tokenType; }
}