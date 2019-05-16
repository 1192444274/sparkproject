package com.fjs.sparkproject.domain;


public class UserPortrait {

  private Integer id;
  private Integer userId;
  private Double activeDepth;
  private Double contribution;
  private Double profitability;

  public UserPortrait(Integer id, Integer userId, Double activeDepth, Double contribution, Double profitability) {
    this.id = id;
    this.userId = userId;
    this.activeDepth = activeDepth;
    this.contribution = contribution;
    this.profitability = profitability;
  }

  public UserPortrait() {
  }

  @Override
  public String toString() {
    return "UserPortrait{" +
            "id=" + id +
            ", userId=" + userId +
            ", activeDepth=" + activeDepth +
            ", contribution=" + contribution +
            ", profitability=" + profitability +
            '}';
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }


  public Double getActiveDepth() {
    return activeDepth;
  }

  public void setActiveDepth(Double activeDepth) {
    this.activeDepth = activeDepth;
  }


  public Double getContribution() {
    return contribution;
  }

  public void setContribution(Double contribution) {
    this.contribution = contribution;
  }


  public Double getProfitability() {
    return profitability;
  }

  public void setProfitability(Double profitability) {
    this.profitability = profitability;
  }

}
