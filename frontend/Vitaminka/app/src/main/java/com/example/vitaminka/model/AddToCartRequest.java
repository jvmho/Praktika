package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class AddToCartRequest {
    @SerializedName("drugId")
    private int drugId;
    @SerializedName("quantity")
    private int quantity;

    public AddToCartRequest(int drugId, int quantity) {
        this.drugId = drugId;
        this.quantity = quantity;
    }
}