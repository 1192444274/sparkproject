package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.dao.UserActiveDAO;
import com.fjs.sparkproject.domain.UserActive;
import com.fjs.sparkproject.jdbc.JDBCHelper;

public class UserActiveDAOImpl implements UserActiveDAO {
    @Override
    public void insert(UserActive userActive) {
        String sql = "insert into user_active(date,website_id,active_type_id,dimension_id_1,information_id_1,dimension_id_2,information_id_2,active_num) values(?,?,?,?,?,?,?,?)";
        Object[] params = new Object[]{userActive.getDate(),
                userActive.getWebsiteId(),
                userActive.getActiveTypeId(),
                userActive.getDimensionId1(),
                userActive.getInformationId1(),
                userActive.getDimensionId2(),
                userActive.getInformationId2(),
                userActive.getActiveNum()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
