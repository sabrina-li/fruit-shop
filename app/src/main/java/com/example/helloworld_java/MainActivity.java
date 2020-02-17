package com.example.helloworld_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.helloworld_java.utilities.NetworkUtils;
import com.fullstory.FS;
import com.fullstory.FSSessionData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity implements ProductRecyclerViewAdapter.FruitAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private BottomNavigationView mBottomNavView;
    private ProductRecyclerViewAdapter mFruitRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mRecyclerView = (RecyclerView) findViewById(R.id.rv_product);
//
//        LinearLayoutManager layoutManager
//                =new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(layoutManager);
//
//        mRecyclerView.setHasFixedSize(true);
//
//        mFruitRecyclerViewAdapter = new ProductRecyclerViewAdapter(this);
//        mRecyclerView.setAdapter(mFruitRecyclerViewAdapter);
//
//        showFruitList();

        mBottomNavView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                switch (item.getItemId()) {
                    case R.id.nav_market:
                        Log.d("Mainactivity","Market");
                        break;
                    case R.id.nav_cart:
                        Log.d("Mainactivity","Cart");
                        break;
                }
                return true;
            }
        });

    }

    private void showFruitList(){
        String urlStr = getString(R.string.aws_host);
        new FetchProductListTask().execute(urlStr);
    }

    @Override
    public void onClick(String fruitName){
        Toast.makeText(this,fruitName,Toast.LENGTH_SHORT)
                .show();
        String sessionURL = new FS().getCurrentSessionURL();
        Log.d("MainActivity","fullstory sessionurl "+sessionURL);
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

