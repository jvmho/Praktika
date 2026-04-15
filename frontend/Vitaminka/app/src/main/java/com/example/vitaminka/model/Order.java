package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Order {

    @SerializedName("Id")
    private int id;

    @SerializedName("Status")
    private String status;

    @SerializedName("DeliveryType")
    private String deliveryType;

    @SerializedName("TotalAmount")
    private double totalAmount;

    @SerializedName("CreatedAt")
    private String createdAt;

    @SerializedName("Items")
    private List<OrderItem> items;

    public int getId()               { return id; }
    public String getStatus()        { return status; }
    public String getDeliveryType()  { return deliveryType; }
    public double getTotalAmount()   { return totalAmount; }
    public List<OrderItem> getItems() { return items; }
    public String getCreatedAt()      { return createdAt; }
}