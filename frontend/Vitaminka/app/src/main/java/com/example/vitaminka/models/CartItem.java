package com.example.vitaminka.models;

public class CartItem {
    private int drugId;
    private String name;
    private double oldPrice;
    private double newPrice;
    private int imageResId;
    private int checkResId;
    private boolean isLiked;
    private int count;

    public CartItem(int drugId, String name, double oldPrice, double newPrice, int imageResId, int checkResId, boolean isLiked, int count){
        this.drugId = drugId;
        this.name = name;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.imageResId = imageResId;
        this.checkResId = checkResId;
        this.isLiked = isLiked;
        this.count = count;
    }

    public int getDrugId() {
        return drugId;
    }

    public String getName() {
        return name;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getCheckResId() {
        return checkResId;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
