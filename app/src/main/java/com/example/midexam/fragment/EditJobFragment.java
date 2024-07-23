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
import android.widget.TextView;
import android.widget.Toast;

import com.example.midexam.R;
import com.example.midexam.activity.DesktopActivity;
import com.example.midexam.adapter.ItemAdapter;
import com.example.midexam.overrideview.HorizontalScrollMenu;
import com.github.mikephil.charting.components.XAxis;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


public class EditJobFragment extends DialogFragment implements View.OnClickListener {

    Button back;
    Button confirm;
    Dialog dialog;
    TextView title;
    TextInputEditText eventName;
    TextInputEditText startTime;
    TextInputEditText duringTime;
    private HorizontalScrollMenu scroll;

    String titleContent;
    List<String> dataList;

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
        initView(view,titleContent);
        return dialog;
    }

    private void initView(View v,String s){
        back= v.findViewById(R.id.back_to_job);
        confirm=v.findViewById(R.id.bt_confirm);
        eventName=v.findViewById(R.id.tved_name);
        startTime=v.findViewById(R.id.tved_startTime);
        duringTime=v.findViewById(R.id.tved_duringTime);
        title=v.findViewById(R.id.dialog_title);
        //接下来写数据存储和确认事件逻辑

        back.setOnClickListener(this);
        confirm.setOnClickListener(this);

        dataList=new ArrayList<>();

        if (s == null) title.setText("添加标题");
        else title.setText(s);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_to_job:
                dialog.dismiss();
                break;

            case R.id.bt_confirm:
                if(jugeDataList()){

                }else{

                }
                break;

            default:
                break;
        }
    }

    public boolean jugeDataList(){
        boolean ready=false;
        List<String> data=new ArrayList<>();
        int num=0;//需要获取的数据项数，这里只做初始化，要增加需要自己写num++
        String eventname=eventName.getText().toString().trim();
        String start=startTime.getText().toString().trim();
        String during=duringTime.getText().toString().trim();

        if(eventname==null||eventname.equals("")){
            Toast.makeText(getContext(),"请输入事件名称",Toast.LENGTH_SHORT).show();
            num++;
            return ready;
        }else{
            data.add(eventName.getText().toString());
            num++;
        }
        if(start==null||start.equals("")){
            Toast.makeText(getContext(),"请输入起始时间",Toast.LENGTH_SHORT).show();
            num++;
            return ready;
        }else{
            data.add(startTime.getText().toString());
            num++;
        }
        if(during==null||during.equals("")){
            Toast.makeText(getContext(),"请输入持续时间",Toast.LENGTH_SHORT).show();
            num++;
            return ready;
        }else{
            data.add(duringTime.getText().toString());
            num++;
        }
        if(data.size()==num){ready=true;dataList=data;}

        return ready;
    }


    public void setTitle(String s){
       titleContent=s;
    }
}