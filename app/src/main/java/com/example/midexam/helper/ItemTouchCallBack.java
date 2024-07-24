package com.example.midexam.helper;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midexam.R;
import com.example.midexam.adapter.ItemAdapter;
import com.example.midexam.fragment.JobFragment;

public class ItemTouchCallBack extends ItemTouchHelper.SimpleCallback/*ItemTouchHelper.Callback*/ {
    private final ItemAdapter adapter;
    public ItemTouchCallBack(int dragDirs, int swipeDirs, ItemAdapter adapter) {
        super(dragDirs, swipeDirs);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }


    public void onLongPress(RecyclerView.ViewHolder viewHolder) {
        // 长按事件的处理
        int position = viewHolder.getAdapterPosition();
        showPopupMenu(viewHolder.itemView, position);
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    JobFragment.deleteItem(position);
                    return true;
                case R.id.menu_edit:
                    JobFragment.switchDialog(JobFragment.MODIFY_JOB);
                    return true;
                default:
                    return false;
            }
        });

        popup.show();
    }
}
    /*private static final String TAG = "ItemTouchCallBack";

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
        holder.option.setTranslationX(dX);

        Log.d(TAG, "onChildDraw: " + dX);
    }*/