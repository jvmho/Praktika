package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class Discount {

    @SerializedName("Id")
    private int id;

    @SerializedName("Percent")
    private int percent;

    @SerializedName("ValidFrom")
    private String validFrom;

    @SerializedName("ValidTo")
    private String validTo;

    public int getId()         { return id; }
    public int getPercent()    { return percent; }
    public String getValidFrom() { return validFrom; }
    public String getValidTo()   { return validTo; }
}