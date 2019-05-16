package com.fjs.sparkproject.spark.page;

import java.util.Date;

public class PageTimePeriod {
    String url;
    Date startTime;
    Date endTime;

    public PageTimePeriod() {
    }

    public PageTimePeriod(String url, Date startTime, Date endTime) {
        this.url = url;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
