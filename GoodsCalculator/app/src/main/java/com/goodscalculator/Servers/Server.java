package com.goodscalculator.Servers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.goodscalculator.JSONhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class Server{
    private int id_server;
    private String name;
    private String ip_address;
    private String productDetailsLink;
    private String promotionsLink;
    private String productsLink;

    public String getJSONfromServer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }

    public boolean loadServerFromFile(Context context) {
        SharedPreferences sPref = context.getSharedPreferences("mySettings", MODE_PRIVATE);
        String serverJSON = sPref.getString("Server", "");
        if (serverJSON.equals("")) {
            Log.i("ProductServerload", "false load " + serverJSON);
            return false;
        } else {
            this.getServerFromJSON(serverJSON);
            Log.i("ProductServerLoad", serverJSON);
            return true;
        }
    }

    public void saveServerToFile(Context context) {
        SharedPreferences sPref = context.getSharedPreferences("mySettings", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        String JSONserver = this.getJSONfromServer();
        ed.putString("Server", JSONserver);
        ed.putString("ProductsCollection", "[]");
        ed.commit();
        Log.i("ServerAct, Server saved", this.toString());
    }

    public void getServerFromJSON(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Server server = new Server();
        server = gson.fromJson(json, Server.class);
        this.id_server = server.id_server;
        this.ip_address = server.ip_address;
        this.name = server.name;
        this.promotionsLink = server.promotionsLink;
        this.productsLink = server.productsLink;
        this.productDetailsLink = server.productDetailsLink;
    }

    public boolean getServerFromDB(String host, String serverLink, int id_server) {
        try {
            JSONhelper jh = new JSONhelper();
            String request = "http://" + host + serverLink + "?id_server=" + id_server;
            jh.execute(request);
            String JSONresponse = jh.get();
            if (!JSONresponse.equals("false\n")) {
                this.getServerFromJSON(JSONresponse);
                Log.i(String.valueOf(this.getClass()), this.toString());
                return true;
            } else {
                Log.e(String.valueOf(this.getClass()), "false");
                return false;
            }
        } catch (ExecutionException e) {
            Log.e(String.valueOf(this.getClass()), e.getMessage());
            return false;
        } catch (InterruptedException e) {
            Log.e(String.valueOf(this.getClass()), e.getMessage());
            return false;
        }

    }

    public int getId_server() {
        return id_server;
    }

    public String getName() {
        return name;
    }

    public String getIp_address() {
        return ip_address;
    }

    public String getPromotionsLink() {
        return promotionsLink;
    }

    public String getProductDetailsLink() {
        return productDetailsLink;
    }

    @Override
    public String toString() {
        return "Название сервера: " + this.name + "\n" +
                "IP адрес: " + this.ip_address + "\n" +
                "Ссылка продукта: " + this.productDetailsLink + "\n" +
                "Ссылка продуктоа: " +this.productsLink + "\n" +
                "Ссылка акций" + this.promotionsLink;
    }
}
