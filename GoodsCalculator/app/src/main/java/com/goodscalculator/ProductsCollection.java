package com.goodscalculator;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class ProductsCollection {

    private static final String TAG = ProductsCollection.class.getSimpleName();

    private ArrayList<Product> productsCollection = new ArrayList<Product>();

    private double totalAmount = 0;

    private void countingTotalAmount() {
        double totalAmount = 0;
        for (Product productCol : productsCollection) {
            totalAmount = totalAmount + productCol.getPrice();
        }
        this.totalAmount = totalAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getProductCollectionString() {
        String collection = "";
        for (Product productCol : productsCollection) {
            collection = collection + productCol.getName() + " " + productCol.getPrice() + "\n";
        }
        return collection;
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
                countingTotalAmount();
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
