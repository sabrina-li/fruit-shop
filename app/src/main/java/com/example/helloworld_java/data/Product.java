package com.example.helloworld_java.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/*    {
        "title": "Bananas",
            "description": "The old reliable.",
            "price": 2.99,
            "image": "banans.jpg",
            "quantity": "lb."
    }
 */

@Entity
public class Product {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "price")
    public Double price;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "quantity")
    public String quantity;
}