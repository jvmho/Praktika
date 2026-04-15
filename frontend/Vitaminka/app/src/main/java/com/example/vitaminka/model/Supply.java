package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class Supply {

    @SerializedName("Id")
    private int id;

    @SerializedName("Supplier")
    private Supplier supplier;

    @SerializedName("SupplyDate")
    private String supplyDate;

    @SerializedName("Status")
    private String status;

    public int getId()             { return id; }
    public Supplier getSupplier()  { return supplier; }
    public String getSupplyDate()  { return supplyDate; }
    public String getStatus()      { return status; }
}