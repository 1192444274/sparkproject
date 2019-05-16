package com.fjs.sparkproject.domain;

public class Dimension {
    Integer dimensionId;
    String dimensionCName;
    String dimensionName;
    String dimensionType;

    public Dimension() {
    }

    public Dimension(Integer dimensionId, String dimensionCName, String dimensionName, String dimensionType) {
        this.dimensionId = dimensionId;
        this.dimensionCName = dimensionCName;
        this.dimensionName = dimensionName;
        this.dimensionType = dimensionType;
    }

    public String getDimensionCName() {
        return dimensionCName;
    }

    public void setDimensionCName(String dimensionCName) {
        this.dimensionCName = dimensionCName;
    }

    public Integer getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(Integer dimensionId) {
        this.dimensionId = dimensionId;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public String getDimensionType() {
        return dimensionType;
    }

    public void setDimensionType(String dimensionType) {
        this.dimensionType = dimensionType;
    }
}
