package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.FunnelMessageDAO;
import com.fjs.sparkproject.domain.FunnelMessage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FunnelMessageDAOImpl implements FunnelMessageDAO {
    @Override
    public List<FunnelMessage> findAllFunnel() {
        final List<FunnelMessage> funnelMessages = new ArrayList<FunnelMessage>();

        String sql = "select * from funnel";

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, null, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    funnelMessages.add(new FunnelMessage(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4)));
                }
            }
        });

        return funnelMessages;
    }
}
