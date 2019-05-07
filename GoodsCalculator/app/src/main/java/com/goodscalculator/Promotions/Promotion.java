package com.goodscalculator.Promotions;

import android.graphics.Bitmap;

public class Promotion {

    private int id_promotion;
    private String description;
    private String name;
    private String imageURL;
    private Bitmap image;

    public int getId_promotion() {
        return id_promotion;
    }

    public void setId_promotion(int id_promotion) {
        this.id_promotion = id_promotion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public String toString() {
        return this.id_promotion + " " + this.name + " " + this.description + " " + this.imageURL;
    }
}
