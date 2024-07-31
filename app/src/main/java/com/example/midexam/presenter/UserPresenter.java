package com.example.midexam.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.midexam.R;
import com.example.midexam.activity.DesktopActivity;
import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.helper.Api;
import com.example.midexam.model.UserData;
import com.example.midexam.model.WeatherData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

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
    public String baseUrl = "http://47.113.224.195:31112/";
    public String token;
    public String backgroundImagePath;
    public String headImagePath;
    public UserData userData;
    public WeatherData weatherData;
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
    public static final int STATUS_HEART_START = 7;
    public static final int STATUS_HEART_WAIT = 8;
    public static final int STATUS_HEART_FINISH = 9;


    public static UserPresenter getInstance(UserDataShowInterface activity) {
        presenter.activity = activity;
        return presenter;
    }

    //判断上次登录是否为今天
    //上次登录是今天，返回false；否则true。--------别搞混了
    public boolean isNextDay(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor ed = sp.edit();

        String date = sp.getString("date", null);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String tempDate = month + "月" + day + "日";

        if (date == null) {//判读是否未app安装后首次启动，且必须先判断date是否为空，不然的话会导致空指针错误
            date = tempDate;
            ed.putString("date", date);
            ed.commit();
            return true;
        } else if (!date.equals(tempDate)) {//判断是否隔日打开app，如果是则更新luckyValue
            date = tempDate;
            ed.putString("date", date);
            ed.commit();
            return true;
        } else return false;
    }

    public int getWaterTarget() {
        if(userData == null) return 0;
        return userData.getWaterTarget();
    }

    public void setWaterTarget(int waterTarget) {
        userData.setWaterTarget(waterTarget);
    }

    public int getWaterDrink() {
        if(userData == null) return 0;
        return userData.getWaterDrink();
    }

    public void setWaterDrink(int waterDrink) {
        userData.setWaterDrink(waterDrink);
    }

    public List<String> getWaterToday() {
        if (userData == null) return new ArrayList<>();
        if (userData.getWaterToday() == null) {
            userData.setWaterToday(new ArrayList<>());
        }
        return userData.getWaterToday();
    }

    public List<String> getWaterPerDay() {
        if (userData == null) return new ArrayList<>();
        if (userData.getWaterPerDay() == null) {
            userData.setWaterPerDay(new ArrayList<>());
        }
        return userData.getWaterPerDay();
    }

    public List<String> getWaterPerMonth() {
        if (userData == null) return new ArrayList<>();
        if (userData.getWaterPerMonth() == null) {
            userData.setWaterPerMonth(new ArrayList<>());
        }
        return userData.getWaterPerMonth();
    }

    public void drink(String date, int drinkValue) {
        int waterDrink = getWaterDrink();
        waterDrink += drinkValue;
        setWaterDrink(waterDrink);

        getWaterToday().add(date + drinkValue);
    }

    public List<String> getFinishJobs() {
        if (userData == null) return new ArrayList<>();
        if (userData.getFinishJobs() == null) {
            userData.setFinishJobs(new ArrayList<>());
        }
        return userData.getFinishJobs();
    }

    public List<String> getJobs() {
        if (userData == null) return new ArrayList<>();
        if (userData.getJobs() == null) {
            userData.setJobs(new ArrayList<>());
        }
        return userData.getJobs();
    }

    public String getUserName() {
        if (userData == null) return "";
        String name = userData.getName();
        if (name == null) return "新用户";
        else return userData.getName();
    }

    public void setUserName(String userName) {
        userData.setName(userName);
    }

    public String getIntervalStr() {
        return userData.getIntervalTime();
    }

    public void setIntervalStr(String hhmm) {
        userData.setIntervalTime(hhmm);
    }

    public String getAccount(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sp.getString("account", null);
    }

    public void accordAccount(Context context, String account) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor ed = sp.edit();
        ed.putString("account", account);
        ed.commit();
    }

    public String getPassword(Context context) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sp.getString("password", "null");
    }

    public void accordPassword(Context context, String password) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor ed = sp.edit();
        ed.putString("password", password);
        ed.commit();
    }

    //获取登录状态，已登录则返回true
    public boolean isLogged(Context context) {
        try {
            SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
            return sp.getBoolean("isLogged", false);
        } catch (Exception e) {

        }
        return false;
    }

    //登陆状态记录
    public void accordLoggedStatus(Context context, boolean isLogged) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("isLogged", isLogged);
        ed.commit();
    }


    //================================================================//


    //获取登录状态，并通过STATUS状态码调用UserDataShowInterface实现类的log(int STATUS)方法
    /*
    TODO 推荐UserPresenter.getInstance(this).requestLog(mContext, account, password)

    TODO **如果存在需要连续使用UserPresenter内的方法的情况（即可能需要多次使用且确保其他实现类或方法此时不会使用的情况），
    TODO **允许单独获取UserPresenter实例mUserPresenter，以实现mUserPresenter.requestLog(account, password)
    TODO **完成业务后，应该销毁mPresenter，禁止跨方法复用

    TODO STATUS有四种 使用时应该在实现类的对应方法里根据STATUS做出相应处理以提醒用户
    STATUS_ACCOUNT_NOT_EXIST--------账号不存在
    STATUS_PASSWORD_INCORRECT--------账号存在但密码不匹配
    STATUS_SUCCESS--------登录成功
    STATUS_NO_INTERNET--------无网络，请求失败
     */
    public void requestLog(Context context, String account, String password) {
        Log.d(TAG, "requestLog: 登录请求");
        Log.d(TAG, "account = " + account + ", password = " + password);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Log.d(TAG, "baseUrl = " + baseUrl);

        Call<UserData> dataCall = api.log(account, password);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                UserData tempData = response.body();
                if (tempData == null) {
                    activity.log(STATUS_ACCOUNT_NOT_EXIST);
                    accordLoggedStatus(context, false);
                } else if ("accountNotExist".equals(tempData.getMsg()))
                    activity.log(STATUS_ACCOUNT_NOT_EXIST);
                else if ("passwordIncorrect".equals(tempData.getMsg()))
                    activity.log(STATUS_PASSWORD_INCORRECT);
                else {
                    accordAccount(context, account);
                    accordPassword(context, password);
                    accordLoggedStatus(context, true);
                    userData = tempData;
                    userData.setAccount(account);
                    token = userData.getMsg();
                    activity.log(STATUS_SUCCESS);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable throwable) {
                Log.d(TAG, throwable.toString());
                activity.log(STATUS_NO_INTERNET);
            }
        });
    }

    //获取注册状态，并通过STATUS状态码调用UserDataShowInterface实现类的register(int STATUS)方法
    /*
    TODO STATUS有五种
    STATUS_ACCOUNT_OR_PASSWORD_NOT_SATISFIABLE--------格式错误，账号密码小于八位
    STATUS_PASSWORDS_INCONSISTENT--------两次输入的密码不一致
    STATUS_ACCOUNT_ALREADY_EXIST--------账号已存在
    STATUS_SUCCESS--------成功
    STATUS_NO_INTERNET--------无网络
     */
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
                else if ("accountAlreadyExist".equals(tempData.getMsg()))
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

    //获取更新图片状态，并通过STATUS状态码调用UserDataShowInterface实现类的updateUserImage(int STATUS)方法
    /*
    TODO STATUS有三种
    STATUS_UPDATE_ERROR--------未知错误
    STATUS_SUCCESS--------成功
    STATUS_NO_INTERNET--------无网络
     */
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
                else if ("updateError".equals(tempData.getMsg()))
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

    //获取更新数据状态，并通过STATUS状态码调用UserDataShowInterface实现类的updateUserData(int STATUS)方法
    /*
    TODO STATUS有三种
    STATUS_UPDATE_ERROR--------未知错误
    STATUS_SUCCESS--------成功
    STATUS_NO_INTERNET--------无网络
     */
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
                else if ("updateError".equals(tempData.getMsg()))
                    activity.updateUserData(STATUS_UPDATE_ERROR);
                else {
                    activity.updateUserData(STATUS_SUCCESS);
                    Log.d(TAG, "update success");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable throwable) {
                activity.updateUserData(STATUS_NO_INTERNET);
            }
        });
    }

    public void heart(Context context, DesktopActivity desktopActivity) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<UserData> dataCall = api.heart(userData.getMsg(), userData.getAccount());

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                UserData tempData = response.body();
                if (tempData == null) desktopActivity.updateUserData(STATUS_NO_INTERNET);
                else if ("start".equals(tempData.getMsg()))
                    desktopActivity.heartCallback(STATUS_HEART_START);
                else if ("wait".equals(tempData.getMsg())) {
                    desktopActivity.heartCallback(STATUS_HEART_WAIT);
                } else {
                    desktopActivity.heartCallback(STATUS_HEART_FINISH);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable throwable) {
                desktopActivity.updateUserData(STATUS_NO_INTERNET);
            }
        });
    }


    //================================================================//


    //路径初始化
    public void initImagesPath(Context context) {
        headImagePath = context.getFilesDir().getAbsolutePath() + "/headImage.jpg";
        backgroundImagePath = context.getFilesDir().getAbsolutePath() + "/backgroundImage.jpg";
    }

    //获取头像的文件路径
    public String getHeadImagePath() {
        return headImagePath;
    }

    //获取背景图片的文件路径
    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }

    //重置图片,谨慎使用
    public void resetHeadImage() {
        File image = new File(headImagePath);
        if (!image.exists())
            try {
                image.createNewFile();
            } catch (IOException e) {
            }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(image);
            fos.write(new byte[0]);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "焯！");
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    //重置图片,谨慎使用
    public void resetBackgroundImage() {
        File image = new File(backgroundImagePath);
        if (!image.exists())
            try {
                image.createNewFile();
            } catch (IOException e) {
            }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(image);
            fos.write(new byte[0]);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "焯！");
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public String getCity() {
        if (weatherData == null) return "未知";
        return weatherData.getCity();
    }

    public String getWeather() {
        if (weatherData == null) return null;
        return weatherData.getWea_img();
    }

    public void requestWeather() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://v1.yiketianqi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<WeatherData> dataCall = api.getWeather(1, "v61", 56126631, "Mpl05lHH");

        dataCall.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                WeatherData tempData = response.body();
                if (tempData == null) return;
                weatherData = tempData;
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable throwable) {

            }
        });
    }

    public void postWeather(int weatherId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        String token = "null";
        if (userData != null) token = userData.getMsg();
        Call<UserData> dataCall = api.postWeather(token, weatherId);

        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                Log.d("天气", "post weather success!");
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable throwable) {
                Log.d("天气", "post weather fail!");
            }
        });
    }
}
