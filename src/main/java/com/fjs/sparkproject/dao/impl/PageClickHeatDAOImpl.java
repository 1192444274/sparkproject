package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.PageClickHeatDAO;
import com.fjs.sparkproject.domain.PageClickHeat;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PageClickHeatDAOImpl implements PageClickHeatDAO {
    @Override
    public void insert(PageClickHeat pageClickHeat) {
        String sql = "insert into page_click_heat(page_id,X,Y,click_num,date) values(?,?,?,?,?)";

        Object[] params = new Object[]{pageClickHeat.getPageId(),
                pageClickHeat.getX(),
                pageClickHeat.getY(),
                pageClickHeat.getClickNum(),
                pageClickHeat.getDate()};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }

    @Override
    public List<PageClickHeat> findById(int i) {
        String sql = "select * from page_click_heat where page_id =?";
        Object[] params = new Object[]{i};
        final List<PageClickHeat> pageMessages = new ArrayList<PageClickHeat>();

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    pageMessages.add(new PageClickHeat(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getDate(6)));
                }
            }
        });
        return pageMessages;
    }

}
