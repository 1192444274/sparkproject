package com.fjs.sparkproject.domain;

import java.util.Date;

public class PageBrowseHeat {

  private Integer id;
  private Integer pageId;
  private Integer X;
  private Integer Y;
  private Integer num;
  private java.util.Date date;

  public PageBrowseHeat() {
  }

  public PageBrowseHeat(Integer id, Integer pageId, Integer x, Integer y, Integer num, Date date) {
    this.id = id;
    this.pageId = pageId;
    X = x;
    Y = y;
    this.num = num;
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


  public Integer getX() {
    return X;
  }

  public void setX(Integer X) {
    this.X = X;
  }


  public Integer getY() {
    return Y;
  }

  public void setY(Integer Y) {
    this.Y = Y;
  }


  public Integer getNum() {
    return num;
  }

  public void setNum(Integer num) {
    this.num = num;
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

}
