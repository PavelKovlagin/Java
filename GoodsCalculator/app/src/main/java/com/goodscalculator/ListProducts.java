package com.goodscalculator;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListProducts {

    private static final String TAG = ListProducts.class.getSimpleName();

    private ArrayList<Product> listProduct = new ArrayList<Product>();

    public ArrayList<Product> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
    }

    public String addProduct(String bar_code) {

        try {
            JSONhelper json = new JSONhelper();
            json.execute(bar_code);
            String JSONstr = JSONstr = json.get();
            Log.i("JSONstr: ", JSONstr);
            if (JSONstr.equals("false\n")) {
                return "Товар не найден";
            } else {
                Gson gson = new Gson();
                Product product = new Product();
                product = gson.fromJson(JSONstr, Product.class);
                return product.toString();
            }
        } catch (ExecutionException e) {
            Log.e(TAG, "ExecutionException: " + e.getMessage());
            return null;
        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException: " + e.getMessage());
            return null;
        }
    }
}
