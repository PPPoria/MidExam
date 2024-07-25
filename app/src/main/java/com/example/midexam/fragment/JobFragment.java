package com.example.midexam.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midexam.R;
import com.example.midexam.adapter.ItemAdapter;
import com.example.midexam.adapter.ItemSelectedAdapter;
import com.example.midexam.model.ItemData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class JobFragment extends Fragment implements View.OnClickListener {
    Button addJob;
    Button multipleSelect;
    Button multipleDelete;
    Button multipleCancel;
    static RecyclerView jobContent;
    TextView jobGreeting;
    static EditJobFragment editJobFragment;
    static TimePickFragment timePickFragment;

    private static List<ItemData> jobList = new ArrayList<>();
    private static FragmentManager fragmentManager;
    private ItemAdapter itemAdapter;
    private ItemSelectedAdapter itemSelectedAdapter;

    static int itemPosition=0;
    static Activity activity;
    static List<Integer> deleteList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ADD_JOB="AddJob";
    public static final String MODIFY_JOB="ModifyJob";
    public static final String SET_START_TIME="SetStartTime";
    private String mParam1;
    private String mParam2;

    public JobFragment() {

    }

    public static JobFragment newInstance(String param1, String param2) {
        JobFragment fragment = new JobFragment();
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
        return inflater.inflate(R.layout.fragment_job, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initNewsListView();
    }
    private void initView(View v){
        jobContent=v.findViewById(R.id.job_content);
        addJob=v.findViewById(R.id.add_job);
        jobGreeting =v.findViewById(R.id.greeting);
        multipleSelect=v.findViewById(R.id.bt_mutipleSelect);
        multipleDelete=v.findViewById(R.id.bt_mutipleSelect_delete);
        multipleCancel=v.findViewById(R.id.bt_mutipleSelect_cancel);

        activity=getActivity();
        fragmentManager= getActivity().getSupportFragmentManager(); //获取为了给编辑代办时代码运用
        editJobFragment =new EditJobFragment();
        timePickFragment=new TimePickFragment();
        deleteList=new ArrayList<>();

        editJobFragment.setJobFragment(this);//设置说明这个碎片所在的碎片位置

        addJob.setOnClickListener(this);
        multipleSelect.setOnClickListener(this);
        multipleDelete.setOnClickListener(this);
        multipleCancel.setOnClickListener(this);

        multipleDelete.setVisibility(View.GONE);
        multipleCancel.setVisibility(View.GONE);
    }

    private void initNewsListView() {
        jobList.add(new ItemData("1","1","b"));
        jobList.add(new ItemData("2","c","d"));
        jobList.add(new ItemData("3","1","b"));
        jobList.add(new ItemData("4","c","d"));
        jobList.add(new ItemData("5","1","b"));
        jobList.add(new ItemData("6","c","d"));
        jobList.add(new ItemData("7","1","b"));
        jobList.add(new ItemData("8","c","d"));
        jobList.add(new ItemData("9","1","b"));
        jobList.add(new ItemData("10","c","d"));
        jobList.add(new ItemData("11","1","b"));
        jobList.add(new ItemData("12","c","d"));
        jobList.add(new ItemData("13","1","b"));
        jobList.add(new ItemData("14","c","d"));
        jobList.add(new ItemData("15","1","b"));
        jobList.add(new ItemData("16","c","d"));
        jobList.add(new ItemData("17","1","b"));
        jobList.add(new ItemData("18","c","d"));
        jobList.add(new ItemData("19","1","b"));
        jobList.add(new ItemData("20","c","d"));


        itemAdapter=new ItemAdapter(getActivity(),getActivity(), jobList);
        itemSelectedAdapter=new ItemSelectedAdapter(getActivity(),getActivity(), jobList);
        jobContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        jobContent.setAdapter(itemAdapter);

        updataList();
    }

    private static void updataList() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jobContent.getAdapter().notifyDataSetChanged();
            }
        });
    }

    //用于后面弹出任务编辑调用
    public static FragmentManager getfragmentManager(){
        return fragmentManager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_job:
                switchDialog(JobFragment.ADD_JOB);
                break;
            case R.id.bt_mutipleSelect:
                addJob.setVisibility(View.GONE);
                multipleDelete.setVisibility(View.VISIBLE);
                multipleCancel.setVisibility(View.VISIBLE);
                jobContent.setAdapter(itemSelectedAdapter);
                updataList();
                break;
            case R.id.bt_mutipleSelect_delete:
                deleteByList(itemSelectedAdapter.getSelectedItems());
                addJob.setVisibility(View.VISIBLE);
                multipleDelete.setVisibility(View.GONE);
                multipleCancel.setVisibility(View.GONE);
          //      Toast.makeText(getContext(),"多选删除",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_mutipleSelect_cancel:
                addJob.setVisibility(View.VISIBLE);
                multipleDelete.setVisibility(View.INVISIBLE);
                multipleCancel.setVisibility(View.INVISIBLE);
                jobContent.setAdapter(itemAdapter);
                break;
            default:
                break;
        }
    }

    public void addItem(ItemData itemData){
        jobList.add(itemData);
        initNewsListView();
    }
    public static void switchDialog(String dialogName){
        switch(dialogName){
            case ADD_JOB:
                editJobFragment.setTitle("添加待办");//日后优化
                editJobFragment.setType(EditJobFragment.EDIT);
                editJobFragment.show(getfragmentManager(),"addJobs");
                break;
            case MODIFY_JOB:
                editJobFragment.setTitle("修改待办");
                editJobFragment.setEvent(jobList.get(itemPosition).getItemText());
                editJobFragment.setStartTime(jobList.get(itemPosition).getJobData());
                editJobFragment.setDuringTime(jobList.get(itemPosition).getJobDuring());
                editJobFragment.show(getfragmentManager(),"modifyJobs");
                editJobFragment.setType(EditJobFragment.MODIFY);

                break;
            case SET_START_TIME:
                timePickFragment.show(getfragmentManager(),"timepicker");//调createDialog

                break;
        }
    }


    public static EditJobFragment getEditJobFragment() {
        return editJobFragment;
    }

    public static TimePickFragment getTimePickFragment() {
        return timePickFragment;
    }

    public static void setItemPosition(int position){
        itemPosition=position;
    }

    public void modify(ItemData itemData){
        ItemData origin=jobList.get(itemPosition);
        origin.setItemText(itemData.getItemText());
        origin.setJobData(itemData.getJobData());
        origin.setJobDuring(itemData.getJobDuring());
        updataList();
    }

    public static void deleteItem(int position){
        jobList.remove(position);
        updataList();
        Toast.makeText(jobContent.getContext(), "已删除",Toast.LENGTH_LONG).show();
    }
    private void deleteByList(List<Integer> deleteList){
        Collections.sort(deleteList, Collections.reverseOrder());
        Log.i("this",String.valueOf(deleteList));
        for (int i = 0; i < deleteList.size(); i++) {
            deleteItem(deleteList.get(i));
        }
        deleteList.clear();
    }

}