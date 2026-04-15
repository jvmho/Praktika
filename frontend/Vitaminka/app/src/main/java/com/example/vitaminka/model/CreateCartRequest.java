package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class CreateCartRequest {
    @SerializedName("UserId")
    private int userId;
    public CreateCartRequest(int userId) { this.userId = userId; }
}