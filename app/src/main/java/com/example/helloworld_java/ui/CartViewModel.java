package com.example.helloworld_java.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.helloworld_java.data.Product;
import com.example.helloworld_java.data.ProductRepository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private ProductRepository mRepository;

    private LiveData<List<Product>> mAllProducts;

    public CartViewModel (Application application) {
        super(application);
        mRepository = new ProductRepository(application);
        mAllProducts = mRepository.getAllProducts();
    }

    LiveData<List<Product>> getAllWords() { return mAllProducts; }

    public void insert(Product product) { mRepository.insertAll(product); }
}