package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.WebsiteMessage;

import java.util.List;

public interface WebsiteMessageDAO {
    List<WebsiteMessage> findAllWebsite();
    WebsiteMessage findById(int id);
    WebsiteMessage findByUrl(String url);
}
