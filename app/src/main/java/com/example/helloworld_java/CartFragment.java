package com.example.helloworld_java;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld_java.data.AppDatabase;
import com.example.helloworld_java.data.Product;
import com.example.helloworld_java.data.ProductDao;
import com.example.helloworld_java.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements ProductRecyclerViewAdapter.ProductAdapterHandler {
    private RecyclerView mRecyclerView;
    private ProductRecyclerViewAdapter mFruitRecyclerViewAdapter;
    private ProductDao mProductDao;
    AppDatabase db;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_cart);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mFruitRecyclerViewAdapter = new ProductRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mFruitRecyclerViewAdapter);

        db = AppDatabase.getDatabase(getContext());
        mProductDao = db.productDao();

        showCartList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }


    private void showCartList() {
        new FetchCartFromDBTask().execute();//fetch all from DB
    }

    @Override
    public void onClick(Product productObj) {
        try {
            Toast.makeText(getContext(), productObj.title, Toast.LENGTH_SHORT)
                    .show();
            //TODO: show menu for delete, changing quantity etc.
        } catch (Exception e) {
            e.printStackTrace();
        }


        final Product product;
        try {
            product = new Product(productObj.title,productObj.description,productObj.price,productObj.image,productObj.unit,1);
            new RemoveProductFromCartTask().execute(product);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View createFragmentSpecificView(Product product){
        LinearLayout layout = new LinearLayout(getContext());

        TextView count = new TextView(getContext());
        count.setText(product.quantityInCart + " in cart");
        layout.addView(count);

        return layout;
    }

    @Override
    public String buttonText(){
        return getString(R.string.remove_from_cart);
    }

    private class RemoveProductFromCartTask extends  AsyncTask<Product, Void, Product>{
        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected Product doInBackground (final Product... products){
            if (products.length > 0) {
                //transaction here
                db.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        mProductDao.delete(products[0]);//Need live data here
                    }
                });
                return products[0];
            }
            return null;
        }

        @Override
        protected void onPostExecute(Product product){
            if(product != null){
                Toast.makeText(getContext(),product.title + " removed from cart!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class FetchCartFromDBTask extends AsyncTask<Void, Void, ArrayList<Product>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Product> doInBackground(Void... params) {
            return (ArrayList<Product>) mProductDao.getAll();
        }

        @Override
        protected void onPostExecute(ArrayList<Product> productsList) {
            String imgBaseURLStr = getString(R.string.img_host);
            mFruitRecyclerViewAdapter.setProductList(productsList, imgBaseURLStr);
        }
    }
}
