package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class DrugDetail {

    @SerializedName("Name")
    private String name;

    @SerializedName("Category")
    private String category;

    @SerializedName("Description")
    private String description;

    @SerializedName("Inn")
    private String inn;

    @SerializedName("TypeId")
    private int typeId;

    @SerializedName("Dose")
    private String dose;

    @SerializedName("ManufacturerId")
    private int manufacturerId;

    @SerializedName("Barcode")
    private long barcode;

    public String getName()        { return name; }
    public String getCategory()    { return category; }
    public String getDescription() { return description; }
    public String getInn()         { return inn; }
    public int getTypeId()         { return typeId; }
    public String getDose()        { return dose; }
    public int getManufacturerId() { return manufacturerId; }
    public long getBarcode()       { return barcode; }
}