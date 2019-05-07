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

    public int getSize() {
        return promotionsCollection.size();
    }

    public Bitmap getImage(int index) {
        return promotionsCollection.get(index).getImage();
    }

    public Bitmap randomImage() {
        Random random = new Random();
        int index = random.nextInt(promotionsCollection.size());
        return promotionsCollection.get(index).getImage();
    }

    public boolean addPromotions(String host) {
        try {
            JSONhelper json = new JSONhelper();
            json.execute("http://" + host + "/products/get_promotions.php");
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
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
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
