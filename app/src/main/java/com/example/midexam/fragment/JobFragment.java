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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midexam.R;
import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.adapter.ItemAdapter;
import com.example.midexam.adapter.ItemSelectedAdapter;
import com.example.midexam.helper.UpDownSwitch;
import com.example.midexam.model.ItemData;
import com.example.midexam.model.UserData;
import com.example.midexam.observer.UserObserver;
import com.example.midexam.presenter.UserPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class JobFragment extends Fragment implements View.OnClickListener, UserDataShowInterface {
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

    static int itemPosition = 0;//表示被选中的item（即将被操作的item）
    static Activity activity;
    static List<Integer> deleteList;
    static UserPresenter userPresenter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ADD_JOB = "AddJob";
    public static final String MODIFY_JOB = "ModifyJob";
    public static final String SET_START_TIME = "SetStartTime";
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
        userPresenter=UserPresenter.getInstance(this);
        activity = getActivity();
        if(userPresenter.isLogged(getContext())){
            initView(view);
            initNewsListView();
        }
       
    }

    private void initView(View v) {
        jobContent = v.findViewById(R.id.job_content);
        addJob = v.findViewById(R.id.add_job);
        jobGreeting = v.findViewById(R.id.greeting);
        multipleSelect = v.findViewById(R.id.bt_mutipleSelect);
        multipleDelete = v.findViewById(R.id.bt_mutipleSelect_delete);
        multipleCancel = v.findViewById(R.id.bt_mutipleSelect_cancel);

        activity = getActivity();
        fragmentManager = getActivity().getSupportFragmentManager(); //获取为了给编辑代办时代码运用
        editJobFragment = new EditJobFragment();
        timePickFragment = new TimePickFragment();
        deleteList = new ArrayList<>();

        editJobFragment.setJobFragment(this);//设置说明这个碎片所在的碎片位置
        timePickFragment.setJobFragment(this);

        addJob.setOnClickListener(this);
        multipleSelect.setOnClickListener(this);
        multipleDelete.setOnClickListener(this);
        multipleCancel.setOnClickListener(this);

        multipleDelete.setVisibility(View.GONE);
        multipleCancel.setVisibility(View.GONE);
    }

    private void initNewsListView() {
        initJobLists();
        itemAdapter = new ItemAdapter(getActivity(), getActivity(), jobList);
        itemSelectedAdapter = new ItemSelectedAdapter(getActivity(), getActivity(), jobList);
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
    public static FragmentManager getfragmentManager() {
        return fragmentManager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    public static List<ItemData> getJobList() {
        return jobList;
    }

    public void addItem(ItemData itemData) {
        jobList.add(itemData);
        initNewsListView();
    }

    public static void switchDialog(String dialogName) {
        switch (dialogName) {
            case ADD_JOB:
                editJobFragment.setTitle("添加待办");//日后优化
                editJobFragment.setType(EditJobFragment.EDIT);
                editJobFragment.show(getfragmentManager(), "addJobs");
                break;
            case MODIFY_JOB:
                editJobFragment.setTitle("修改待办");
                editJobFragment.setEvent(jobList.get(itemPosition).getItemText());
                editJobFragment.setStartTime(jobList.get(itemPosition).getJobData());
                editJobFragment.setDuringTime(jobList.get(itemPosition).getJobDuring());
                editJobFragment.show(getfragmentManager(), "modifyJobs");
                editJobFragment.setType(EditJobFragment.MODIFY);

                break;
            case SET_START_TIME:
                timePickFragment.show(getfragmentManager(), "timepicker");//调createDialog

                break;
        }
    }


    public static EditJobFragment getEditJobFragment() {
        return editJobFragment;
    }

    public static TimePickFragment getTimePickFragment() {
        return timePickFragment;
    }

    public static void setItemPosition(int position) {//表示被选中的item
        itemPosition = position;
    }

    public void modify(ItemData itemData) {
        ItemData origin = jobList.get(itemPosition);
        origin.setItemText(itemData.getItemText());
        origin.setJobData(itemData.getJobData());
        origin.setJobDuring(itemData.getJobDuring());
        jobList.add(itemData);
        userPresenter.updateUserData(getContext());
        userPresenter.userData.setJobs(UpDownSwitch.setJobUPType(jobList));
        initJobLists();
        updataList();
    }

    public static void deleteItem(int position) {
        jobList.remove(position);
        userPresenter.updateUserData(activity);
        userPresenter.userData.setJobs(UpDownSwitch.setJobUPType(jobList));
        initJobLists();
        updataList();
    }

    private void deleteByList(List<Integer> deleteList) {
        Collections.sort(deleteList, Collections.reverseOrder());
        Log.i("this", String.valueOf(deleteList));
        for (int i = 0; i < deleteList.size(); i++) {
            deleteItem(deleteList.get(i));
        }
        deleteList.clear();
    }
    
    private static void initJobLists(){
        /*### "jobs": ["x","y"]
"072517300145说的道理"，表示待办任务的开启时间为07月25日17点30分，持续时间01小时45分钟，任务名为“说的道理”。*/
        UserData userData=userPresenter.userData;
        List<String> jobs=userData.getJobs();
        for (int i = 0; i < jobs.size(); i++) {
            String sbeginTime=jobs.get(i).substring(0,8);
            String beginTime= UpDownSwitch.getDateDownType(sbeginTime);
            String sduringTime=jobs.get(i).substring(8,12);
            String duringTime=UpDownSwitch.getDuringDownType(sduringTime);
            String jobName=jobs.get(i).substring(12);
            jobList.add(new ItemData(beginTime,duringTime,jobName));
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
        if(STATUS==UserPresenter.STATUS_UPDATE_ERROR){
            Toast.makeText(getContext(),"数据更新失败",Toast.LENGTH_SHORT).show();
        }else if (STATUS==UserPresenter.STATUS_NO_INTERNET){
            Toast.makeText(getContext(),"网络差，数据更新失败",Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(getContext(),"数据更新成功",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateUserImage(int STATUS) {

    }

    @Override
    public void receiveUpdate() {
        UserDataShowInterface.super.receiveUpdate();
        initJobLists();
        initNewsListView();
    }
}