package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Batch {

    @SerializedName("Id")
    private int id;

    @SerializedName("DrugId")
    private int drugId;

    @SerializedName("Number")
    private int number;

    @SerializedName("Price")
    private double price;

    @SerializedName("ShelfLife")
    private String shelfLife;

    @SerializedName("Stock")
    private List<StockItem> stock;

    public int getId()              { return id; }
    public int getDrugId()          { return drugId; }
    public int getNumber()          { return number; }
    public double getPrice()        { return price; }
    public String getShelfLife()    { return shelfLife; }
    public List<StockItem> getStock() { return stock; }
}