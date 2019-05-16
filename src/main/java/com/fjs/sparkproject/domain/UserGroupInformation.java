package com.fjs.sparkproject.domain;

import java.util.Date;

public class UserGroupInformation {

  private Integer id;
  private String userGroupName;
  private Integer websiteId;
  private String userAttr;
  private String eventIds;
  private java.util.Date startDate;
  private java.util.Date endDate;

  public UserGroupInformation() {
  }

  public Integer getWebsiteId() {
    return websiteId;
  }

  public void setWebsiteId(Integer websiteId) {
    this.websiteId = websiteId;
  }

  public UserGroupInformation(Integer id, String userGroupName, Integer websiteId, String userAttr, String eventIds, Date startDate, Date endDate) {
    this.id = id;
    this.userGroupName = userGroupName;
    this.websiteId = websiteId;
    this.userAttr = userAttr;
    this.eventIds = eventIds;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getUserGroupName() {
    return userGroupName;
  }

  public void setUserGroupName(String userGroupName) {
    this.userGroupName = userGroupName;
  }


  public String getUserAttr() {
    return userAttr;
  }

  public void setUserAttr(String userAttr) {
    this.userAttr = userAttr;
  }


  public String getEventIds() {
    return eventIds;
  }

  public void setEventIds(String eventIds) {
    this.eventIds = eventIds;
  }


  public java.util.Date getStartDate() {
    return startDate;
  }

  public void setStartDate(java.util.Date startDate) {
    this.startDate = startDate;
  }


  public java.util.Date getEndDate() {
    return endDate;
  }

  public void setEndDate(java.util.Date endDate) {
    this.endDate = endDate;
  }

}
