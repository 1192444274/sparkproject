package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.EventSplitAnalysisDAO;
import com.fjs.sparkproject.domain.EventSplitAnalysis;

public class EventSplitAnalysisDAOImpl implements EventSplitAnalysisDAO {
    @Override
    public void insert(EventSplitAnalysis eventSplitAnalysis) {
        String sql = "insert into event_split_analysis(id,website_id,split_name,happen_time,conversion_rate,date) " +
                "values(?,?,?,?,?,?)";

        Object[] params = new Object[]{
                eventSplitAnalysis.getId(),
                eventSplitAnalysis.getWebsiteId(),
                eventSplitAnalysis.getSplitName(),
                eventSplitAnalysis.getHappenTime(),
                eventSplitAnalysis.getConversionRate(),
                eventSplitAnalysis.getDate()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
