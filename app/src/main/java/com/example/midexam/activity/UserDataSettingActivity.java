package com.example.midexam.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.midexam.R;
import com.example.midexam.helper.ScaleHelper;
import com.example.midexam.observer.UserObserver;
import com.example.midexam.presenter.UserPresenter;
import com.soundcloud.android.crop.Crop;

import java.io.File;

public class UserDataSettingActivity extends AppCompatActivity implements UserDataShowInterface {
    private static final String TAG = "UserDataSettingActivity";
    private UserObserver observer;

    private int mode;
    private ConstraintLayout measureXY;

    private EditText newNameView;
    private EditText newTargetView;
    private EditText newIntervalView;
    private ConstraintLayout changeHeadButton;
    private ImageView newHead;
    private ConstraintLayout changeBackgroundButton;
    private ImageView newBackground;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_data_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setNavigationBarColor(getColor(R.color.grey));

        initView();

        initWaterData();
        initImageData();

        initListener();

        overrideBackMethod();
        observer = registerObserver(this);
    }

    private void overrideBackMethod() {
        //重写回退方法
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DesktopActivity.personPage.initUserData();
                DesktopActivity.personPage.clearImageMemoryAndDisk();
                DesktopActivity.personPage.initUserImage();
                finish();
            }
        };
        dispatcher.addCallback(callback);
    }


    @SuppressLint("DefaultLocale")
    private void initListener() {
        changeHeadButton.setOnClickListener(v -> {
            mode = UserPresenter.MODE_HEAD_CROP;
            Crop.pickImage(this);
        });
        changeBackgroundButton.setOnClickListener(v -> {
            mode = UserPresenter.MODE_BACKGROUND_CROP;
            Crop.pickImage(this);
        });
        saveButton.setOnClickListener(v -> {
            int m = Integer.parseInt(newIntervalView.getText().toString());
            int h = m / 60;
            m = m - h * 60;
            if (h > 99) {
                Toast.makeText(this, "提醒间隔最大5999", Toast.LENGTH_SHORT).show();
                return;
            }
            UserPresenter userPresenter = UserPresenter.getInstance(this);
            //更新userData中的name
            userPresenter.setUserName(newNameView.getText().toString());
            //更新target
            userPresenter.setWaterTarget(Integer.parseInt(newTargetView.getText().toString()));
            Log.d(TAG, "waterTarget = " + newTargetView.getText().toString());
            //更新间隔时间
            userPresenter.setIntervalStr(String.format("%02d%02d", h, m));

            userPresenter.updateUserData(this);
            userPresenter.updateUserImage(this);
        });
    }

    private void initView() {
        newNameView = findViewById(R.id.new_name);
        newTargetView = findViewById(R.id.new_target);
        newIntervalView = findViewById(R.id.new_interval);
        changeHeadButton = findViewById(R.id.change_head_button);
        newHead = findViewById(R.id.new_head);
        changeBackgroundButton = findViewById(R.id.change_background_button);
        newBackground = findViewById(R.id.new_background);
        saveButton = findViewById(R.id.save_personal_information);
        measureXY = findViewById(R.id.measure_xy);
    }


    private void initWaterData() {
        UserPresenter userPresenter = UserPresenter.getInstance(this);

        newNameView.setText(userPresenter.getUserName());

        newTargetView.setText(String.valueOf(userPresenter.getWaterTarget()));

        if (userPresenter.getIntervalStr() == null) newIntervalView.setText("0");
        else {
            int h = Integer.parseInt(userPresenter.getIntervalStr().substring(0, 2));
            int m = Integer.parseInt(userPresenter.getIntervalStr().substring(2, 4));
            newIntervalView.setText(String.valueOf(h * 60 + m));
        }
    }

    private void initImageData() {
        UserPresenter userPresenter = UserPresenter.getInstance(this);
        String headImagePath = userPresenter.getHeadImagePath();
        Log.d(TAG, "headImagePath = " + headImagePath);
        Glide.with(this)
                .load(BitmapFactory.decodeFile(headImagePath))
                .error(R.drawable.head)
                .circleCrop()
                .into(newHead);

        String backgroundImagePath = userPresenter.getBackgroundImagePath();
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(ScaleHelper.dp2px(this, 10)));
        Glide.with(this)
                .load(BitmapFactory.decodeFile(backgroundImagePath))
                .error(R.drawable.wave_vertical)
                .apply(options)
                .into(newBackground);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;
        else if (data.getData() == null) return;

        if (requestCode == Crop.REQUEST_PICK && mode == UserPresenter.MODE_HEAD_CROP) {
            Uri destinationUri = Uri.fromFile(new File(UserPresenter.getInstance(this).getHeadImagePath()));
            Crop.of(data.getData(), destinationUri).withAspect(1, 1).start(this);
        } else if (requestCode == Crop.REQUEST_PICK && mode == UserPresenter.MODE_BACKGROUND_CROP) {
            Uri destinationUri = Uri.fromFile(new File(UserPresenter.getInstance(this).getBackgroundImagePath()));
            Crop.of(data.getData(), destinationUri).withAspect(measureXY.getWidth(), measureXY.getHeight()).start(this);
            Log.d(TAG, "x = " + measureXY.getWidth());
            Log.d(TAG, "y = " + measureXY.getHandler());
        }
        clearImageMemoryAndDisk();
        initImageData();
    }

    //清除图片缓存，不然图片更改不成功
    private void clearImageMemoryAndDisk() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(UserDataSettingActivity.this).clearDiskCache();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    initView();
                }
            }
        }).start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.get(UserDataSettingActivity.this).clearMemory();
            }
        });
    }

    @Override
    public void log(int STATUS) {

    }

    @Override
    public void register(int STATUS) {

    }

    @Override
    public void updateUserData(int STATUS) {
        if (STATUS == UserPresenter.STATUS_SUCCESS) {
            Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
            observer.updateObservedViews();
        } else if (STATUS == UserPresenter.STATUS_NO_INTERNET)
            Toast.makeText(this, "无网络", Toast.LENGTH_SHORT).show();
        else if (STATUS == UserPresenter.STATUS_UPDATE_ERROR)
            Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateUserImage(int STATUS) {
    }

    @Override
    public UserObserver registerObserver(UserDataShowInterface observedView) {
        return UserDataShowInterface.super.registerObserver(observedView);
    }
}