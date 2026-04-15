package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class CreateSupplyRequest {

    @SerializedName("SupplierId")
    private final int supplierId;

    @SerializedName("SupplyDate")
    private final String supplyDate;

    @SerializedName("Status")
    private final String status;

    public CreateSupplyRequest(int supplierId,
                               String supplyDate,
                               String status) {
        this.supplierId = supplierId;
        this.supplyDate = supplyDate;
        this.status     = status;
    }
}