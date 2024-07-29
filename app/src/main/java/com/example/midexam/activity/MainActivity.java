package com.example.midexam.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.midexam.R;
import com.example.midexam.presenter.UserPresenter;

public class MainActivity extends AppCompatActivity implements UserDataShowInterface {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setNavigationBarColor(getColor(R.color.blue));

        UserPresenter userPresenter = UserPresenter.getInstance(this);
        userPresenter.initImagesPath(this);
        userPresenter.requestWeather();


        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!userPresenter.isLogged(MainActivity.this)) {
                    userPresenter.resetHeadImage();
                    userPresenter.resetBackgroundImage();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } finally {
                                startActivity(new Intent(MainActivity.this, DesktopActivity.class));
                                finish();
                            }
                        }
                    }).start();
                } else {
                    String account = userPresenter.getAccount(MainActivity.this);
                    String password = userPresenter.getPassword(MainActivity.this);
                    userPresenter.requestLog(MainActivity.this, account, password);
                }
            }
        }).start();
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void log(int STATUS) {
        if (STATUS == UserPresenter.STATUS_NO_INTERNET) {
            Toast.makeText(this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
            UserPresenter.getInstance(this).accordLoggedStatus(this, false);
        } else if (STATUS == UserPresenter.STATUS_SUCCESS)
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            if(UserPresenter.getInstance(this).isNextDay(this)) {
                UserPresenter.getInstance(this).getWaterToday().clear();
                UserPresenter.getInstance(this).setWaterDrink(0);
            }

        startActivity(new Intent(MainActivity.this, DesktopActivity.class));
        finish();

    }

    @Override
    public void register(int STATUS) {

    }

    @Override
    public void updateUserData(int STATUS) {

    }

    @Override
    public void updateUserImage(int STATUS) {

    }
}