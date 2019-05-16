package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.UserAttrAgeDAO;
import com.fjs.sparkproject.domain.UserAttrAge;

public class UserAttrAgeDAOImpl implements UserAttrAgeDAO {

    @Override
    public void insert(UserAttrAge userAttrAge) {
        String sql = "insert into user_attr_age(pageid,0_17,18_24,25_34,35_44,45_54,55_100,date) values(?,?,?,?,?,?,?,?)";
        Object[] params = new Object[]{userAttrAge.getPageid(),
                userAttrAge.getUser_age_0_17(),
                userAttrAge.getUser_age_17_24(),
                userAttrAge.getUser_age_25_34(),
                userAttrAge.getUser_age_35_44(),
                userAttrAge.getUser_age_45_54(),
                userAttrAge.getUser_age_55_100(),
                userAttrAge.getDate()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);


    }
}
