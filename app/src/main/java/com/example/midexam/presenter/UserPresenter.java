package com.example.midexam.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

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
    private static final String TAG = "UserPresenter";
    public String baseUrl = "http://192.168.31.176:8080/";
    public String backgroundImagePath;
    public String headImagePath;
    public UserData userData;
    public UserDataShowInterface activity;
    public static UserPresenter presenter = new UserPresenter();

    public static final int MODE_HEAD_CROP = 98;
    public static final int MODE_BACKGROUND_CROP = 99;
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

    public String getAccount(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sp.getString("account", "null");
    }

    public void changeName(String newName){
        userData.setName(newName);
    }

    public String getUserName(){
        return userData.getName();
    }

    public void accordAccount(Context context, String account) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor ed = sp.edit();
        ed.putString("account", account);
    }

    public String getPassword(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sp.getString("password", "null");
    }

    public void accordPassword(Context context, String password) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor ed = sp.edit();
        ed.putString("password", password);
    }

    public boolean isLogged(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sp.getBoolean("isLogged", false);
    }

    public void accordLoggedStatus(Context context, boolean isLogged) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("isLogged", isLogged);
    }

    //获取登录状态，并通过STATUS状态码调用UserDataShowInterface实现类的函数
    public void requestLog(Context context, String account, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<UserData> dataCall = api.log(account, password);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                UserData tempData = response.body();
                if (tempData == null) activity.log(STATUS_ACCOUNT_NOT_EXIST);
                else if ("accountNotExist".equals(tempData.getMsg()))
                    activity.log(STATUS_ACCOUNT_NOT_EXIST);
                else if (tempData.getMsg().equals("passwordIncorrect"))
                    activity.log(STATUS_PASSWORD_INCORRECT);
                else {
                    accordAccount(context, account);
                    accordPassword(context, password);
                    accordLoggedStatus(context, true);
                    userData = tempData;
                    userData.setAccount(account);
                    activity.log(STATUS_SUCCESS);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable throwable) {
                activity.log(STATUS_NO_INTERNET);
            }
        });
    }

    //获取注册状态，并通过STATUS状态码回调UserDataShowInterface的函数
    public void requestRegister(Context context, String account, String password, String passwordAgain) {
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

        Call<UserData> dataCall = api.register(account, password);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                UserData tempData = response.body();
                if (tempData == null) activity.register(STATUS_NO_INTERNET);
                else if (tempData.getMsg().equals("accountAlreadyExist"))
                    activity.register(STATUS_ACCOUNT_ALREADY_EXIST);
                else {
                    accordAccount(context, account);
                    accordPassword(context, password);
                    accordLoggedStatus(context, true);
                    userData = tempData;
                    userData.setAccount(account);
                    activity.register(STATUS_SUCCESS);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable throwable) {
                activity.register(STATUS_NO_INTERNET);
            }
        });
    }

    //更新图片
    public void updateUserImage(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        File headFile = new File(getHeadImagePath());
        RequestBody requestHead = RequestBody.create(MediaType.parse("image/*"), headFile);
        MultipartBody.Part head = MultipartBody.Part.createFormData("imageA", headFile.getName(), requestHead);

        File backgroundFile = new File(getBackgroundImagePath());
        RequestBody requestBackground = RequestBody.create(MediaType.parse("image/*"), headFile);
        MultipartBody.Part background = MultipartBody.Part.createFormData("imageB", backgroundFile.getName(), requestBackground);

        RequestBody accountBody = RequestBody.create(MediaType.parse("text/plain"), userData.getAccount());

        Call<UserData> dataCall = api.updateImage(userData.getMsg(), accountBody, head, background);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                UserData tempData = response.body();
                if (tempData == null) activity.updateUserImage(STATUS_UPDATE_ERROR);
                else if ("updateError".equals(userData.getMsg()))
                    activity.updateUserImage(STATUS_UPDATE_ERROR);
                else {
                    activity.updateUserImage(STATUS_SUCCESS);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable throwable) {
                activity.updateUserImage(STATUS_NO_INTERNET);
            }
        });
    }

    //更新数据
    public void updateUserData(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Log.d(TAG, "update msg = " + userData.getMsg());
        Call<UserData> dataCall = api.updateData(userData.getMsg(), userData);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                UserData tempData = response.body();
                if (tempData == null) activity.updateUserData(STATUS_UPDATE_ERROR);
                else if ("updateError".equals(userData.getMsg()))
                    activity.updateUserData(STATUS_UPDATE_ERROR);
                else {
                    activity.updateUserData(STATUS_SUCCESS);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable throwable) {
                System.out.println(throwable.toString());
                activity.updateUserData(STATUS_NO_INTERNET);
            }
        });
    }

    //路径初始化
    public void initImagesPath(Context context) {
        headImagePath = context.getFilesDir().getAbsolutePath() + "/headImage.jpg";
        backgroundImagePath = context.getFilesDir().getAbsolutePath() + "/backgroundImage.jpg";
    }

    public String getHeadImagePath() {
        return headImagePath;
    }

    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }
}
