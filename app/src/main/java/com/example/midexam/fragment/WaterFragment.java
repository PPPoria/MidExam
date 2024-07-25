package com.example.midexam.fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.midexam.R;
import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.adapter.WaterHistoryAdapter;
import com.example.midexam.observer.UserObserver;
import com.example.midexam.overrideview.AddButton;
import com.example.midexam.overrideview.BlueWaveView;
import com.example.midexam.overrideview.ShallowBlueWaveView;

import java.util.ArrayList;
import java.util.List;

public class WaterFragment extends Fragment implements UserDataShowInterface {
    private static final String TAG = "WaterFragment";
    private View view;
    private UserObserver observer;
    private BlueWaveView blueWave;
    private ShallowBlueWaveView shallowBlueWave;
    private ConstraintLayout addButton;
    private TextView waterPercent;
    private ConstraintLayout targetLayout;
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
        initListener();
        return view;
    }

    private void initHistory() {
        waterList.add("000");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        waterHistory.setLayoutManager(layoutManager);
        waterHistory.setAdapter(new WaterHistoryAdapter(getContext(), waterList));
    }

    private void addWater(){
        waterList.add("over");
        waterHistory.getAdapter().notifyDataSetChanged();
    }

    private void initListener() {
        addButton.setOnClickListener(v -> {
            addWater();
        });
    }

    private void initView() {
        blueWave = view.findViewById(R.id.blueWaveView);
        shallowBlueWave = view.findViewById(R.id.shallowBlueWaveView);
        addButton = view.findViewById(R.id.drink);
        waterPercent = view.findViewById(R.id.water_percent);
        targetLayout = view.findViewById(R.id.water_target_layout);
        waterHistory = view.findViewById(R.id.water_history);
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