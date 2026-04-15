package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Cart {
    @SerializedName("Id")
    private int id;
    @SerializedName("UserId")
    private int userId;
    @SerializedName("CreatedAt")
    private String createdAt;
    @SerializedName("UpdatedAt")
    private String updatedAt;
    @SerializedName("Status")
    private String status;

    // геттеры
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public String getStatus() { return status; }
}