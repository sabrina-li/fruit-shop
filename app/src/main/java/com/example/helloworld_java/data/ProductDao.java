package com.example.helloworld_java.data;

        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;

        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.MutableLiveData;
        import androidx.room.Dao;
        import androidx.room.Delete;
        import androidx.room.Insert;
        import androidx.room.OnConflictStrategy;
        import androidx.room.Query;
        import androidx.room.Transaction;
        import androidx.room.Update;

        import java.util.List;


@Dao
public abstract class ProductDao {
    @Query("SELECT * FROM product")
    abstract LiveData<List<Product>> getAll();

    @Query("SELECT * FROM product WHERE title = :productTitles LIMIT 1")
    protected abstract Product getProductByTitle(String productTitles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(Product products);

    @Delete
    abstract void delete(Product product);

    @Update
    abstract void updateProduct(Product product);

    @Query("UPDATE product SET quantityInCart = quantityInCart + 1 WHERE title=:title")
    abstract int increaseQuantity(String title);

    @Query("UPDATE product SET quantityInCart = quantityInCart - 1 WHERE title=:title")
    abstract int decreaseQuantity(String title);

    @Transaction
    void increaseQuantityOrInsert(Product product) {
        Log.d("here",product.title);
        if(increaseQuantity(product.title) == 0){
            Log.d("here","trying to insert");
            product.quantityInCart =1;
            insert(product);
        }
    }
    @Transaction
    void decreaseQuantityOrDelete(Product product) {
        Product productInCart = getProductByTitle(product.title);
        Log.d("here",String.valueOf(productInCart)+' '+productInCart.quantityInCart);
        if(productInCart != null && productInCart.quantityInCart > 1 ){
            decreaseQuantity(product.title);
        }else{
            delete(product);
        }
    }
}