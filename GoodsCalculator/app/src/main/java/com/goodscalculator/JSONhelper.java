package com.goodscalculator;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONhelper extends AsyncTask<String, String, String> {

    private static final String TAG = JSONhelper.class.getSimpleName();

    public String getJSON(String bar_code) {
        String response = null;
        try {
            URL url = new URL("http://192.168.1.9:777/products/get_product_details.php?bar_code=" + bar_code);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(is);
            return response;
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
            return "false1";
        } catch (IOException e) {
            Log.e(TAG,"IOException: " + e.getMessage());
            return "false2";
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    protected String doInBackground(String... strings) {
        return getJSON(strings[0]);
    }
}
