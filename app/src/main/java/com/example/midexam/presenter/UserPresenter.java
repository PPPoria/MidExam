package com.example.midexam.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.helper.Api;
import com.example.midexam.model.UserData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public String baseUrl = "https://what/";
    public String backgroundImagePath;
    public String headImagePath;
    public UserData userData;
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

    public String getAccount(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sp.getString("account", "null");
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
                else if (tempData.getMsg().equals("0")) activity.log(STATUS_ACCOUNT_NOT_EXIST);
                else {
                    accordAccount(context, account);
                    accordPassword(context, password);
                    accordLoggedStatus(context, true);
                    userData = tempData;
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
                else if (tempData.getMsg().equals("0"))
                    activity.register(STATUS_ACCOUNT_ALREADY_EXIST);
                else {
                    accordAccount(context, account);
                    accordPassword(context, password);
                    accordLoggedStatus(context, true);
                    userData = tempData;
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

        RequestBody accountBody = RequestBody.create(MediaType.parse("text/plain"), getAccount(context));

        Call<UserData> dataCall = api.updateImage(accountBody, list);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
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

        Call<UserData> dataCall = api.updateData(getAccount(context), getPassword(context), userData);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
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
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable throwable) {
                activity.updateUserData(STATUS_NO_INTERNET);
            }
        });
    }

    //路径初始化
    public void initImagesPath(Context context) {
        headImagePath = context.getFilesDir().getAbsolutePath() + "/headImage.jpg";
        backgroundImagePath = context.getFilesDir().getAbsolutePath() + "/backgroundImage.jpg";
    }


    //bitmap的占用内存很大，在此不设置全局变量
    public void saveImage(Bitmap bitmap, int MODE) throws IOException {
        File headImage = new File(headImagePath);
        Log.d(TAG, "开始保存图片");
        if (!headImage.exists())
            try {
                headImage.createNewFile();
                Log.d(TAG, "首次设置头像");
            } catch (IOException e) {
                Log.d(TAG, "???");
            }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(headImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Log.d(TAG, "保存成功");
        } catch (IOException e) {
            Log.d(TAG, "焯！头像修改失败\nheadImage = " + headImage.getAbsolutePath());
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                }
        }
    }

    public String getHeadImagePath() {
        return headImagePath;
    }

    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }
}
