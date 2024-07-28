package com.example.midexam.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class JobFragment extends Fragment implements View.OnClickListener, UserDataShowInterface {
    Button addJob;
    Button multipleSelect;
    Button multipleDelete;
    Button multipleCancel;
    static RecyclerView jobContent;
    TextView jobGreeting;
    View view;
    static TextView tvhide;
    UserObserver userObserver1 = registerObserver(this);
    ;
    static EditJobFragment editJobFragment;
    static TimePickFragment timePickFragment;

    private static List<ItemData> jobList = new ArrayList<>();
    private static FragmentManager fragmentManager;
    private ItemAdapter itemAdapter;
    private ItemSelectedAdapter itemSelectedAdapter;


    static int itemPosition = 0;//表示被选中的item（即将被操作的item）
    static Activity activity;
    static List<Integer> deleteList;

    //static List<String> jobs=new ArrayList<>();//////////////////////////////////////////////////////////////////////////////////////
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_job, container, false);

        activity = getActivity();

        initView();

        showUI();
        initNewsListView();
        return view;
    }



    //有网络需求
    private void showUI() {
        if (UserPresenter.getInstance(this).isLogged(requireContext())) {
            multipleSelect.setVisibility(View.VISIBLE);
            addJob.setVisibility(View.VISIBLE);
            if (jobList == null || jobList.isEmpty()) {
                tvhide.setText("暂无任务");
                tvhide.setVisibility(View.VISIBLE);
            } else {
                tvhide.setVisibility(View.GONE);
            }
        } else {
            tvhide.setText("尚未登陆");
            tvhide.setVisibility(View.VISIBLE);
            multipleSelect.setVisibility(View.INVISIBLE);
            addJob.setVisibility(View.INVISIBLE);
        }
    }

    //一些关键参数在创建时就赋值了
    private void initView() {
        jobContent = view.findViewById(R.id.job_content);
        addJob = view.findViewById(R.id.add_job);
        jobGreeting = view.findViewById(R.id.greeting);
        multipleSelect = view.findViewById(R.id.bt_mutipleSelect);
        multipleDelete = view.findViewById(R.id.bt_mutipleSelect_delete);
        multipleCancel = view.findViewById(R.id.bt_mutipleSelect_cancel);

        tvhide = view.findViewById(R.id.nothing);

        activity = getActivity();
        fragmentManager = getActivity().getSupportFragmentManager(); //获取为了给编辑代办时代码运用
        editJobFragment = new EditJobFragment();
        editJobFragment.setContext(requireContext());
        timePickFragment = new TimePickFragment();
        timePickFragment.setContext(requireContext());
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
        if (UserPresenter.getInstance(this).isLogged(requireContext()))
            initJobLists();
        itemAdapter = new ItemAdapter(this, getActivity(), jobList);
        itemSelectedAdapter = new ItemSelectedAdapter(getActivity(), getActivity(), jobList);
        jobContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        jobContent.setAdapter(itemAdapter);
        updataList();
    }

    //这里有网络判断
    private void initJobLists() {
        /*### "jobs": ["x","y"]
"072517300145说的道理"，表示待办任务的开启时间为07月25日17点30分，持续时间01小时45分钟，任务名为“说的道理”。*/
        UserData userData = UserPresenter.getInstance(this).userData;
        List<String> jobs = null;
        if (userData!=null) {
            jobs = userData.getJobs();
        }
        if(jobList!=null||jobList.size()!=0){jobList.clear();}
        if (jobs!=null&&!jobs.isEmpty()) {
            for (int i = 0; i < jobs.size(); i++) {
                try {
                    String sbeginTime = jobs.get(i).substring(0, 8);
                    String beginTime = UpDownSwitch.getDateDownType(sbeginTime);
                    String sduringTime = jobs.get(i).substring(8, 12);
                    String duringTime = UpDownSwitch.getDuringDownType(sduringTime);
                    String jobName = jobs.get(i).substring(12);

                    jobList.add(new ItemData(jobName, beginTime, duringTime));
                    Log.d("this", String.valueOf(jobList));
                } catch (Exception e) {

                }
            }
            jobList = arrangeJob(jobList);
        }
    }

    private static void updataList() {
        if (jobList == null || jobList.size() == 0) {
            tvhide.setVisibility(View.VISIBLE);
        } else {
            tvhide.setVisibility(View.GONE);
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jobContent.getAdapter().notifyDataSetChanged();
            }
        });
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
                List<Integer> temp = itemSelectedAdapter.getSelectedItems();
                if (temp.size() == 0 || temp == null) {
                    Toast.makeText(getContext(), "未选中任何项目", Toast.LENGTH_SHORT).show();
                } else {
                    deleteByList(temp);
                    addJob.setVisibility(View.VISIBLE);
                    multipleDelete.setVisibility(View.GONE);
                    multipleCancel.setVisibility(View.GONE);
                    jobContent.setAdapter(itemAdapter);
                }

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

    //有网络请求
    public void modify(ItemData itemData) {
        ItemData origin = jobList.get(itemPosition);
        origin.setItemText(itemData.getItemText());
        origin.setJobData(itemData.getJobData());
        origin.setJobDuring(itemData.getJobDuring());
        UserPresenter.getInstance(this).userData.setJobs(UpDownSwitch.setJobUPType(jobList));
        UserPresenter.getInstance(this).updateUserData(getContext());
        updataList();
    }

    //有网络需求
    public void addItem(ItemData itemData) {
        jobList.add(itemData);
        //传
        UserPresenter.getInstance(this).userData.setJobs(UpDownSwitch.setJobUPType(jobList));
        UserPresenter.getInstance(this).updateUserData(getContext());/////////////////////////////////////////////////////////////////////////////////////////////////////////
        //更本地(initNewListView=initJob+updatalist)
        initNewsListView();
    }

    //滑动删除用，有网络请求
    public void deleteItem(int position) {
        jobList.remove(position);
        //传
        UserPresenter.getInstance(this).userData.setJobs(UpDownSwitch.setJobUPType(jobList));
        UserPresenter.getInstance(this).updateUserData(activity);/////////////////////////////////////////////////////////////////////////
        //更本地(initNewListView=initJob+updatalist)
        initNewsListView();
    }

    //多选删除用，有网络请求
    private void deleteByList(List<Integer> deleteList) {
        Collections.sort(deleteList, Collections.reverseOrder());
        Log.i("this", String.valueOf(deleteList));
        for (int i = 0; i < deleteList.size(); i++) {
            int k=deleteList.get(i);
            jobList.remove(k);
        }
        //传
        UserPresenter.getInstance(this).userData.setJobs(UpDownSwitch.setJobUPType(jobList));
        UserPresenter.getInstance(this).updateUserData(activity);/////////////////////////////////////////////////////////////////////////
        //更本地(initNewListView=initJob+updatalist)
        initNewsListView();
        deleteList.clear();
    }

    private static List<ItemData> arrangeJob(List<ItemData> ml) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<String> dateStrings = new ArrayList<>();

        for (ItemData item : ml) {
            dateStrings.add(item.getJobData()); // 假设 ItemData 有一个 getDateString() 方法
        }

        dateStrings.sort(Comparator.comparing(dateStr ->
                LocalDateTime.parse(dateStr, formatter)
        ));

        List<ItemData> sortedItems = new ArrayList<>();
        for (String dateStr : dateStrings) {
            for (ItemData item : ml) {
                if (item.getJobData().equals(dateStr)) {
                    sortedItems.add(item);
                    break;
                }
            }
        }
        return sortedItems;
    }

    //用于后面弹出任务编辑调用
    public static FragmentManager getfragmentManager() {
        return fragmentManager;
    }

    public static List<ItemData> getJobList() {
        return jobList;
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


    @Override
    public void log(int STATUS) {

    }

    @Override
    public void register(int STATUS) {

    }

    @Override
    public void updateUserData(int STATUS) {
        if (STATUS == UserPresenter.STATUS_UPDATE_ERROR) {
            Toast.makeText(getContext(), "数据更新失败", Toast.LENGTH_SHORT).show();
        } else if (STATUS == UserPresenter.STATUS_NO_INTERNET) {
            Toast.makeText(getContext(), "网络差，数据更新失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "数据更新成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateUserImage(int STATUS) {

    }

    @Override
    public void receiveUpdate() {
        initView();
        showUI();
        initNewsListView();
    }
}