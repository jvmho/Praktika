package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class DrugType {

    @SerializedName("Id")
    private int id;

    @SerializedName("Name")
    private String name;

    @SerializedName("ParentId")
    private Integer parentId;

    public int getId()     { return id; }
    public String getName() { return name; }
    public Integer getParentId() { return parentId; }
}