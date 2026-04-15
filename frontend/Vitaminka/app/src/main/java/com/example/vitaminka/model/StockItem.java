package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class StockItem {

    // ── используется внутри Batch ──────────────────────────────
    @SerializedName("WarehouseId")
    private int warehouseId;

    // ── используется в /stock ──────────────────────────────────
    @SerializedName("Warehouse")
    private Warehouse warehouse;

    @SerializedName("BatchId")
    private int batchId;

    @SerializedName("Amount")
    private int amount;

    public int getWarehouseId()    { return warehouseId; }
    public Warehouse getWarehouse() { return warehouse; }
    public int getBatchId()        { return batchId; }
    public int getAmount()         { return amount; }
}
