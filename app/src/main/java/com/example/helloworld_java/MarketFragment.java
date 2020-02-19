package com.example.helloworld_java;

import android.app.Application;
import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld_java.data.AppDatabase;
import com.example.helloworld_java.data.Product;
import com.example.helloworld_java.data.ProductDao;
import com.example.helloworld_java.utilities.NetworkUtils;
import com.fullstory.FS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment implements ProductRecyclerViewAdapter.FruitAdapterOnClickHandler{
    private RecyclerView mRecyclerView;
    private ProductRecyclerViewAdapter mFruitRecyclerViewAdapter;
    private ProductDao mProductDao;



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_product);
        LinearLayoutManager layoutManager
                =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mFruitRecyclerViewAdapter = new ProductRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mFruitRecyclerViewAdapter);

        AppDatabase db = AppDatabase.getDatabase(getContext());
        mProductDao = db.productDao();

        showFruitList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_market, container, false);
    }


    private void showFruitList(){
        String urlStr = getString(R.string.aws_host);
        new FetchProductListTask().execute(urlStr);
    }

    @Override
    public void onClick(Product productObj){
        try {
            Toast.makeText(getContext(),productObj.title,Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //add 1lb to cart
        final Product product;
        try {
            product = new Product(productObj.title,productObj.description,productObj.price,productObj.image,productObj.unit,1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        mProductDao.insertAll(product);//TODO: this should be update count in cart, but this will do for now for building cart ui
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class FetchProductListTask extends AsyncTask<String, Void, ArrayList<Product>> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Product> doInBackground(String... urlStr){
            if (urlStr.length > 0) {
                return NetworkUtils.getProductJSONfromURL(urlStr[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> productsListArr){
            String imgBaseURLStr = getString(R.string.img_host);
            mFruitRecyclerViewAdapter.setFruitList(productsListArr,imgBaseURLStr);
        }
    }
}