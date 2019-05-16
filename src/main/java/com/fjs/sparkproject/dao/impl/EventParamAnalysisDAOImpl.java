package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.dao.EventAnalysisDAO;
import com.fjs.sparkproject.dao.EventParamAnalysisDAO;
import com.fjs.sparkproject.domain.EventAnalysis;
import com.fjs.sparkproject.domain.EventParamAnalysis;
import com.fjs.sparkproject.jdbc.JDBCHelper;

public class EventParamAnalysisDAOImpl implements EventParamAnalysisDAO {
    @Override
    public void insert(EventParamAnalysis eventParamAnalysis) {
        String sql = "insert into event_param_analysis values(?,?,?,?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{
                eventParamAnalysis.getId(),
                eventParamAnalysis.getEventId(),
                eventParamAnalysis.getParamType(),
                eventParamAnalysis.getParamContent(),
                eventParamAnalysis.getDimensionId1(),
                eventParamAnalysis.getDimensionInformationId1(),
                eventParamAnalysis.getDimensionId2(),
                eventParamAnalysis.getDimensionInformationId2(),
                eventParamAnalysis.getClickTime(),
                eventParamAnalysis.getRate(),
                eventParamAnalysis.getDate()};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }

}
