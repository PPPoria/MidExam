package com.example.midexam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemTouchActivity extends AppCompatActivity {

    private List<String> newsList = new ArrayList<>();

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
    }

    private void initNewsListView() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new NewsAdapter());

        new Thread(new Runnable() {
            @Override
            public void run() {
                newsList.add("劲爆内容");
                newsList.add("乙烯一克一克");
                newsList.add("注意看，这个男人叫小帅");
                rv.getAdapter().notifyDataSetChanged();
            }
        }).start();
    }

    private void initView() {
        rv = findViewById(R.id.news_list);
    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
        @NonNull
        @Override
        public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(ItemTouchActivity.this).inflate(R.layout.news, parent, false);
            NewsHolder holder = new NewsHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
            String news = newsList.get(position);
            holder.newsText.setText(news);
        }

        @Override
        public int getItemCount() {
            if (newsList == null) return 0;
            return newsList.size();
        }

        class NewsHolder extends RecyclerView.ViewHolder {
            public TextView newsText;

            public NewsHolder(@NonNull View itemView) {
                super(itemView);
                newsText = itemView.findViewById(R.id.news_text);
            }
        }
    }
}