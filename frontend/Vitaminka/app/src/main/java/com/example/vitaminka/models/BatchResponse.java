package com.example.vitaminka.models;

import java.util.List;

public class BatchResponse {
    public int id;
    public int drugId;
    public int number;
    public double price;
    public String shelfLife;
    public List<Stock> stock;

    public double getPrice() { return price; }
    public int getDrugId() { return drugId; }
}

class Stock {
    public int warehouseId;
    public int amount;
}