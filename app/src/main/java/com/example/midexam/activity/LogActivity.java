package com.example.midexam.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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

public class LogActivity extends AppCompatActivity implements UserDataShowInterface {
    private static final String TAG = "LogActivity";

    private TextInputEditText account;
    private TextInputEditText password;
    private Button logButton;
    private TextView toRegister;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log);
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
        logButton.setOnClickListener(v -> {
            String accountStr = Objects.requireNonNull(account.getText()).toString();
            String passwordStr = Objects.requireNonNull(password.getText()).toString();
            UserPresenter.getInstance(this).requestLog(this,accountStr, passwordStr);
        });
        toRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void initView() {
        account = findViewById(R.id.log_account);
        password = findViewById(R.id.log_password);
        logButton = findViewById(R.id.log_button);
        toRegister = findViewById(R.id.toRegister);
    }

    @Override
    public void log(int STATUS) {
        if (STATUS == UserPresenter.STATUS_PASSWORD_INCORRECT)
            Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
        else if (STATUS == UserPresenter.STATUS_NO_INTERNET)
            Toast.makeText(this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
        else if (STATUS == UserPresenter.STATUS_SUCCESS)
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