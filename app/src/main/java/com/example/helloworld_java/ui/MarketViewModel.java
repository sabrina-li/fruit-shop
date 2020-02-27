package com.example.helloworld_java.ui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.helloworld_java.data.Product;
import com.example.helloworld_java.data.ProductRepository;

import java.util.List;

public class MarketViewModel extends AndroidViewModel {

    private ProductRepository mRepository;

    private LiveData<List<Product>> mAllProducts;

    public MarketViewModel (Application application) {
        super(application);
        mRepository = new ProductRepository(application);
        mAllProducts = mRepository.getAll();
    }

    public LiveData<List<Product>> getAll() { return mAllProducts; }

    public boolean addQuantityInCartByOne(Product product) { return mRepository.updateQuantityInCartByOne(product,true); }
}