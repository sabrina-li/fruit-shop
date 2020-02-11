package com.example.helloworld_java;

import android.content.Context;
import android.service.autofill.TextValueSanitizer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class FruitRecyclerViewAdapter extends RecyclerView.Adapter<FruitRecyclerViewAdapter.FruitAdapterViewHolder> {

    private String[] mFruitList;
    private final FruitAdapterOnClickHandler mClickHandler;

    public interface FruitAdapterOnClickHandler {
        void onClick(String fruit);
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
            String fruitName = mFruitList[adapterPosition];
            mClickHandler.onClick(fruitName);
        }
    }

    @Override
    public FruitAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();

    }
}
