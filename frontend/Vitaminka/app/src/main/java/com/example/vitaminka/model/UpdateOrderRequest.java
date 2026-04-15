package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class UpdateOrderRequest {

    @SerializedName("Status")
    private final String status;

    public UpdateOrderRequest(String status) {
        this.status = status;
    }
}