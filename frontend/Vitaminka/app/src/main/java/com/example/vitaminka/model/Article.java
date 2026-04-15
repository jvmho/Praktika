package com.example.vitaminka.model;

public class Article {
    private String text;
    private int imageResId;

    public Article(String text, int imageResId)
    {
        this.text = text;
        this.imageResId = imageResId;
    }

    public String getText() {
        return text;
    }

    public int getImageResId() {
        return imageResId;
    }
}
