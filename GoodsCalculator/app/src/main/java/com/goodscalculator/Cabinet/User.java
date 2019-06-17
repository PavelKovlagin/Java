package com.goodscalculator.Cabinet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.goodscalculator.JSONhelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class User {

    private int id_user;
    private String login;
    @SerializedName("password")
    private String hashPassword;

    public User() {

    }

    public User(String login, String password) {
        this.login = login;
        this.hashPassword = getShalHex(password);
    }

    public int getId_user() {
        return id_user;
    }

    public String getLogin() {
        return login;
    }

    @SuppressLint("LongLogTag")
    public boolean loadUserFromFile(Context context) {
        SharedPreferences sPref = context.getSharedPreferences("mySettings", MODE_PRIVATE);
        String userJSON = sPref.getString("user", "");
        if (!userJSON.equals("")) {
            this.getUserFromJSON(userJSON);
            Log.i("User, loadUserFromFile", userJSON);
            return true;
        } else {
            Log.e("User, loadUserFromFile", "false");
            return false;
        }
    }

    public void saveUserToFile(Context context) {
        SharedPreferences sPref = context.getSharedPreferences("mySettings", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        String userJSON = this.getJSONfromUser();
        ed.putString("user", userJSON);
        ed.commit();
        Log.i("User, saveUserToFile", this.toString());
    }

    public void logOut(Context context){
        SharedPreferences sPref = context.getSharedPreferences("mySettings", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("user", "");
        ed.commit();
        Log.i("User, logOut", "LogOut");
    }

    public String getShalHex(String password) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(password.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            Log.i("User, getShalHex", builder.toString());
            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getJSONfromUser() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }

    public void getUserFromJSON(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        User user = new User();
        user = gson.fromJson(json, User.class);
        this.id_user = user.id_user;
        this.login = user.login;
        this.hashPassword = user.hashPassword;
    }

    public boolean doRegister(String host, String registerLink) {
        try {
            JSONhelper jsoNhelper = new JSONhelper();
            jsoNhelper.execute("http://" + host + registerLink + "?login=" + this.login + "&password=" + this.hashPassword);
            String JSONstring = jsoNhelper.get();
            if (JSONstring.equals("false\n")) {
                Log.e("User, doRegister", "false");
                return false;
            } else {
                Log.i("User, doRegister", JSONstring);
                return true;
            }
        } catch (InterruptedException e) {
            Log.e("User, doRegister", "InterruptedException");
            return false;
        } catch (ExecutionException e) {
            Log.e("User, doRegister", "ExecutionException");
            return false;
        }
    }

    public boolean doLogin(String host, String usersLink) {
        try {
            JSONhelper jsonHelper = new JSONhelper();
            jsonHelper.execute("http://" + host + usersLink + "?login=" + this.login + "&password=" + this.hashPassword);
            String JSONstring = jsonHelper.get();
            if (JSONstring.equals("false\n")) {
                Log.e("User, doLogin", JSONstring);
                return false;
            } else {
                Log.i("User, doLogin", JSONstring);
                this.getUserFromJSON(JSONstring);
                return true;
            }
        } catch (ExecutionException e) {
            Log.e("User, doLogin", "ExecutionException");
            return false;
        } catch (InterruptedException e) {
            Log.e("User, doLogin", "InterruptedException");
            return true;
        }
    }

    @Override
    public String toString() {
        return "id_user: " + id_user + ", Login: " + this.login + ", password: " + hashPassword;
    }
}
