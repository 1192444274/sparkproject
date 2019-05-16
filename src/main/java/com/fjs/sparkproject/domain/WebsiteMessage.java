package com.fjs.sparkproject.domain;

public class WebsiteMessage {
    Integer websiteId;
    Integer smuserId;
    String domain;
    String websiteName;
    Integer bury_num;

    public WebsiteMessage() {
    }

    public WebsiteMessage(Integer websiteId, Integer smuserId, String domain, String websiteName, Integer bury_num) {
        this.websiteId = websiteId;
        this.smuserId = smuserId;
        this.domain = domain;
        this.websiteName = websiteName;
        this.bury_num = bury_num;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public Integer getSmuserId() {
        return smuserId;
    }

    public void setSmuserId(Integer smuserId) {
        this.smuserId = smuserId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public Integer getBury_num() {
        return bury_num;
    }

    public void setBury_num(Integer bury_num) {
        this.bury_num = bury_num;
    }
}
