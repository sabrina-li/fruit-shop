package com.example.helloworld_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld_java.data.FruitListData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements ProductRecyclerViewAdapter.FruitAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private ProductRecyclerViewAdapter mFruitRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_product);

        LinearLayoutManager layoutManager
                =new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mFruitRecyclerViewAdapter = new ProductRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mFruitRecyclerViewAdapter);

        showFruitList();
    }

    private void showFruitList(){
        String urlStr = getString(R.string.aws_host);
        new FetchProductListTask().execute(urlStr);

    }

    @Override
    public void onClick(String fruitName){
        Toast.makeText(this,fruitName,Toast.LENGTH_SHORT)
                .show();
    }

    public class FetchProductListTask extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected JSONArray doInBackground(String... urlStr){
            String jsonRes = null;
            if (urlStr.length > 0) {
                try {
                    URL url = new URL(urlStr[0]);
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

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray productsListArr){

            mFruitRecyclerViewAdapter.setFruitList(productsListArr);
        }


    }
}

