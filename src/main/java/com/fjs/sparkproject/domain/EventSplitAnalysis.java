package com.fjs.sparkproject.domain;

import java.util.Date;

public class EventSplitAnalysis {

  private Integer id;
  private Integer websiteId;
  private String splitName;
  private Integer happenTime;
  private Double conversionRate;
  private java.util.Date date;

  public EventSplitAnalysis() {
  }

  public EventSplitAnalysis(Integer id, Integer websiteId, String splitName, Integer happenTime, Double conversionRate, Date date) {
    this.id = id;
    this.websiteId = websiteId;
    this.splitName = splitName;
    this.happenTime = happenTime;
    this.conversionRate = conversionRate;
    this.date = date;
  }

  @Override
  public String toString() {
    return "EventSplitAnalysis{" +
            "id=" + id +
            ", websiteId=" + websiteId +
            ", splitName='" + splitName + '\'' +
            ", happenTime=" + happenTime +
            ", conversionRate=" + conversionRate +
            ", date=" + date +
            '}';
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


  public String getSplitName() {
    return splitName;
  }

  public void setSplitName(String splitName) {
    this.splitName = splitName;
  }


  public Integer getHappenTime() {
    return happenTime;
  }

  public void setHappenTime(Integer happenTime) {
    this.happenTime = happenTime;
  }


  public Double getConversionRate() {
    return conversionRate;
  }

  public void setConversionRate(Double conversionRate) {
    this.conversionRate = conversionRate;
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

}
