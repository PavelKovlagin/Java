package com.goodscalculator.ProductList;

import android.content.SharedPreferences;
import android.util.Log;

import com.goodscalculator.JSONhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ProductsCollection extends ArrayList<Product> {

    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<Product> productsCollection = new ArrayList<Product>();
    private SharedPreferences sPref;

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
