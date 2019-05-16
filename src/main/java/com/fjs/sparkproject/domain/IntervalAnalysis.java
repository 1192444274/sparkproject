package com.fjs.sparkproject.domain;

import java.util.Date;

public class IntervalAnalysis {

  private Integer id;
  private Integer matterId;
  private Integer matter2Id;
  private Integer dimensionId1;
  private Integer dimensionInformationId1;
  private Integer dimensionId2;
  private Integer dimensionInformationId2;
  private Double median;
  private Double avIntervaltime;
  private Double biggestIntervaltime;
  private Double smallestIntervaltime;
  private Integer totalTime;
  private java.util.Date date;

  @Override
  public String toString() {
    return "IntervalAnalysis{" +
            "id=" + id +
            ", matterId=" + matterId +
            ", matter2Id=" + matter2Id +
            ", dimensionId1=" + dimensionId1 +
            ", dimensionInformationId1=" + dimensionInformationId1 +
            ", dimensionId2=" + dimensionId2 +
            ", dimensionInformationId2=" + dimensionInformationId2 +
            ", median=" + median +
            ", avIntervaltime=" + avIntervaltime +
            ", biggestIntervaltime=" + biggestIntervaltime +
            ", smallestIntervaltime=" + smallestIntervaltime +
            ", totalTime=" + totalTime +
            ", date=" + date +
            '}';
  }

  public IntervalAnalysis() {
  }

  public IntervalAnalysis(Integer id, Integer matterId, Integer matter2Id, Integer dimensionId1, Integer dimensionInformationId1, Integer dimensionId2, Integer dimensionInformationId2, Double median, Double avIntervaltime, Double biggestIntervaltime, Double smallestIntervaltime, Integer totalTime, Date date) {
    this.id = id;
    this.matterId = matterId;
    this.matter2Id = matter2Id;
    this.dimensionId1 = dimensionId1;
    this.dimensionInformationId1 = dimensionInformationId1;
    this.dimensionId2 = dimensionId2;
    this.dimensionInformationId2 = dimensionInformationId2;
    this.median = median;
    this.avIntervaltime = avIntervaltime;
    this.biggestIntervaltime = biggestIntervaltime;
    this.smallestIntervaltime = smallestIntervaltime;
    this.totalTime = totalTime;
    this.date = date;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public Integer getMatterId() {
    return matterId;
  }

  public void setMatterId(Integer matterId) {
    this.matterId = matterId;
  }


  public Integer getMatter2Id() {
    return matter2Id;
  }

  public void setMatter2Id(Integer matter2Id) {
    this.matter2Id = matter2Id;
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


  public Double getMedian() {
    return median;
  }

  public void setMedian(Double median) {
    this.median = median;
  }


  public Double getAvIntervaltime() {
    return avIntervaltime;
  }

  public void setAvIntervaltime(Double avIntervaltime) {
    this.avIntervaltime = avIntervaltime;
  }


  public Double getBiggestIntervaltime() {
    return biggestIntervaltime;
  }

  public void setBiggestIntervaltime(Double biggestIntervaltime) {
    this.biggestIntervaltime = biggestIntervaltime;
  }


  public Double getSmallestIntervaltime() {
    return smallestIntervaltime;
  }

  public void setSmallestIntervaltime(Double smallestIntervaltime) {
    this.smallestIntervaltime = smallestIntervaltime;
  }


  public Integer getTotalTime() {
    return totalTime;
  }

  public void setTotalTime(Integer totalTime) {
    this.totalTime = totalTime;
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

}
