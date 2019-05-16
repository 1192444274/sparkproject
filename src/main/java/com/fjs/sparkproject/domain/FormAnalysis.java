package com.fjs.sparkproject.domain;

import java.util.Date;

public class FormAnalysis {

  private Integer id;
  private String pageUrl;
  private String formName;
  private Integer visitNum;
  private Integer submitNum;
  private Double translateRate;
  private Double abandonRate;
  private Double writeTime;
  private Integer averageBlankField;
  private String remark;
  private java.util.Date date;

  public FormAnalysis() {
  }

  public FormAnalysis(Integer id, String pageUrl, String formName, Integer visitNum, Integer submitNum, Double translateRate, Double abandonRate, Double writeTime, Integer averageBlankField, String remark, Date date) {
    this.id = id;
    this.pageUrl = pageUrl;
    this.formName = formName;
    this.visitNum = visitNum;
    this.submitNum = submitNum;
    this.translateRate = translateRate;
    this.abandonRate = abandonRate;
    this.writeTime = writeTime;
    this.averageBlankField = averageBlankField;
    this.remark = remark;
    this.date = date;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getPageUrl() {
    return pageUrl;
  }

  public void setPageUrl(String pageUrl) {
    this.pageUrl = pageUrl;
  }


  public String getFormName() {
    return formName;
  }

  public void setFormName(String formName) {
    this.formName = formName;
  }


  public Integer getVisitNum() {
    return visitNum;
  }

  public void setVisitNum(Integer visitNum) {
    this.visitNum = visitNum;
  }


  public Integer getSubmitNum() {
    return submitNum;
  }

  public void setSubmitNum(Integer submitNum) {
    this.submitNum = submitNum;
  }


  public Double getTranslateRate() {
    return translateRate;
  }

  public void setTranslateRate(Double translateRate) {
    this.translateRate = translateRate;
  }


  public Double getAbandonRate() {
    return abandonRate;
  }

  public void setAbandonRate(Double abandonRate) {
    this.abandonRate = abandonRate;
  }


  public Double getWriteTime() {
    return writeTime;
  }

  public void setWriteTime(Double writeTime) {
    this.writeTime = writeTime;
  }


  public Integer getAverageBlankField() {
    return averageBlankField;
  }

  public void setAverageBlankField(Integer averageBlankField) {
    this.averageBlankField = averageBlankField;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

}
