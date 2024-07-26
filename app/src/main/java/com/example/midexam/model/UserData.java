package com.example.midexam.model;

import java.util.List;

public class UserData {
    private String msg = "success";
    private String account = "account";
    private String name = "UserName";
    private String color = "#6bbef2";
    private String headImageUrl;
    private String backgroundImageUrl;
    private String intervalTime;
    private int waterTarget;
    private int waterDrink;
    private  int weather;

    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public List<String> getWaterToday() {
        return waterToday;
    }

    public void setWaterToday(List<String> waterToday) {
        this.waterToday = waterToday;
    }

    private int waterSum;
    private List<String> waterToday;
    private List<String> waterPerDay;
    private List<String> waterPerMonth;

    private List<String> jobs;
    private List<String> finishJobs;
    private int focusSum;

    //TODO 天气 晴天0，多云1，阴天2，下雨3


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
