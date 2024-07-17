package com.example.midexam.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.midexam.R;
import com.example.midexam.graph.LineChartForm;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LineGraph extends AppCompatActivity {
    LineChart lc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_line_graph);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lc=findViewById(R.id.chart);

        List<Entry> mEntry=new ArrayList<>();
        mEntry.add(new Entry(1F, 1));
        mEntry.add(new Entry(2, 3F));
        mEntry.add(new Entry(3, 2F));
        mEntry.add(new Entry(4, 10F));
        mEntry.add(new Entry(5, 10F));
        mEntry.add(new Entry(6, 10F));
        mEntry.add(new Entry(7, 10F));
        mEntry.add(new Entry(8, 10F));
        mEntry.add(new Entry(9, 10F));

        LineDataSet lineDataSet=new LineDataSet(mEntry,"精力");
        LineData lineData=new LineData(lineDataSet);
        lc.setData(lineData);

        LineChartForm lineChartForm=new LineChartForm(lc);
        try {
            lineChartForm.setXData("BOTTOM",10.0f,0.0f,false,false, Color.BLACK,mEntry,1.0f);
            lineChartForm.setYData("LEFT",15F,0F,false,false,Color.GREEN,mEntry,1F,false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //折线
        //设置折线的式样   这个是圆滑的曲线（有好几种自己试试）     默认是直线
        //lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setColor(Color.GREEN);  //折线的颜色
        lineDataSet.setLineWidth(2);        //折线的粗细
        //是否画折线点上的空心圆  false表示直接画成实心圆
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setCircleHoleRadius(3);  //空心圆的圆心半径
        //圆点的颜色     可以实现超过某个值定义成某个颜色的功能   这里先不讲 后面单独写一篇
        lineDataSet.setCircleColor(Color.RED);
        lineDataSet.setCircleRadius(3);      //圆点的半径
        //定义折线上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (entry.getY()==v){
                    return v+"℃";
                }
                return "";
            }
        });
    }
}