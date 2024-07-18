package com.example.midexam.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midexam.R;
import com.example.midexam.activity.ItemActivity;
import com.example.midexam.model.ItemData;

import java.lang.reflect.Field;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private static final String TAG = "ItemAdapter";

    private Activity activity;
    private Context context;
    private List<ItemData> itemList;

    public ItemAdapter(Activity activity, Context context, List<ItemData> itemList) {
        this.activity = activity;
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
        public ConstraintLayout slideLayout;
        public ConstraintLayout success;
        public int openState = 0;//展开状态，0为未展开，1为二者之间，2为已展开

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.item_text);
            slideItem = itemView.findViewById(R.id.slide_item);
            slideLayout = itemView.findViewById(R.id.slide_layout);
            success = itemView.findViewById(R.id.success);
        }

        public float getActionWidth(){
            return success.getWidth();
        }
    }
}
