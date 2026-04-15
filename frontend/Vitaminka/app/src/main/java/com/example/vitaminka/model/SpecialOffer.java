package com.example.vitaminka.model;

public class SpecialOffer {
    private int colorRes; // ресурс цвета (R.color.some_color)

    public SpecialOffer(int colorRes) {
        this.colorRes = colorRes;
    }

    public int getColorRes() {
        return colorRes;
    }
}