package com.goodscalculator.Cabinet;

import android.annotation.SuppressLint;
import android.util.Log;

import com.goodscalculator.JSONhelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PurchaseCollection extends ArrayList<Purchase>  {

    private ArrayList<Purchase> purchaseCollection = new ArrayList<Purchase>();

    public ArrayList<Purchase> getPurchaseCollection() {
        return purchaseCollection;
    }

    @SuppressLint("LongLogTag")
    public void getPurchasesFromDB(String host, String purchasesLink, int id_user) {
        try {
            JSONhelper json = new JSONhelper();
            json.execute("http://" + host + purchasesLink + "?id_user=" + id_user);
            String JSONstr = json.get();
            Log.i("JSONstr: ", JSONstr);
            if (JSONstr.equals("false\n")) {
                Log.e("PurchaseCollection, getPurchasesFromDB", "false");
            } else {
                Gson gson = new Gson();
                purchaseCollection = gson.fromJson(JSONstr, PurchaseCollection.class);
            }
        } catch (ExecutionException e) {
            Log.e( "PurchasesCollection, getPurchasesFromDB", e.getMessage());
        } catch (InterruptedException e) {
            Log.e("PurchasesCollection, getPurchasesFromDB", e.getMessage());
        }
    }
}
