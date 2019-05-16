package com.fjs.sparkproject.domain;

import java.util.Date;

public class WebsiteSource {

  private Integer id;
  private Integer websiteId;
  private Integer channelId;
  private Integer sourceId;
  private String area;
  private String userType;
  private Integer userNum;
  private Integer visitNum;
  private Integer visitorNum;
  private Integer ipNum;
  private Integer newVisitorNum;
  private Double bounceRate;
  private Double averageAccessTime;
  private Double averageAccessDepth;
  private Integer transferNum;
  private Double transferRate;
  private java.util.Date date;

  @Override
  public String toString() {
    return "WebsiteSource{" +
            "id=" + id +
            ", websiteId=" + websiteId +
            ", channelId=" + channelId +
            ", sourceId=" + sourceId +
            ", area='" + area + '\'' +
            ", userType='" + userType + '\'' +
            ", userNum=" + userNum +
            ", visitNum=" + visitNum +
            ", visitorNum=" + visitorNum +
            ", ipNum=" + ipNum +
            ", newVisitorNum=" + newVisitorNum +
            ", bounceRate=" + bounceRate +
            ", averageAccessTime=" + averageAccessTime +
            ", averageAccessDepth=" + averageAccessDepth +
            ", transferNum=" + transferNum +
            ", transferRate=" + transferRate +
            ", date=" + date +
            '}';
  }

  public WebsiteSource(Integer id, Integer websiteId, Integer channelId, Integer sourceId, String area, String userType, Integer userNum, Integer visitNum, Integer visitorNum, Integer ipNum, Integer newVisitorNum, Double bounceRate, Double averageAccessTime, Double averageAccessDepth, Integer transferNum, Double transferRate, Date date) {
    this.id = id;
    this.websiteId = websiteId;
    this.channelId = channelId;
    this.sourceId = sourceId;
    this.area = area;
    this.userType = userType;
    this.userNum = userNum;
    this.visitNum = visitNum;
    this.visitorNum = visitorNum;
    this.ipNum = ipNum;
    this.newVisitorNum = newVisitorNum;
    this.bounceRate = bounceRate;
    this.averageAccessTime = averageAccessTime;
    this.averageAccessDepth = averageAccessDepth;
    this.transferNum = transferNum;
    this.transferRate = transferRate;
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


  public Integer getChannelId() {
    return channelId;
  }

  public void setChannelId(Integer channelId) {
    this.channelId = channelId;
  }


  public Integer getSourceId() {
    return sourceId;
  }

  public void setSourceId(Integer sourceId) {
    this.sourceId = sourceId;
  }


  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }


  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }


  public Integer getUserNum() {
    return userNum;
  }

  public void setUserNum(Integer userNum) {
    this.userNum = userNum;
  }


  public Integer getVisitNum() {
    return visitNum;
  }

  public void setVisitNum(Integer visitNum) {
    this.visitNum = visitNum;
  }


  public Integer getVisitorNum() {
    return visitorNum;
  }

  public void setVisitorNum(Integer visitorNum) {
    this.visitorNum = visitorNum;
  }


  public Integer getIpNum() {
    return ipNum;
  }

  public void setIpNum(Integer ipNum) {
    this.ipNum = ipNum;
  }


  public Integer getNewVisitorNum() {
    return newVisitorNum;
  }

  public void setNewVisitorNum(Integer newVisitorNum) {
    this.newVisitorNum = newVisitorNum;
  }


  public Double getBounceRate() {
    return bounceRate;
  }

  public void setBounceRate(Double bounceRate) {
    this.bounceRate = bounceRate;
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


  public Integer getTransferNum() {
    return transferNum;
  }

  public void setTransferNum(Integer transferNum) {
    this.transferNum = transferNum;
  }


  public Double getTransferRate() {
    return transferRate;
  }

  public void setTransferRate(Double transferRate) {
    this.transferRate = transferRate;
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

}
