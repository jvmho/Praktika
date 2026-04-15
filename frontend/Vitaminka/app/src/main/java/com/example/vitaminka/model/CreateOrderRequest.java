package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class CreateOrderRequest {

    @SerializedName("DeliveryType")
    private final String deliveryType;

    public CreateOrderRequest(String deliveryType) {
        this.deliveryType = deliveryType;
    }
}
