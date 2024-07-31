package com.example.midexam.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.midexam.R;
import com.example.midexam.fragment.FocusFragment;
import com.example.midexam.fragment.JobFragment;
import com.example.midexam.fragment.PersonFragment;
import com.example.midexam.fragment.StatisticsFragment;
import com.example.midexam.fragment.WaterFragment;
import com.example.midexam.presenter.UserPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DesktopActivity extends AppCompatActivity implements View.OnClickListener, UserDataShowInterface {
    private static final String TAG = "DesktopActivity";

    private boolean hearting = false;
    private Timer timer;

    private static ViewPager2 pagesContainer;
    private static List<Fragment> pages = new ArrayList<>();
    private static DesktopAdapter adapter;
    private int pagePosition = 0;
    public static JobFragment jobPage;
    public static WaterFragment waterPage;
    public static StatisticsFragment statisticsPage;
    public static PersonFragment personPage;

    private FrameLayout focusFragmentContainer;

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
        initFragment();
        initListener();
        initHeart();
    }


    private void initHeart() {
        if (UserPresenter.getInstance(this).isLogged(this)) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        UserPresenter.presenter.heart(DesktopActivity.this, DesktopActivity.this);
                    } catch (Exception e) {

                    }
                }
            };
            if (timer == null) timer = new Timer();
            timer.schedule(timerTask, 1000, 2000);
        }
    }

    public void heartCallback(int STATUS) {
        if (STATUS == UserPresenter.STATUS_HEART_START) {
            Log.d(TAG, "heartCallback: start");
            focusFragmentContainer.setVisibility(View.VISIBLE);
        } else if (STATUS == UserPresenter.STATUS_HEART_WAIT) {
            Log.d(TAG, "heartCallback: wait");
            focusFragmentContainer.setVisibility(View.VISIBLE);
        } else if (STATUS == UserPresenter.STATUS_HEART_FINISH) {
            Log.d(TAG, "heartCallback: finish");
            focusFragmentContainer.setVisibility(View.INVISIBLE);
        } else if (STATUS == UserPresenter.STATUS_NO_INTERNET) {
            Log.d(TAG, "heartCallback: no internet");
            Toast.makeText(this, "啊欧，断线了", Toast.LENGTH_SHORT).show();
            focusFragmentContainer.setVisibility(View.INVISIBLE);
        }
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
                } catch (Exception e) {
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
        });
    }


    //初始化fragment并添加
    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FocusFragment focusFragment = new FocusFragment();
        transaction.add(R.id.focus_fragment_container, focusFragment);
        transaction.commit();


        waterPage = new WaterFragment();
        jobPage = new JobFragment();
        statisticsPage = new StatisticsFragment();
        personPage = new PersonFragment();

        pages.add(waterPage);
        pages.add(jobPage);
        pages.add(statisticsPage);
        pages.add(personPage);

        adapter = new DesktopAdapter(this);
        pagesContainer.setAdapter(adapter);
    }

    private void initView() {
        pagesContainer = findViewById(R.id.pages_container);

        jobButton = findViewById(R.id.job_page_button);
        waterButton = findViewById(R.id.water_page_button);
        statisticsButton = findViewById(R.id.statistics_page_button);
        personButton = findViewById(R.id.person_page_button);

        focusFragmentContainer = findViewById(R.id.focus_fragment_container);

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
            UserPresenter.getInstance(waterPage).requestLog(
                    this,
                    UserPresenter.presenter.getAccount(this),
                    UserPresenter.presenter.getPassword(this)
            );
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