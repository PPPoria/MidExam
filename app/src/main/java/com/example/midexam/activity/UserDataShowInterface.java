package com.example.midexam.activity;

import com.example.midexam.observer.UserObserver;

public interface UserDataShowInterface {
    UserObserver userObserver = null;
    void log(int STATUS);

    void register(int STATUS);

    void updateUserData(int STATUS);

    void updateUserImage(int STATUS);

    default UserObserver registerObserver(UserDataShowInterface observedView){
        return UserObserver.registerObserver(observedView);
    }

    default void receiveUpdate(){
        //TODO 当数据更新的时候，被观察的对象的数据应该改变
    }
}
