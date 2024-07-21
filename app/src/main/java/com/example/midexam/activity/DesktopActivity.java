package com.example.midexam.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
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

public class DesktopActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DesktopActivity";

    private ViewPager2 pagesContainer;
    private List<Fragment> pages = new ArrayList<>();
    private int pagePosition = 0;
    private Fragment jobPage;
    private Fragment waterPage;
    private Fragment statisticsPage;
    private Fragment personPage;

    private ConstraintLayout navigationBar;
    private ConstraintLayout jobButton;
    private ConstraintLayout waterButton;
    private ConstraintLayout statisticsButton;
    private ConstraintLayout personButton;
    private View stroke;

    @SuppressLint("ResourceAsColor")
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
        getWindow().setNavigationBarColor(getColor(R.color.grey));

        initView();
        initPages();
        initListener();
    }

    private void initListener() {
        pagesContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                pagePosition = position;
                switch (position) {
                    case 0: {
                        moveStroke();
                        break;
                    }
                    case 1: {
                        moveStroke();
                        break;
                    }
                    case 2: {
                        moveStroke();
                        break;
                    }
                    case 3: {
                        moveStroke();
                        break;
                    }
                }
            }
        });

        jobButton.setOnClickListener(this);
        waterButton.setOnClickListener(this);
        statisticsButton.setOnClickListener(this);
        personButton.setOnClickListener(this);
    }

    //移动方框
    private void moveStroke() {
        if (stroke == null) return;

        float buttonDistance = (float) navigationBar.getWidth() / 4;
        ObjectAnimator animator = null;

        if (pagePosition == 0)
            animator = ObjectAnimator.ofFloat(stroke, "translationX", stroke.getTranslationX(), 0);
        else if (pagePosition == 1)
            animator = ObjectAnimator.ofFloat(stroke, "translationX", stroke.getTranslationX(), buttonDistance);
        else if (pagePosition == 2)
            animator = ObjectAnimator.ofFloat(stroke, "translationX", stroke.getTranslationX(), buttonDistance * 2);
        else if (pagePosition == 3)
            animator = ObjectAnimator.ofFloat(stroke, "translationX", stroke.getTranslationX(), buttonDistance * 3);

        assert animator != null;
        animator.setDuration(150);
        animator.start();
    }

    //初始化fragment并添加
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
        navigationBar = findViewById(R.id.navigation_bar);
        jobButton = findViewById(R.id.job_page_button);
        waterButton = findViewById(R.id.water_page_button);
        statisticsButton = findViewById(R.id.statistics_page_button);
        personButton = findViewById(R.id.person_page_button);
        stroke = findViewById(R.id.button_stroke);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.job_page_button) {
            pagePosition = 0;
        } else if (id == R.id.water_page_button) {
            pagePosition = 1;
        } else if (id == R.id.statistics_page_button) {
            pagePosition = 2;
        } else if (id == R.id.person_page_button) {
            pagePosition = 3;
        }
        pagesContainer.setCurrentItem(pagePosition);
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