package com.example.midexam.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.midexam.R;
import com.example.midexam.activity.DesktopActivity;
import com.example.midexam.adapter.ItemAdapter;
import com.example.midexam.overrideview.HorizontalScrollMenu;
import com.github.mikephil.charting.components.XAxis;


public class EditJobFragment extends DialogFragment implements View.OnClickListener {

    Button back;
    Dialog dialog;

    private HorizontalScrollMenu scroll;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用自定义布局创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_job, null);
        builder.setView(view);
        // 这里可以初始化对话框中的控件
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.square_all_4);
        dialog.getWindow().setBackgroundDrawable(drawable);
        initView(view);
        return dialog;
    }

    private void initView(View v){
        back= v.findViewById(R.id.back_to_job);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_to_job:
                dialog.dismiss();


                break;
            default:
                break;
        }
    }

}