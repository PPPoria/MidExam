package com.example.midexam.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.midexam.R;
import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.observer.UserObserver;
import com.example.midexam.presenter.UserPresenter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class StatisticWaterPageFragment extends Fragment implements UserDataShowInterface {
    private static final String TAG = "StatisticWaterPageFragment";
    private View view;

    BarChart waterChart;
    TextView tipsNoData;

    List<BarEntry> dayData;
    List<BarEntry> monthData;
    List<BarEntry> yearData;
    List<String> dayDate;
    List<String> monthDate;
    List<String> yearDate;
    int position = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistic_water_page, container, false);

        initView();

        requireData();

        initMonthDate();
        initYearDate();

        initMonthData();
        initYearData();

        ///initBarData()、setDataInBar(dayData, dayDate);来更新，前面的初始化一下就好了

        initBarData();

        setDataInBar(dayData, dayDate);

        return view;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private void requireData() {
        //增添数据
        dayData = dataManager.getInstance().getDayDataList();
        monthData = dataManager.getInstance().getMonthDataList();
        yearData = dataManager.getInstance().getYearDataList();
        dayDate = new ArrayList<>();
        monthDate = new ArrayList<>();
        yearDate = new ArrayList<>();//这几个要在上面以避免空指针，否则就老老实实找用法加判断
    }

    public void initView() {
        waterChart = view.findViewById(R.id.bar_chart);
        tipsNoData = view.findViewById(R.id.noDataTips_water);
    }

    //有网络需求
    public void initBarData() {

        List<String> day;
        List<String> month;
        List<String> year;
        if (UserPresenter.getInstance(this).isLogged(requireContext())) {
            UserPresenter userPresenter = UserPresenter.getInstance(this);
            day = userPresenter.getWaterToday();//格式为"1540180"，前两位表示在15小时40分钟，后面跟着就是饮水量。**后台应该在一天结束的时候，清空waterToday。
            month = userPresenter.getWaterPerDay();//格式为"07255999"，前四位表示07月25日，后面跟着的就是饮水量。
            year = userPresenter.getWaterPerMonth();//格式为"0751000"，前两位表示07月，后面跟着的就是饮水量。
            Log.d(TAG, "waterPerDay = " + month);
            Log.d(TAG, "waterPerMonth = " + year);
        } else {
            day = new ArrayList<>();
            month = new ArrayList<>();
            year = new ArrayList<>();
        }

  /*      List<String> day = new ArrayList<>();
        List<String> month = new ArrayList<>();
        List<String> year = new ArrayList<>();
        day.add("1540180");
        month.add("07255999");
        year.add("0751000");*/
        if (!dayData.isEmpty()) {
            dayData.clear();
        }
        if (!dayDate.isEmpty()) {
            dayDate.clear();
        }//清空旧数据，避免重复添加


            for (int i = 0; i < day.size(); i++) {
                String hour = day.get(i).substring(0, 2);
                String min = day.get(i).substring(2, 4);
                String water = day.get(i).substring(4);
                int V = Integer.parseInt(water);

                dayData.add(new BarEntry(i, V));
                dayDate.add(hour + ":" + min);
            }

            for (int i = 0; i < month.size(); i++) {
                String Day = month.get(i).substring(2, 4);
                String water = month.get(i).substring(4);//格式为"0725  5999"
                int V = Integer.parseInt(water);
                Log.d(TAG, "V = " + V);
                monthData.get(Integer.parseInt(Day)-1).setY(V);
            }

            for (int i = 0; i < year.size(); i++) {
                String Month = year.get(i).substring(0, 2);
                String water = year.get(i).substring(2);
                int V = Integer.parseInt(water);
                yearData.get(Integer.parseInt(Month)).setY(V);
            }

    }

    public void setDataInBar(List<BarEntry> dataList, List<String> dateList) {

        if (dataList == null || dataList.isEmpty()) {//判断是否数据源为空
            waterChart.setVisibility(View.INVISIBLE);
            tipsNoData.setVisibility(View.VISIBLE);
        } else {//数据源不为空根据传入的数据源类型进行显示
            if (dataList != dayData) {//如果是月和年
                boolean noData = true;
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).getY() != 0) {//如果喝水值为零
                        noData = false;
                        break;
                    }
                }//判断是不是都为零（因为前面为了初始化数据把它们都设成了零）
                if (noData == false) {
                    waterChart.setVisibility(View.VISIBLE);
                    tipsNoData.setVisibility(View.INVISIBLE);
                }//不是全为零，则显示
                else {
                    waterChart.setVisibility(View.INVISIBLE);
                    tipsNoData.setVisibility(View.VISIBLE);
                }//全为零，显示无数据
            } else {//入伙数据源是日，由于日的数据源没有初始化过，那么就按照原策略进行
                waterChart.setVisibility(View.VISIBLE);
                tipsNoData.setVisibility(View.INVISIBLE);
            }
//设置数据
            BarDataSet dataSet = initDataSet(waterChart, dataList);

            //定义柱子上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.8f);

            initAxis(waterChart, dateList);

            waterChart.setData(barData);


            waterChart.invalidate();//更新视图
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

    //月坐标
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
            monthDate.add(String.valueOf(everyDay) + "日");
            Log.d("this", String.valueOf(everyDay) + "日");
        }
    }

    //年坐标
    public void initYearDate() {
        for (int i = 1; i < 13; i++) {
            yearDate.add(String.valueOf(i) + "月");
        }
    }

    //让他都有数据，好看一点
    public void initYearData() {
        for (int i = 0; i < 12; i++) {
            yearData.add(new BarEntry(i, 0));
        }
    }

    //让他都有数据，好看一点
    public void initMonthData() {
        for (int i = 0; i < monthDate.size(); i++) {
            monthData.add(new BarEntry(i, 0));
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

    }

    @Override
    public void updateUserImage(int STATUS) {

    }

    @Override
    public void receiveUpdate() {
        initBarData();
        if (position == 0)
            setDataInBar(dayData, dayDate);
        else if (position == 1)
            setDataInBar(monthData, monthDate);
        else
            setDataInBar(yearData, yearDate);
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