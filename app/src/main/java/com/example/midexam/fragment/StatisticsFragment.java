package com.example.midexam.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.midexam.R;
import com.example.midexam.activity.DesktopActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    ConstraintLayout cmain;
    ScrollView sc_label;
    PieChart mPieChart;
    Description description;
    List<PieEntry> pieEntries;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public StatisticsFragment() {
    }

    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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

        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);

        //设置文字颜色
        List<Integer> mColor=new ArrayList<>();
        mColor.add(Color.BLACK);

        //设置圆弧颜色
        List<Integer> mColor1=new ArrayList<>();
        mColor1.add(Color.CYAN);
        mColor1.add(Color.GREEN);
        mColor1.add(Color.RED);

        //增添数据
        pieEntries= new ArrayList<>();
        pieEntries.add(new PieEntry(1, "读书"));
        pieEntries.add(new PieEntry(2f, "吃饭"));
        pieEntries.add(new PieEntry(3, "睡觉"));

        init_Pie();
        init_PieLegend(pieEntries, mColor1);

        PieDataSet iPieDataSet = init_PieDataSet(pieEntries, mColor1);
        PieData pieData = new PieData(iPieDataSet);
        pieData.setValueFormatter(new PercentFormatter());//使其有百分号
        // PieDataSet
        iPieDataSet.setValueFormatter(new PercentFormatter());
        mPieChart.setData(pieData);
    }

    @NonNull
    private static PieDataSet init_PieDataSet(List<PieEntry> pieEntries, List<Integer> mColor1) {
        //图解
        PieDataSet iPieDataSet = new PieDataSet(pieEntries, "我在专注");

        //设定图的细节
        iPieDataSet.setColors(mColor1);//图颜色
        iPieDataSet.setValueTextColors(mColor1);//图中文字的颜色
        iPieDataSet.setValueTextSize(20);//图中文字的大小
        iPieDataSet.setSliceSpace(3);   // 每块之间的距离
        iPieDataSet.setValueLinePart1OffsetPercentage(80.f);

        //设置线的长度
        iPieDataSet.setValueLinePart1Length(0.3f);
        iPieDataSet.setValueLinePart2Length(0.3f);
        //设置文字和数据图外显示
        iPieDataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        iPieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        iPieDataSet.setValueTextSize(20);
        return iPieDataSet;
    }

    private void init_Pie() {
      //  mPieChart.setDescription(null);//设置描述
        mPieChart.setUsePercentValues(true);//百分比显示
        mPieChart.setCenterText("专注时间");//圆环中心文字
        mPieChart.setCenterTextSize(20);//设置中心文字大小
        description=mPieChart.getDescription();
        description.setText("专注图");
        description.setTextSize(10f); // 设置字体大小
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.graph_marker); // 设置点击事件
        mPieChart.setMarker(mv); // 将自定义的MarkerView设置到饼状图中
    }

    private void init_PieLegend(List<PieEntry> pieEntries, List<Integer> mColor1) {
        //隐藏原有图例
        Legend legend=mPieChart.getLegend();
        legend.setEnabled(false);

        //建立一个整体布局加入scrollview
        LinearLayout l=new LinearLayout(getActivity());
        //整体布局中的单个布局
        LinearLayout.LayoutParams lp=new LinearLayout.
                LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setLayoutParams(lp);
        //装数据入单个布局
        for (int i = 0; i < pieEntries.size(); i++) {
            LinearLayout layout =getLegend(mColor1.get(i%3), pieEntries.get(i).getLabel(), (int) pieEntries.get(i).getValue());
            l.addView(layout);
        }
        //整体布局装箱
        sc_label.addView(l);
    }

    private void initview(View view) {
        cmain=view.findViewById(R.id.main);
        sc_label=view.findViewById(R.id.sv_label);
        mPieChart = (PieChart) view.findViewById(R.id.pie_chart);
    }

    @NonNull
    private LinearLayout getLegend(Integer color, String label, int data) {
        LinearLayout.LayoutParams lp=new LinearLayout.
                LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.weight=1;//设置比重为1


        LinearLayout layout=new LinearLayout(getActivity());//单个图例的布局
        layout.setOrientation(LinearLayout.HORIZONTAL);//水平排列
        layout.setGravity(Gravity.CENTER_VERTICAL);//垂直居中
        layout.setLayoutParams(lp);

        //添加color
        LinearLayout.LayoutParams colorLP=new LinearLayout.
                LayoutParams(50,50);
        colorLP.setMargins(0, 0, 20, 0);
        LinearLayout colorLayout=new LinearLayout(getActivity());
        colorLayout.setLayoutParams(colorLP);
        colorLayout.setBackgroundColor(color);
        layout.addView(colorLayout);

        //添加label
        TextView labelTV=new TextView(getActivity());
        labelTV.setText(label+" ");
        labelTV.setTextSize(20);
        layout.addView(labelTV);

        //添加data
        TextView dataTV=new TextView(getActivity());
        dataTV.setText(data+""+"min");
        dataTV.setTextSize(20);
        layout.addView(dataTV);
        return layout;
    }

    class MyMarkerView extends MarkerView {//设置点击显示

        private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView) findViewById(R.id.maker_tv);
        }

        // 每次MarkerView被调用时，都会回调此方法，可以更新UI
        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            PieEntry pieEntry = (PieEntry) e;
            // 假设你的Entry中有具体的数值，这里我们假设它是float类型的value
            float value = pieEntry.getY();
            float percent = (float) (value / getTotal()) * 100f; // getTotal() 是你需要自定义的方法来获取所有Entry的Y值之和

            tvContent.setText("时长: " + value + "\n" + "百分比: " + String.format("%.2f", percent) + "%");
            tvContent.setTextColor(Color.BLACK);
            tvContent.setTextSize(13);
            super.refreshContent(e, highlight);
        }

        // 自定义方法来获取所有Entry的Y值之和，这取决于你如何管理你的数据集
        private float getTotal() {
            float sum=0;
            for (int i = 0; i < pieEntries.size(); i++) {
                sum+=pieEntries.get(i).getValue();
            }
            return sum; // 示例值，应该替换为你的实际计算值
        }

        @Override
        public MPPointF getOffset() {
            // 设置MarkerView的偏移量
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }
}