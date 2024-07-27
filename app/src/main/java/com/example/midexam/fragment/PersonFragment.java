package com.example.midexam.fragment;

import android.content.Intent;
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
import com.example.midexam.activity.UserDataSettingActivity;
import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.helper.ScaleHelper;
import com.example.midexam.observer.UserObserver;
import com.example.midexam.presenter.UserPresenter;

public class PersonFragment extends Fragment implements UserDataShowInterface {
    private static final String TAG = "PersonFragment";
    private UserObserver observer;
    private View view;
    private boolean isLogged = false;

    private ImageView userBackgroundImage;
    private ImageView userHeadImage;
    private TextView userName;
    private TextView userAccount;
    private ConstraintLayout clearButton;
    private ConstraintLayout toUserDataButton;
    private ConstraintLayout logOut;
    private boolean delayedLogOut = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person, container, false);

        initView();
        initListener();
        initUserDataInformation();
        observer = registerObserver(this);
        return view;
    }

    private void initListener() {
        userName.setOnClickListener(v -> {
            if (!isLogged) startActivity(new Intent(getActivity(), LogActivity.class));
            else startActivity(new Intent(getActivity(), UserDataSettingActivity.class));
        });

        clearButton.setOnClickListener(v -> {
            clearImageMemoryAndDisk();
            Toast.makeText(getContext(), "清理完成", Toast.LENGTH_SHORT).show();
        });

        toUserDataButton.setOnClickListener(v -> {
            if (!isLogged) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(getActivity(), UserDataSettingActivity.class));
        });

        logOut.setOnClickListener(v -> {
            if (!isLogged) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                return;
            } else if (!delayedLogOut) {
                Toast.makeText(getContext(), "双击退出", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        delayedLogOut = true;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            delayedLogOut = false;
                        }
                    }
                }).start();
            } else if (delayedLogOut) {
                Log.d(TAG, "退出登录");
                UserPresenter.getInstance(this).accordLoggedStatus(requireContext(), false);
                UserPresenter.getInstance(this).resetHeadImage();
                UserPresenter.getInstance(this).resetBackgroundImage();
                observer.updateObservedViews();
            }
        });
    }

    private void initView() {
        userBackgroundImage = view.findViewById(R.id.user_background_image);
        userHeadImage = view.findViewById(R.id.user_head_image);
        userName = view.findViewById(R.id.user_name);
        userAccount = view.findViewById(R.id.user_account);
        clearButton = view.findViewById(R.id.clear);
        toUserDataButton = view.findViewById(R.id.to_user_data);
        logOut = view.findViewById(R.id.log_out);
    }

    //设置图片
    private void initUserDataInformation() {
        UserPresenter userPresenter = UserPresenter.getInstance(this);

        String name = "登录/注册";
        String account = "account";
        isLogged = userPresenter.isLogged(view.getContext());
        if (isLogged) {
            name = userPresenter.getUserName();
            account = userPresenter.getAccount(getContext());
        }
        userName.setText(name);
        userAccount.setText(account);

        String headImagePath = userPresenter.getHeadImagePath();
        Log.d(TAG, "headImagePath = " + headImagePath);
        Glide.with(view)
                .load(BitmapFactory.decodeFile(headImagePath))
                .error(R.drawable.head)
                .circleCrop()
                .into(userHeadImage);

        String backgroundImagePath = userPresenter.getBackgroundImagePath();
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(ScaleHelper.dp2px(view.getContext(), 10)));
        Glide.with(view)
                .load(BitmapFactory.decodeFile(backgroundImagePath))
                .error(R.drawable.wave_vertical)
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

    @Override
    public UserObserver registerObserver(UserDataShowInterface observedView) {
        return UserDataShowInterface.super.registerObserver(observedView);
    }

    @Override
    public void receiveUpdate() {
        initUserDataInformation();
    }
}