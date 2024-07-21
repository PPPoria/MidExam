package com.example.midexam.model;

import java.util.List;

public class UserData {
    private String msg = "0";
    private String name;
    private String headImageUrl;
    private String backgroundImageUrl;
    private String intervalTime;
    private int waterTarget;
    private int waterDrink;
    private int waterSum;
    private List<String> waterPerDay;
    private List<String> waterPerMonth;
    private List<String> jobs;
    private List<String> finishJobs;
    private int focusSum;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getWaterTarget() {
        return waterTarget;
    }

    public void setWaterTarget(int waterTarget) {
        this.waterTarget = waterTarget;
    }

    public int getWaterDrink() {
        return waterDrink;
    }

    public void setWaterDrink(int waterDrink) {
        this.waterDrink = waterDrink;
    }

    public int getWaterSum() {
        return waterSum;
    }

    public void setWaterSum(int waterSum) {
        this.waterSum = waterSum;
    }

    public List<String> getWaterPerDay() {
        return waterPerDay;
    }

    public void setWaterPerDay(List<String> waterPerDay) {
        this.waterPerDay = waterPerDay;
    }

    public List<String> getWaterPerMonth() {
        return waterPerMonth;
    }

    public void setWaterPerMonth(List<String> waterPerMonth) {
        this.waterPerMonth = waterPerMonth;
    }

    public List<String> getJobs() {
        return jobs;
    }

    public void setJobs(List<String> jobs) {
        this.jobs = jobs;
    }

    public List<String> getFinishJobs() {
        return finishJobs;
    }

    public void setFinishJobs(List<String> finishJobs) {
        this.finishJobs = finishJobs;
    }

    public int getFocusSum() {
        return focusSum;
    }

    public void setFocusSum(int focusSum) {
        this.focusSum = focusSum;
    }
}
