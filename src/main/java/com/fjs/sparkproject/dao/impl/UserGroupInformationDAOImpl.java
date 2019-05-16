package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.UserGroupInformationDAO;
import com.fjs.sparkproject.domain.UserGroupInformation;

import java.sql.ResultSet;

public class UserGroupInformationDAOImpl implements UserGroupInformationDAO {
    @Override
    public UserGroupInformation findByGroupNameAndWebsiteId(String groupName, Integer websiteId) {
        final UserGroupInformation userGroupInformation = new UserGroupInformation();

        String sql = "select * from user_group_information where user_group_name = ? and website_id = ?";

        Object[] params = new Object[]{groupName,websiteId};

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    userGroupInformation.setId(rs.getInt(1));
                    userGroupInformation.setUserGroupName(rs.getString(2));
                    userGroupInformation.setWebsiteId(rs.getInt(3));
                    userGroupInformation.setUserAttr(rs.getString(4));
                    userGroupInformation.setEventIds(rs.getString(5));
                    userGroupInformation.setStartDate(rs.getDate(6));
                    userGroupInformation.setEndDate(rs.getDate(7));
                }
            }
        });

        return userGroupInformation;
    }
}
