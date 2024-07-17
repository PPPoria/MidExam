package com.example.midexam.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midexam.R;
import com.example.midexam.adapter.ItemAdapter;
import com.example.midexam.helper.ItemTouchCallBack;
import com.example.midexam.model.ItemData;

import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity {

    private List<ItemData> itemList = new ArrayList<>();

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_touch);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initNewsListView();
        initItemSlide();
    }

    private void initItemSlide() {
        ItemTouchCallBack callBack = new ItemTouchCallBack();
        ItemTouchHelper helper = new ItemTouchHelper(callBack);
        helper.attachToRecyclerView(rv);
    }

    private void initNewsListView() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ItemAdapter(this,itemList));

        new Thread(new Runnable() {
            @Override
            public void run() {
                itemList.add(new ItemData("劲爆内容"));
                itemList.add(new ItemData("乙烯一克一克"));
                itemList.add(new ItemData("注意看，这个男人叫小帅"));
                rv.getAdapter().notifyDataSetChanged();
            }
        }).start();
    }

    private void initView() {
        rv = findViewById(R.id.news_list);
    }

}