package com.example.midexam.observer;

import com.example.midexam.activity.UserDataShowInterface;

import java.util.ArrayList;
import java.util.List;

public class UserObserver {
    public static UserObserver observer = new UserObserver();
    private static List<UserDataShowInterface> observedViewList = new ArrayList<>();

    //public static UserObserver getInstance(){};

    public static UserObserver registerObserver(UserDataShowInterface observedView) {
        observedViewList.add(observedView);
        return observer;
    }
}
