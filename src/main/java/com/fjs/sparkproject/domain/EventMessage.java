package com.fjs.sparkproject.domain;

import java.util.Date;

public class EventMessage {

  private Integer eventId;
  private Integer websiteId;
  private String eventName;
  private String eventMethod;
  private String eventType;
  private String formUrl;
  private String remark;
  private Date date;
  private Date reviseDate;
  private String parameter1;
  private String parameter2;
  private String parameter3;
  private String parameter4;

  public Date getReviseDate() {
    return reviseDate;
  }

  public String getParameter1() {
    return parameter1;
  }

  public void setParameter1(String parameter1) {
    this.parameter1 = parameter1;
  }

  public String getParameter2() {
    return parameter2;
  }

  public void setParameter2(String parameter2) {
    this.parameter2 = parameter2;
  }

  public String getParameter3() {
    return parameter3;
  }

  public void setParameter3(String parameter3) {
    this.parameter3 = parameter3;
  }

  public String getParameter4() {
    return parameter4;
  }

  public void setParameter4(String parameter4) {
    this.parameter4 = parameter4;
  }

  public EventMessage(Integer eventId, Integer websiteId, String eventName, String eventMethod, String eventType, String formUrl, String remark, Date date, Date reviseDate, String parameter1, String parameter2, String parameter3, String parameter4) {
    this.eventId = eventId;
    this.websiteId = websiteId;
    this.eventName = eventName;
    this.eventMethod = eventMethod;
    this.eventType = eventType;
    this.formUrl = formUrl;
    this.remark = remark;
    this.date = date;
    this.reviseDate = reviseDate;
    this.parameter1 = parameter1;
    this.parameter2 = parameter2;
    this.parameter3 = parameter3;
    this.parameter4 = parameter4;
  }

  public void setReviseDate(Date reviseDate) {
    this.reviseDate = reviseDate;
  }

  @Override
  public String toString() {
    return "EventMessage{" +
            "eventId=" + eventId +
            ", websiteId=" + websiteId +
            ", eventName='" + eventName + '\'' +
            ", eventMethod='" + eventMethod + '\'' +
            ", eventType='" + eventType + '\'' +
            ", formUrl='" + formUrl + '\'' +
            ", remark='" + remark + '\'' +
            ", date=" + date +
            ", reviseDate=" + reviseDate +
            '}';
  }

  public EventMessage(Integer eventId, Integer websiteId, String eventName, String eventMethod, String eventType, String formUrl, String remark, Date date, Date reviseDate) {
    this.eventId = eventId;
    this.websiteId = websiteId;
    this.eventName = eventName;
    this.eventMethod = eventMethod;
    this.eventType = eventType;
    this.formUrl = formUrl;
    this.remark = remark;
    this.date = date;
    this.reviseDate = reviseDate;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public EventMessage() {
  }

  public EventMessage(Integer eventId, Integer websiteId, String eventName, String eventMethod, String eventType, String formUrl) {
    this.eventId = eventId;
    this.websiteId = websiteId;
    this.eventName = eventName;
    this.eventMethod = eventMethod;
    this.eventType = eventType;
    this.formUrl = formUrl;
  }

  public Integer getEventId() {
    return eventId;
  }

  public void setEventId(Integer eventId) {
    this.eventId = eventId;
  }


  public Integer getWebsiteId() {
    return websiteId;
  }

  public void setWebsiteId(Integer websiteId) {
    this.websiteId = websiteId;
  }


  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }


  public String getEventMethod() {
    return eventMethod;
  }

  public void setEventMethod(String eventMethod) {
    this.eventMethod = eventMethod;
  }


  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }


  public String getFormUrl() {
    return formUrl;
  }

  public void setFormUrl(String formUrl) {
    this.formUrl = formUrl;
  }

}
