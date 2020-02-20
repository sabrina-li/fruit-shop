package com.example.helloworld_java.data;

import android.content.Context;
import android.content.Intent;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import kotlin.jvm.JvmDefault;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Query("SELECT * FROM product WHERE title IN (:productTitles)")
    List<Product> loadAllByTitles(String... productTitles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Product... products);

    @Delete
    void delete(Product product);

    @Update
    void updateQuantityInCart(Product product);

}