package com.example.vitaminka.models;

public class DrugType {
    private int id;
    private String name;
    private Integer parentId;

    public DrugType(int id, String name, Integer parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public int getId() {
        return id;
    }
}