package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.UserAttrSexDAO;
import com.fjs.sparkproject.domain.UserAttrSex;

public class UserAttrSexDAOImpl implements UserAttrSexDAO {
    @Override
    public void insert(UserAttrSex userAttrSex) {
        String sql = "insert into user_attr_sex(page_id,sex,user_num,date) values(?,?,?,?)";

        Object[] params = new Object[]{userAttrSex.getPage_id(),
                userAttrSex.getSex(),
                userAttrSex.getUserNum(),
                userAttrSex.getDate()};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
