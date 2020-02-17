package com.example.helloworld_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.helloworld_java.utilities.NetworkUtils;
import com.fullstory.FS;
import com.fullstory.FSSessionData;

import org.json.JSONArray;

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
        String sessionURL = new FSSessionData().getCurrentSessionURL();
        Log.d("fullstory",String.valueOf(sessionURL));
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
            if (urlStr.length > 0) {
                return NetworkUtils.getProductJSONfromURL(urlStr[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray productsListArr){
            String imgBaseURLStr = getString(R.string.img_host);
            mFruitRecyclerViewAdapter.setFruitList(productsListArr,imgBaseURLStr);
        }


    }
}

