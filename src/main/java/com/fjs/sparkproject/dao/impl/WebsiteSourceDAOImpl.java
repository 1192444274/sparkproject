package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.WebsiteSourceDAO;
import com.fjs.sparkproject.domain.WebsiteSource;

public class WebsiteSourceDAOImpl implements WebsiteSourceDAO {
    @Override
    public void insert(WebsiteSource websiteSource) {
        String sql = "insert into website_source " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{
               websiteSource.getId(),
                websiteSource.getWebsiteId(),
                websiteSource.getChannelId(),
                websiteSource.getSourceId(),
                websiteSource.getArea(),
                websiteSource.getUserType(),
                websiteSource.getUserNum(),
                websiteSource.getVisitNum(),
                websiteSource.getVisitorNum(),
                websiteSource.getIpNum(),
                websiteSource.getNewVisitorNum(),
                websiteSource.getBounceRate(),
                websiteSource.getAverageAccessTime(),
                websiteSource.getAverageAccessDepth(),
                websiteSource.getTransferNum(),
                websiteSource.getTransferRate(),
                websiteSource.getDate()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
