package com.example.starter_proj.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.starter_proj.data.Product;
import com.example.starter_proj.data.ProductRepository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private ProductRepository mRepository;

    private LiveData<List<Product>> mAllProducts;

    public CartViewModel (Application application) {
        super(application);
        mRepository = new ProductRepository(application);
        mAllProducts = mRepository.getAll();
    }

    public LiveData<List<Product>> getAll() { return mAllProducts; }

    public void insert(Product product) { mRepository.insert(product); }

    public void reduceQuantityInCartByOne(Product product) { mRepository.decreaseQuantityInCart(product); }
}