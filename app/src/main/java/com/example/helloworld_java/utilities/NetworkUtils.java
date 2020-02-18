package com.example.helloworld_java.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public final class NetworkUtils {

    public static JSONArray getProductJSONfromURL(String urlStr){
        String jsonResStr = null;
        InputStream stream = null;
        HttpURLConnection connection = null;
        Scanner scanner = null;
        JSONArray productArr = null;

        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = connection.getInputStream();
                scanner = new Scanner(stream);
                scanner.useDelimiter("\\A");
                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    jsonResStr = scanner.next();
                }
            }

            productArr = new JSONArray(jsonResStr);
        } catch (Exception e) {
            //handle exception
            e.printStackTrace();
        }finally {
            try{
                scanner.close();
                stream.close();
                connection.disconnect();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return productArr;
    }

    public static Bitmap getImageForProduct(String urlStr){
        InputStream stream = null;
        HttpURLConnection connection = null;
        Bitmap productImg = null;

        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = connection.getInputStream();
                productImg = BitmapFactory.
                        decodeStream(stream);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try{
                stream.close();
                connection.disconnect();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return productImg;
    }
}
