package com.goodscalculator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = JSONhelper.class.getSimpleName();

    private Bitmap downloadImage(String request) {
        Bitmap image = null;
        try {
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            InputStream in = url.openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "DoanloadeImage. MalformedURLException:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "DownloadImage. IOException" + e.getMessage());
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        return downloadImage(urls[0]);
    }
}
