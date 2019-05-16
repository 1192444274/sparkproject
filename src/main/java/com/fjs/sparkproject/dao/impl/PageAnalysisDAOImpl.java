package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.PageAnalysisDAO;
import com.fjs.sparkproject.domain.PageAnalysis;

public class PageAnalysisDAOImpl implements PageAnalysisDAO {
    @Override
    public void insert(PageAnalysis pageAnalysis) {
        String sql = "insert into page_analysis values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{
                pageAnalysis.getId(),
                pageAnalysis.getUrl(),
                pageAnalysis.getWebsiteId(),
                pageAnalysis.getUserType(),
                pageAnalysis.getSource(),
                pageAnalysis.getBrowseNum(),
                pageAnalysis.getConversationNum(),
                pageAnalysis.getDate(),
                pageAnalysis.getUserNum(),
                pageAnalysis.getIpNum(),
                pageAnalysis.getExitRate(),
                pageAnalysis.getConversionRate(),
                pageAnalysis.getAverageAccessTime(),
                pageAnalysis.getEntryNum()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
