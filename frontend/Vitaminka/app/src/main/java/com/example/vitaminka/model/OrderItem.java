package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class OrderItem {

    @SerializedName("Drug")
    private String drug;

    @SerializedName("Quantity")
    private int quantity;

    @SerializedName("Price")
    private double price;

    public String getDrug()  { return drug; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}