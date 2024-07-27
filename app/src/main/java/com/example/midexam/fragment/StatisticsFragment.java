package com.example.midexam.fragment;

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


    UserObserver observer = registerObserver(this);

    ViewPager2 statisticsViewPager;
    StatisticTimePageFragment statisticTimePageFragment;
    StatisticWaterPageFragment statisticWaterPageFragment;
    List<Fragment> pages = new ArrayList<>();
    int pagePosition = 0;


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

        btFocusChart.setOnClickListener(this);
        btWaterChart.setOnClickListener(this);
        btDay.setOnClickListener(this);
        btMonth.setOnClickListener(this);
        btYear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_pie_focus) {
            pagePosition = 0;
            statisticsViewPager.setCurrentItem(0);
            return;
        } else if (id == R.id.bt_bar_water) {
            pagePosition = 1;
            statisticsViewPager.setCurrentItem(1);
            return;
        }


        if (id == R.id.bt_day) {
            if (pagePosition == 0) {
                statisticTimePageFragment.setPosition(0);
                statisticTimePageFragment.receiveUpdate();
            } else {
                statisticWaterPageFragment.setPosition(0);
                statisticWaterPageFragment.receiveUpdate();
            }
        } else if (id == R.id.bt_month) {
            if (pagePosition == 0) {
                statisticTimePageFragment.setPosition(1);
                statisticTimePageFragment.receiveUpdate();
            } else {
                statisticWaterPageFragment.setPosition(1);
                statisticWaterPageFragment.receiveUpdate();
            }
        } else if (id == R.id.bt_year) {
            if (pagePosition == 0) {
                statisticTimePageFragment.setPosition(2);
                statisticTimePageFragment.receiveUpdate();
            } else {
                statisticWaterPageFragment.setPosition(2);
                statisticWaterPageFragment.receiveUpdate();
            }
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
        statisticWaterPageFragment.receiveUpdate();
        statisticTimePageFragment.receiveUpdate();
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