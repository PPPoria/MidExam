package com.example.midexam.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
public class StatisticsFragment extends Fragment implements View.OnClickListener{

    Button btFocusChart;
    Button btWaterChart;

    ViewPager2 statisticsViewPager;

    StatisticTimePageFragment statisticTimePageFragment;
    StatisticWaterPageFragment statisticWaterPageFragment;


    List<Fragment> pages;

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

    }








    private void initview(View view) {

        btFocusChart =view.findViewById(R.id.bt_pie_focus);
        btWaterChart=view.findViewById(R.id.bt_bar_water);
        statisticsViewPager=view.findViewById(R.id.statistics_viewpager);
        /*title=view.findViewById(R.id.pie_1).findViewById(R.id.title);*/

        statisticTimePageFragment=new StatisticTimePageFragment();
        statisticWaterPageFragment=new StatisticWaterPageFragment();
        pages=new ArrayList<>();
        pages.add(statisticTimePageFragment);
        pages.add(statisticWaterPageFragment);

        statisticsAdapter statisticsAdapter=new statisticsAdapter(getActivity());
        statisticsViewPager.setAdapter(statisticsAdapter);

        btFocusChart.setOnClickListener(this);
        btWaterChart.setOnClickListener(this);
        /*btDay.setOnClickListener(this);
        btMonth.setOnClickListener(this);
        btYear.setOnClickListener(this);*/


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_pie_focus:
                /*List<PieEntry> mlist=new ArrayList<>();
                mlist.add(new PieEntry(10,"打搅"));
                mlist.add(new PieEntry(5,"睡觉"));
                Toast.makeText(getActivity(),"已增加",Toast.LENGTH_LONG).show();
                addList=mlist;
                addData(addList,pieEntriesDay);
                updataPie(pieEntriesDay);*/
                statisticsViewPager.setCurrentItem(0);

                break;

            case R.id.bt_bar_water:
                statisticsViewPager.setCurrentItem(1);
                break;

         /*   case R.id.bt_pie_day:
                init_Pie();
                pieEntriesDay=dataManager.getInstance().getDayDataList();
                updataPie(pieEntriesDay);

                break;

            case R.id.bt_pie_month:
                init_Pie();
                pieEntriesMonth=dataManager.getInstance().getMonthDataList();
                //pieEntriesMonth.add(new PieEntry(1.0f,"打搅"));
                updataPie(pieEntriesMonth);
                break;

            case R.id.bt_pie_year:

                break;
*/
           /* case R.id.bt_pie_clearday:

                break;

            case R.id.bt_pie_clearmonth:

                break;
*/
            default:
                break;
        }
    }

    //当pager发生变化时调用
    public void Pagerchange(){
        statisticsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // pagePosition = position;
                switch (position) {
                    case 0: {

                        break;
                    }
                    case 1: {

                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }



    class statisticsAdapter extends FragmentStateAdapter {

        public statisticsAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return pages.get(position);
        }

        @Override
        public int getItemCount() {
            return pages.size();
        }
    }
}