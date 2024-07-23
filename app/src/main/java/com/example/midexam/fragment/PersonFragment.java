package com.example.midexam.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.midexam.R;
import com.example.midexam.activity.LogActivity;
import com.example.midexam.activity.UpdateInformationActivity;
import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.helper.ScaleHelper;
import com.example.midexam.presenter.UserPresenter;

public class PersonFragment extends Fragment implements UserDataShowInterface {
    private static final String TAG = "PersonFragment";
    private View view;

    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private boolean isLogged = false;

    private ImageView userBackgroundImage;
    private ImageView userHeadImage;
    private TextView userName;
    private ConstraintLayout clearButton;
    private ConstraintLayout toUserDataButton;


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
        isLogged = UserPresenter.getInstance(this).isLogged(view.getContext());
        Log.d(TAG, "isLogged = " + isLogged);
        if (isLogged)
            name = sp.getString("name", "登录/注册");
        userName.setText(name);
    }

    private void initListener() {
        userName.setOnClickListener(v -> {
            if (isLogged) changeUserName();
            else startActivity(new Intent(getActivity(), LogActivity.class));
        });
        clearButton.setOnClickListener(v -> {
            clearImageMemoryAndDisk();
            Toast.makeText(getContext(), "清理完成", Toast.LENGTH_SHORT).show();
        });
        toUserDataButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UpdateInformationActivity.class));
        });
    }

    private void changeUserName() {

    }

    private void initView() {
        userBackgroundImage = view.findViewById(R.id.user_background_image);
        userHeadImage = view.findViewById(R.id.user_head_image);
        userName = view.findViewById(R.id.user_name);
        clearButton = view.findViewById(R.id.clear);
        toUserDataButton = view.findViewById(R.id.to_user_data);
    }

    //设置图片
    private void initUserImage() {
        String headImagePath = UserPresenter.getInstance(this).getHeadImagePath();
        Log.d(TAG, "headImagePath = " + headImagePath);
        Glide.with(view)
                .load(BitmapFactory.decodeFile(headImagePath))
                .circleCrop()
                .into(userHeadImage);

        String backgroundImagePath = UserPresenter.getInstance(this).getBackgroundImagePath();
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(ScaleHelper.dp2px(view.getContext(),10)));
        Glide.with(view)
                .load(BitmapFactory.decodeFile(backgroundImagePath))
                .apply(options)
                .into(userBackgroundImage);
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