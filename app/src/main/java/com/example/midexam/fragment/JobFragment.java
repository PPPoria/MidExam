package com.example.midexam.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midexam.R;
import com.example.midexam.activity.UserDataShowInterface;
import com.example.midexam.adapter.ItemAdapter;
import com.example.midexam.helper.ItemTouchCallBack;
import com.example.midexam.model.ItemData;

import java.util.ArrayList;
import java.util.List;


public class JobFragment extends Fragment implements View.OnClickListener {
    Button addJob;
    static RecyclerView jobContent;
    TextView jobGreeting;
    static EditJobFragment editJobFragment;
    static TimePickFragment timePickFragment;

    private static List<ItemData> jobList = new ArrayList<>();
    private static FragmentManager fragmentManager;
    private ItemAdapter itemAdapter;

    static int itemPosition=0;
    static Activity activity;

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

        activity=getActivity();
        fragmentManager= getActivity().getSupportFragmentManager(); //获取为了给编辑代办时代码运用
        editJobFragment =new EditJobFragment();
        timePickFragment=new TimePickFragment();

        editJobFragment.setJobFragment(this);//设置说明这个碎片所在的碎片位置

        addJob.setOnClickListener(this);

    }

    private void initNewsListView() {
        itemAdapter=new ItemAdapter(getActivity(),getActivity(), jobList);
        jobContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        jobContent.setAdapter(itemAdapter);


        /*ItemTouchHelper.Callback callback = new ItemTouchCallBack(0,0,itemAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(jobContent);
*/
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
}