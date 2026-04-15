package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class UpdateCartItemRequest {
    @SerializedName("Quantity")
    private int quantity;
    public UpdateCartItemRequest(int quantity) { this.quantity = quantity; }
}