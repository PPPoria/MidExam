package com.example.midexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midexam.R;

import java.util.List;

public class WaterHistoryAdapter extends RecyclerView.Adapter<WaterHistoryAdapter.WaterHistoryHolder> {

    private Context context;
    private List<String> drinkList;

    public void setDrinkList(List<String> drinkList) {
        this.drinkList = drinkList;
    }

    public WaterHistoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public WaterHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.water, parent, false);
        return new WaterHistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterHistoryHolder holder, int position) {
        String date = drinkList.get(position).substring(0, 2) + ":" + drinkList.get(position).substring(2, 4);
        String value = drinkList.get(position).substring(4);
        holder.drinkTime.setText(date);
        holder.drinkValue.setText(value);
    }

    @Override
    public int getItemCount() {
        if (drinkList == null) return 0;
        return drinkList.size();
    }

    class WaterHistoryHolder extends RecyclerView.ViewHolder {
        public TextView drinkTime;
        public TextView drinkValue;

        public WaterHistoryHolder(@NonNull View itemView) {
            super(itemView);
            drinkTime = itemView.findViewById(R.id.drink_time);
            drinkValue = itemView.findViewById(R.id.drink_value);
        }
    }
}
