package com.example.helloworld_java.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;


/*    {
        "title": "Bananas",
            "description": "The old reliable.",
            "price": 2.99,ΩΩΩΩ
            "image": "banans.jpg",
            "unit": "lb.", // this is called quantity in json api
            "quantityInCart": "2" // this is different from the quantity returned from json
    }
 */

@Entity
public class Product {

    @PrimaryKey@NonNull
    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "price")
    public Double price;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "unit")
    public String unit="lb";

    @ColumnInfo(name = "quantityInCart",defaultValue = "1")
    public int quantityInCart;


    public Product(String title, String description, Double price, String image, String unit, int quantityInCart) {
            this.title = title;
            this.description = description;
            this.price = price;
            this.image = image;
            this.unit = unit;
            this.quantityInCart=quantityInCart;
    }
}