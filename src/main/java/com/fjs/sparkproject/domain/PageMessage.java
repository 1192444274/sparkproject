package com.fjs.sparkproject.domain;

public class PageMessage {
    Integer pageId;
    Integer websiteId;
    String url;

    public PageMessage() {
    }

    public PageMessage(Integer pageId, Integer websiteId, String url) {
        this.pageId = pageId;
        this.websiteId = websiteId;
        this.url = url;
    }

    public PageMessage(Integer websiteId, String url) {
        this.websiteId = websiteId;
        this.url = url;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
