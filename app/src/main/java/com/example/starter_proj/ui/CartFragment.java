package com.example.starter_proj.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starter_proj.ProductRecyclerViewAdapter;
import com.example.starter_proj.R;
import com.example.starter_proj.data.Product;
//import com.example.helloworld_java.data.ProductDao;
import com.fullstory.FS;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment implements ProductRecyclerViewAdapter.ProductAdapterHandler {
    private RecyclerView mRecyclerView;
    private ProductRecyclerViewAdapter mFruitRecyclerViewAdapter;
//    private ProductDao mProductDao;
    private CartViewModel mCartViewModel;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.rv_cart);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mFruitRecyclerViewAdapter = new ProductRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mFruitRecyclerViewAdapter);


        Button purchaseButton = new Button(getContext());
        purchaseButton.setText("Purchase!");
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Map<String,Integer> clickEvent = new HashMap<>();
                clickEvent.put("itemInCart",10);
                FS.event("PurchaseClicked",clickEvent);

                try {
                    throw new RuntimeException("Test non-fatal error"); // Force a crash
                }catch (Exception e){
                    e.printStackTrace();
                    FirebaseCrashlytics.getInstance().recordException(e);
                    Map<String, String> eventVars = new HashMap<>();
                    eventVars.put("errorMessage", e.getMessage());
                    FS.event("nonFatalException",eventVars);
                    FS.log(FS.LogLevel.ERROR,e.getMessage());
                }
            }
        });

        Button crashButton = new Button(getContext());
        crashButton.setText("Purchase(Crash!)");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Map<String,Integer> clickEvent = new HashMap<>();
                clickEvent.put("itemInCart",10);
                FS.event("PurchaseClicked",clickEvent);

                throw new RuntimeException("Test Fatal error"); // Force a crash
            }
        });

        FrameLayout layout = (FrameLayout) view.getParent();
        layout.addView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mCartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        View root =inflater.inflate(R.layout.fragment_cart, container, false);

        mCartViewModel.getAll().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                // Update the cached copy of the words in the adapter.
                mFruitRecyclerViewAdapter.setProductList(products);
            }
        });
        return root;
    }

    @Override
    public void onClick(Product productObj) {
        try {
//            new RemoveProductToCartTask().execute(productObj);
            mCartViewModel.reduceQuantityInCartByOne(productObj);
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



    private class RemoveProductToCartTask extends  AsyncTask<Product, Void, String>{
        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected String doInBackground (final Product... products){
            if (products.length > 0) {
                mCartViewModel.reduceQuantityInCartByOne(products[0]);
                return products[0].title;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String title){
            Toast t;
            if(title != null){
                t= Toast.makeText(getContext(), "you've removed one "+title+" from cart", Toast.LENGTH_SHORT);
            }else{
                t=Toast.makeText(getContext(), "failed to remove from cart!",Toast.LENGTH_SHORT);
            }
            t.show();
            FS.addClass(t.getView(),FS.UNMASK_CLASS);
        }
    }
}