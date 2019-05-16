package com.fjs.sparkproject.domain;

import java.util.Date;

public class PageFlowSplitAnalysis {

  private Integer id;
  private Integer websiteId;
  private String pageSplit;
  private Integer happenTime;
  private Double conversionRate;
  private java.util.Date date;

  public PageFlowSplitAnalysis() {
  }

  public PageFlowSplitAnalysis(Integer id, Integer websiteId, String pageSplit, Integer happenTime, Double conversionRate, Date date) {
    this.id = id;
    this.websiteId = websiteId;
    this.pageSplit = pageSplit;
    this.happenTime = happenTime;
    this.conversionRate = conversionRate;
    this.date = date;
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


  public String getPageSplit() {
    return pageSplit;
  }

  public void setPageSplit(String pageSplit) {
    this.pageSplit = pageSplit;
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
