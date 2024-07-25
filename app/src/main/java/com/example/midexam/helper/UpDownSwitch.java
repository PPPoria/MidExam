package com.example.midexam.helper;

import com.example.midexam.model.ItemData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpDownSwitch {
    public static String getDateDownType(String itemText) {
        String Year=  String.valueOf(Calendar.getInstance().get(Calendar.YEAR));;
        String Month=itemText.substring(0,2);
        String Day=itemText.substring(2,4);
        String Hour= itemText.substring(4,6);
        String Min=itemText.substring(6,8);
        return  Year+"-"+Month+"-"+Day+" "+Hour+":"+Min;
    }

    public static String getDuringDownType(String itemText){
        String shour=itemText.substring(0,2);
        String smin=itemText.substring(2,4);
        int hour=Integer.valueOf(shour)*60;
        int during=hour+Integer.valueOf(smin);
        return String.valueOf(during);
    }


    public static String setDateUpType(String itemText) {
       //2024-01-01 01:01
        String Month=itemText.substring(5,7);
        String Day=itemText.substring(8,10);
        String Hour=itemText.substring(11,13);
        String Min=itemText.substring(14,16);
        return Month+Day+Hour+Min;
    }
    public static String setDuringUpType(String itemText) {
        int Hour=Integer.valueOf(itemText)/60;
        int Min=Integer.valueOf(itemText)%60;
        return String.valueOf(Hour)+String.valueOf(Min);
    }



    public static List<String> setJobUPType(List<ItemData> list){
        List<String> jobs=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String date=setDateUpType(list.get(i).getJobData());
            String during=setDuringUpType( list.get(i).getJobDuring());
            String itemText=list.get(i).getItemText();
            String up=date+during+itemText;
            jobs.add(up);
        }
        return jobs;
    }
}
