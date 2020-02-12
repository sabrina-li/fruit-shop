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

public class MainActivity extends AppCompatActivity implements FruitRecyclerViewAdapter.FruitAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private FruitRecyclerViewAdapter mFruitRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_fruit);

        LinearLayoutManager layoutManager
                =new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mFruitRecyclerViewAdapter = new FruitRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mFruitRecyclerViewAdapter);

        showFruitList();
    }

    private void showFruitList(){
        String urlStr = "http://reactshoppe.s3-website-us-east-1.amazonaws.com/data/products.json";
        new FetchProductListTask().execute(urlStr);

    }

    @Override
    public void onClick(String fruitName){
        Toast.makeText(this,fruitName,Toast.LENGTH_SHORT)
                .show();
    }

    public class FetchProductListTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected ArrayList<String> doInBackground(String... urlStr){
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

                    ArrayList<String> stringArray = new ArrayList<>();

                    for(int i = 0, count = productArr.length(); i< count; i++) {
                        JSONObject jsonObject = productArr.getJSONObject(i);
                        stringArray.add(jsonObject.getString("title"));
                    }

                    return stringArray;
                } catch (Exception e) {
                    //handle exception
                    e.printStackTrace();
                    return null;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> productsList){
            mFruitRecyclerViewAdapter.setFruitList(productsList);
        }


    }
}
