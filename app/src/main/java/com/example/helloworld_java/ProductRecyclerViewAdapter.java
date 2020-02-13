package com.example.helloworld_java;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductAdapterViewHolder> {

    private ArrayList<String>  mProductNameList = new ArrayList<>();
    private ArrayList<String>  mImgArray = new ArrayList<>();

    private final FruitAdapterOnClickHandler mClickHandler;

    private String imgBaseURLStr;
    private Bitmap productImg;

    public interface FruitAdapterOnClickHandler {
        void onClick(String fruit);
    }

    //constructor
    public ProductRecyclerViewAdapter(FruitAdapterOnClickHandler clickHander){
        mClickHandler = clickHander;
    }

    //create view holder
    @Override
    public ProductAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.fruit_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        imgBaseURLStr = viewGroup.getContext().getString(R.string.img_host);

        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachToParentImmediately);

        return new ProductAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductAdapterViewHolder fruitAdapterViewHolder, int pos){
        final int position = pos;
        String fruitName = mProductNameList.get(position);

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream stream = null;
                URLConnection connection = null;

                try {

                    URL url = new URL(imgBaseURLStr+mImgArray.get(position));
                    connection = url.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    httpConnection.setRequestMethod("GET");
                    httpConnection.connect();

                    if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        stream = httpConnection.getInputStream();
                        productImg = BitmapFactory.
                                decodeStream(stream);
                        stream.close();
                        fruitAdapterViewHolder.mProductImageView.setImageBitmap(productImg);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();


        fruitAdapterViewHolder.mFruitTextView.setText(fruitName);
    }

    @Override
    public int getItemCount(){
        if (null == mProductNameList) return 0;
        return mProductNameList.size();
    }

    public void setFruitList(JSONArray productsListArr){

            try{
                for(int i = 0, count = productsListArr.length(); i< count; i++) {
                    JSONObject jsonObject = productsListArr.getJSONObject(i);
                    mProductNameList.add(jsonObject.getString("title"));
                    mImgArray.add(jsonObject.getString("image"));
                }
            }catch (Exception e){
                //handle exception
                e.printStackTrace();
            }

        notifyDataSetChanged();
    }




    public class ProductAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mFruitTextView;
        public final ImageView mProductImageView;


        public ProductAdapterViewHolder(View view){
            super(view);
            mFruitTextView = (TextView) view.findViewById(R.id.tv_product_name);
            mProductImageView = (ImageView) view.findViewById(R.id.iv_product_img);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String fruitName = mProductNameList.get(adapterPosition);
            mClickHandler.onClick(fruitName);
        }
    }

}
