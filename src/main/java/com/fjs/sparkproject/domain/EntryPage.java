package com.fjs.sparkproject.domain;

import java.util.Date;

public class EntryPage {

  private Integer id;
  private Integer pageId;
  private String url;
  private Integer websiteId;
  private Integer visitNum;
  private java.util.Date date;

  public EntryPage() {
  }

  public EntryPage(Integer id, Integer pageId, String url, Integer websiteId, Integer visitNum, Date date) {
    this.id = id;
    this.pageId = pageId;
    this.url = url;
    this.websiteId = websiteId;
    this.visitNum = visitNum;
    this.date = date;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public Integer getPageId() {
    return pageId;
  }

  public void setPageId(Integer pageId) {
    this.pageId = pageId;
  }


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  public Integer getWebsiteId() {
    return websiteId;
  }

  public void setWebsiteId(Integer websiteId) {
    this.websiteId = websiteId;
  }


  public Integer getVisitNum() {
    return visitNum;
  }

  public void setVisitNum(Integer visitNum) {
    this.visitNum = visitNum;
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

}
