package com.example.midexam.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.midexam.R;
import com.example.midexam.adapter.ItemAdapter;
import com.example.midexam.model.ItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobFragment extends Fragment {


    RecyclerView jobContent;

    private List<ItemData> jobList = new ArrayList<>();
    private static FragmentManager fragmentManager;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initNewsListView();
    }
    private void initView(View v){
        jobContent=v.findViewById(R.id.job_content);
        //获取下发这个为了给编辑代办时代码运用
        fragmentManager= getActivity().getSupportFragmentManager();
    }

    private void initNewsListView() {
        jobContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        jobContent.setAdapter(new ItemAdapter(getActivity(),getActivity(), jobList));

        if (jobList==null||jobList.size()==0) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    jobList.add(new ItemData("乙烯一克一克"));
                    jobList.add(new ItemData("注意看，这个男人叫小帅。"));
                    jobContent.getAdapter().notifyDataSetChanged();
                }
            });
          /*  new Thread(new Runnable() {
                @Override
                public void run() {
                    jobList.add(new ItemData("乙烯一克一克"));
                    jobList.add(new ItemData("注意看，这个男人叫小帅。"));
                   // jobContent.getAdapter().notifyDataSetChanged();
                }
            }).start();*/
        }
    }
    //用于后面弹出任务编辑调用
    public static FragmentManager getfragmentManager(){
        return fragmentManager;
    }
}