package com.example.midexam.presenter;

import android.content.Context;

import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.helper.Api;
import com.example.midexam.model.UserData;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPresenter {
    public UserData userData;
    public UserDataShowInterface activity;
    public static UserPresenter presenter = new UserPresenter();

    public static UserPresenter getInstance(UserDataShowInterface activity) {
        presenter.activity = activity;
        return presenter;
    }

    //获取登录状态，没有获取成功就返回false
    public void requestLog(String account, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://a/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<UserData> dataCall = api.getUserData("log", account, password);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                userData = response.body();
                if(userData == null) activity.logFail();
                else if (userData.getMsg().equals("0")) activity.logFail();
                else activity.logSuccess();
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable throwable) {
                activity.errorInternet();
            }
        });
    }
}
