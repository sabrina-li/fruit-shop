package com.example.starter_proj.ui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.starter_proj.data.Product;
import com.example.starter_proj.data.ProductRepository;

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

    public void addQuantityInCartByOne(Product product) {  Log.d("here","addQuantityInCartByOne"); mRepository.increaseQuantityInCart(product); }
}