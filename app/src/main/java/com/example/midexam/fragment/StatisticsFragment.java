package com.example.midexam.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.midexam.R;

import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.observer.UserObserver;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment implements View.OnClickListener, UserDataShowInterface {
    private static final String TAG = "StatisticsFragment";
    private View view;

    Button btFocusChart;
    Button btWaterChart;
    Button btDay;
    Button btMonth;
    Button btYear;

    ViewPager2 statisticsViewPager;
    StatisticTimePageFragment statisticTimePageFragment;
    StatisticWaterPageFragment statisticWaterPageFragment;
    List<Fragment> pages = new ArrayList<>();
    int chartPagePosition = 0;
    int timePagePosition=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initViewAndListener();
        initRV();
        return view;
    }

    private void initRV() {
        statisticTimePageFragment = new StatisticTimePageFragment();
        statisticWaterPageFragment = new StatisticWaterPageFragment();

        pages.add(statisticTimePageFragment);
        pages.add(statisticWaterPageFragment);
        statisticsViewPager.setAdapter(new statisticsAdapter(requireActivity()));
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initViewAndListener() {
        btFocusChart = view.findViewById(R.id.bt_pie_focus);
        btWaterChart = view.findViewById(R.id.bt_bar_water);
        btDay = view.findViewById(R.id.bt_day);
        btMonth = view.findViewById(R.id.bt_month);
        btYear = view.findViewById(R.id.bt_year);

        statisticsViewPager = view.findViewById(R.id.statistics_viewpager);
        statisticsViewPager.setSaveEnabled(false);

        btFocusChart.setOnClickListener(this);
        btWaterChart.setOnClickListener(this);
        btDay.setOnClickListener(this);
        btMonth.setOnClickListener(this);
        btYear.setOnClickListener(this);

        statisticsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    statisticTimePageFragment.receiveUpdate();
                } else if (position == 1) {
                    statisticWaterPageFragment.receiveUpdate();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_pie_focus) {
            chartPagePosition = 0;
            statisticsViewPager.setCurrentItem(0);
            statisticTimePageFragment.setPosition(timePagePosition);

            btFocusChart.setTextColor(Color.WHITE);
            btFocusChart.setBackgroundColor(Color.parseColor("#6bbef2"));

            btWaterChart.setTextColor(Color.parseColor("#969696"));
            btWaterChart.setBackgroundColor(Color.WHITE);
            return;
        } else if (id == R.id.bt_bar_water) {
            chartPagePosition = 1;
            statisticsViewPager.setCurrentItem(1);
            statisticWaterPageFragment.setPosition(timePagePosition);

            btWaterChart.setTextColor(Color.WHITE);
            btWaterChart.setBackgroundColor(Color.parseColor("#6bbef2"));

            btFocusChart.setTextColor(Color.parseColor("#969696"));
            btFocusChart.setBackgroundColor(Color.WHITE);
            //我移动到statisticWaterPageFrag
            return;
        }


        if (id == R.id.bt_day) {
            if (chartPagePosition == 0) {
                statisticTimePageFragment.setPosition(0);
                statisticTimePageFragment.receiveUpdate();
            } else {
                statisticWaterPageFragment.setPosition(0);
                statisticWaterPageFragment.receiveUpdate();
            }

            btDay.setTextColor(Color.WHITE);
            btDay.setBackgroundColor(Color.parseColor("#6bbef2"));

            btMonth.setTextColor(Color.parseColor("#969696"));
            btMonth.setBackgroundColor(Color.WHITE);

            btYear.setTextColor(Color.parseColor("#969696"));
            btYear.setBackgroundColor(Color.WHITE);
            timePagePosition=0;

        } else if (id == R.id.bt_month) {
            if (chartPagePosition == 0) {
                statisticTimePageFragment.setPosition(1);
                statisticTimePageFragment.receiveUpdate();
            } else {
                statisticWaterPageFragment.setPosition(1);
                statisticWaterPageFragment.receiveUpdate();
            }

            btMonth.setTextColor(Color.WHITE);
            btMonth.setBackgroundColor(Color.parseColor("#6bbef2"));

            btDay.setTextColor(Color.parseColor("#969696"));
            btDay.setBackgroundColor(Color.WHITE);

            btYear.setTextColor(Color.parseColor("#969696"));
            btYear.setBackgroundColor(Color.WHITE);
            timePagePosition=1;

        } else if (id == R.id.bt_year) {
            if (chartPagePosition == 0) {
                statisticTimePageFragment.setPosition(2);
                statisticTimePageFragment.receiveUpdate();
            } else {
                statisticWaterPageFragment.setPosition(2);
                statisticWaterPageFragment.receiveUpdate();
            }

            btYear.setTextColor(Color.WHITE);
            btYear.setBackgroundColor(Color.parseColor("#6bbef2"));

            btDay.setTextColor(Color.parseColor("#969696"));
            btDay.setBackgroundColor(Color.WHITE);

            btMonth.setTextColor(Color.parseColor("#969696"));
            btMonth.setBackgroundColor(Color.WHITE);
            timePagePosition=2;
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

    @Override
    public UserObserver registerObserver(UserDataShowInterface observedView) {
        return UserDataShowInterface.super.registerObserver(observedView);
    }

    @Override
    public void receiveUpdate() {
        if (chartPagePosition == 0) {
            statisticTimePageFragment.receiveUpdate();
        } else if (chartPagePosition == 1) {
            statisticWaterPageFragment.receiveUpdate();
        }
    }

    class statisticsAdapter extends FragmentStateAdapter {

        public statisticsAdapter(@NonNull FragmentActivity fragmentActivity) {
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