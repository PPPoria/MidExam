package com.example.midexam.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.midexam.R;
import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.model.UserData;
import com.example.midexam.presenter.UserPresenter;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticTimePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticTimePageFragment extends Fragment implements View.OnClickListener,UserDataShowInterface {

    Button btDay;
    Button btMonth;
    Button btYear;
    Description description;
    LinearLayout legendLinerLayout;
    UserPresenter userPresenter=UserPresenter.getInstance(this);

    PieChart mPieChart;
    ScrollView scrollView;
    TextView title;
    TextView tipsNoData;
    PopupWindow currentPop=null;

    List<PieEntry> addList=null;
    List<PieEntry> pieEntriesDay;
    List<PieEntry> pieEntriesMonth;
    List<PieEntry> pieEntriesYear;
    List<String> mColorPie;

    List<PieEntry> currentPieEntry=null;//拿来点击图的时候用(MarkView)

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public StatisticTimePageFragment() {

    }

    public static StatisticTimePageFragment newInstance(String param1, String param2) {
        StatisticTimePageFragment fragment = new StatisticTimePageFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistic_time_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pieEntriesDay = dataManager.getInstance().getDayDataList();
        pieEntriesMonth=dataManager.getInstance().getMonthDataList();
        pieEntriesYear=dataManager.getInstance().getYearDataList();//必须在前面，否则后面初始化数据空指针错误
        mColorPie = getColorPie();
        initView(view);
        init_Pie();
        initPieData();
        showChart(pieEntriesDay);
        currentPieEntry=pieEntriesDay;
    }

    private void initView(View view) {
        btDay=view.findViewById(R.id.statistic_switchbutton_time).findViewById(R.id.bt_day);
        btMonth=view.findViewById(R.id.statistic_switchbutton_time).findViewById(R.id.bt_month);
        btYear=view.findViewById(R.id.statistic_switchbutton_time).findViewById(R.id.bt_year);
        legendLinerLayout=view.findViewById(R.id.pie_1).findViewById(R.id.pie_linerayout);
        mPieChart=view.findViewById(R.id.pie_1).findViewById(R.id.pie_chart);
        scrollView=view.findViewById(R.id.time_scroll);
        title=view.findViewById(R.id.pie_1).findViewById(R.id.title);
        tipsNoData=view.findViewById(R.id.tip_no_data);

        btDay.setOnClickListener(this);
        btMonth.setOnClickListener(this);
        btYear.setOnClickListener(this);
    }
//饼图初始化图像
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
        if(currentPop!=null) currentPop.dismiss();
    }
//增加数据
    private void addData(List<PieEntry> mlist, List<PieEntry> root) {
        if (mlist!=null) {
            boolean noConflict=true;
            for (int i = 0; i < mlist.size(); i++) {
                noConflict=true;//确保每一次都是当作没有重复的，不然的话有可能因为上一个数据为false导致这里没有及时更新
                String label=mlist.get(i).getLabel();
                float time=mlist.get(i).getValue();
                for (int i1 = 0; i1 < root.size(); i1++) {
                    if(label.equals(root.get(i1).getLabel())){
                        float originTime=root.get(i1).getValue();
                        root.get(i1).setY(originTime+time);
                        noConflict=false;
                        break;
                    }
                }
                if(noConflict){
                    root.add(mlist.get(i));
                    noConflict=true;
                }
            }
        }
    }
//展示饼图
    private void showChart(List<PieEntry> dataList){
        if(dataList.size()==0||dataList==null){
            tipsNoData.setVisibility(View.VISIBLE);
            mPieChart.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            legendLinerLayout.setVisibility(View.INVISIBLE);

        }else{
            updataPie(dataList);
            tipsNoData.setVisibility(View.GONE);
            mPieChart.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            legendLinerLayout.setVisibility(View.VISIBLE);
        }
        if(currentPop!=null) currentPop.dismiss();
    }
//单个图例
    private LinearLayout getLineLegend(Integer color, String label, int data,List<PieEntry> pieEntries) {
        LinearLayout.LayoutParams lp=new LinearLayout.
                LayoutParams(legendLinerLayout.getWidth()/2, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,50);
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
        labelTV.setMaxLines(1);//最大行
        labelTV.setWidth(legendLinerLayout.getWidth()/5);//宽度
        labelTV.setEllipsize(TextUtils.TruncateAt.END);//省略模式
        labelTV.setText(label+" ");
        labelTV.setTextSize(15);
        layout.addView(labelTV);

        //添加data
        TextView dataTV=new TextView(getActivity());

        if(pieEntries==pieEntriesMonth){

            int remainingMinutes = data % (24 * 60);
            int hours = remainingMinutes / 60;
            int minutes = remainingMinutes % 60;
            dataTV.setText(hours+"时"+minutes+"分");
        }
        else if (pieEntries==pieEntriesYear) {
            int remainingMinutes = data % (30 * 24 * 60);
            int days = remainingMinutes / (24 * 60);
            remainingMinutes = remainingMinutes % (24 * 60);
            int hours = remainingMinutes / 60;
            dataTV.setText(days+"天"+hours+"时");
        }else {
            int hours=data/60;
            int mins=data%60;
            dataTV.setText(hours+"时"+mins+"分");
        }
        dataTV.setTextSize(15);
        layout.addView(dataTV);
        return layout;
    }
//饼图颜色
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
//初始化饼图数据
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

//饼图图例更新
    private void updataPieLegend(List<PieEntry> pieEntries, List<String> mColor1) {
        //隐藏原有图例
        Legend legendOrigin=mPieChart.getLegend();
        legendOrigin.setEnabled(false);
        legendLinerLayout.removeAllViews();
        //在视图布局完成后调用
        legendLinerLayout.post(new Runnable() {
            @Override
            public void run() {
                int num=2;//每行个数
                LinearLayout linelayout=null;//行视图
                //装数据入单个布局
                for (int i = 0; i < pieEntries.size(); i++) {
                    //获取图例
                   LinearLayout legend = getLineLegend//第一个参数是获得到字符串转为数字传入的意思
                            (Color.parseColor(mColor1.get(i%mColor1.size())), pieEntries.get(i).getLabel(),
                                    (int) pieEntries.get(i).getValue(),pieEntries);

                    legend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDetailedInfo(legend);
                        }
                    });
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
                    if(i%2==0) legendLinerLayout.addView(linelayout);//加到整个
                }
            }
        });
    }
//饼图数据更新
    private void updataPie(List<PieEntry> dataResourse) {
        PieDataSet iPieDataSet = init_PieDataSet(dataResourse, mColorPie);
        PieData pieData = new PieData(iPieDataSet);
        pieData.setValueFormatter(new PercentFormatter());//使其有百分号
        // PieDataSet
        iPieDataSet.setValueFormatter(new PercentFormatter());
        updataPieLegend(dataResourse, mColorPie);
        mPieChart.setData(pieData);
        mPieChart.invalidate();//更新图表
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
          case R.id.bt_day:
                pieEntriesDay=dataManager.getInstance().getDayDataList();
                showChart(pieEntriesDay);
                currentPieEntry=pieEntriesDay;
                break;

            case R.id.bt_month:

                pieEntriesMonth=dataManager.getInstance().getMonthDataList();
                showChart(pieEntriesMonth);
                currentPieEntry=pieEntriesMonth;
                break;

            case R.id.bt_year:
                pieEntriesYear=dataManager.getInstance().getYearDataList();
                showChart(pieEntriesYear);
                currentPieEntry=pieEntriesYear;
                break;

            default:
                break;
        }
    }
//展示图例具体信息
    private void showDetailedInfo(LinearLayout layout) {
        if (currentPop!=null) {
            currentPop.dismiss();
        }
        // 创建一个布局，用于显示详细信息
        View popupView = getLayoutInflater().inflate(R.layout.statistics_popwindow, null);

        // 初始化PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        TextView show=popupWindow.getContentView().findViewById(R.id.statistics_popText);
        TextView labelView=(TextView) layout.getChildAt(1);
        String label=labelView.getText().toString();
        show.setText("活动:"+label);
        currentPop=popupWindow;
        // 设置PopupWindow的显示位置
        popupWindow.showAsDropDown(layout, 0, 0);
        // 如果需要，你还可以为popupView中的元素设置监听器等
    }

    private void initPieData(){
        /*"finishJobs": ["x","y"]
"072517300145说的道理"，表示已完成任务的开启时间为07月25日17点30分，持续时间01小时45分钟，任务名为“说的道理”。
  */
        UserData userData=userPresenter.userData;
        List<String> finishJobs=userData.getFinishJobs();

        List<PieEntry> year=new ArrayList<>();//这里可以优化内存改进
        List<PieEntry> month=new ArrayList<>();
        List<PieEntry> day=new ArrayList<>();


        for (int i = 0; i < finishJobs.size(); i++) {
            String Month=finishJobs.get(i).substring(0,2);
            String Day=finishJobs.get(i).substring(2,4);
            String duringHour=finishJobs.get(i).substring(8,10);
            String duringmin=finishJobs.get(i).substring(10,12);
            String eventName=finishJobs.get(i).substring(12);
            int duringTime=Integer.valueOf(duringHour)*60+Integer.valueOf(duringmin);
            if(Integer.valueOf(Month)==Calendar.getInstance().get(Calendar.MONTH)+1){
                PieEntry MonthPie=new PieEntry(duringTime,Day+"日");
                month.add(MonthPie);
                if(Integer.valueOf(Day)== Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
                    PieEntry DayPie=new PieEntry(duringTime,eventName);
                    day.add(DayPie);
                }//日
            }//月


            PieEntry YearPie=new PieEntry(duringTime,Month+"月");
            year.add(YearPie);//all进入年，后面函数自动判定合并，可优化逻辑
        }
        addData(day,pieEntriesDay);
        addData(month,pieEntriesMonth);
        addData(year,pieEntriesYear);
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

    //饼图点击
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

            tvContent.setText("时长: " + value+"min\n"+"占比："+(String.format("%.2f", percent) + "%"));
            tvContent.setBackgroundColor(Color.parseColor("#FFCCCCCC"));
            tvContent.setTextColor(Color.BLACK);
            super.refreshContent(e, highlight);
        }

        // 自定义方法来获取所有Entry的Y值之和，这取决于你如何管理你的数据集
        private float getTotal() {
            float sum=0;
            for (int i = 0; i < currentPieEntry.size(); i++) {
                sum+= currentPieEntry.get(i).getValue();
            }
            return sum; // 示例值，应该替换为你的实际计算值
        }

        @Override
        public MPPointF getOffset() {
            // 设置MarkerView的偏移量
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }
//获取单例
    static class dataManager{
        List<PieEntry> dayDataList;
        List<PieEntry> monthDataList;
        List<PieEntry> yearDataList;

        public dataManager(List<PieEntry> dayDataList, List<PieEntry> monthDataList, List<PieEntry> yearDataList) {
            this.dayDataList = dayDataList;
            this.monthDataList = monthDataList;
            this.yearDataList = yearDataList;
        }

        //2.静态内部类初始化
        private static final class dataManagerHolder{
            private static final dataManager INSTANCE=new dataManager(new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        }
        //3.对外提供静态类方法获取该对象
        public static dataManager getInstance(){
            return dataManagerHolder.INSTANCE;
        }

        public List<PieEntry> getDayDataList(){
            return dayDataList;
        }

        public List<PieEntry> getMonthDataList() {
            return monthDataList;
        }

        public List<PieEntry> getYearDataList() {
            return yearDataList;
        }
    }

}