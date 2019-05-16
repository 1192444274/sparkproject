package com.fjs.sparkproject.domain;

import java.util.Date;

public class PageAttentionHeat {

  private Integer id;
  private Integer pageId;
  private Integer X;
  private Integer Y;
  private Double time;
  private java.util.Date date;

  public PageAttentionHeat() {
  }

  public PageAttentionHeat(Integer id, Integer pageId, Integer x, Integer y, Double time, Date date) {
    this.id = id;
    this.pageId = pageId;
    X = x;
    Y = y;
    this.time = time;
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


  public Double getTime() {
    return time;
  }

  public void setTime(Double time) {
    this.time = time;
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

}
