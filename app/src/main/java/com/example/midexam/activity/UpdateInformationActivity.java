package com.example.midexam.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;
import java.io.InputStream;

public class UpdateInformationActivity extends AppCompatActivity implements UserDataShowInterface {
    private static final String TAG = "UserDataSettingActivity";
    private final int MODE_HEAD_CROP = 1;
    private final int MODE_BACKGROUND_CROP = 2;
    private Uri headUri;
    private Uri backgroundUri;
    private Bitmap headBitmap;
    private Bitmap backgroundBitmap;

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
            openGallery(MODE_HEAD_CROP);
        });
        changeBackgroundButton.setOnClickListener(v -> {
            openGallery(MODE_BACKGROUND_CROP);
        });
        saveButton.setOnClickListener(v -> {

        });
    }

    private void initView() {
        newNameView = findViewById(R.id.new_name);
        changeHeadButton = findViewById(R.id.change_head_button);
        changeBackgroundButton = findViewById(R.id.change_background_button);
        saveButton = findViewById(R.id.save_personal_information);
    }

    private void openGallery(int MODE) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, MODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;
        else if (data.getData() == null) return;

        if (requestCode == MODE_HEAD_CROP) {
            Log.d(TAG, "修改头像");
            headUri = data.getData();
            headBitmap = uriToBitmap(headUri);

            if (headBitmap != null) {
                try {
                    UserPresenter.getInstance(this).saveImage(headBitmap,MODE_HEAD_CROP);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else
                Log.d(TAG, "更改背景错误\nheadUri = " + headUri + "\nheadBitmap = " + headBitmap);
        }
    }

    //将Uri实例转为Bitmap实例
    private Bitmap uriToBitmap(Uri uri) {
        InputStream inputStream = null;
        Bitmap bitmap = null;

        //将uri处理成输入流，再解码成bitmap
        try {
            inputStream = getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bitmap != null || inputStream != null) {//关闭流
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
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