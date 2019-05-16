package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.WebsiteMessageDAO;
import com.fjs.sparkproject.domain.WebsiteMessage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WebsiteMessageDAOImpl implements WebsiteMessageDAO {
    @Override
    public List<WebsiteMessage> findAllWebsite() {
        final List<WebsiteMessage> websiteMessages = new ArrayList<WebsiteMessage>();

        String sql = "select * from website";

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, null, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while(rs.next()) {
                    websiteMessages.add(new WebsiteMessage(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getInt(5)));
                }
            }
        });
        return websiteMessages;
    }

    @Override
    public WebsiteMessage findById(int id) {
        final WebsiteMessage websiteMessage = new WebsiteMessage();

        String sql = "select * from website where website_id = ?";

        Object[] params = new Object[]{id};

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    websiteMessage.setWebsiteId(rs.getInt(1));
                    websiteMessage.setSmuserId(rs.getInt(2));
                    websiteMessage.setDomain(rs.getString(3));
                    websiteMessage.setWebsiteName(rs.getString(4));
                    websiteMessage.setBury_num(rs.getInt(5));
                }
            }
        });

        return websiteMessage;

    }

    @Override
    public WebsiteMessage findByUrl(String url) {
        String[] splitUrl = url.split("/");
        final WebsiteMessage websiteMessage = new WebsiteMessage();

        String sql = "select * from website where instr(?,domain)>0";

        Object[] params = new Object[]{splitUrl[0]};

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    websiteMessage.setWebsiteId(rs.getInt(1));
                    websiteMessage.setSmuserId(rs.getInt(2));
                    websiteMessage.setDomain(rs.getString(3));
                    websiteMessage.setWebsiteName(rs.getString(4));
                }
            }
        });

        return websiteMessage;
    }


}
