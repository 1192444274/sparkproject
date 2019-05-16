package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.KeepTypeDAO;
import com.fjs.sparkproject.domain.KeepType;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KeepTypeDAOImpl implements KeepTypeDAO {
    @Override
    public KeepType findById(int keepTypeId) {
        final KeepType keepType = new KeepType();

        String sql = "select * from keep_type where id=?";
        Object[] params = new Object[]{keepTypeId};

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if(rs.next()) {
                    int keepTypeId = rs.getInt(1);
                    int pageId = rs.getInt(2);
                    String keepName = rs.getString(3);
                    String eventIds = rs.getString(4);

                    keepType.setKeepTypeId(keepTypeId);
                    keepType.setPageId(pageId);
                    keepType.setKeepName(keepName);
                    keepType.setEventIds(eventIds);
                }
            }

        });



        return keepType;
    }

    @Override
    public List<KeepType> findAllKeepType() {
        final List<KeepType> keepTypes = new ArrayList<KeepType>();

        String sql = "select * from keep_type";

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, null, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while(rs.next()) {
                    int keepTypeId = rs.getInt(1);
                    int pageId = rs.getInt(2);
                    String keepName = rs.getString(3);
                    String eventIds = rs.getString(4);
                    KeepType keepType = new KeepType();
                    keepType.setKeepTypeId(keepTypeId);
                    keepType.setPageId(pageId);
                    keepType.setKeepName(keepName);
                    keepType.setEventIds(eventIds);
                    keepTypes.add(keepType);
                }
            }
        });
        return keepTypes;
    }
}

