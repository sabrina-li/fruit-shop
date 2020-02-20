package com.example.helloworld_java;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_cart);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mFruitRecyclerViewAdapter = new ProductRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mFruitRecyclerViewAdapter);

        AppDatabase db = AppDatabase.getDatabase(getContext());
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
    }

    @Override
    public View createActionView(){
        Button actonBtn = new Button(getContext());
        actonBtn.setText("Remove from Cart");
        return actonBtn;
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
            mFruitRecyclerViewAdapter.setFruitList(productsList, imgBaseURLStr);
        }
    }
}
