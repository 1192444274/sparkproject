package com.fjs.sparkproject.domain;

import java.util.Date;

public class WebsiteAnalysis {
    private Integer id;
    private Integer websiteId;
    private Integer dimensionId1;
    private Integer dimensionInformationId1;
    private Integer dimensionId2;
    private Integer dimensionInformationId2;
    private Date date;
    private Long userNum;
    private Long ipNum;
    private Double bounceRate;
    private Double secondSkipRate;
    private Double averageAccessTime;
    private Double averageAccessDepth;

    public WebsiteAnalysis() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public Integer getDimensionId1() {
        return dimensionId1;
    }

    public void setDimensionId1(Integer dimensionId1) {
        this.dimensionId1 = dimensionId1;
    }

    public Integer getDimensionInformationId1() {
        return dimensionInformationId1;
    }

    public void setDimensionInformationId1(Integer dimensionInformationId1) {
        this.dimensionInformationId1 = dimensionInformationId1;
    }

    public Integer getDimensionId2() {
        return dimensionId2;
    }

    public void setDimensionId2(Integer dimensionId2) {
        this.dimensionId2 = dimensionId2;
    }

    public Integer getDimensionInformationId2() {
        return dimensionInformationId2;
    }

    public void setDimensionInformationId2(Integer dimensionInformationId2) {
        this.dimensionInformationId2 = dimensionInformationId2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getUserNum() {
        return userNum;
    }

    public void setUserNum(Long userNum) {
        this.userNum = userNum;
    }

    public Long getIpNum() {
        return ipNum;
    }

    public void setIpNum(Long ipNum) {
        this.ipNum = ipNum;
    }

    public Double getBounceRate() {
        return bounceRate;
    }

    public void setBounceRate(Double bounceRate) {
        this.bounceRate = bounceRate;
    }

    public Double getSecondSkipRate() {
        return secondSkipRate;
    }

    public void setSecondSkipRate(Double secondSkipRate) {
        this.secondSkipRate = secondSkipRate;
    }

    public Double getAverageAccessTime() {
        return averageAccessTime;
    }

    public void setAverageAccessTime(Double averageAccessTime) {
        this.averageAccessTime = averageAccessTime;
    }

    public Double getAverageAccessDepth() {
        return averageAccessDepth;
    }

    public void setAverageAccessDepth(Double averageAccessDepth) {
        this.averageAccessDepth = averageAccessDepth;
    }

    public WebsiteAnalysis(Integer websiteId, Integer dimensionId1, Integer dimensionInformationId1, Integer dimensionId2, Integer dimensionInformationId2, Date date, Long userNum, Long ipNum, Double bounceRate, Double secondSkipRate, Double averageAccessTime, Double averageAccessDepth) {
        this.websiteId = websiteId;
        this.dimensionId1 = dimensionId1;
        this.dimensionInformationId1 = dimensionInformationId1;
        this.dimensionId2 = dimensionId2;
        this.dimensionInformationId2 = dimensionInformationId2;
        this.date = date;
        this.userNum = userNum;
        this.ipNum = ipNum;
        this.bounceRate = bounceRate;
        this.secondSkipRate = secondSkipRate;
        this.averageAccessTime = averageAccessTime;
        this.averageAccessDepth = averageAccessDepth;
    }

    @Override
    public String toString() {
        return "WebsiteAnalysis{" +
                "id=" + id +
                ", websiteId=" + websiteId +
                ", dimensionId1=" + dimensionId1 +
                ", dimensionInformationId1=" + dimensionInformationId1 +
                ", dimensionId2=" + dimensionId2 +
                ", dimensionInformationId2=" + dimensionInformationId2 +
                ", date=" + date +
                ", userNum=" + userNum +
                ", ipNum=" + ipNum +
                ", bounceRate=" + bounceRate +
                ", secondSkipRate=" + secondSkipRate +
                ", averageAccessTime=" + averageAccessTime +
                ", averageAccessDepth=" + averageAccessDepth +
                '}';
    }

    public WebsiteAnalysis(Integer id, Integer websiteId, Integer dimensionId1, Integer dimensionInformationId1, Integer dimensionId2, Integer dimensionInformationId2, Date date, Long userNum, Long ipNum, Double bounceRate, Double secondSkipRate, Double averageAccessTime, Double averageAccessDepth) {
        this.id = id;
        this.websiteId = websiteId;
        this.dimensionId1 = dimensionId1;
        this.dimensionInformationId1 = dimensionInformationId1;
        this.dimensionId2 = dimensionId2;
        this.dimensionInformationId2 = dimensionInformationId2;
        this.date = date;
        this.userNum = userNum;
        this.ipNum = ipNum;
        this.bounceRate = bounceRate;
        this.secondSkipRate = secondSkipRate;
        this.averageAccessTime = averageAccessTime;
        this.averageAccessDepth = averageAccessDepth;
    }
}
