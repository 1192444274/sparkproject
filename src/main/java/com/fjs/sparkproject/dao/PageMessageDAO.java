package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.PageMessage;

import java.util.List;

public interface PageMessageDAO {

    void insertPageMessages(List<PageMessage> pageMessages);

    void insertPageMessage(PageMessage pageMessage);

    List<PageMessage> findAllPageMessages();

    List<PageMessage> findByWebsiteId(int id);

    PageMessage findByWebsiteIdAndUrl(int id,String url);

    PageMessage findByUrl(String url);
}
