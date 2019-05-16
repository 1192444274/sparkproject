package com.fjs.sparkproject.domain;

import java.util.Date;

public class EventParamAnalysis {

  private Integer id;
  private Integer eventId;
  private String paramType;
  private String paramContent;
  private Integer dimensionId1;
  private Integer dimensionInformationId1;
  private Integer dimensionId2;
  private Integer dimensionInformationId2;
  private Integer clickTime;
  private Double rate;
  private java.util.Date date;

  @Override
  public String toString() {
    return "EventParamAnalysis{" +
            "id=" + id +
            ", eventId=" + eventId +
            ", paramType='" + paramType + '\'' +
            ", paramContent='" + paramContent + '\'' +
            ", dimensionId1=" + dimensionId1 +
            ", dimensionInformationId1=" + dimensionInformationId1 +
            ", dimensionId2=" + dimensionId2 +
            ", dimensionInformationId2=" + dimensionInformationId2 +
            ", clickTime=" + clickTime +
            ", rate=" + rate +
            ", date=" + date +
            '}';
  }

  public EventParamAnalysis() {
  }

  public EventParamAnalysis(Integer id, Integer eventId, String paramType, String paramContent, Integer dimensionId1, Integer dimensionInformationId1, Integer dimensionId2, Integer dimensionInformationId2, Integer clickTime, Double rate, Date date) {
    this.id = id;
    this.eventId = eventId;
    this.paramType = paramType;
    this.paramContent = paramContent;
    this.dimensionId1 = dimensionId1;
    this.dimensionInformationId1 = dimensionInformationId1;
    this.dimensionId2 = dimensionId2;
    this.dimensionInformationId2 = dimensionInformationId2;
    this.clickTime = clickTime;
    this.rate = rate;
    this.date = date;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public Integer getEventId() {
    return eventId;
  }

  public void setEventId(Integer eventId) {
    this.eventId = eventId;
  }


  public String getParamType() {
    return paramType;
  }

  public void setParamType(String paramType) {
    this.paramType = paramType;
  }


  public String getParamContent() {
    return paramContent;
  }

  public void setParamContent(String paramContent) {
    this.paramContent = paramContent;
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


  public Integer getClickTime() {
    return clickTime;
  }

  public void setClickTime(Integer clickTime) {
    this.clickTime = clickTime;
  }


  public Double getRate() {
    return rate;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

}
