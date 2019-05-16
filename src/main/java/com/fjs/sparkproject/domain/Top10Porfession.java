package com.fjs.sparkproject.domain;

import java.util.Date;

public class Top10Porfession {
    private int pageid;
    private String profession;
    private int userNum;
    private Date date;

    public Top10Porfession() {
    }

    public Top10Porfession(int pageid, String profession, int userNum, Date date) {
        this.pageid = pageid;
        this.profession = profession;
        this.userNum = userNum;
        this.date = date;
    }

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
