package com.fjs.sparkproject.domain;

public class DimensionInformation {
    Integer dimensionInformationId;
    Integer dimensionId;
    String  dimensionInformation;

    public DimensionInformation() {
    }

    public DimensionInformation(Integer dimensionInformationId, Integer dimensionId, String dimensionInformation) {
        this.dimensionInformationId = dimensionInformationId;
        this.dimensionId = dimensionId;
        this.dimensionInformation = dimensionInformation;
    }

    public Integer getDimensionInformationId() {
        return dimensionInformationId;
    }

    public void setDimensionInformationId(Integer dimensionInformationId) {
        this.dimensionInformationId = dimensionInformationId;
    }

    public Integer getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(Integer dimensionId) {
        this.dimensionId = dimensionId;
    }

    public String getDimensionInformation() {
        return dimensionInformation;
    }

    public void setDimensionInformation(String dimensionInformation) {
        this.dimensionInformation = dimensionInformation;
    }
}
