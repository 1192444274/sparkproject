package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.EventAnalysisDAO;
import com.fjs.sparkproject.domain.EventAnalysis;

public class EventAnalysisDAOImpl implements EventAnalysisDAO {
    @Override
    public void insert(EventAnalysis eventAnalysis) {
        String sql = "insert into event_analysis(event_id,dimension_id_1,dimension_information_id_1,dimension_id_2,dimension_information_id_2," +
                "click_time,user_num,ip_num,conversation_num,money,goods_amount,date) values(?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{eventAnalysis.getEventId(),
                eventAnalysis.getDimensionId1(),
                eventAnalysis.getDimensionInformationId1(),
                eventAnalysis.getDimensionId2(),
                eventAnalysis.getDimensionInformationId2(),
                eventAnalysis.getClickTime(),
                eventAnalysis.getUserNum(),
                eventAnalysis.getIpNum(),
                eventAnalysis.getConversationNum(),
                eventAnalysis.getMoney(),
                eventAnalysis.getGoodsAmount(),
                eventAnalysis.getDate()};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
