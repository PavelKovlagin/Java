package com.goodscalculator.Servers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public int getId_server() {
        return id_server;
    }

    public void setId_server(int id_server) {
        this.id_server = id_server;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_adress) {
        this.ip_address = ip_adress;
    }

    public String getPromotionsLink() {
        return promotionsLink;
    }

    public void setPromotionsLink(String promotionsLink) {
        this.promotionsLink = promotionsLink;
    }

    public String getProductDetailsLink() {
        return productDetailsLink;
    }

    public void setProductDetailsLink(String productDetailsLink) {
        this.productDetailsLink = productDetailsLink;
    }

    public String getProductsLink() {
        return productsLink;
    }

    public void setProductsLink(String productsLink) {
        this.productsLink = productsLink;
    }

    @Override
    public String toString() {
        return this.name + " " +
                this.ip_address + " " +
                this.productDetailsLink + " " +
                this.productsLink + " " +
                this.promotionsLink;
    }
}
