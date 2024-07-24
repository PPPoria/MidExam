package com.example.midexam.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.midexam.R;
import com.example.midexam.presenter.UserPresenter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements UserDataShowInterface {
    private static final String TAG = "RegisterActivity";

    private TextInputEditText account;
    private TextInputEditText password;
    private TextInputEditText passwordAgain;
    private Button registerButton;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setNavigationBarColor(getColor(R.color.blue));
        initView();
        initListener();
    }

    private void initListener() {
        registerButton.setOnClickListener(v -> {
            String accountStr = Objects.requireNonNull(account.getText()).toString();
            String passwordStr = Objects.requireNonNull(password.getText()).toString();
            String passwordAgainStr = Objects.requireNonNull(passwordAgain.getText()).toString();
            UserPresenter.getInstance(this).requestRegister(this, accountStr, passwordStr, passwordAgainStr);
        });
    }

    private void initView() {
        account = findViewById(R.id.register_account);
        password = findViewById(R.id.register_password);
        passwordAgain = findViewById(R.id.register_password_again);
        registerButton = findViewById(R.id.register_button);
    }

    @Override
    public void log(int STATUS) {

    }

    @Override
    public void register(int STATUS) {
        Log.d(TAG, "registerFail: STATUS = " + STATUS);
        if (STATUS == UserPresenter.STATUS_NO_INTERNET)
            Toast.makeText(this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
        else if (STATUS == UserPresenter.STATUS_ACCOUNT_ALREADY_EXIST)
            Toast.makeText(this, "账号已存在", Toast.LENGTH_SHORT).show();
        else if (STATUS == UserPresenter.STATUS_PASSWORDS_INCONSISTENT)
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
        else if (STATUS == UserPresenter.STATUS_ACCOUNT_OR_PASSWORD_NOT_SATISFIABLE)
            Toast.makeText(this, "账号或密码必须不小于八位", Toast.LENGTH_SHORT).show();
        else if (STATUS == UserPresenter.STATUS_SUCCESS) {
            Log.d(TAG, "registerSuccess");
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    finally {
                        finish();
                    }
                }
            }).start();
        }
    }

    @Override
    public void updateUserData(int STATUS) {

    }

    @Override
    public void updateUserImage(int STATUS) {

    }
}