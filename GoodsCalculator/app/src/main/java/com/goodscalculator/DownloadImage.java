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

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon = null;
        try {
            URL url = new URL(urldisplay);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            InputStream in = url.openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            Log.e("MalformedURLException", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
            e.printStackTrace();
        }
        return mIcon;
    }
}
