package com.example.midexam.graph;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.io.IOException;
import java.util.List;

public class LineChartForm {
    LineChart lineChart;
    YAxis yAxis=null;
    XAxis xAxis=null;

    public LineChartForm(LineChart lineChart) {
        this.lineChart = lineChart;
    }

    //设置x的坐标始、终数据，表现位置，坐标谁否全部显示，网格是否显示（竖的）。
    public <T> void setXData
            (String position, float IndexEnd, float IndexStart, boolean showall, boolean GridLine, int AxisColor, List<Entry> resourse, float LineWidth) throws IOException {
            xAxis=lineChart.getXAxis();
            //位置
            if (position!=null) {
                switch(position.trim()){
                    case "TOP":
                    case "top":
                        xAxis.setPosition(XAxis.XAxisPosition.TOP);
                        break;
                    case "BOTTOM":
                    case "bottom":
                    case "":
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        break;
                    case "TOP_INSIDE":
                    case "top_insde":
                        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
                        break;
                    case "BOTTOM_INSIDE":
                    case "bottom_inside":
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
                        break;
                    default:
                        throw new IOException("Wrong keys for position.");
                }
            }
            else{
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            }

            //坐标范围
            xAxis.setAxisMaximum(IndexEnd);
            xAxis.setAxisMinimum(IndexStart);

            //颜色
            xAxis.setAxisLineColor(AxisColor);

            //网格
            xAxis.setDrawGridLines(GridLine);

            //显示个数
            xAxis.setLabelCount(resourse.size(),showall);

            //轴宽
            xAxis.setAxisLineWidth(LineWidth);
    }

    //设置y的坐标始、终数据，表现位置，坐标谁否全部显示，网格是否显示（竖的）。
    public <T> void setYData
            (String position, float IndexEnd, float IndexStart, boolean showall, boolean GridLine, int AxisColor, List<Entry> resourse, float LineWidth,boolean HideOther) throws IOException {
        //位置

        if (position!=null) {
            switch(position.trim()){
                case "LEFT":
                case "left":
                    yAxis=lineChart.getAxisLeft();
                    //隐藏另一条
                    lineChart.getAxisRight().setEnabled(HideOther);
                    break;
                case "RIGHT":
                case "right":
                    yAxis=lineChart.getAxisRight();
                    //隐藏另一条
                    lineChart.getAxisLeft().setEnabled(HideOther);
                    break;
                default:
                    throw new IOException("Wrong keys for position.");
            }
        }
        else{
            yAxis=lineChart.getAxisLeft();
        }

        //坐标范围
        yAxis.setAxisMaximum(IndexEnd);
        yAxis.setAxisMinimum(IndexStart);

        //颜色
        yAxis.setAxisLineColor(AxisColor);

        //网格
        yAxis.setDrawGridLines(GridLine);

        //显示个数
        yAxis.setLabelCount(resourse.size(),showall);

        //轴宽
        yAxis.setAxisLineWidth(LineWidth);

    }

    //设置x的下标表示形式
    public void setXForm(IAxisValueFormatter form){
        xAxis.setValueFormatter(form);
    }

    //设置y的下标表示形式
    public void setYForm(IAxisValueFormatter form){
        yAxis.setValueFormatter(form);
    }

    //设置是否有描述
    public void setDescription(String s,float x,float y,int Color,int textSize){
        if(s==null){s="";}
        lineChart.getDescription().setEnabled(true);
        lineChart.getDescription().setText(s);
        lineChart.getDescription().setPosition(x,y);
        lineChart.getDescription().setTextSize(textSize);
        lineChart.getDescription().setTextColor(Color);
    }

    //动画时间
    public void animation(int X_Duration,int Y_Duration){
        lineChart.animateXY(X_Duration,Y_Duration);//XY两轴混合动画
    }

    //更新数据并启动
    public void start(){
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    //背景色
    public void background(int Color){
        lineChart.setBackgroundColor(Color);   //背景颜色
    }
}
