package com.example.midexam.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.midexam.R;

import java.util.Calendar;


public class TimePickFragment extends DialogFragment implements View.OnClickListener {

    Button back;
    Button complete;
    Dialog dialog;
    TextView title;
    TimePicker timePicker;
    DatePicker datePicker;

    String sYear;
    String sMonth;
    String sDay;
    String sHour;
    String sMin;
    String date;
    String time;
    String finalDate;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用自定义布局创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_time_pick, null);
        builder.setView(view);
        // 这里可以初始化对话框中的控件
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.square_all_4);
        dialog.getWindow().setBackgroundDrawable(drawable);
        initView(view);
        datePicker.setVisibility(View.VISIBLE);
        timePicker.setVisibility(View.GONE);
        return dialog;
    }
    public void initView(View v){
        title=v.findViewById(R.id.TimeTitle);
        timePicker=v.findViewById(R.id.timePicker);
        datePicker=v.findViewById(R.id.datePicker);
        back=v.findViewById(R.id.bt_time_back);
        complete=v.findViewById(R.id.bt_dateSelectOK);
        initListener();
        initTimePicker();
        initDatePicker();
    }

    private void initTimePicker() {
        Calendar calendar = Calendar.getInstance();
        timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(calendar.get(Calendar.MINUTE));
    }

    private void initDatePicker() {
        // 创建一个Calendar实例
        Calendar calendar = Calendar.getInstance();
// 设置最小日期为今天（或之前的某个日期）
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)); //
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)); //
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)); //
        long minDate = calendar.getTimeInMillis(); // 转换为毫秒数

// 设置最大日期为今年年底
        calendar.set(Calendar.YEAR, 9999); //
        calendar.set(Calendar.MONTH, Calendar.DECEMBER); //
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); // 获取该月的最后一天
        long maxDate = calendar.getTimeInMillis(); // 转换为毫秒数

// 应用最小日期和最大日期限制
        datePicker.setMinDate(minDate);
        datePicker.setMaxDate(maxDate);
    }

    private void initListener() {
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // 在这里处理时间变化
                // 例如，更新 UI 或将时间保存到某个变量中
                sHour=String.valueOf(hourOfDay);
                sMin=formatMin(minute);
                time=sHour+":"+sMin;
                Log.d("时间",String.valueOf(hourOfDay+"+"+minute));
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
                    // 处理日期改变事件(month比原来的小一个月)
                    int Month=month+1;
                    sYear=String.valueOf(year);
                    sMonth=String.valueOf(Month);
                    sDay=String.valueOf(dayOfMonth);
                    date=sYear+"-"+sMonth+"-"+sDay;
                    Log.d("时间",String.valueOf(year+"+"+month+"+"+dayOfMonth));
                }
            });
        }

        back.setOnClickListener(this);
        complete.setOnClickListener(this);
    }

    //关于日期选择的控件不在这里，去init对应的方法找。
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_time_back:
                if(datePicker.getVisibility()==View.VISIBLE){
                    dismiss();
                }else{
                    datePicker.setVisibility(View.VISIBLE);
                    timePicker.setVisibility(View.GONE);
                }
                break;
            case R.id.bt_dateSelectOK:
                if(datePicker.getVisibility()==View.VISIBLE){
                    timePicker.setVisibility(View.VISIBLE);
                    datePicker.setVisibility(View.GONE);
                }else {
                    finalDate=timeSet();
                    EditJobFragment editJobFragment=JobFragment.getEditJobFragment();
                    editJobFragment.getStartTime().setText(finalDate);
                }
                break;
        }
    }

    private String timeSet() {
        Calendar calendar=Calendar.getInstance();
        String dateAndTime=null;
        int min=0;//当为一位数时处理用，没别的，即23：6转23：06
        if(date==null) {
            sDay=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            sMonth=String.valueOf(calendar.get(Calendar.MONTH));
            sYear=String.valueOf(calendar.get(Calendar.YEAR));
            date=sYear+"-"+sMonth+"-"+sDay;
        }//此时日期必有值

        if(time==null) {//此时时间必有值
            sHour=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
            sMin=formatMin(calendar.get(Calendar.MINUTE));
            time=sHour+":"+sMin;
            dateAndTime=date+"  "+time;
            Log.e("时间",dateAndTime);
            dismiss();
        }else{
            //有时间在优化时间判定
            if(judgeLegalTime(calendar)) {
                time=sHour+":"+sMin;
                dateAndTime=date+"  "+time;
                Log.e("时间",dateAndTime);
                dismiss();
            }
            else Toast.makeText(getContext(),"您选择的时间已经过去，重新考虑一个更适合的时段吧！",Toast.LENGTH_SHORT).show();
        }
        return dateAndTime;
    }

    private boolean judgeLegalTime(Calendar calendar) {
        boolean legel=true;
        if(Integer.valueOf(sYear)== calendar.get(Calendar.YEAR)&& Integer.valueOf(sMonth)==(calendar.get(Calendar.MONTH)+1)&& Integer.valueOf(sDay)== calendar.get(Calendar.DAY_OF_MONTH)){
           //时间判定
            if(Integer.valueOf(sHour)< calendar.get(Calendar.HOUR_OF_DAY)){
               legel=false;
            } else if (Integer.valueOf(sHour)== calendar.get(Calendar.HOUR_OF_DAY)) {
                if(Integer.valueOf(sMin)< calendar.get(Calendar.MINUTE)){
                    legel=false;
                }
            }
        }
        Log.d("this",String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        return legel;
    }

    private String formatMin(int min){
        String minute=null;
        if(min>=0&&min<=9){
            minute=String.valueOf("0"+min);
        }else{
            minute=String.valueOf(min);
        }
        return minute;
    }
}