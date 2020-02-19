package com.example.helloworld_java.data;

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
    @PrimaryKey(autoGenerate = true)

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

    @ColumnInfo(name = "quantityInCart")
    public int quantityInCart = 0;

    public Product(JSONObject productObj) {
        try {
            this.title = productObj.getString("title");
            this.description = productObj.getString("description");
            this.price = productObj.getDouble("price");
            this.image = productObj.getString("image");
            this.unit = productObj.getString("unit");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}