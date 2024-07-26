package com.example.midexam.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.midexam.R;
import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.model.UserData;
import com.example.midexam.observer.UserObserver;
import com.example.midexam.presenter.UserPresenter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class StatisticWaterPageFragment extends Fragment implements UserDataShowInterface {

    Button btDay;
    Button btMonth;
    Button btYear;

    BarChart WaterChart;
    TextView tvTitle;
    TextView tipsNoData;
    UserPresenter userPresenter=UserPresenter.getInstance(this);
    UserObserver observer;
    List<BarEntry> dayData;
    List<BarEntry> monthData;
    List<BarEntry> yearData;
    List<String> dayDate;
    List<String> monthDate;
    List<String> yearDate;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public StatisticWaterPageFragment() {

    }


    public static StatisticWaterPageFragment newInstance(String param1, String param2) {
        StatisticWaterPageFragment fragment = new StatisticWaterPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistic_water_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

            //增添数据
            dayData = dataManager.getInstance().getDayDataList();
            monthData = dataManager.getInstance().getMonthDataList();
            yearData = dataManager.getInstance().getYearDataList();
            dayDate = new ArrayList<>();
            monthDate = new ArrayList<>();
            yearDate = new ArrayList<>();//这几个要在上面以避免空指针，否则就老老实实找用法加判断
            observer=registerObserver(this);

            initView(view);
            initBarData();
            setDataInBar(WaterChart, dayData, dayDate, "日饮水量");

    }

    public void initView(View v) {

        btDay = v.findViewById(R.id.statistic_switchbutton_water).findViewById(R.id.bt_day);
        btMonth = v.findViewById(R.id.statistic_switchbutton_water).findViewById(R.id.bt_month);
        btYear = v.findViewById(R.id.statistic_switchbutton_water).findViewById(R.id.bt_year);
        WaterChart = v.findViewById(R.id.bar_1).findViewById(R.id.bar_chart);
        tvTitle = v.findViewById(R.id.bar_1).findViewById(R.id.title);
        tipsNoData = v.findViewById(R.id.tip_no_data);
        initListener();
        initMonthDate();
        initYearDate();
        initMonthData();
        initYearData();
    }

    private void initListener() {
        btDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataInBar(WaterChart, dayData, dayDate, "日饮水量");
            }
        });
        btMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataInBar(WaterChart, monthData, monthDate, "月饮水量");
            }
        });
        btYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataInBar(WaterChart, yearData, yearDate, "年饮水量");
            }
        });
    }

    public void setDataInBar(BarChart barChart, List<BarEntry> dataList, List<String> dateList, String title) {

        if (dataList == null || dataList.size() == 0) {
            barChart.setVisibility(View.INVISIBLE);
            tvTitle.setVisibility(View.INVISIBLE);
            tipsNoData.setVisibility(View.VISIBLE);
        } else {

            BarDataSet dataSet = initDataSet(barChart, dataList);

            //定义柱子上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.8f);

            initAxis(barChart, dateList);


            barChart.setData(barData);
            barChart.setFitBars(true);

            barChart.invalidate();//更新视图

            barChart.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
            tipsNoData.setVisibility(View.INVISIBLE);
        }


    }

    @NonNull
    private static BarDataSet initDataSet(BarChart barChart, List<BarEntry> dataList) {
        BarDataSet dataSet = new BarDataSet(dataList, null);
        barChart.getLegend().setEnabled(false);
        dataSet.setColors(Color.parseColor("#ADD8E6"));//设置柱子多种颜色  循环使用
        dataSet.setBarBorderColor(Color.WHITE);//柱子边框颜色
        dataSet.setBarBorderWidth(1);       //柱子边框厚度
        dataSet.setBarShadowColor(Color.RED);
        dataSet.setBarBorderWidth(0.5f);
        dataSet.setHighlightEnabled(false);//选中柱子是否高亮显示  默认为true
        dataSet.setDrawValues(true);

        barChart.getDescription().setEnabled(false);
        return dataSet;
    }

    public void initXAxis(BarChart barChart, List<String> dateList) {

        XAxis xAxis = barChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(dateList.size());
        xAxis.setLabelRotationAngle(-40.5f);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateList));



        //xAxis.setDrawGridLines(false);
    }

    public void initAxis(BarChart barChart, List<String> dateList) {
        initXAxis(barChart, dateList);
        initYAxis(barChart);
    }

    public void initYAxis(BarChart barChart) {
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        barChart.getAxisRight().setEnabled(false);
        yAxis.setDrawGridLines(false);
    }

    public void initMonthDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        // 创建一个LocalDate实例，表示该月的第一天
        LocalDate firstDayOfMonth = LocalDate.of(year, Month.of(month), 1);

        // 获取该月的最后一天
        LocalDate lastDayOfMonth = firstDayOfMonth.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

        // 打印该月的每一天
        for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
            int everyDay = date.getDayOfMonth();
            monthDate.add( String.valueOf(everyDay) + "日");
            Log.d("this", String.valueOf(everyDay) + "日");
        }


    }

    public void initYearDate() {
        for (int i = 1; i < 13; i++) {
            yearDate.add(String.valueOf(i) + "月");
        }
    }

    public void initYearData(){
        for (int i = 0; i < 12; i++) {
            yearData.add(new BarEntry(i,0));
        }
    }

    public void initMonthData(){
        for (int i = 0; i < monthDate.size(); i++) {
            monthData.add(new BarEntry(i,0));
        }
    }
    public void initBarData(){

        UserData userData=userPresenter.userData;
   /*    List<String> day=userData.getWaterToday();//格式为"1540180"，前两位表示在15小时40分钟，后面跟着就是饮水量。**后台应该在一天结束的时候，清空waterToday。
        List<String> month=userData.getWaterPerDay();//格式为"07255999"，前四位表示07月25日，后面跟着的就是饮水量。
        List<String> year=userData.getWaterPerMonth();//格式为"0751000"，前两位表示07月，后面跟着的就是饮水量。*/
        List<String> day=new ArrayList<>();
                List<String> month=new ArrayList<>();
                        List<String> year=new ArrayList<>();
                        day.add("1540180");
                        month.add("07255999");
                        year.add("0751000");
        for (int i = 0; i < day.size(); i++) {
            String hour=day.get(i).substring(0,2);
            String min=day.get(i).substring(2,4);
            String water=day.get(i).substring(4);
            int volumn=Integer.valueOf(water);
            dayData.add(new BarEntry(i,volumn));
            dayDate.add(hour+":"+min);
        }
        for (int i = 0; i < month.size(); i++) {
            String Month=month.get(i).substring(0,2);
            String Day=month.get(i).substring(2,4);
            String water=month.get(i).substring(4);
            int volumn=Integer.valueOf(water);
            monthData.get(Integer.valueOf(Day)).setY(volumn);

        }
        for (int i = 0; i < year.size(); i++) {
            String Month=year.get(i).substring(0,2);
            String water=year.get(i).substring(2);
            int volumn=Integer.valueOf(water);
            yearData.get(Integer.valueOf(Month)).setY(volumn);
        }


    }
    @Override
    public void log(int STATUS) {

    }

    @Override
    public void register(int STATUS) {

    }

    @Override
    public void updateUserData(int STATUS) {
        initListener();
        initMonthDate();
        initYearDate();
        initMonthData();
        initYearData();
        initBarData();
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
        UserDataShowInterface.super.receiveUpdate();
        initBarData();
        setDataInBar(WaterChart, dayData, dayDate, "日饮水量");
    }

    static class dataManager {
        List<BarEntry> dayDataList;
        List<BarEntry> monthDataList;
        List<BarEntry> yearDataList;

        public dataManager(List<BarEntry> dayDataList, List<BarEntry> monthDataList, List<BarEntry> yearDataList) {
            this.dayDataList = dayDataList;
            this.monthDataList = monthDataList;
            this.yearDataList = yearDataList;
        }

        //2.静态内部类初始化
        private static final class dataManagerHolder {
            private static final StatisticWaterPageFragment.dataManager INSTANCE = new StatisticWaterPageFragment.dataManager(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        //3.对外提供静态类方法获取该对象
        public static StatisticWaterPageFragment.dataManager getInstance() {
            return StatisticWaterPageFragment.dataManager.dataManagerHolder.INSTANCE;
        }

        public List<BarEntry> getDayDataList() {
            return dayDataList;
        }

        public List<BarEntry> getMonthDataList() {
            return monthDataList;
        }

        public List<BarEntry> getYearDataList() {
            return yearDataList;
        }
    }

}