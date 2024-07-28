package com.example.midexam.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.midexam.R;
import com.example.midexam.helper.UpDownSwitch;
import com.example.midexam.model.ItemData;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;


public class TimePickFragment extends DialogFragment implements View.OnClickListener {

    Button back;
    Button complete;
    Dialog dialog;
    TextView title;
    TimePicker timePicker;
    DatePicker datePicker;
    JobFragment jbf;
    String sYear;
    String sMonth;
    String sDay;
    String sHour;
    String sMin;
    String date;
    String time;
    String finalDate;
    private Context context;
    public void setContext(Context context){
        this.context = context;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用自定义布局创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_time_pick, null);
        builder.setView(view);
        // 这里可以初始化对话框中的控件
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.square_all_4);
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
        timePicker.setMinute(calendar.get(Calendar.MINUTE)+1);//让他不要在此时此刻
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
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)); //
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
                sHour=formatHour(hourOfDay);
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
                    sMonth=formatMonth(month+1);
                    sDay=formatDay(dayOfMonth);
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
        int id = v.getId();
        if (id == R.id.bt_time_back) {
            if (datePicker.getVisibility() == View.VISIBLE) {
                dismiss();
            } else {
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.GONE);
            }
        } else if (id == R.id.bt_dateSelectOK) {
            if (datePicker.getVisibility() == View.VISIBLE) {
                timePicker.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.GONE);
            } else {
                finalDate = timeSet();
                EditJobFragment editJobFragment = JobFragment.getEditJobFragment();
                editJobFragment.getStartTime().setText(finalDate);
            }
        }
    }

    private String timeSet() {
        Calendar calendar=Calendar.getInstance();
        String dateAndTime=null;
        int min=0;//当为一位数时处理用，没别的，即23：6转23：06
        if(date==null) {
            sDay=formatDay(calendar.get(Calendar.DAY_OF_MONTH));
            sMonth=formatMonth(calendar.get(Calendar.MONTH)+1);
            sYear=String.valueOf(calendar.get(Calendar.YEAR));
            date=sYear+"-"+sMonth+"-"+sDay;
        }//此时日期必有值

        if(time==null) {//此时时间必有值
            sHour=formatHour(calendar.get(Calendar.HOUR_OF_DAY));
            sMin=formatMin(calendar.get(Calendar.MINUTE));
            if(judgeLegalTime(calendar)) {
                time=sHour+":"+sMin;
                dateAndTime=date+" "+time;
                Log.e("时间",dateAndTime);
                dismiss();
            }
        }else{
            //有时间在优化时间判定
            if(judgeLegalTime(calendar)) {
                time=sHour+":"+sMin;
                dateAndTime=date+" "+time;
                Log.e("时间",dateAndTime);
                dismiss();
            }

        }
        return dateAndTime;
    }

    private boolean judgeLegalTime(Calendar calendar) {
        boolean legel=true;
        List<ItemData> tempList= jbf.getJobList().stream()
                .collect(Collectors.toList());

        if (EditJobFragment.Type==EditJobFragment.MODIFY) {
            tempList.remove(jbf.itemPosition);
        }

        if(Integer.valueOf(sYear)== calendar.get(Calendar.YEAR)&& Integer.valueOf(sMonth)==(calendar.get(Calendar.MONTH)+1)&& Integer.valueOf(sDay)== calendar.get(Calendar.DAY_OF_MONTH)){
            if(Integer.valueOf(sHour)<calendar.get(Calendar.HOUR_OF_DAY)){
                Toast.makeText(context,"您选择的时间已经过去，重新考虑一个更适合的时段吧！",Toast.LENGTH_SHORT).show();
                legel=false;
            }else if(Integer.valueOf(sHour)==calendar.get(Calendar.HOUR_OF_DAY)){
                int a=calendar.get(Calendar.MINUTE);
                if (Integer.valueOf(sMin)<=calendar.get(Calendar.MINUTE)){
                    Toast.makeText(context,"您选择的时间已经过去，重新考虑一个更适合的时段吧！",Toast.LENGTH_SHORT).show();
                    legel=false;
                }else{legel = taskConflict(tempList, legel);}
            }
        }else {legel = taskConflict(tempList, legel);}
        Log.d("this",String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        return legel;
    }

    private boolean taskConflict(List<ItemData> tempList, boolean legel) {
        long lStart= DateUtil.parse(sYear+"-"+sMonth+"-"+sDay+" "+sHour+":"+sMin+":" +"00").getTime();//增添的任务开始时间戳
        for (int i = 0; i < tempList.size(); i++) {
            String begin= tempList.get(i).getJobData();//2024-01-01 12:12
            String currentTime=begin+":00";//2024-01-01 12:12:00
            long beginTime= DateUtil.parse(currentTime).getTime();//item的开始时间戳
            long endTime=DateUtil.parse(currentTime).getTime()+Integer.valueOf(tempList.get(i).getJobDuring())*60*1000;//item的结束时间戳
            if (lStart>=beginTime&&lStart<endTime ) {
                Toast.makeText(context,"与其他任务时间冲突",Toast.LENGTH_SHORT).show();
                legel =false;
                break;
            }
        }
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

    private String formatHour(int Hour){
        String H=null;
        if(Hour>=0&&Hour<=9){
            H=String.valueOf("0"+Hour);
        }else{
            H=String.valueOf(Hour);
        }
        return H;
    }

    private String formatMonth(int Month){
        String M=null;
        if(Month>=0&&Month<=9){
            M=String.valueOf("0"+Month);
        }else{
            M=String.valueOf(Month);
        }
        return M;
    }

    private String formatDay(int Day){
        String D=null;
        if(Day>=0&&Day<=9){
            D=String.valueOf("0"+Day);
        }else{
            D=String.valueOf(Day);
        }
        return D;
    }

    public void setJobFragment(JobFragment jobFragment){
        jbf=jobFragment;
    }

}