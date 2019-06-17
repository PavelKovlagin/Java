package com.goodscalculator.ProductList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.goodscalculator.JSONhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class ProductsCollection extends ArrayList<Product> {

    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<Product> productsCollection = new ArrayList<Product>();

    public  int getSize() {
        return productsCollection.size();
    }

    public void clearCollection() {
        productsCollection.clear();
    }

    public double countingTotalAmount() {
        double totalAmount = 0;
        for (Product productCol : productsCollection) {
            totalAmount = totalAmount + productCol.getPrice();
        }
        return Math.round(totalAmount *100.00) / 100.00;
    }

    public void loadProductsCollectionsFromFile(Context context) {
        SharedPreferences sPref = context.getSharedPreferences("mySettings", MODE_PRIVATE);
        String productsCollectionJSON = sPref.getString("ProductsCollection", "[]");
        Log.i("GSON load", productsCollectionJSON);
        this.getProductsCollectionFromJSON(productsCollectionJSON);
    }

    public void saveProductsCollectionToFile(Context context) {
        SharedPreferences sPref = context.getSharedPreferences("mySettings", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        String productsCollectionJSON = this.getJSONfromProductsCollection();
        ed.putString("ProductsCollection", productsCollectionJSON);
        ed.commit();
        Log.i("GSON saved", productsCollectionJSON);
    }

    public String getJSONfromProductsCollection() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(productsCollection);
    }

    public void getProductsCollectionFromJSON(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        productsCollection = gson.fromJson(json, ProductsCollection.class);
    }

    public ArrayList<Product> getProductsCollection() {
        return productsCollection;
    }

    public boolean addProduct(String host, String productLink, String bar_code) {
        if (bar_code.substring(0,1).equals("2")) {
           Log.i(TAG, "addProduct: addWeightProduct");
           return addWeightProduct(host, productLink, bar_code);
        } else {
            Log.i(TAG, "addProduct: addUsualProduct");
            return addUsualProduct(host, productLink, bar_code);
        }
    }

    private boolean addWeightProduct(String host, String productLink, String bar_code) {
        String productCode = bar_code.substring(2,7);
        int productWeight = Integer.parseInt(bar_code.substring(7,12));
        Log.i(TAG, "addWeightProduct: productcode = " + productCode + ", productWeight = " + productWeight);
        Product product = getProductFromDB(host, productLink, productCode);
        if (product != null) {
            Log.i(TAG,"addWeightProduct: " + productWeight + "*" + product.getPrice() + "/" + "1000");
            double price = (productWeight * product.getPrice()) / 1000;
            product.setPrice(Math.round(price *100.00) / 100.00);
            productsCollection.add(product);
            return true;
        } else {
            return false;
        }
    }

    private boolean addUsualProduct(String host, String productLink, String bar_code) {
        Product product = getProductFromDB(host, productLink, bar_code);
        if (product != null) {
            productsCollection.add(product);
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("LongLogTag")
    public void savePurchase(String host, String insertProductsLink, int id_user) {
        try {
            JSONhelper jsoNhelper = new JSONhelper();
            Date dateNow = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
            String purchaseDate = simpleDateFormat.format(dateNow);
            String productsJSON = this.getJSONfromProductsCollection();
            double amount = this.countingTotalAmount();
            Log.i("ProductsCollections, savePurchase","http://" + host + insertProductsLink + "?id_user="+id_user+"&productsJSON="+productsJSON+"&purchaseDate="+purchaseDate+"&amount="+amount);
            jsoNhelper.execute("http://" + host + insertProductsLink + "?id_user="+id_user+"&productsJSON="+productsJSON+"&purchaseDate="+purchaseDate+"&amount="+amount);
            jsoNhelper.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Product getProductFromDB(String host, String productLink, String bar_code) {
        try {
            JSONhelper json = new JSONhelper();
            json.execute("http://" + host + productLink + bar_code);
            String JSONstr = json.get();
            Log.i("JSONstr: ", JSONstr);
            if (JSONstr.equals("false\n")) {
                return null;
            } else {
                Gson gson = new Gson();
                Product product;
                product = gson.fromJson(JSONstr, Product.class);;
                return product;
            }
        } catch (ExecutionException e) {
            Log.e(TAG, "addProduct, ExecutionException: " + e.getMessage());
            return null;
        } catch (InterruptedException e) {
            Log.e(TAG, "addProduct, InterruptedException: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteProduct(String bar_code) {
        for (Product productCol : productsCollection) {
            if (productCol.getBar_code().equals(bar_code)) {
                productsCollection.remove(productCol);
                return true;
            }
        }
        return false;
    }
}
