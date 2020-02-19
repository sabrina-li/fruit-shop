package com.example.helloworld_java;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    private ArrayList<String>  mProductNameList = new ArrayList<>();
    private ArrayList<Bitmap> mImgBmList = new ArrayList<>();
    private List<Product> mProductsListArr;

    private final FruitAdapterOnClickHandler mClickHandler;

    public interface FruitAdapterOnClickHandler {
        void onClick(Product product);
    }

    //constructor
    public ProductRecyclerViewAdapter(FruitAdapterOnClickHandler clickHander){
        mClickHandler = clickHander;
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
        String productName = mProductNameList.get(pos);
        Bitmap productImg = mImgBmList.get(pos);
        fruitAdapterViewHolder.mFruitTextView.setText(productName);
        fruitAdapterViewHolder.mProductImageView.setImageBitmap(productImg);
    }

    @Override
    public int getItemCount(){
        if (null == mProductNameList || null ==mImgBmList) return 0;
        return Math.min(mProductNameList.size(),mImgBmList.size());
    }

    public void setFruitList(List productsListArr, String imgBaseURLStr){
        mProductsListArr = productsListArr;
        try{
            for(int i = 0, count = productsListArr.size(); i< count; i++) {
                Product product = (Product) productsListArr.get(i);
                mProductNameList.add(product.title);
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

        public ProductAdapterViewHolder(View view){
            super(view);
            mFruitTextView = (TextView) view.findViewById(R.id.tv_product_name);
            mProductImageView = (ImageView) view.findViewById(R.id.iv_product_img);
            FS.addClass(mFruitTextView, FS.UNMASK_CLASS);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            try {
                Product product = (Product) mProductsListArr.get(adapterPosition);
                mClickHandler.onClick(product);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
