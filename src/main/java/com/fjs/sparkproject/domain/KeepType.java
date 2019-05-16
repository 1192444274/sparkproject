package com.fjs.sparkproject.domain;

public class KeepType {
    int keepTypeId;
    int pageId;
    String keepName;
    String eventIds;

    public KeepType() {
    }

    public KeepType(int keepTypeId, int pageId, String keepName, String eventIds) {
        this.keepTypeId = keepTypeId;
        this.pageId = pageId;
        this.keepName = keepName;
        this.eventIds = eventIds;
    }

    public int getKeepTypeId() {
        return keepTypeId;
    }

    public void setKeepTypeId(int keepTypeId) {
        this.keepTypeId = keepTypeId;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getKeepName() {
        return keepName;
    }

    public void setKeepName(String keepName) {
        this.keepName = keepName;
    }

    public String getEventIds() {
        return eventIds;
    }

    public void setEventIds(String eventIds) {
        this.eventIds = eventIds;
    }
}
