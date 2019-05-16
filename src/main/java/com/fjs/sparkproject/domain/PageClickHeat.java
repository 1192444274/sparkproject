package com.fjs.sparkproject.domain;

import java.util.Date;

public class PageClickHeat {

  private Integer id;
  private Integer pageId;
  private Integer X;
  private Integer Y;
  private Integer clickNum;
  private Date date;


  public PageClickHeat() {
  }

  public PageClickHeat(Integer id, Integer pageId, Integer x, Integer y, Integer clickNum, Date date) {
    this.id = id;
    this.pageId = pageId;
    X = x;
    Y = y;
    this.clickNum = clickNum;
    this.date = date;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
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


  public Integer getClickNum() {
    return clickNum;
  }

  public void setClickNum(Integer clickNum) {
    this.clickNum = clickNum;
  }

}
