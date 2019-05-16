package com.fjs.sparkproject.domain;

import java.util.Date;

public class UserActive {

  private Integer id;
  private java.util.Date date;
  private Integer websiteId;
  private Integer activeTypeId;
  private Integer dimensionId1;
  private Integer informationId1;
  private Integer dimensionId2;
  private Integer informationId2;
  private Integer activeNum;

  public UserActive(Integer id, Date date, Integer websiteId, Integer activeTypeId, Integer dimensionId1, Integer informationId1, Integer dimensionId2, Integer informationId2, Integer activeNum) {
    this.id = id;
    this.date = date;
    this.websiteId = websiteId;
    this.activeTypeId = activeTypeId;
    this.dimensionId1 = dimensionId1;
    this.informationId1 = informationId1;
    this.dimensionId2 = dimensionId2;
    this.informationId2 = informationId2;
    this.activeNum = activeNum;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }


  public Integer getWebsiteId() {
    return websiteId;
  }

  public void setWebsiteId(Integer websiteId) {
    this.websiteId = websiteId;
  }


  public Integer getActiveTypeId() {
    return activeTypeId;
  }

  public void setActiveTypeId(Integer activeTypeId) {
    this.activeTypeId = activeTypeId;
  }


  public Integer getDimensionId1() {
    return dimensionId1;
  }

  public void setDimensionId1(Integer dimensionId1) {
    this.dimensionId1 = dimensionId1;
  }


  public Integer getInformationId1() {
    return informationId1;
  }

  public void setInformationId1(Integer informationId1) {
    this.informationId1 = informationId1;
  }


  public Integer getDimensionId2() {
    return dimensionId2;
  }

  public void setDimensionId2(Integer dimensionId2) {
    this.dimensionId2 = dimensionId2;
  }


  public Integer getInformationId2() {
    return informationId2;
  }

  public void setInformationId2(Integer informationId2) {
    this.informationId2 = informationId2;
  }


  public Integer getActiveNum() {
    return activeNum;
  }

  public void setActiveNum(Integer activeNum) {
    this.activeNum = activeNum;
  }

}
