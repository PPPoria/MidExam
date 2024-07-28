package com.example.midexam.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.Toast;

import com.example.midexam.R;
public class FocusFragment extends Fragment {
    private static final String TAG = "FocusFragment";
    private View view;

    private View consumeView;
    private TextClock clock;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_focus, container, false);
        initView();
        initListener();
        initClockTextStyle();
        return view;
    }

    private void initClockTextStyle() {
        clock.setFormat24Hour("M/dd H:mm");
    }

    private void initListener() {
        consumeView.setOnClickListener(v->{
            Toast.makeText(requireContext(), "要认真哦", Toast.LENGTH_SHORT).show();
        });
    }

    private void initView(){
        consumeView = view.findViewById(R.id.consume_touch_event);
        clock = view.findViewById(R.id.focus_clock_text);
    }
}