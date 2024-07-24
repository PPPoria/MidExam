package com.example.midexam.model;

public class ItemData {
    private String itemText;
    private String jobData;
    private String jobDuring;

    public ItemData(String itemText, String jobData, String jobDuring) {
        this.itemText = itemText;
        this.jobData = jobData;
        this.jobDuring = jobDuring;
    }

    public String getJobData() {
        return jobData;
    }

    public void setJobData(String jobData) {
        this.jobData = jobData;
    }

    public String getJobDuring() {
        return jobDuring;
    }

    public void setJobDuring(String jobDuring) {
        this.jobDuring = jobDuring;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }
}
