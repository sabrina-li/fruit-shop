package com.example.helloworld_java;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld_java.data.Product;
import com.example.helloworld_java.utilities.NetworkUtils;
import com.fullstory.FS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductAdapterViewHolder> {

    private ArrayList<Bitmap> mImgBmList = new ArrayList<>();
    private List<Product> mProductsListArr;
    private List<Product> mProducts;

    public interface ProductAdapterHandler {
        void onClick(Product product);
        View createFragmentSpecificView(Product product);
        String buttonText();
    }

    private final ProductAdapterHandler mHandler;

    //constructor
    public ProductRecyclerViewAdapter(ProductAdapterHandler handler){
        mHandler = handler;
    }

    //create view holder
    @Override
    public ProductAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.product_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ProductAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductAdapterViewHolder fruitAdapterViewHolder, int pos){
//        if (mProducts != null) {
//            Product current = mProducts.get(pos);
//            fruitAdapterViewHolder.mFruitTextView.setText(current.title);
//        } else {
//            // Covers the case of data not being ready yet.
//            fruitAdapterViewHolder.mFruitTextView.setText("No Product found in Cart");
//        }
        Product product = mProductsListArr.get(pos);
        if(mImgBmList!=null && mImgBmList.size()>pos){
            Bitmap productImg = mImgBmList.get(pos);
            fruitAdapterViewHolder.mProductImageView.setImageBitmap(productImg);
        }

        fruitAdapterViewHolder.mFruitTextView.setText(product.title);
        fruitAdapterViewHolder.mActionButton.setText(mHandler.buttonText());

        fruitAdapterViewHolder.mFragmentSpecificViewGroup.removeAllViews();
        fruitAdapterViewHolder.mFragmentSpecificViewGroup.addView(mHandler.createFragmentSpecificView(product));
    }

    @Override
    public int getItemCount(){
        if (null == mProductsListArr ) return 0;
        return mProductsListArr.size();
//        if (mProducts != null)
//            return mProducts.size();
//        else return 0;
    }


    public void setProductList(List<Product> products){
        mProductsListArr = products;
        notifyDataSetChanged();
    }

    public void setProductList(List productsListArr, String imgBaseURLStr){
        mProductsListArr = productsListArr;
        try{
            for(int i = 0, count = productsListArr.size(); i< count; i++) {
                Product product = (Product) productsListArr.get(i);
                mProductsListArr.add(product);
                new FetchProductImgTask().execute(imgBaseURLStr+product.image);
            }
        }catch (Exception e){
            //handle exception
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }


    public class FetchProductImgTask extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... urlStr){
            if (urlStr.length > 0) {
                return NetworkUtils.getImageForProduct(urlStr[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap imgBm){
            mImgBmList.add(imgBm);
            notifyDataSetChanged();//probably needs optimize?
        }
    }

    public class ProductAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mFruitTextView;
        public final ImageView mProductImageView;
        public final ViewGroup mFragmentSpecificViewGroup;
        public final Button mActionButton;


        public ProductAdapterViewHolder(View view){
            super(view);

            mFruitTextView = view.findViewById(R.id.tv_product_name);
            mProductImageView = view.findViewById(R.id.iv_product_img);
            mFragmentSpecificViewGroup = view.findViewById(R.id.vg_fragment_specific_view);
            mActionButton = view.findViewById(R.id.btn_product_action);





            FS.addClass(mFruitTextView, FS.UNMASK_CLASS);
            mActionButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            try {
                Product product = mProductsListArr.get(adapterPosition);
                mHandler.onClick(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
