package com.example.vitaminka;


public class Product {
    private String name;
    private double newPrice;
    private double oldPrice;
    private boolean isOnSale;
    private boolean isNew;
    private int imageResId;
    private boolean isLiked;

    public Product(String name, double newPrice, double oldPrice, boolean isOnSale, boolean isNew, int imageResId) {
        this.name = name;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.isOnSale = isOnSale;
        this.isNew = isNew;
        this.imageResId = imageResId;
    }
    public Product(String name, double newPrice, double oldPrice, boolean isOnSale, int imageResId, boolean isLiked) {
        this.name = name;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.isOnSale = isOnSale;
        this.imageResId = imageResId;
        this.isLiked = isLiked;
    }

    public String getName() {
        return name;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public boolean isNew() {
        return isNew;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isItLiked() {
        return isLiked;
    }
    public void setItLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}
