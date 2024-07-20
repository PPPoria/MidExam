package com.example.midexam.graph;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class PercentValueFormatter implements IValueFormatter {
    private DecimalFormat mFormat;

    public PercentValueFormatter() {
        mFormat = new DecimalFormat("#,##0.00"); // 使用你想要的格式
    }


    public String getFormattedValue(float value) {
        // 这里假设value已经是百分比（即0-1之间的数），如果不是，你需要先转换它s
        String s= mFormat.format(value * 100) + "%";
        return s;// 乘以100转换为百分比，并添加“%”符号
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return null;
    }


}

