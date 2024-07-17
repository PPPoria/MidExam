package com.example.midexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midexam.R;
import com.example.midexam.model.ItemData;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private Context context;
    private List<ItemData> itemList;

    public ItemAdapter(Context context, List<ItemData> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.itemText.setText(itemList.get(position).getItemText());
    }

    @Override
    public int getItemCount() {
        if (itemList == null) return 0;
        return itemList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView itemText;
        public ConstraintLayout slideItem;
        public TextView success;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.item_text);
            slideItem = itemView.findViewById(R.id.slide_item);
            success = itemView.findViewById(R.id.success);
        }

        public float getActionWidth(){
            return success.getWidth();
        }
    }
}
