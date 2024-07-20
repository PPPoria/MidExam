package com.example.midexam.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midexam.R;

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
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    Button btAdd;
    Button bt;
    ConstraintLayout cmain;
    Description description;
    LinearLayout linearLayout;
    PieChart mPieChart;
    ScrollView scrollView;


    List<PieEntry> pieEntriesDay;
    List<PieEntry> pieEntriesMonth;
    List<PieEntry> pieEntriesYear;
    List<String> mColorPie;

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


       mColorPie = getColorPie();
        //增添数据
        addData();
        init_Pie();
        init_PieLegend(pieEntriesDay, mColorPie);
        updataPie(pieEntriesDay);
    }

    private void updataPie(List<PieEntry> dataResourse) {
        PieDataSet iPieDataSet = init_PieDataSet(dataResourse, mColorPie);
        PieData pieData = new PieData(iPieDataSet);
        pieData.setValueFormatter(new PercentFormatter());//使其有百分号
        // PieDataSet
        iPieDataSet.setValueFormatter(new PercentFormatter());
        mPieChart.setData(pieData);
        mPieChart.invalidate();//更新图表
    }


    private void addData() {
        pieEntriesDay = new ArrayList<>();
        pieEntriesDay.add(new PieEntry(1, "读书"));
        pieEntriesDay.add(new PieEntry(2f, "吃饭aaaaaa"));
        pieEntriesDay.add(new PieEntry(3, "睡觉"));
        pieEntriesDay.add(new PieEntry(4,"好"));
    }

    @NonNull
    private static List<String> getColorPie() {
        //设置圆弧颜色
        List<String> mColorPie=new ArrayList<>();
        mColorPie.add("#007BFF");
        mColorPie.add("#28A745");
        mColorPie.add("#FFC107");
        mColorPie.add("#DC3545");
        mColorPie.add("#00BFFF");
        mColorPie.add("#6F42C1");
        mColorPie.add("#FFA500");
        mColorPie.add("#6C757D");
        mColorPie.add("#ADD8E6");
        mColorPie.add("#FFC0CB");
        return mColorPie;
    }

    @NonNull
    private static PieDataSet init_PieDataSet(List<PieEntry> pieEntries, List<String> colorPie) {
        //图解
        PieDataSet iPieDataSet = new PieDataSet(pieEntries, "我在专注");


        int[] colors = new int[colorPie.size()];
        for (int i = 0; i < colorPie.size(); i++) {
            colors[i] = Color.parseColor(String.valueOf(colorPie.get(i)));
        }
        //设定图的细节
        iPieDataSet.setColors(colors);//图颜色
        iPieDataSet.setValueTextColors(Collections.singletonList(Color.BLACK));//图中文字的颜色,黑色给了个转换为集合才能接受
        iPieDataSet.setValueTextSize(20);//图中文字的大小
        iPieDataSet.setSliceSpace(3);   // 每块之间的距离
        iPieDataSet.setValueLinePart1OffsetPercentage(80.f);

        //设置线的长度
        iPieDataSet.setValueLinePart1Length(0.3f);
        iPieDataSet.setValueLinePart2Length(0.2f);
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
        description.setText("专注图");//图例
        description.setTextSize(10f); // 设置字体大小
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.graph_marker); // 设置点击事件
        mPieChart.setMarker(mv); // 将自定义的MarkerView设置到饼状图中
        mPieChart.setEntryLabelColor(Color.BLACK);
        mPieChart.setEntryLabelTypeface(Typeface.DEFAULT);
    }

    private void init_PieLegend(List<PieEntry> pieEntries, List<String> mColor1) {
        //隐藏原有图例
        Legend legend=mPieChart.getLegend();
        legend.setEnabled(false);
        linearLayout.removeAllViews();
        //在视图布局完成后调用
        linearLayout.post(new Runnable() {
            @Override
            public void run() {
                int num=2;//每行个数
                LinearLayout linelayout=null;//行视图
                //装数据入单个布局
                for (int i = 0; i < pieEntries.size(); i++) {
                    //获取图例
                    LinearLayout legend = getLineLegend//第一个参数是获得到字符串转为数字传入的意思
                            (Color.parseColor(mColor1.get(i%mColor1.size())), pieEntries.get(i).getLabel(), (int) pieEntries.get(i).getValue());

                    //超过num时再创建一个新的行，否则复用
                    if (i%num==0) {
                        linelayout= null;//单个图例的布局
                        linelayout = new LinearLayout(getActivity());
                        LinearLayout.LayoutParams lp=new LinearLayout.
                                LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        linelayout.setOrientation(LinearLayout.HORIZONTAL);//水平排列
                        linelayout.setLayoutParams(lp);
                    }

                   //行视图添加
                    linelayout.addView(legend);//加到行
                    if(i%2==0) linearLayout.addView(linelayout);//加到整个
                }
            }
        });
    }

    private void initview(View view) {
        cmain=view.findViewById(R.id.main);
        linearLayout=view.findViewById(R.id.pie_linerayout);
        mPieChart = (PieChart) view.findViewById(R.id.pie_chart);
        scrollView=view.findViewById(R.id.pie_scroll);
        btAdd=view.findViewById(R.id.bt_pie_add);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieEntriesDay.add(new PieEntry(3.3f,"打搅"));
                Toast.makeText(getActivity(),"已增加",Toast.LENGTH_LONG).show();
                init_PieLegend(pieEntriesDay,mColorPie);
                updataPie(pieEntriesDay);
            }
        });
    }

    @NonNull
    private LinearLayout getLineLegend(Integer color, String label, int data) {
        LinearLayout.LayoutParams lp=new LinearLayout.
                LayoutParams(linearLayout.getWidth()/2, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight=1;//设置比重为1


        LinearLayout layout=new LinearLayout(getActivity());//单个图例的布局
        layout.setOrientation(LinearLayout.HORIZONTAL);//水平排列
        layout.setGravity(Gravity.CENTER_VERTICAL);//垂直居中
        layout.setLayoutParams(lp);

        //添加color
        LinearLayout.LayoutParams colorLP=new LinearLayout.
                LayoutParams(50,50);//大小
        colorLP.setMargins(0, 0, 20, 0);//位置
        LinearLayout colorLayout=new LinearLayout(getActivity());//创建对象
        colorLayout.setLayoutParams(colorLP);//颜色设置
        colorLayout.setBackgroundColor(color);
        layout.addView(colorLayout);

        //添加label
        TextView labelTV=new TextView(getActivity());
        labelTV.setMaxLines(2);//最大行
        labelTV.setWidth(linearLayout.getWidth()/4);//宽度
        labelTV.setEllipsize(TextUtils.TruncateAt.END);//省略模式
        labelTV.setText(label+" ");
        if(labelTV.getLineCount()>1)labelTV.setTextSize(10);//字体大小
        else labelTV.setTextSize(20);
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

            tvContent = findViewById(R.id.maker_tv);

    }

        // 每次MarkerView被调用时，都会回调此方法，可以更新UI
        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            PieEntry pieEntry = (PieEntry) e;
            // 假设你的Entry中有具体的数值，这里我们假设它是float类型的value
            float value = pieEntry.getY();
            float percent = (float) (value / getTotal()) * 100f; // getTotal() 是你需要自定义的方法来获取所有Entry的Y值之和

            tvContent.setText("活动:"+pieEntry.getLabel()+"\n"+"时长: " + value+"\n"+"占比："+(String.format("%.2f", percent) + "%"));
            tvContent.setBackgroundColor(Color.parseColor("#FFCCCCCC"));
            tvContent.setTextColor(Color.BLACK);
            super.refreshContent(e, highlight);
        }

        // 自定义方法来获取所有Entry的Y值之和，这取决于你如何管理你的数据集
        private float getTotal() {
            float sum=0;
            for (int i = 0; i < pieEntriesDay.size(); i++) {
                sum+= pieEntriesDay.get(i).getValue();
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