package com.example.helloworld_java;

import android.content.Context;
import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FruitRecyclerViewAdapter extends RecyclerView.Adapter<FruitRecyclerViewAdapter.FruitAdapterViewHolder> {

    private ArrayList<String>  mFruitList;
    private final FruitAdapterOnClickHandler mClickHandler;

    public interface FruitAdapterOnClickHandler {
        void onClick(String fruit);
    }

    public FruitRecyclerViewAdapter(FruitAdapterOnClickHandler clickHander){
        mClickHandler = clickHander;
    }

    public class FruitAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mFruitTextView;

        public FruitAdapterViewHolder(View view){
            super(view);
            mFruitTextView = (TextView) view.findViewById(R.id.tv_fruit_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String fruitName = mFruitList.get(adapterPosition);
            mClickHandler.onClick(fruitName);
        }
    }

    @Override
    public FruitAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.fruit_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachToParentImmediately);
        return new FruitAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FruitAdapterViewHolder fruitAdapterViewHolder, int position){
        String fruiteName = mFruitList.get(position);
        fruitAdapterViewHolder.mFruitTextView.setText(fruiteName);
    }

    @Override
    public int getItemCount(){
        if (null == mFruitList) return 0;
        return mFruitList.size();
    }

    public void setFruitList(ArrayList<String> fruitList){
        mFruitList = fruitList;
        notifyDataSetChanged();
    }
}
