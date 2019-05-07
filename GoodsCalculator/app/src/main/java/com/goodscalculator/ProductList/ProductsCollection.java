package com.goodscalculator.ProductList;

import android.util.Log;

import com.goodscalculator.JSONhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ProductsCollection extends ArrayList<Product> {

    private static final String TAG = ProductsCollection.class.getSimpleName();

    private ArrayList<Product> productsCollection = new ArrayList<Product>();

    public double countingTotalAmount() {
        double totalAmount = 0;
        for (Product productCol : productsCollection) {
            totalAmount = totalAmount + productCol.getPrice();
        }
        return totalAmount;
    }

    public String getProductCollectionString() {
        String collection = "";
        for (Product productCol : productsCollection) {
            collection = collection + productCol.getName() + " " + productCol.getPrice() + "\n";
        }
        return collection;
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

    public boolean addProduct(String host, String bar_code) {

        try {
            JSONhelper json = new JSONhelper();
            json.execute("http://" + host + "/products/get_product_details.php?bar_code=" + bar_code);
            String JSONstr = json.get();
            Log.i("JSONstr: ", JSONstr);
            if (JSONstr.equals("false\n")) {
                return false;
            } else {
                Gson gson = new Gson();
                Product product = new Product();
                product = gson.fromJson(JSONstr, Product.class);
                productsCollection.add(product);
                return true;
            }
        } catch (ExecutionException e) {
            Log.e(TAG, "addProduct, ExecutionException: " + e.getMessage());
            return false;
        } catch (InterruptedException e) {
            Log.e(TAG, "addProduct, InterruptedException: " + e.getMessage());
            return false;
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
