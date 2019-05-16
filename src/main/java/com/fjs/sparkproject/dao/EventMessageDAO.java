package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.EventMessage;

import java.util.List;

public interface EventMessageDAO {
    EventMessage findById(int id);

    List<EventMessage> findMattersByWebsiteId(int id);

    Integer findIdByEventMethodAndWebsiteId(String string, Integer websiteId);

    List<String> findEventsByType(String type);

    void insert(EventMessage eventMessage);
}
