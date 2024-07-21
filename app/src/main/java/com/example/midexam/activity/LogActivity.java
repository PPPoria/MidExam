package com.example.midexam.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.midexam.R;
import com.google.android.material.textfield.TextInputEditText;

public class LogActivity extends AppCompatActivity {
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
        getWindow().setNavigationBarColor(R.color.green);

        initView();
        initListener();
    }

    private void initListener() {
        logButton.setOnClickListener(v -> {

        });
        toRegister.setOnClickListener(v -> {

        });
    }

    private void initView() {
        account = findViewById(R.id.log_account);
        password = findViewById(R.id.log_password);
        logButton = findViewById(R.id.log_button);
        toRegister = findViewById(R.id.toRegister);
    }
}