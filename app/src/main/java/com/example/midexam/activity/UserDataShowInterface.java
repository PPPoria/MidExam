package com.example.midexam.activity;

import com.example.midexam.observer.UserObserver;

public interface UserDataShowInterface {
    UserObserver userObserver = null;

    //TODO 以下四个方法均为回调方法，如果需要使用，可以查看UserPresenter
    void log(int STATUS);

    void register(int STATUS);

    void updateUserData(int STATUS);

    void updateUserImage(int STATUS);

    //TODO 注册观察者
    default UserObserver registerObserver(UserDataShowInterface observedView) {
        return UserObserver.registerObserver(observedView);
    }

    //TODO 实现类必须注册了观察者，才可能被调用这个方法
    //TODO 当UserData数据更新了的时候，此方法会被调用
    default void receiveUpdate() {
    }
}
