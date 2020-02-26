package com.example.helloworld_java.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.helloworld_java.data.Product;
import com.example.helloworld_java.data.ProductRepository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private ProductRepository mRepository;

    private LiveData<List<Product>> mAllProducts;

    public CartViewModel (Application application) {
        super(application);
        mRepository = new ProductRepository(application);
        mAllProducts = mRepository.getAll();
        Log.d("here","viewmodel "+ String.valueOf(mAllProducts.getValue()));
    }

    public LiveData<List<Product>> getAll() { return mAllProducts; }

    public void insert(Product product) { mRepository.insertAll(product); }
}