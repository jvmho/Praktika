package com.example.vitaminka.model;
import com.google.gson.annotations.SerializedName;

public class Role {

    @SerializedName("Id")
    private int id;

    @SerializedName("Name")
    private String name;

    public int getId() { return id; }
    public String getName() { return name; }
}