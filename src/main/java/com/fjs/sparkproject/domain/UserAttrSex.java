package com.fjs.sparkproject.domain;

import java.util.Date;

public class UserAttrSex {
    private int id;
    private int page_id;
    //0男1女
    private int sex;
    private int userNum;
    private Date date;

    public UserAttrSex() {
    }

    public UserAttrSex(int page_id, int sex, int userNum, Date date) {
        this.page_id = page_id;
        this.sex = sex;
        this.userNum = userNum;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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


