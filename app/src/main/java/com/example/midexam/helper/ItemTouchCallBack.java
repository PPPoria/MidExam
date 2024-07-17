package com.example.midexam.helper;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midexam.adapter.ItemAdapter;

public class ItemTouchCallBack extends ItemTouchHelper.Callback {
    private static final String TAG = "ItemTouchCallBack";

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        try {
            ItemAdapter.ItemHolder holder = (ItemAdapter.ItemHolder) viewHolder;
            float actionWidth = holder.getActionWidth();
            if (dX < -actionWidth) dX = -actionWidth;
            holder.slideItem.setTranslationX(dX);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
