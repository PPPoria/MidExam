package com.example.midexam.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.midexam.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;


public class StatisticWaterPageFragment extends Fragment implements View.OnClickListener{

    Button btDay;
    Button btMonth;
    Button btYear;

    BarChart WaterChart;
    TextView tvTitle;
    TextView tipsNoData;

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
        dayData= StatisticWaterPageFragment.dataManager.getInstance().getDayDataList();
        dayData.add(new BarEntry(1,200));
        dayData.add(new BarEntry(2,500));
        dayData.add(new BarEntry(3,6000));
        dayData.add(new BarEntry(4,15000));

        dayDate=new ArrayList<>();
        dayDate.add("7月22日");
        dayDate.add("7月23日");
        dayDate.add("7月24日");
        dayDate.add("7月25日");

        initView(view);
        setDataInBar(WaterChart,dayData,dayDate,"日饮水量");
    }

    public void initView(View v){

        btDay=v.findViewById(R.id.statistic_switchbutton_water).findViewById(R.id.bt_day);
        btMonth=v.findViewById(R.id.statistic_switchbutton_water).findViewById(R.id.bt_month);
        btYear=v.findViewById(R.id.statistic_switchbutton_water).findViewById(R.id.bt_year);
        WaterChart =v.findViewById(R.id.bar_1).findViewById(R.id.bar_chart);
        tvTitle=v.findViewById(R.id.bar_1).findViewById(R.id.title);
        tipsNoData=v.findViewById(R.id.tip_no_data);

        btDay.setOnClickListener(this);
        btMonth.setOnClickListener(this);
        btYear.setOnClickListener(this);

    }

    public void setDataInBar(BarChart barChart, List<BarEntry> dataList, List<String> dateList, String title){

        if(dataList==null||dataList.size()==0){
            barChart.setVisibility(View.INVISIBLE);
            tvTitle.setVisibility(View.INVISIBLE);
            tipsNoData.setVisibility(View.VISIBLE);
        }else{
            BarDataSet dataSet=new BarDataSet(dataList,null);
            barChart.getLegend().setEnabled(false);
            dataSet.setColors(Color.parseColor("#ADD8E6"));//设置柱子多种颜色  循环使用
            dataSet.setBarBorderColor(Color.WHITE);//柱子边框颜色
            dataSet.setBarBorderWidth(1);       //柱子边框厚度
            dataSet.setBarShadowColor(Color.RED);
            dataSet.setHighlightEnabled(false);//选中柱子是否高亮显示  默认为true
            barChart.getDescription().setEnabled(false);
            //定义柱子上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
            BarData barData=new BarData(dataSet);
            initAxis(barChart,dateList);
            barChart.setData(barData);
            barChart.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            tipsNoData.setVisibility(View.INVISIBLE);
        }


    }


    public void initAxis(BarChart barChart,List<String> dateList){
        initXAxis(barChart,dateList);
        initYAxis(barChart);
    }
    public void initXAxis(BarChart barChart,List<String> dateList){
        int labelcount=6;
        XAxis xAxis=barChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(labelcount,false);

        //坐标范围
        xAxis.setAxisMaximum(labelcount);
        xAxis.setAxisMinimum(0);
        xAxis.setDrawGridLines(false);//禁用竖线
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                float decimal = value%1;
                // 检查索引是否在有效范围内
                if (decimal == 0 && value <= dateList.size()&&value>0) {
                    // 返回对应的标签
                    return dateList.get((int)(value-1))/*String.valueOf(value)*/;
                }
                // 如果索引无效，可以返回一个默认字符串或空字符串
                return /*String.valueOf(value)*/"";
            }
        });
    }

    public void initYAxis(BarChart barChart){
        YAxis yAxis=barChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setDrawGridLines(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_day:
                setDataInBar(WaterChart,dayData,dayDate,"日饮水量");
                break;

            case R.id.bt_month:
                setDataInBar(WaterChart,monthData,dayDate,"月饮水量");
                break;

            case R.id.bt_year:
                setDataInBar(WaterChart,monthData,dayDate,"年饮水量");
                break;

            default:
                break;
        }
    }

    static class dataManager{
        List<BarEntry> dayDataList;
        List<BarEntry> monthDataList;
        List<BarEntry> yearDataList;

        public dataManager(List<BarEntry> dayDataList, List<BarEntry> monthDataList, List<BarEntry> yearDataList) {
            this.dayDataList = dayDataList;
            this.monthDataList = monthDataList;
            this.yearDataList = yearDataList;
        }

        //2.静态内部类初始化
        private static final class dataManagerHolder{
            private static final StatisticWaterPageFragment.dataManager INSTANCE=new StatisticWaterPageFragment.dataManager(new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        }
        //3.对外提供静态类方法获取该对象
        public static StatisticWaterPageFragment.dataManager getInstance(){
            return StatisticWaterPageFragment.dataManager.dataManagerHolder.INSTANCE;
        }

        public List<BarEntry> getDayDataList(){
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