package com.fjs.sparkproject.domain;

import java.util.Date;

public class UserRetention {
    Date startDate;
    Date endDate;
    int pageId;
    int keepTypeId;
    Integer dimensionId1;
    Integer dimensionInformationId1;
    Integer dimensionId2;
    Integer dimensionInformationId2;
    int original_num;
    int retention_num;

    public UserRetention() {
    }

    public UserRetention(Date startDate, Date endDate, int pageId, int keepTypeId, Integer dimensionId1, Integer dimensionInformationId1, Integer dimensionId2, Integer dimensionInformationId2, int original_num, int retention_num) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.pageId = pageId;
        this.keepTypeId = keepTypeId;
        this.dimensionId1 = dimensionId1;
        this.dimensionInformationId1 = dimensionInformationId1;
        this.dimensionId2 = dimensionId2;
        this.dimensionInformationId2 = dimensionInformationId2;
        this.original_num = original_num;
        this.retention_num = retention_num;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getKeepTypeId() {
        return keepTypeId;
    }

    public void setKeepTypeId(int keepTypeId) {
        this.keepTypeId = keepTypeId;
    }

    public Integer getDimensionId1() {
        return dimensionId1;
    }

    public void setDimensionId1(Integer dimensionId1) {
        this.dimensionId1 = dimensionId1;
    }

    public Integer getDimensionInformationId1() {
        return dimensionInformationId1;
    }

    public void setDimensionInformationId1(Integer dimensionInformationId1) {
        this.dimensionInformationId1 = dimensionInformationId1;
    }

    public Integer getDimensionId2() {
        return dimensionId2;
    }

    public void setDimensionId2(Integer dimensionId2) {
        this.dimensionId2 = dimensionId2;
    }

    public Integer getDimensionInformationId2() {
        return dimensionInformationId2;
    }

    public void setDimensionInformationId2(Integer dimensionInformationId2) {
        this.dimensionInformationId2 = dimensionInformationId2;
    }

    public int getOriginal_num() {
        return original_num;
    }

    public void setOriginal_num(int original_num) {
        this.original_num = original_num;
    }

    public int getRetention_num() {
        return retention_num;
    }

    public void setRetention_num(int retention_num) {
        this.retention_num = retention_num;
    }
}
