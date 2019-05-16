package com.fjs.sparkproject.domain;

import java.util.Date;

public class PageAnalysis {

  private Integer id;
  private String url;
  private Integer websiteId;
  private String userType;
  private String source;
  private Integer browseNum;
  private Integer conversationNum;
  private java.util.Date date;
  private Integer userNum;
  private Integer ipNum;
  private Double exitRate;
  private Double conversionRate;
  private Double averageAccessTime;

    public PageAnalysis(Integer id, String url, Integer websiteId, String userType, String source, Integer browseNum, Integer conversationNum, Date date, Integer userNum, Integer ipNum, Double exitRate, Double conversionRate, Double averageAccessTime, Integer entryNum) {
        this.id = id;
        this.url = url;
        this.websiteId = websiteId;
        this.userType = userType;
        this.source = source;
        this.browseNum = browseNum;
        this.conversationNum = conversationNum;
        this.date = date;
        this.userNum = userNum;
        this.ipNum = ipNum;
        this.exitRate = exitRate;
        this.conversionRate = conversionRate;
        this.averageAccessTime = averageAccessTime;
        this.entryNum = entryNum;
    }

    @Override
    public String toString() {
        return "PageAnalysis{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", websiteId=" + websiteId +
                ", userType='" + userType + '\'' +
                ", source=" + source +
                ", browseNum=" + browseNum +
                ", conversationNum=" + conversationNum +
                ", date=" + date +
                ", userNum=" + userNum +
                ", ipNum=" + ipNum +
                ", exitRate=" + exitRate +
                ", conversionRate=" + conversionRate +
                ", averageAccessTime=" + averageAccessTime +
                ", entryNum=" + entryNum +
                '}';
    }

    private Integer entryNum;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  public Integer getWebsiteId() {
    return websiteId;
  }

  public void setWebsiteId(Integer websiteId) {
    this.websiteId = websiteId;
  }


  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }


  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }


  public Integer getBrowseNum() {
    return browseNum;
  }

  public void setBrowseNum(Integer browseNum) {
    this.browseNum = browseNum;
  }


  public Integer getConversationNum() {
    return conversationNum;
  }

  public void setConversationNum(Integer conversationNum) {
    this.conversationNum = conversationNum;
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }


  public Integer getUserNum() {
    return userNum;
  }

  public void setUserNum(Integer userNum) {
    this.userNum = userNum;
  }


  public Integer getIpNum() {
    return ipNum;
  }

  public void setIpNum(Integer ipNum) {
    this.ipNum = ipNum;
  }


  public Double getExitRate() {
    return exitRate;
  }

  public void setExitRate(Double exitRate) {
    this.exitRate = exitRate;
  }


  public Double getConversionRate() {
    return conversionRate;
  }

  public void setConversionRate(Double conversionRate) {
    this.conversionRate = conversionRate;
  }


  public Double getAverageAccessTime() {
    return averageAccessTime;
  }

  public void setAverageAccessTime(Double averageAccessTime) {
    this.averageAccessTime = averageAccessTime;
  }


  public Integer getEntryNum() {
    return entryNum;
  }

  public void setEntryNum(Integer entryNum) {
    this.entryNum = entryNum;
  }

}
