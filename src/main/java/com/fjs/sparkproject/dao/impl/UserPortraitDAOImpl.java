package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.dao.UserPortraitDAO;
import com.fjs.sparkproject.domain.UserPortrait;
import com.fjs.sparkproject.jdbc.JDBCHelper;

import java.sql.ResultSet;

public class UserPortraitDAOImpl implements UserPortraitDAO {
    @Override
    public void insert(UserPortrait userPortrait) {
        String sql = "insert into user_portrait values(?,?,?,?,?)";

        Object[] params = new Object[]{
                userPortrait.getId(),
                userPortrait.getUserId(),
                userPortrait.getActiveDepth(),
                userPortrait.getContribution(),
                userPortrait.getProfitability()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }

    @Override
    public UserPortrait query(int id) {
        String sql = "select * from user_portrait where id = ?";

        Object[] params = new Object[]{id};

        final UserPortrait userPortrait= new UserPortrait();

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    userPortrait.setId(rs.getInt(1));
                    userPortrait.setUserId(rs.getInt(2));
                    userPortrait.setActiveDepth(rs.getDouble(3));
                }
            }
        });

        return userPortrait;

    }
}
