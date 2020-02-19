package com.example.helloworld_java.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Query("SELECT * FROM product WHERE title IN (:productTitles)")
    List<Product> loadAllByTitles(String[] productTitles);

    @Insert
    void insertAll(Product... products);

    @Delete
    void delete(Product product);
}