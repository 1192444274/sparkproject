package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.SourceDAO;
import com.fjs.sparkproject.domain.Source;

import java.sql.ResultSet;

public class SourceDAOImpl implements SourceDAO {

    @Override
    public Source findByUrl(String url, int websiteId) {

        String sql = "select * from source where instr(?,rule) and (website_id =? or website_id is null)";
        Object[] params = new Object[]{url,websiteId};
        final Source source = new Source();

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    source.setId(rs.getInt(1));
                    source.setWebsiteId(rs.getInt(2));
                    source.setChannelId(rs.getInt(3));
                    source.setSourceName(rs.getString(4));
                    source.setRule(rs.getString(5));
                }
            }
        });
        return source;
    }

    @Override
    public Source findById(Integer integer) {
        String sql = "select * from source where id=?";
        Object[] params = new Object[]{integer};
        final Source source = new Source();

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    source.setId(rs.getInt(1));
                    source.setWebsiteId(rs.getInt(2));
                    source.setChannelId(rs.getInt(3));
                    source.setSourceName(rs.getString(4));
                    source.setRule(rs.getString(5));
                }
            }
        });
        return source;
    }
}
