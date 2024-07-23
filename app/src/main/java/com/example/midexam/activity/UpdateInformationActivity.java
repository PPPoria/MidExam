package com.example.midexam.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
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

public class UpdateInformationActivity extends AppCompatActivity implements UserDataShowInterface {
    private static final String TAG = "UserDataSettingActivity";
    private UserObserver observer;

    private int mode;
    private ConstraintLayout measureXY;

    private EditText newNameView;
    private ConstraintLayout changeHeadButton;
    private ImageView newHead;
    private ConstraintLayout changeBackgroundButton;
    private ImageView newBackground;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setNavigationBarColor(getColor(R.color.grey));

        initView();
        initUserDataInformation();
        initListener();
        observer = registerObserver(this);
        overrideBackMethod();
    }

    //重写回退方法
    private void overrideBackMethod() {
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                observer.updateObservedViews();
                finish();
            }
        };
        dispatcher.addCallback(callback);
    }


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
            UserPresenter userPresenter = UserPresenter.getInstance(this);
            userPresenter.updateUserData(this);
            userPresenter.updateUserImage(this);
        });
    }

    private void initView() {
        newNameView = findViewById(R.id.new_name);
        changeHeadButton = findViewById(R.id.change_head_button);
        newHead = findViewById(R.id.new_head);
        changeBackgroundButton = findViewById(R.id.change_background_button);
        newBackground = findViewById(R.id.new_background);
        saveButton = findViewById(R.id.save_personal_information);
        measureXY = findViewById(R.id.measure_xy);
    }


    private void initUserDataInformation() {
        UserPresenter userPresenter = UserPresenter.getInstance(this);

        newNameView.setText(userPresenter.getUserName());

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
        } else if (requestCode == Crop.REQUEST_CROP) initUserDataInformation();
    }

    @Override
    public void log(int STATUS) {

    }

    @Override
    public void register(int STATUS) {

    }

    @Override
    public void updateUserData(int STATUS) {
        if (STATUS == UserPresenter.STATUS_SUCCESS)
            Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
        else if (STATUS == UserPresenter.STATUS_NO_INTERNET)
            Toast.makeText(this, "无网络", Toast.LENGTH_SHORT).show();
        else if (STATUS == UserPresenter.STATUS_UPDATE_ERROR)
            Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateUserImage(int STATUS) {
        if (STATUS == UserPresenter.STATUS_SUCCESS)
            Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
        else if (STATUS == UserPresenter.STATUS_NO_INTERNET)
            Toast.makeText(this, "无网络", Toast.LENGTH_SHORT).show();
        else if (STATUS == UserPresenter.STATUS_UPDATE_ERROR)
            Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public UserObserver registerObserver(UserDataShowInterface observedView) {
        return UserDataShowInterface.super.registerObserver(observedView);
    }
}