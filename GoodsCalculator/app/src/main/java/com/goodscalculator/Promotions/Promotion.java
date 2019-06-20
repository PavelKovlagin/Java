package com.goodscalculator.Promotions;

import android.graphics.Bitmap;

public class Promotion {

    private int id_promotion;
    private String description;
    private String name;
    private String imageURL;
    private String promotionURL;
    private Bitmap image;

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPromotionURL() {
        return promotionURL;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public String toString() {
        return this.id_promotion + " " + this.name + " " + this.description + " " + this.imageURL + " " + this.promotionURL;
    }
}
