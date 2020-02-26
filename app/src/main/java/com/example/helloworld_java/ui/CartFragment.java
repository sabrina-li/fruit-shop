package com.example.helloworld_java.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld_java.ProductRecyclerViewAdapter;
import com.example.helloworld_java.R;
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
    private CartViewModel mCartViewModel;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_cart);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mFruitRecyclerViewAdapter = new ProductRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mFruitRecyclerViewAdapter);
//        showCartList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
//        mCartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        mCartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        Log.d("here","cart frag  "+String.valueOf(mCartViewModel));
        View root =inflater.inflate(R.layout.fragment_cart, container, false);

        mCartViewModel.getAll().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                Log.d("here","onchange "+ String.valueOf(products.get(0).title));
                // Update the cached copy of the words in the adapter.
                mFruitRecyclerViewAdapter.setProductList(products);
            }
        });


        return root;
    }


//    private void showCartList() {
//        new FetchCartFromDBTask().execute();//fetch all from DB
//    }

    @Override
    public void onClick(Product productObj) {
        try {
            Toast.makeText(getContext(), productObj.title, Toast.LENGTH_SHORT)
                    .show();
            //TODO: show menu for delete, changing quantity etc.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String buttonText(){
        return "remove from cart";
    }

    @Override
    public View createFragmentSpecificView(Product product){
        LinearLayout layout = new LinearLayout(getContext());

        TextView count = new TextView(getContext());
        count.setText(product.quantityInCart + " in cart");
        layout.addView(count);

        return layout;
    }


//    public class FetchCartFromDBTask extends AsyncTask<Void, Void, ArrayList<Product>> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected ArrayList<Product> doInBackground(Void... params) {
//            return (ArrayList<Product>) mProductDao.getAll();
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Product> productsList) {
//            String imgBaseURLStr = getString(R.string.img_host);
//            mFruitRecyclerViewAdapter.setProductList(productsList, imgBaseURLStr);
//        }
//    }
}
