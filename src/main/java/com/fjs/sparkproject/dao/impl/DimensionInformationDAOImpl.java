package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.DimensionInformationDAO;
import com.fjs.sparkproject.domain.DimensionInformation;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DimensionInformationDAOImpl implements DimensionInformationDAO {
    @Override
    public List<DimensionInformation> findDimensionInformationByDimensionId(int id) {
        final List<DimensionInformation> dimensionInformations =new ArrayList<DimensionInformation>();

        String sql = "select * from dimension_information where dimension_id=?";
        Object[] params = new Object[]{id};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    DimensionInformation dimensionInformation = new DimensionInformation();
                    dimensionInformation.setDimensionId(rs.getInt(2));
                    dimensionInformation.setDimensionInformationId(rs.getInt(1));
                    dimensionInformation.setDimensionInformation(rs.getString(3));
                    dimensionInformations.add(dimensionInformation);
                }
            }

        });
        return dimensionInformations;
    }
}
