package com.example.midexam.activity;

import com.example.midexam.observer.UserObserver;

public interface UserDataShowInterface {
    void log(int STATUS);

    void register(int STATUS);

    void updateUserData(int STATUS);

    void updateUserImage(int STATUS);
    default void registerObserver(UserDataShowInterface observedView){
        UserObserver.registerObserver(observedView);
    };
}
