package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.DimensionDAO;
import com.fjs.sparkproject.domain.Dimension;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DimensionDAOImpl implements DimensionDAO {
    @Override
    public List<Integer> findAllDimensionIds() {
        final List<Integer> dimensionIds = new ArrayList<Integer>();

//        String sql = "select id from dimension";
        String sql = "select dimension_id from dimension";
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, null, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    dimensionIds.add(rs.getInt(1));
                }
            }

        });
        return dimensionIds;
    }

    @Override
    public List<Dimension> findAllDimensions() {
        final List<Dimension> dimensions = new ArrayList<Dimension>();
        String sql = "select * from dimension";

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, null, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    Dimension dimension = new Dimension();
                    dimension.setDimensionId(rs.getInt(1));
                    dimension.setDimensionCName(rs.getString(2));
                    dimension.setDimensionName(rs.getString(3));
                    dimension.setDimensionType(rs.getString(4));
                    dimensions.add(dimension);
                }
            }

        });
        return dimensions;
    }
}
