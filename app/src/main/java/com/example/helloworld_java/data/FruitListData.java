package com.example.helloworld_java.data;

public class FruitListData {
    static private String[] fruitList = {
            "bananas",
            "bluebs",
            "caramabola"
    };

    static public String[] getFruitlist(){
        System.out.println(fruitList.toString());
        return fruitList;
    }
}
