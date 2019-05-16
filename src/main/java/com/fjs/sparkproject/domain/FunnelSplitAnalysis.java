package com.fjs.sparkproject.domain;

import java.util.Date;

public class FunnelSplitAnalysis {

  private Integer id;
  private Integer funnelId;
  private String eventSplit;
  private Integer happenTime;
  private Double conversionRate;
  private java.util.Date date;

  public FunnelSplitAnalysis() {
  }

  @Override
  public String toString() {
    return "FunnelSplitAnalysis{" +
            "id=" + id +
            ", funnelId=" + funnelId +
            ", eventSplit='" + eventSplit + '\'' +
            ", happenTime=" + happenTime +
            ", conversionRate=" + conversionRate +
            ", date=" + date +
            '}';
  }

  public FunnelSplitAnalysis(Integer id, Integer funnelId, String eventSplit, Integer happenTime, Double conversionRate, Date date) {

    this.id = id;
    this.funnelId = funnelId;
    this.eventSplit = eventSplit;
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


  public Integer getFunnelId() {
    return funnelId;
  }

  public void setFunnelId(Integer funnelId) {
    this.funnelId = funnelId;
  }


  public String getEventSplit() {
    return eventSplit;
  }

  public void setEventSplit(String eventSplit) {
    this.eventSplit = eventSplit;
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
