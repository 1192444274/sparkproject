package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.domain.IntervalAnalysis;
import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.IntervalAnalysisDAO;

public class IntervalAnalysisDAOImpl implements IntervalAnalysisDAO {
    @Override
    public void insert(IntervalAnalysis intervalAnalysis) {
        String sql = "insert into interval_analysis values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{
                intervalAnalysis.getId(),
                intervalAnalysis.getMatterId(),
                intervalAnalysis.getMatter2Id(),
                intervalAnalysis.getDimensionId1(),
                intervalAnalysis.getDimensionInformationId1(),
                intervalAnalysis.getDimensionId2(),
                intervalAnalysis.getDimensionInformationId2(),
                intervalAnalysis.getMedian(),
                intervalAnalysis.getAvIntervaltime(),
                intervalAnalysis.getBiggestIntervaltime(),
                intervalAnalysis.getSmallestIntervaltime(),
                intervalAnalysis.getTotalTime(),
                intervalAnalysis.getDate()};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
