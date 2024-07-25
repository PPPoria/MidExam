package com.example.midexam.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midexam.R;
import com.example.midexam.fragment.JobFragment;
import com.example.midexam.model.ItemData;
import com.example.midexam.overrideview.HorizontalScrollMenu;

import java.util.ArrayList;
import java.util.List;

public class ItemSelectedAdapter extends RecyclerView.Adapter<ItemSelectedAdapter.ItemSelectedHolder> {

    private static final String TAG = "ItemAdapter";

    private Activity activity;
    private Context context;
    private List<ItemData> itemList;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();

    public ItemSelectedAdapter(Activity activity, Context context, List<ItemData> itemList) {
        this.activity = activity;
        this.context = context;
        this.itemList = itemList;

    }

    @NonNull
    @Override
    public ItemSelectedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.itemselected, parent, false);
        return new ItemSelectedHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSelectedHolder holder, int position) {
        //holder.selected.setSelected(itemList.get(holder.getAdapterPosition()).getChosen());
        ItemData itemData=itemList.get(position);
        Log.d("this",String.valueOf(itemList.get(holder.getAdapterPosition()).getChosen()));
        holder.itemText.setText(itemList.get(holder.getAdapterPosition()).getItemText());
        holder.tvdate.setText(itemList.get(holder.getAdapterPosition()).getJobData());
        holder.tvduring.setText(itemList.get(holder.getAdapterPosition()).getJobDuring());
        holder.position= holder.getAdapterPosition();
        holder.selected.setVisibility(View.VISIBLE);
        holder.selected.setChecked(false);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.selected.isChecked()){holder.selected.setChecked(false);}
                else {holder.selected.setChecked(true);}
                Log.d("this",String.valueOf(holder.selected.isSelected()));
            }
        });

       holder.selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mSelectedItems.put(holder.getAdapterPosition(), isChecked);
                }else{

                }
            }
        });
    }

    public List<Integer> getSelectedItems() {
        List<Integer> selectedItems = new ArrayList<>();
        for (int i = 0; i < mSelectedItems.size(); i++) {
            if (mSelectedItems.valueAt(i)) {
                selectedItems.add(mSelectedItems.keyAt(i));
            }
        }
        return selectedItems;
    }
    @Override
    public int getItemCount() {
        if (itemList == null) return 0;
        return itemList.size();
    }

    public class ItemSelectedHolder extends RecyclerView.ViewHolder {
        public  ConstraintLayout layout;
        public TextView itemText;
        private TextView tipsdate;
        private TextView tipsduring;
        private TextView tvdate;
        private TextView tvduring;
        private CheckBox selected;
        private int position;

        public int openState = 0;//展开状态，0为未展开，1为二者之间，2为已展开

        public ItemSelectedHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.itemSelect_text);

            tvdate=itemView.findViewById(R.id.itemSelect_date);
            tvduring=itemView.findViewById(R.id.itemSelect_during);
            tipsdate=itemView.findViewById(R.id.itemSelect_tipsDate);
            tipsduring=itemView.findViewById(R.id.itemSelect_tipsDuring);
            selected=itemView.findViewById(R.id.cb_selected);
            layout=itemView.findViewById(R.id.slide_itemSelect);

        }
    }

}
