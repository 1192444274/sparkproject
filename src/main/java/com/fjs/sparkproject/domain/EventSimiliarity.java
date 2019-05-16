package com.fjs.sparkproject.domain;


public class EventSimiliarity {

  private Integer id;
  private Integer websiteId;
  private Integer event1;
  private Integer event2;
  private Integer similiarityDegree;

  public EventSimiliarity(Integer id, Integer websiteId, Integer event1, Integer event2, Integer similiarityDegree) {
    this.id = id;
    this.websiteId = websiteId;
    this.event1 = event1;
    this.event2 = event2;
    this.similiarityDegree = similiarityDegree;
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


  public Integer getEvent1() {
    return event1;
  }

  public void setEvent1(Integer event1) {
    this.event1 = event1;
  }


  public Integer getEvent2() {
    return event2;
  }

  public void setEvent2(Integer event2) {
    this.event2 = event2;
  }


  public Integer getSimiliarityDegree() {
    return similiarityDegree;
  }

  public void setSimiliarityDegree(Integer similiarityDegree) {
    this.similiarityDegree = similiarityDegree;
  }

}
