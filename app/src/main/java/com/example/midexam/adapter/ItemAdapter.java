package com.example.midexam.adapter;

import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midexam.R;
import com.example.midexam.activity.DesktopActivity;
import com.example.midexam.activity.ItemActivity;
import com.example.midexam.fragment.EditJobFragment;
import com.example.midexam.fragment.JobFragment;
import com.example.midexam.model.ItemData;
import com.example.midexam.overrideview.HorizontalScrollMenu;

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
        holder.tvdate.setText(itemList.get(position).getJobData());
        holder.tvduring.setText(itemList.get(position).getJobDuring());
        holder.position= holder.getAdapterPosition();
        holder.showTaskLayout.setOnLongClickListener(v -> {
            // 处理长按事件
            showPopupMenu(v, position);
            return true; // 返回 true 表示事件已被处理
        });
    }

    @Override
    public int getItemCount() {
        if (itemList == null) return 0;
        return itemList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemText;
        public ConstraintLayout slideItem;
        public ConstraintLayout slideLayout;
        public ConstraintLayout option;
        public ConstraintLayout showTaskLayout;
        public HorizontalScrollMenu scroll;

        private Button btEdit;
        private Button btDelete;
        private Button btComplete;
        private TextView tipsdate;
        private TextView tipsduring;
        private TextView tvdate;
        private TextView tvduring;
        private int position;

        public int openState = 0;//展开状态，0为未展开，1为二者之间，2为已展开

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.item_text);
            slideItem = itemView.findViewById(R.id.slide_item);
            slideLayout = itemView.findViewById(R.id.slide_layout);
            option = itemView.findViewById(R.id.option);
            showTaskLayout=itemView.findViewById(R.id.item_show_clayout);
            btEdit=itemView.findViewById(R.id.btForEdit);
            btDelete=itemView.findViewById(R.id.btForDelete);
            scroll=itemView.findViewById(R.id.scroll_item);
            btComplete=itemView.findViewById(R.id.bt_complete);
            tvdate=itemView.findViewById(R.id.item_date);
            tvduring=itemView.findViewById(R.id.item_during);
            tipsdate=itemView.findViewById(R.id.item_tipsDate);
            tipsduring=itemView.findViewById(R.id.item_tipsDuring);

            btEdit.setOnClickListener(this);
            btDelete.setOnClickListener(this);
            btComplete.setOnClickListener(this);
            showTaskLayout.setOnClickListener(this);
        }

        public float getActionWidth(){
            return option.getWidth();
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btForEdit:
                    JobFragment.setItemPosition(position);
                    JobFragment.switchDialog(JobFragment.MODIFY_JOB);
                    scroll.smoothScrollTo(0,0);//点击后就返回原位，当然如果需要编辑后在返回原位则需要把他的scroll传给dialog
                    break;
                case R.id.btForDelete:
                    JobFragment.deleteItem(position);

                    break;
                case R.id.bt_complete:
                    Toast.makeText(v.getContext(), "启用",Toast.LENGTH_LONG).show();
                    break;
                case R.id.item_show_clayout:
                    JobFragment.setItemPosition(position);
                    JobFragment.switchDialog(JobFragment.MODIFY_JOB);
                    break;
            }
        }
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
