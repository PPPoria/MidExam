package com.example.midexam.presenter;

import android.content.Context;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.midexam.helper.Api;
import com.example.midexam.model.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPresenter {
    public UserData userData;
    public Context context;
    public static UserPresenter presenter = new UserPresenter();

    public static UserPresenter getInstance(Context context) {
        presenter.context = context;
        return presenter;
    }

    //没有获取成功就返回false
    public boolean getLogCallback(String account, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.uomg.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<UserData> dataCall = api.getUserData("log", account, password);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                userData = response.body();
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable throwable) {

            }
        });

        return !userData.getMsg().equals("0");
    }
}
