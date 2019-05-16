package com.fjs.sparkproject.domain;

import java.util.Date;

public class Top10City {
    private int pageid;
    private String cityName;
    private int user_num;
    private Date date;

    public Top10City(int pageid, String cityName, int user_num, Date date) {
        this.pageid = pageid;
        this.cityName = cityName;
        this.user_num = user_num;
        this.date = date;
    }

    public Top10City() {

    }

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getUser_num() {
        return user_num;
    }

    public void setUser_num(int user_num) {
        this.user_num = user_num;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
