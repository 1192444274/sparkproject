package com.fjs.sparkproject.domain;


public class FunnelMessage {

  private Integer id;
  private Integer websiteId;
  private String funnelName;
  private String eventIds;

  public FunnelMessage(Integer id, Integer websiteId, String funnelName, String eventIds) {
    this.id = id;
    this.websiteId = websiteId;
    this.funnelName = funnelName;
    this.eventIds = eventIds;
  }

  public FunnelMessage() {
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


  public String getFunnelName() {
    return funnelName;
  }

  public void setFunnelName(String funnelName) {
    this.funnelName = funnelName;
  }


  public String getEventIds() {
    return eventIds;
  }

  public void setEventIds(String eventIds) {
    this.eventIds = eventIds;
  }

}
