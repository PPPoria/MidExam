package com.example.midexam.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.Toast;

import com.example.midexam.R;
import com.example.midexam.activity.LogActivity;
import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.adapter.WaterHistoryAdapter;
import com.example.midexam.observer.UserObserver;
import com.example.midexam.overrideview.BlueWaveView;
import com.example.midexam.overrideview.MeasureXYView;
import com.example.midexam.overrideview.ShallowBlueWaveView;
import com.example.midexam.presenter.UserPresenter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaterFragment extends Fragment implements UserDataShowInterface {
    private static final String TAG = "WaterFragment";
    private View view;

    private MeasureXYView measureXYView;

    private TextView greet;
    private TextView waterName;
    private TextClock waterDate;
    private BlueWaveView blueWave;
    private ShallowBlueWaveView shallowBlueWave;
    private ConstraintLayout drinkButton;
    private TextView waterPercent;
    private TextView waterTarget;
    private TextView waterDrink;

    private RecyclerView waterHistory;
    private WaterHistoryAdapter adapter;
    private final List<String> drinkList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_water, container, false);
        adapter = new WaterHistoryAdapter(requireContext());

        initView();

        initHistory();
        initWaterData();

        initListener();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    receiveUpdate();
                }
            }
        }).start();

        return view;
    }

    private void initHistory() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        waterHistory.setLayoutManager(layoutManager);
        waterHistory.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void drink() {
        int h = LocalDateTime.now().getHour();
        int m = LocalDateTime.now().getMinute();
        Log.d(TAG, "drink: h = " + h + ", m = " + m);
        @SuppressLint("DefaultLocale") String date = String.format("%02d%02d", h, m);
        UserPresenter.getInstance(this).drink(date, 200);
        UserPresenter.getInstance(this).updateUserData(requireContext());
    }

    @SuppressLint("DefaultLocale")
    private void initListener() {
        drinkButton.setOnClickListener(v -> {
            if (!UserPresenter.getInstance(this).isLogged(requireContext())) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            } else drink();
        });
        waterName.setOnClickListener(v -> {
            if (!UserPresenter.getInstance(this).isLogged(requireContext()))
                startActivity(new Intent(getActivity(), LogActivity.class));
        });
    }

    private void initView() {
        greet = view.findViewById(R.id.greet);
        waterName = view.findViewById(R.id.water_name);
        waterDate = view.findViewById(R.id.water_date);
        blueWave = view.findViewById(R.id.blueWaveView);
        shallowBlueWave = view.findViewById(R.id.shallowBlueWaveView);
        drinkButton = view.findViewById(R.id.drink);
        waterPercent = view.findViewById(R.id.water_percent);
        waterTarget = view.findViewById(R.id.water_target);
        waterDrink = view.findViewById(R.id.water_drink);
        waterHistory = view.findViewById(R.id.water_history);
        measureXYView = view.findViewById(R.id.measure_water_xy);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    private void initWaterData() {
        try {
            waterDate.setFormat24Hour("M/dd H:mm");
        } catch (Exception e) {
        }

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

            String nameValue = userPresenter.getUserName();
            int targetValue = userPresenter.getWaterTarget();
            int drinkValue = userPresenter.getWaterDrink();
            int percentVale = (int) (100f * drinkValue / targetValue);
            if (percentVale > 100) percentVale = 100;

            waterName.setText(nameValue);
            waterTarget.setText(targetValue + "ml");
            waterDrink.setText(drinkValue + "ml");
            if (percentVale < 20f) waterDrink.setTextColor(Color.BLACK);
            else waterDrink.setTextColor(Color.WHITE);
            waterPercent.setText(percentVale + "%");

            adapter.setDrinkList(userPresenter.getWaterToday());
            Objects.requireNonNull(adapter).notifyDataSetChanged();

            float d = Math.min(measureXYView.getMeasureX() / 2f, measureXYView.getMeasureY()) - 100f;
            Log.d(TAG, "d = " + d);
            blueWave.targetY = d * (1 - percentVale / 100f);
            shallowBlueWave.targetY = (d - 50f) * (1 - percentVale / 100f);
        } else {
            adapter.setDrinkList(null);
            Objects.requireNonNull(adapter).notifyDataSetChanged();

            waterName.setText("请先登录/注册");
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
        if (STATUS == UserPresenter.STATUS_SUCCESS) {
            receiveUpdate();
        } else {
            Toast.makeText(requireContext(), "网络错误", Toast.LENGTH_SHORT).show();
        }
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
        try {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initWaterData();
                }
            });
        } catch (Exception e) {
        }
    }
}