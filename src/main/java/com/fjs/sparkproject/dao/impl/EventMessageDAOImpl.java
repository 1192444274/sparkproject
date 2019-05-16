package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.EventMessageDAO;
import com.fjs.sparkproject.domain.EventMessage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EventMessageDAOImpl implements EventMessageDAO {
    @Override
    public EventMessage findById(int id) {
        final EventMessage eventMessage = new EventMessage();

        String sql = "select * from event_message where event_id = ?";

        Object[] params = new Object[]{id};

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    eventMessage.setEventId(rs.getInt(1));
                    eventMessage.setWebsiteId(rs.getInt(2));
                    eventMessage.setEventName(rs.getString(3));
                    eventMessage.setEventMethod(rs.getString(4));
                    eventMessage.setEventType(rs.getString(5));
                    eventMessage.setFormUrl(rs.getString(6));
                    eventMessage.setParameter1(rs.getString(10));
                    eventMessage.setParameter2(rs.getString(11));
                    eventMessage.setParameter3(rs.getString(12));
                    eventMessage.setParameter4(rs.getString(13));
                }
            }
        });

        return eventMessage;
    }

    @Override
    public List<EventMessage> findMattersByWebsiteId(int id) {
        final List<EventMessage> eventMessages = new ArrayList<EventMessage>();

        String sql = "select * from event_message where website_id = ?";

        Object[] params = new Object[]{id};

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    eventMessages.add(new EventMessage(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),
                            null,null,null,rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13)));
                }
            }
        });

        return eventMessages;
    }

    @Override
    public Integer findIdByEventMethodAndWebsiteId(String string, Integer websiteId) {
        final EventMessage eventMessage = new EventMessage();

        String sql = "select * from event_message where event_method = ? and website_id = ?";

        Object[] params = new Object[]{string,websiteId};

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    eventMessage.setEventId(rs.getInt(1));
                }
            }
        });

        return eventMessage.getEventId();
    }

    @Override
    public List<String> findEventsByType(String type) {
        final List<String> formPageUrls = new ArrayList<String>();

        String sql = "select form_url from event_message where event_type = ? group by form_url";

        Object[] params = new Object[]{type};

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    formPageUrls.add(rs.getString(1));
                }
            }
        });

        return formPageUrls;
    }

    @Override
    public void insert(EventMessage eventMessage) {
        String sql = "insert into event_message values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{eventMessage.getEventId(),
                eventMessage.getWebsiteId(),
                eventMessage.getEventName(),
                eventMessage.getEventMethod(),
                eventMessage.getEventType(),
                eventMessage.getFormUrl(),
                eventMessage.getRemark(),
                eventMessage.getDate(),
                eventMessage.getReviseDate(),
                eventMessage.getParameter1(),
                eventMessage.getParameter2(),
                eventMessage.getParameter3(),
                eventMessage.getParameter4(),};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
