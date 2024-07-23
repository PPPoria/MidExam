package com.example.midexam.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.midexam.R;
import com.example.midexam.presenter.UserPresenter;
import com.soundcloud.android.crop.Crop;

import java.io.File;

public class UpdateInformationActivity extends AppCompatActivity implements UserDataShowInterface {
    private static final String TAG = "UserDataSettingActivity";

    private int mode;
    private ConstraintLayout measureXY;

    private EditText newNameView;
    private ConstraintLayout changeHeadButton;
    private ConstraintLayout changeBackgroundButton;
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
        initListener();
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

        });
    }

    private void initView() {
        newNameView = findViewById(R.id.new_name);
        changeHeadButton = findViewById(R.id.change_head_button);
        changeBackgroundButton = findViewById(R.id.change_background_button);
        saveButton = findViewById(R.id.save_personal_information);
        measureXY = findViewById(R.id.measure_xy);
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
    }

    @Override
    public void log(int STATUS) {

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