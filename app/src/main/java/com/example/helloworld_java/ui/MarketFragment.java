package com.example.helloworld_java.ui;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld_java.ProductRecyclerViewAdapter;
import com.example.helloworld_java.R;
import com.example.helloworld_java.data.AppDatabase;
import com.example.helloworld_java.data.Product;
import com.example.helloworld_java.data.ProductDao;
import com.example.helloworld_java.data.ProductRepository;
import com.example.helloworld_java.utilities.NetworkUtils;
import com.fullstory.FS;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment implements ProductRecyclerViewAdapter.ProductAdapterHandler {
    private RecyclerView mRecyclerView;
    private ProductRecyclerViewAdapter mFruitRecyclerViewAdapter;
    private ProductDao mProductDao;
    private ArrayList<Product> mProductsList;
    private MarketViewModel mMarketViewModel;
    AppDatabase db;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mRecyclerView = view.findViewById(R.id.rv_product);
        LinearLayoutManager layoutManager
                =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mFruitRecyclerViewAdapter = new ProductRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mFruitRecyclerViewAdapter);

        db = AppDatabase.getDatabase(getContext());
        mProductDao = db.productDao();

        showFruitList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        mMarketViewModel = ViewModelProviders.of(this).get(MarketViewModel.class);
        return inflater.inflate(R.layout.fragment_market, container, false);

    }


    private void showFruitList(){
        String urlStr = getString(R.string.aws_host);
        new FetchProductListTask().execute(urlStr);
    }

    public String buttonText(){
        return "Add to cart";
    }
    @Override
    public void onClick(Product productObj){
        //add 1lb to cart
        final Product product;
        try {
            product = new Product(productObj.title,productObj.description,productObj.price,productObj.image,productObj.unit,1);
            new AddProductToCartTask().execute(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View createFragmentSpecificView(Product product){
        LinearLayout layout = new LinearLayout(getContext());
        TextView priceTextView = new TextView(getContext());
        priceTextView.setText(product.price.toString()+'/'+product.unit);
        layout.addView(priceTextView);

        return layout;
    }


    private class AddProductToCartTask extends  AsyncTask<Product, Void, String>{
        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected String doInBackground (final Product... products){
            if (products.length > 0) {
                mMarketViewModel.addQuantityInCartByOne(products[0]);
                return products[0].title;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String title){
            Toast t;
            if(title != null){
                t=Toast.makeText(getContext(),title + " added to cart!",Toast.LENGTH_SHORT);
            }else{
                t=Toast.makeText(getContext(),"failed to add to cart!",Toast.LENGTH_SHORT);
            }
            t.show();
            FS.addClass(t.getView(),FS.UNMASK_CLASS);
        }
    }
    private class FetchProductListTask extends AsyncTask<String, Void, ArrayList<Product>> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }//show loading

        @Override
        protected ArrayList<Product> doInBackground(String... urlStr){
            if (urlStr.length > 0) {
                return NetworkUtils.getProductJSONfromURL(urlStr[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> productsListArr){
            if(productsListArr == null || productsListArr.size()==0){
                //show error
            };
            String imgBaseURLStr = getString(R.string.img_host);
            mProductsList = productsListArr;
            mFruitRecyclerViewAdapter.setProductList(productsListArr);
        }
    }
}