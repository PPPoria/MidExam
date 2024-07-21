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

    public static final int STATUS_NO_INTERNET = 0;
    public static final int STATUS_ACCOUNT_ALREADY_EXIST = 1;
    public static final int STATUS_ACCOUNT_NOT_EXIST = 2;
    public static final int STATUS_PASSWORDS_INCONSISTENT = 3;
    public static final int STATUS_PASSWORD_INCORRECT = 4;
    public static final int STATUS_ACCOUNT_OR_PASSWORD_NOT_SATISFIABLE = 5;

    public static UserPresenter getInstance(UserDataShowInterface activity) {
        presenter.activity = activity;
        return presenter;
    }

    //获取登录状态，没有获取成功就返回false
    public void requestLog(String account, String password) {
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
                if (userData == null) activity.logFail(STATUS_ACCOUNT_NOT_EXIST);
                else if (userData.getMsg().equals("0")) activity.logFail(STATUS_ACCOUNT_NOT_EXIST);
                else activity.logSuccess(STATUS_ACCOUNT_ALREADY_EXIST);
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable throwable) {
                activity.logFail(STATUS_NO_INTERNET);
            }
        });
    }

    public void requestRegister(String account, String password, String passwordAgain) {
        if (account.length() < 8 || password.length() < 8 || passwordAgain.length() < 8) {
            activity.registerFail(STATUS_ACCOUNT_OR_PASSWORD_NOT_SATISFIABLE);
            return;
        }
        else if (!password.equals(passwordAgain)) {
            activity.registerFail(STATUS_PASSWORDS_INCONSISTENT);
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://a/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<UserData> dataCall = api.getUserData("register", account, password);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                userData = response.body();
                if (userData == null) activity.registerFail(STATUS_NO_INTERNET);
                else if (userData.getMsg().equals("0")) activity.registerFail(STATUS_ACCOUNT_ALREADY_EXIST);
                else activity.registerSuccess(STATUS_ACCOUNT_ALREADY_EXIST);
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable throwable) {
                activity.registerFail(STATUS_NO_INTERNET);
            }
        });
    }
}
