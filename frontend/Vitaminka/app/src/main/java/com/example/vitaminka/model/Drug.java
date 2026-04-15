package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class Drug {

    @SerializedName("Id")
    private int id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Category")
    private String category;

    @SerializedName("Inn")
    private String inn;

    @SerializedName("Dose")
    private String dose;

    @SerializedName("Manufacturer")
    private Manufacturer manufacturer;

    @SerializedName("TypeId")
    private int type;

    public int getId()                  { return id; }
    public String getName()             { return name; }
    public String getCategory()         { return category; }
    public String getInn()              { return inn; }
    public String getDose()             { return dose; }
    public Manufacturer getManufacturer() { return manufacturer; }
    public int getTypeId()           { return type; }
}