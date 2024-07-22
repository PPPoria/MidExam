package com.example.midexam.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.midexam.R;
import com.example.midexam.activity.LogActivity;

public class PersonFragment extends Fragment {
    private static final String TAG = "PersonFragment";
    private View view;

    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private boolean isLogged = false;

    private ImageView userBackgroundImage;
    private ImageView userHeadImage;
    private TextView userName;
    private ConstraintLayout clearButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person, container, false);

        sp = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        ed = sp.edit();

        initView();
        initListener();
        initUserData();
        initUserImage();
        return view;
    }

    private void initUserData() {
        String name = "登录/注册";
        try {
            if (sp.getBoolean("log", false)) {
                name = sp.getString("name","登录/注册");
                isLogged = true;
            }
        }catch (Exception e){}

        userName.setText(name);
    }

    private void initListener() {
        userName.setOnClickListener(v -> {
            if(isLogged) changeUserName();
            else startActivity(new Intent(getActivity(), LogActivity.class));
        });
        clearButton.setOnClickListener(v -> {
            clearImageMemoryAndDisk();
            Toast.makeText(getContext(), "清理完成", Toast.LENGTH_SHORT).show();
        });
    }

    private void changeUserName() {

    }

    private void initView() {
        userBackgroundImage = view.findViewById(R.id.user_background_image);
        userHeadImage = view.findViewById(R.id.user_head_image);
        userName = view.findViewById(R.id.user_name);
        clearButton = view.findViewById(R.id.clear);
    }

    //设置图片
    private void initUserImage() {
        Glide.with(view).load(R.drawable.head).circleCrop().into(userHeadImage);
        Glide.with(view).load(R.drawable.background).centerCrop().into(userBackgroundImage);
    }

    //清除图片缓存，不然图片更改不成功
    private void clearImageMemoryAndDisk() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(view.getContext()).clearDiskCache();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    initView();
                }
            }
        }).start();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.get(view.getContext()).clearMemory();
            }
        });
    }
}