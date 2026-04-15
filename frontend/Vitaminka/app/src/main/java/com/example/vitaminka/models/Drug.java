package com.example.vitaminka.models;

public class Drug {
    private int id;
    private String name;
    private String category;
    private String description;
    private String INN;
    private int typeId;
    private String dose;
    private int manufacturerID;
    private String barcode;

    public Drug(int id, String name, String description,  int drugTypeId) {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getINN() {
        return INN;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getDose() {
        return dose;
    }

    public int getManufacturerID() {
        return manufacturerID;
    }

    public String getBarcode() {
        return barcode;
    }
}