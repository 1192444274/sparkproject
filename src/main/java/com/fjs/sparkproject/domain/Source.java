package com.fjs.sparkproject.domain;


public class Source {

  private Integer id;
  private Integer websiteId;
  private Integer channelId;
  private String sourceName;
  private String rule;

  public Source(Integer id, Integer websiteId, Integer channelId, String sourceName, String rule) {
    this.id = id;
    this.websiteId = websiteId;
    this.channelId = channelId;
    this.sourceName = sourceName;
    this.rule = rule;
  }

  public Source() {
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


  public Integer getChannelId() {
    return channelId;
  }

  public void setChannelId(Integer channelId) {
    this.channelId = channelId;
  }


  public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }


  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

}
