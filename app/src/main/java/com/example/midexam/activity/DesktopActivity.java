package com.example.midexam.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.midexam.R;
import com.example.midexam.adapter.ItemAdapter;
import com.example.midexam.fragment.BlankFragment;
import com.example.midexam.fragment.EditJobFragment;
import com.example.midexam.fragment.JobFragment;
import com.example.midexam.fragment.PersonFragment;
import com.example.midexam.fragment.StatisticsFragment;
import com.example.midexam.fragment.WaterFragment;
import com.example.midexam.observer.UserObserver;

import java.util.ArrayList;
import java.util.List;

public class DesktopActivity extends AppCompatActivity implements View.OnClickListener, UserDataShowInterface {
    private static final String TAG = "DesktopActivity";

    private static ViewPager2 pagesContainer;
    private static List<Fragment> pages = new ArrayList<>();
    private static DesktopAdapter adapter;
    private int pagePosition = 0;
    public static JobFragment jobPage;
    public static WaterFragment waterPage;
    public static StatisticsFragment statisticsPage;
    public static PersonFragment personPage;

    private ConstraintLayout navigationBar;
    private ConstraintLayout jobButton;
    private ConstraintLayout waterButton;
    private ConstraintLayout statisticsButton;
    private ConstraintLayout personButton;

    private View waterIcon;
    private View jobIcon;
    private View statisticsIcon;
    private View personIcon;
    private List<View> icons = new ArrayList<>();

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
        jobButton.setOnClickListener(this);
        waterButton.setOnClickListener(this);
        statisticsButton.setOnClickListener(this);
        personButton.setOnClickListener(this);

        pagesContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                pagePosition = position;
                changeIcon();
                try {
                    if (position == 0) {
                        waterPage.receiveUpdate();
                    } else if (position == 1) {
                        jobPage.receiveUpdate();
                    } else if (position == 2) {
                        statisticsPage.receiveUpdate();
                    } else if (position == 3) {
                        personPage.receiveUpdate();
                    }
                }catch (Exception e){}
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
        });
    }


    //初始化fragment并添加
    private void initPages() {
        waterPage = new WaterFragment();
        jobPage = new JobFragment();
        statisticsPage = new StatisticsFragment();
        personPage = new PersonFragment();
        /*editJobFragment=new EditJobFragment();*/

        pages.add(waterPage);
        pages.add(jobPage);
        //pages.add(new BlankFragment());
        pages.add(statisticsPage);
        pages.add(personPage);

        adapter = new DesktopAdapter(this);
        pagesContainer.setAdapter(adapter);
    }

    private void initView() {
        pagesContainer = findViewById(R.id.pages_container);

        navigationBar = findViewById(R.id.navigation_bar);

        jobButton = findViewById(R.id.job_page_button);
        waterButton = findViewById(R.id.water_page_button);
        statisticsButton = findViewById(R.id.statistics_page_button);
        personButton = findViewById(R.id.person_page_button);

        waterIcon = findViewById(R.id.water_icon_2);
        icons.add(waterIcon);
        jobIcon = findViewById(R.id.job_icon_2);
        icons.add(jobIcon);
        statisticsIcon = findViewById(R.id.statistics_icon_2);
        icons.add(statisticsIcon);
        personIcon = findViewById(R.id.person_icon_2);
        icons.add(personIcon);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.job_page_button) {
            pagePosition = 1;
        } else if (id == R.id.water_page_button) {
            pagePosition = 0;
        } else if (id == R.id.statistics_page_button) {
            pagePosition = 2;
        } else if (id == R.id.person_page_button) {
            pagePosition = 3;
        }
        pagesContainer.setCurrentItem(pagePosition);
        changeIcon();
    }

    private void changeIcon() {
        for (int i = 0; i < 4; i++) {
            if (i == pagePosition) {
                icons.get(i).animate().alpha(1f).setDuration(150).start();
                continue;
            }
            icons.get(i).animate().alpha(0f).setDuration(150).start();
        }
    }

    @Override
    public void log(int STATUS) {

    }

    @Override
    public void register(int STATUS) {

    }

    @Override
    public void updateUserData(int STATUS) {

    }

    @Override
    public void updateUserImage(int STATUS) {

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