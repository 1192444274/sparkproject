package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.Top10PorfessionDAO;
import com.fjs.sparkproject.domain.Top10Porfession;

public class Top10PorfessionDAOImpl implements Top10PorfessionDAO {
    @Override
    public void insert(Top10Porfession top10Porfession) {
        String sql = "insert into user_attr_pro(page_id,professional,user_num,date) values(?,?,?,?)";

        Object[] params = new Object[]{top10Porfession.getPageid(),
                top10Porfession.getProfession(),
                top10Porfession.getUserNum(),
                top10Porfession.getDate()};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);

    }
}
