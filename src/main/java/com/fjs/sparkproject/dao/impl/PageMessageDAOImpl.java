package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.PageMessageDAO;
import com.fjs.sparkproject.domain.PageMessage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PageMessageDAOImpl implements PageMessageDAO {

    @Override
    public void insertPageMessages(List<PageMessage> pageMessages) {
        String sql = "insert into page_belong_website(website_id,url) values(?,?)";

        for (PageMessage pageMessage:pageMessages){
            Object[] params = new Object[]{
                    pageMessage.getWebsiteId(),
                    pageMessage.getUrl()
            };
            JDBCHelper jdbcHelper = JDBCHelper.getInstance();
            jdbcHelper.executeUpdate(sql, params);
        }
    }

    @Override
    public void insertPageMessage(PageMessage pageMessage) {
        String sql = "insert into page_belong_website(website_id,url) values(?,?)";
        Object[] params = new Object[]{
                pageMessage.getWebsiteId(),
                pageMessage.getUrl()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }

    @Override
    public List<PageMessage> findAllPageMessages() {
        String sql = "select * from page_belong_website";
        final List<PageMessage> pageMessages = new ArrayList<PageMessage>();

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, null, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    pageMessages.add(new PageMessage(rs.getInt(1),rs.getInt(2),rs.getString(3)));
                }
            }
        });
    return pageMessages;
    }

    @Override
    public List<PageMessage> findByWebsiteId(int id) {
        String sql = "select * from page_belong_website where website_id =?";
        Object[] params = new Object[]{id};
        final List<PageMessage> pageMessages = new ArrayList<PageMessage>();

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    pageMessages.add(new PageMessage(rs.getInt(1),rs.getInt(2),rs.getString(3)));
                }
            }
        });
        return pageMessages;
    }

    @Override
    public PageMessage findByWebsiteIdAndUrl(int id, final String url) {
        String sql = "select * from page_belong_website where website_id =? and url =?";
        Object[] params = new Object[]{id,url};
        final PageMessage pageMessage = new PageMessage();

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    pageMessage.setPageId(rs.getInt(1));
                    pageMessage.setWebsiteId(rs.getInt(2));
                    pageMessage.setUrl(url);
                }
            }
        });
        return pageMessage;
    }

    @Override
    public PageMessage findByUrl(final String url) {
        String sql = "select * from page_belong_website where url =?";
        Object[] params = new Object[]{url};
        final PageMessage pageMessage = new PageMessage();

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    pageMessage.setPageId(rs.getInt(1));
                    pageMessage.setWebsiteId(rs.getInt(2));
                    pageMessage.setUrl(url);
                }
            }
        });
        return pageMessage;
    }
}
