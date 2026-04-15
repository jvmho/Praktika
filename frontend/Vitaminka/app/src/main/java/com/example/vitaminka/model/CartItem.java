package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("Id")
    private int id;
    @SerializedName("CartId")
    private int cartId;
    @SerializedName("DrugId")
    private int drugId;
    @SerializedName("Quantity")
    private int quantity;

    // геттеры
    public int getId() { return id; }
    public int getCartId() { return cartId; }
    public int getDrugId() { return drugId; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}