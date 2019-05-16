package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.WebsiteAnalysisDAO;
import com.fjs.sparkproject.domain.WebsiteAnalysis;

public class WebsiteAnalysisDAOImpl implements WebsiteAnalysisDAO {
    @Override
    public void insert(WebsiteAnalysis websiteAnalysis) {
        String sql = "insert into website_analysis(website_id,dimension_id_1,dimension_information_id_1,dimension_id_2,dimension_information_id_2," +
                "date,user_num,ip_num,bounce_rate,second_skip_rate,average_access_time,average_access_deth) values(?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{
                websiteAnalysis.getWebsiteId(),
                websiteAnalysis.getDimensionId1(),
                websiteAnalysis.getDimensionInformationId1(),
                websiteAnalysis.getDimensionId2(),
                websiteAnalysis.getDimensionInformationId2(),
                websiteAnalysis.getDate(),
                websiteAnalysis.getUserNum(),
                websiteAnalysis.getIpNum(),
                websiteAnalysis.getBounceRate(),
                websiteAnalysis.getSecondSkipRate(),
                websiteAnalysis.getAverageAccessTime(),
                websiteAnalysis.getAverageAccessDepth()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
