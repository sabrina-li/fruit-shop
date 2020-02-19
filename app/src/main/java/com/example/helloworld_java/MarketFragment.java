package com.example.helloworld_java;

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

import com.example.helloworld_java.utilities.NetworkUtils;
import com.fullstory.FS;

import org.json.JSONArray;

public class MarketFragment extends Fragment implements ProductRecyclerViewAdapter.FruitAdapterOnClickHandler{
    private RecyclerView mRecyclerView;
    private ProductRecyclerViewAdapter mFruitRecyclerViewAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_product);
        LinearLayoutManager layoutManager
                =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mFruitRecyclerViewAdapter = new ProductRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mFruitRecyclerViewAdapter);

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
    public void onClick(String fruitName){
        Toast.makeText(getContext(),fruitName,Toast.LENGTH_SHORT)
                .show();
        //add 1lb to cart
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