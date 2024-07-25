package com.example.midexam.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midexam.R;
import com.example.midexam.model.ItemData;

import java.util.List;

public class WaterHistoryAdapter extends RecyclerView.Adapter<WaterHistoryAdapter.WaterHistoryHolder>{

    private Context context;
    private List<String> waterList;

    public WaterHistoryAdapter( Context context, List<String> waterList) {
        this.context = context;
        this.waterList = waterList;
    }

    @NonNull
    @Override
    public WaterHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.water, parent, false);
        return new WaterHistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterHistoryHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return waterList.size();
    }

    class WaterHistoryHolder extends RecyclerView.ViewHolder{

        public WaterHistoryHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
