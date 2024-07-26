package com.example.midexam.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.midexam.R;
import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.adapter.WaterHistoryAdapter;
import com.example.midexam.observer.UserObserver;
import com.example.midexam.overrideview.AddButton;
import com.example.midexam.overrideview.BlueWaveView;
import com.example.midexam.overrideview.ShallowBlueWaveView;
import com.example.midexam.presenter.UserPresenter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaterFragment extends Fragment implements UserDataShowInterface {
    private static final String TAG = "WaterFragment";
    private View view;
    private UserObserver observer;

    private TextView greet;
    private TextView waterName;
    private TextClock waterDate;
    private BlueWaveView blueWave;
    private ShallowBlueWaveView shallowBlueWave;
    private ConstraintLayout addButton;
    private TextView waterPercent;
    private ConstraintLayout targetLayout;
    private TextView waterTarget;
    private TextView waterDrink;

    private RecyclerView waterHistory;
    private List<String> waterList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_water, container, false);
        observer = UserObserver.registerObserver(this);

        initView();
        initHistory();
        initWaterData();
        initListener();
        return view;
    }

    private void initHistory() {
        waterList.add("000");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        waterHistory.setLayoutManager(layoutManager);
        waterHistory.setAdapter(new WaterHistoryAdapter(getContext(), waterList));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addWater() {
        waterList.add("over");
        Objects.requireNonNull(waterHistory.getAdapter()).notifyDataSetChanged();
    }

    private void initListener() {
        addButton.setOnClickListener(v -> {
            addWater();
        });
        targetLayout.setOnClickListener(v -> {

        });
    }

    private void initView() {
        greet = view.findViewById(R.id.greet);
        waterName = view.findViewById(R.id.water_name);
        waterDate = view.findViewById(R.id.water_date);
        blueWave = view.findViewById(R.id.blueWaveView);
        shallowBlueWave = view.findViewById(R.id.shallowBlueWaveView);
        addButton = view.findViewById(R.id.drink);
        waterPercent = view.findViewById(R.id.water_percent);
        targetLayout = view.findViewById(R.id.water_target_layout);
        waterTarget = view.findViewById(R.id.water_target);
        waterDrink = view.findViewById(R.id.water_drink);
        waterHistory = view.findViewById(R.id.water_history);
    }

    @SuppressLint("SetTextI18n")
    private void initWaterData() {
        waterDate.setFormat24Hour("M/dd H:mm");

        int hour = LocalDateTime.now().getHour();
        Log.d(TAG, "hour = " + hour);
        if (hour < 4)
            greet.setText("凌晨");
        else if (hour < 11)
            greet.setText("早上好");
        else if (hour < 13)
            greet.setText("中午好");
        else if (hour < 18)
            greet.setText("下午好");
        else if (hour < 21)
            greet.setText("晚上好");
        else if (hour < 24)
            greet.setText("夜深了");



        if (UserPresenter.getInstance(this).isLogged(requireContext())) {
            UserPresenter userPresenter = UserPresenter.getInstance(this);

            String nameValue = "游客";
            int targetValue = 0;
            int drinkValue = 0;
            int percentVale = 0;

            nameValue = userPresenter.getUserName();
            targetValue = userPresenter.getWaterTarget();
            drinkValue = userPresenter.getWaterDrink();
            percentVale = (int) (100f * drinkValue / targetValue);
            if (percentVale > 100) percentVale = 100;

            waterName.setText(nameValue);
            waterTarget.setText(targetValue + "ml");
            waterDrink.setText(drinkValue +"ml");
            waterPercent.setText(percentVale + "%");

            blueWave.y = blueWave.d * (1 - percentVale/100f);
            shallowBlueWave.y = shallowBlueWave.d * (1 - percentVale/100f);
        } else {
            waterName.setText("游客");
            waterTarget.setText("0ml");
            waterDrink.setText("0ml");
            waterPercent.setText("0%");
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
        UserDataShowInterface.super.receiveUpdate();
    }
}