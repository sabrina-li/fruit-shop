package com.example.helloworld_java.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public final class NetworkUtils {

    public static JSONArray getProductJSONfromURL(String urlStr){
        String jsonRes = null;

            try {
                URL url = new URL(urlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();

                    Scanner scanner = new Scanner(in);
                    scanner.useDelimiter("\\A");

                    boolean hasInput = scanner.hasNext();
                    if (hasInput) {
                        jsonRes = scanner.next();
                    } else {
                        return null;
                    }
                } finally {
                    urlConnection.disconnect();
                }

                JSONArray productArr = new JSONArray(jsonRes);

                return productArr;
            } catch (Exception e) {
                //handle exception
                e.printStackTrace();
                return null;
            }
    }

    public static Bitmap getImageForProduct(String urlStr){
        InputStream stream = null;
        URLConnection connection = null;
        Bitmap productImg = null;

        try {

            URL url = new URL(urlStr);
            connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
                productImg = BitmapFactory.
                        decodeStream(stream);
                stream.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return productImg;
    }
}
