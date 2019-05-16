package com.fjs.sparkproject.domain;

import java.util.Date;

public class EventAnalysis {

  private Integer id;
  private Integer eventId;
  private Integer dimensionId1;
  private Integer dimensionInformationId1;
  private Integer dimensionId2;
  private Integer dimensionInformationId2;
  private Integer clickTime;
  private Integer userNum;
  private Integer ipNum;
  private Integer conversationNum;
  private Integer money;
  private Integer goodsAmount;
  private java.util.Date date;

    @Override
    public String toString() {
        return "EventAnalysis{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", dimensionId1=" + dimensionId1 +
                ", dimensionInformationId1=" + dimensionInformationId1 +
                ", dimensionId2=" + dimensionId2 +
                ", dimensionInformationId2=" + dimensionInformationId2 +
                ", clickTime=" + clickTime +
                ", userNum=" + userNum +
                ", ipNum=" + ipNum +
                ", conversationNum=" + conversationNum +
                ", money=" + money +
                ", goodsAmount=" + goodsAmount +
                ", date=" + date +
                '}';
    }

    public EventAnalysis(Integer id, Integer eventId, Integer dimensionId1, Integer dimensionInformationId1, Integer dimensionId2, Integer dimensionInformationId2, Integer clickTime, Integer userNum, Integer ipNum, Integer conversationNum, Integer money, Integer goodsAmount, Date date) {
        this.id = id;
        this.eventId = eventId;
        this.dimensionId1 = dimensionId1;
        this.dimensionInformationId1 = dimensionInformationId1;
        this.dimensionId2 = dimensionId2;
        this.dimensionInformationId2 = dimensionInformationId2;
        this.clickTime = clickTime;
        this.userNum = userNum;
        this.ipNum = ipNum;
        this.conversationNum = conversationNum;
        this.money = money;
        this.goodsAmount = goodsAmount;
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


  public Integer getConversationNum() {
    return conversationNum;
  }

  public void setConversationNum(Integer conversationNum) {
    this.conversationNum = conversationNum;
  }


  public Integer getMoney() {
    return money;
  }

  public void setMoney(Integer money) {
    this.money = money;
  }


  public Integer getGoodsAmount() {
    return goodsAmount;
  }

  public void setGoodsAmount(Integer goodsAmount) {
    this.goodsAmount = goodsAmount;
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

}
