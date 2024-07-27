package com.example.midexam.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midexam.R;
import com.example.midexam.model.ItemData;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class EditJobFragment extends DialogFragment implements View.OnClickListener {

    Button back;
    Button confirm;
    Dialog dialog;
    TextView title;
    TextView tipsUnit;
    JobFragment jbf;
    TextInputEditText eventName;
    TextInputEditText startTime;
    TextInputEditText duringTime;

    static int Type=3;//未指定类型：3
    static String titleContain;
    static String sEventName;
    static String sStartTime;
    static String sDuringTime;
    public static final int EDIT=0;
    public static final int MODIFY=1;

    private Context context;
    public void setContext(Context context){
        this.context = context;
    }

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
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.square_all_4);
        dialog.getWindow().setBackgroundDrawable(drawable);
        initView(view);
        return dialog;
    }

    private void initView(View v) {
        back = v.findViewById(R.id.back_to_job);
        confirm = v.findViewById(R.id.bt_confirm);
        eventName = v.findViewById(R.id.tved_name);
        startTime = v.findViewById(R.id.tved_startTime);
        duringTime = v.findViewById(R.id.tved_duringTime);
        title = v.findViewById(R.id.dialog_title);
        tipsUnit=v.findViewById(R.id.tipsDuringUnit);
        //接下来写数据存储和确认事件逻辑

        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        startTime.setFocusable(false);
        startTime.setOnClickListener(this);
        duringTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    tipsUnit.setVisibility(View.VISIBLE);
                }else{
                    tipsUnit.setVisibility(View.GONE);
                }
            }
        });


        if(titleContain!=null){title.setText(titleContain);titleContain=null;}//后面那个防复用
        if(sEventName!=null){eventName.setText(sEventName);sEventName=null;}
        if(sStartTime!=null){startTime.setText(sStartTime);sStartTime=null;}
        if(sDuringTime!=null){duringTime.setText(sDuringTime);sDuringTime=null;}
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_to_job:
                dialog.dismiss();
                break;

            case R.id.bt_confirm:
                if(jugeDataList()){
                    if(Type == EDIT){//直接数据源list
                        jbf.addItem(getItemData());

                    }else if(Type==MODIFY){//templist
                        jbf.modify(getItemData());//这里再添加一个

                    }
                    dismiss();
                }
                break;
            case R.id.tved_startTime:
                JobFragment.switchDialog(JobFragment.SET_START_TIME);
                break;
            default:
                break;
        }
    }
//时间判定
    private boolean jugeDataList(){
        boolean ready=false;
        List<String> data=new ArrayList<>();
        int num=0;//需要获取的数据项数，这里只做初始化，要增加需要自己写num++
        String eventname=eventName.getText().toString().trim();
        String start=startTime.getText().toString().trim();
        String during=duringTime.getText().toString().trim();

        if(eventname==null||eventname.equals("")){
            Toast.makeText(context,"请输入事件名称",Toast.LENGTH_SHORT).show();

            return ready;
        }else{
            data.add(eventName.getText().toString());
            num++;
        }

        if(start==null||start.equals("")){
            Toast.makeText(context,"请输入起始时间",Toast.LENGTH_SHORT).show();

            return ready;
        }else{
            num++;
            data.add(startTime.getText().toString());
        }

        if(during==null||during.equals("")){

            Toast.makeText(context,"请输入持续时间",Toast.LENGTH_SHORT).show();
            return ready;
        }else{
            data.add(duringTime.getText().toString());
            num++;
        }
        if(data.size()==num){ready=true;}

        return ready;
    }

    private ItemData getItemData(){
        String eventname=eventName.getText().toString().trim();
        String start=startTime.getText().toString().trim();
        String during=duringTime.getText().toString().trim();
        ItemData itemData=new ItemData(eventname,start,during);
        return itemData;//自己重新封装一个传过去
    }

    public void setJobFragment(JobFragment jobFragment){
        jbf=jobFragment;
    }

    public TextInputEditText getStartTime() {
        return startTime;
    }
    public static void setTitle(String s){
        titleContain=s;
    }
    public  void setEvent(String s){
        sEventName=s;
    }
    public  void setStartTime(String s){
        sStartTime=s;
    }
    public  void setDuringTime(String s){
        sDuringTime=s;
    }
    public  void setType(int s){
        Type=s;
    }

    public static int getType() {
        return Type;
    }
}