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
        int swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(0, swipeFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 1f;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return super.getSwipeVelocityThreshold(defaultValue);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        ItemAdapter.ItemHolder holder = (ItemAdapter.ItemHolder) viewHolder;
        float maxWidth = holder.getActionWidth();

        if (dX < -maxWidth) dX = -maxWidth;
        else if (dX > 0f) dX = 0f;

        holder.slideItem.setTranslationX(dX);
        holder.success.setTranslationX(dX);

        Log.d(TAG, "onChildDraw: " + dX);
    }
}
