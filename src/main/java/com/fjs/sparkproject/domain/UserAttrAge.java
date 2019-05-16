package com.fjs.sparkproject.domain;

import java.util.Date;

public class UserAttrAge {
    private int pageid;
    private int user_age_0_17;
    private int user_age_17_24;
    private int user_age_25_34;
    private int user_age_35_44;
    private int user_age_45_54;
    private int user_age_55_100;
    private Date date;

    public UserAttrAge() {
    }

    public UserAttrAge(int pageid, int user_age_0_17, int user_age_17_24, int user_age_25_34, int user_age_35_44, int user_age_45_54, int user_age_55_100, Date date) {
        this.pageid = pageid;
        this.user_age_0_17 = user_age_0_17;
        this.user_age_17_24 = user_age_17_24;
        this.user_age_25_34 = user_age_25_34;
        this.user_age_35_44 = user_age_35_44;
        this.user_age_45_54 = user_age_45_54;
        this.user_age_55_100 = user_age_55_100;
        this.date = date;
    }

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public int getUser_age_0_17() {
        return user_age_0_17;
    }

    public void setUser_age_0_17(int user_age_0_17) {
        this.user_age_0_17 = user_age_0_17;
    }

    public int getUser_age_17_24() {
        return user_age_17_24;
    }

    public void setUser_age_17_24(int user_age_17_24) {
        this.user_age_17_24 = user_age_17_24;
    }

    public int getUser_age_25_34() {
        return user_age_25_34;
    }

    public void setUser_age_25_34(int user_age_25_34) {
        this.user_age_25_34 = user_age_25_34;
    }

    public int getUser_age_35_44() {
        return user_age_35_44;
    }

    public void setUser_age_35_44(int user_age_35_44) {
        this.user_age_35_44 = user_age_35_44;
    }

    public int getUser_age_45_54() {
        return user_age_45_54;
    }

    public void setUser_age_45_54(int user_age_45_54) {
        this.user_age_45_54 = user_age_45_54;
    }

    public int getUser_age_55_100() {
        return user_age_55_100;
    }

    public void setUser_age_55_100(int user_age_55_100) {
        this.user_age_55_100 = user_age_55_100;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
