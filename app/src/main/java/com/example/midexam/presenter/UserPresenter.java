package com.example.midexam.presenter;

import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.helper.Api;
import com.example.midexam.model.UserData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPresenter {
    public String baseUrl = "https://what/";
    public static UserData userData;
    public UserDataShowInterface activity;
    public static UserPresenter presenter = new UserPresenter();

    public static final int STATUS_SUCCESS = 100;
    public static final int STATUS_NO_INTERNET = 0;
    public static final int STATUS_ACCOUNT_ALREADY_EXIST = 1;
    public static final int STATUS_ACCOUNT_NOT_EXIST = 2;
    public static final int STATUS_PASSWORDS_INCONSISTENT = 3;
    public static final int STATUS_PASSWORD_INCORRECT = 4;
    public static final int STATUS_ACCOUNT_OR_PASSWORD_NOT_SATISFIABLE = 5;
    public static final int STATUS_UPDATE_ERROR = 6;

    public static UserPresenter getInstance(UserDataShowInterface activity) {
        presenter.activity = activity;
        return presenter;
    }

    public static UserData getUserDataInstance(UserDataShowInterface activity) {
        presenter.activity = activity;
        return userData;
    }

    //获取登录状态，并通过STATUS状态码回调UserDataShowInterface的函数
    public void requestLog(String account, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<UserData> dataCall = api.getUserData("log", account, password);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                UserData tempData = response.body();
                if (tempData == null) activity.log(STATUS_ACCOUNT_NOT_EXIST);
                else if (tempData.getMsg().equals("0")) activity.log(STATUS_ACCOUNT_NOT_EXIST);
                else {
                    userData = tempData;
                    activity.log(STATUS_SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable throwable) {
                activity.log(STATUS_NO_INTERNET);
            }
        });
    }

    //获取注册状态，并通过STATUS状态码回调UserDataShowInterface的函数
    public void requestRegister(String account, String password, String passwordAgain) {
        if (account.length() < 8 || password.length() < 8 || passwordAgain.length() < 8) {
            activity.register(STATUS_ACCOUNT_OR_PASSWORD_NOT_SATISFIABLE);
            return;
        } else if (!password.equals(passwordAgain)) {
            activity.register(STATUS_PASSWORDS_INCONSISTENT);
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<UserData> dataCall = api.getUserData("register", account, password);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                UserData tempData = response.body();
                if (tempData == null) activity.register(STATUS_NO_INTERNET);
                else if (tempData.getMsg().equals("0"))
                    activity.register(STATUS_ACCOUNT_ALREADY_EXIST);
                else {
                    userData = tempData;
                    activity.register(STATUS_SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable throwable) {
                activity.register(STATUS_NO_INTERNET);
            }
        });
    }

    //更新图片
    public void updateUserImage(String account, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        List<MultipartBody.Part> list = new ArrayList<>();
        RequestBody requestFile;
        MultipartBody.Part body;

        File headFile = new File("app/src/main/res/drawable/head.jpg");
        requestFile = RequestBody.create(MediaType.parse("image/*"), headFile);
        body = MultipartBody.Part.createFormData("head", headFile.getName(), requestFile);
        list.add(body);

        File backgroundFile = new File("app/src/main/res/drawable/background.jpg");
        requestFile = RequestBody.create(MediaType.parse("image/*"), headFile);
        body = MultipartBody.Part.createFormData("background", backgroundFile.getName(), requestFile);
        list.add(body);

        Call<UserData> dataCall = api.updateUserImage("updateImage", account, password, list);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                UserData tempData = response.body();
                if (tempData == null) activity.updateUserImage(STATUS_UPDATE_ERROR);
                else if (tempData.getMsg().equals("0"))
                    activity.updateUserImage(STATUS_UPDATE_ERROR);
                else {
                    userData = tempData;
                    activity.register(STATUS_SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable throwable) {
                activity.updateUserImage(STATUS_NO_INTERNET);
            }
        });
    }

    //更新数据
    public void updateUserData(String account, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<UserData> dataCall = api.updateUserData("updateData", account, password, userData);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                UserData tempData = response.body();
                if (tempData == null) activity.updateUserData(STATUS_UPDATE_ERROR);
                else if (tempData.getMsg().equals("0"))
                    activity.updateUserData(STATUS_UPDATE_ERROR);
                else {
                    userData = tempData;
                    activity.updateUserData(STATUS_SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable throwable) {
                activity.updateUserData(STATUS_NO_INTERNET);
            }
        });
    }

    //追踪数据，如果不一致则令客户端与服务端一致
    public void trackUserData(String account, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<UserData> dataCall = api.updateUserData("trackData", account, password, userData);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                UserData tempData = response.body();
                if (tempData == null) activity.trackUserData(STATUS_NO_INTERNET);
                else if (tempData.getMsg().equals("0"))
                    activity.trackUserData(STATUS_NO_INTERNET);
                else {
                    userData = tempData;
                    activity.trackUserData(STATUS_SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable throwable) {
                activity.trackUserData(STATUS_NO_INTERNET);
            }
        });
    }
}
