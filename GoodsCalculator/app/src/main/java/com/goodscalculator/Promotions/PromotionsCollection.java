package com.goodscalculator.Promotions;

import android.graphics.Bitmap;
import android.util.Log;

import com.goodscalculator.DownloadImage;
import com.goodscalculator.JSONhelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class PromotionsCollection extends ArrayList<Promotion> {

    private ArrayList<Promotion> promotionsCollection = new ArrayList<Promotion>();
    private final String TAG = this.getClass().getSimpleName();

    public int getSize() {
        return promotionsCollection.size();
    }

    public Bitmap getImage(int index) {
        return promotionsCollection.get(index).getImage();
    }

    public String getDescription(int index) {
        return  promotionsCollection.get(index).getDescription();
    }

    public String getPromotionURL(int index) {
        return promotionsCollection.get(index).getPromotionURL();
    }

    public boolean addPromotions(String host, String promotionsLink) {
        try {
            JSONhelper json = new JSONhelper();
            json.execute("http://" + host + promotionsLink);
            String JSONstr = json.get();
            Log.i("JSONstr", JSONstr);
            if (JSONstr.equals("false\n")) {
                return false;
            } else {
                Gson gson = new Gson();
                promotionsCollection = gson.fromJson(JSONstr, PromotionsCollection.class);
                for (Promotion promotion : promotionsCollection) {
                    try {
                        DownloadImage downloadImage = new DownloadImage();
                        downloadImage.execute(promotion.getImageURL());
                        promotion.setImage(downloadImage.get());
                    } catch (ExecutionException e) {
                        Log.e(TAG, "addPromotions, downloadImage, ExecutionException: " + e.getMessage());
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        Log.e(TAG, "addPromotions, downloadImage, InterruptedException: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                return true;
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "addPromotions, InterruptedException: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            Log.e(TAG, "addPromotions, ExecutionException: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getPromotionCollectionString() {
        String promotionString = "";
        for (Promotion promotion : promotionsCollection) {
            promotionString = promotionString + promotion.toString() + "\n";
        }
        return  promotionString;
    }
}
