package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.dao.EventSimiliarityDAO;
import com.fjs.sparkproject.domain.EventSimiliarity;
import com.fjs.sparkproject.jdbc.JDBCHelper;

public class EventSimiliarityDAOImpl implements EventSimiliarityDAO {
    @Override
    public void insert(EventSimiliarity eventSimiliarity) {
        String sql = "insert into event_similiarity(websiteId,event1,event2,similiarity_degree) " +
                "values(?,?,?,?)";

        Object[] params = new Object[]{
                eventSimiliarity.getWebsiteId(),
                eventSimiliarity.getEvent1(),
                eventSimiliarity.getEvent2(),
                eventSimiliarity.getSimiliarityDegree()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
