package com.example.helloworld_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld_java.data.FruitListData;

public class MainActivity extends AppCompatActivity implements FruitRecyclerViewAdapter.FruitAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private FruitRecyclerViewAdapter mFruitRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_fruit);

        LinearLayoutManager layoutManager
                =new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mFruitRecyclerViewAdapter = new FruitRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mFruitRecyclerViewAdapter);

        showFruitList();
    }

    private void showFruitList(){
        String[] fruitList = FruitListData.getFruitlist();
        mFruitRecyclerViewAdapter.setFruitList(fruitList);

    }

    @Override
    public void onClick(String fruitName){
        Toast.makeText(this,fruitName,Toast.LENGTH_SHORT)
                .show();
    }
}
