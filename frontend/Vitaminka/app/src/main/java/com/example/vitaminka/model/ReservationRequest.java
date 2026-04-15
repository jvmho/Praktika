package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class ReservationRequest {

    @SerializedName("OrderId")
    private final int orderId;

    @SerializedName("BatchId")
    private final int batchId;

    @SerializedName("Quantity")
    private final int quantity;

    @SerializedName("ExpiresAt")
    private final String expiresAt;

    public ReservationRequest(int orderId, int batchId,
                              int quantity, String expiresAt) {
        this.orderId   = orderId;
        this.batchId   = batchId;
        this.quantity  = quantity;
        this.expiresAt = expiresAt;
    }
}
