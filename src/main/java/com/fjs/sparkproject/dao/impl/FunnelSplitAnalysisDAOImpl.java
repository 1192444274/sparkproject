package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.FunnelSplitAnalysisDAO;
import com.fjs.sparkproject.domain.FunnelSplitAnalysis;

public class FunnelSplitAnalysisDAOImpl implements FunnelSplitAnalysisDAO {
    @Override
    public void insert(FunnelSplitAnalysis funnelSplitAnalysis) {
        String sql = "insert into funnel_split_analysis " +
                "values(?,?,?,?,?,?)";

        Object[] params = new Object[]{
                funnelSplitAnalysis.getId(),
                funnelSplitAnalysis.getFunnelId(),
                funnelSplitAnalysis.getEventSplit(),
                funnelSplitAnalysis.getHappenTime(),
                funnelSplitAnalysis.getConversionRate(),
                funnelSplitAnalysis.getDate()
             };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
