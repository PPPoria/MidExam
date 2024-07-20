package com.example.midexam.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.midexam.R;
import com.example.midexam.fragment.JobFragment;
import com.example.midexam.fragment.PersonFragment;
import com.example.midexam.fragment.StatisticsFragment;
import com.example.midexam.fragment.WaterFragment;

import java.util.ArrayList;
import java.util.List;

public class DesktopActivity extends AppCompatActivity {
    private static final String TAG = "DesktopActivity";

    private ViewPager2 pagesContainer;
    private List<Fragment> pages = new ArrayList<>();
    private Fragment jobPage;
    private Fragment waterPage;
    private Fragment statisticsPage;
    private Fragment personPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_desktop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initPages();
    }

    private void initPages() {
        jobPage = new JobFragment();
        waterPage = new WaterFragment();
        statisticsPage = new StatisticsFragment();
        personPage = new PersonFragment();
        pages.add(jobPage);
        pages.add(waterPage);
        pages.add(statisticsPage);
        pages.add(personPage);
        pagesContainer.setAdapter(new DesktopAdapter(this));
    }

    private void initView() {
        pagesContainer = findViewById(R.id.pages_container);
    }

    class DesktopAdapter extends FragmentStateAdapter {

        public DesktopAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return pages.get(position);
        }

        @Override
        public int getItemCount() {
            return pages.size();
        }
    }
}