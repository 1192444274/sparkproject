package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.Top10CityDAO;
import com.fjs.sparkproject.domain.Top10City;

public class Top10CityDAOImpl implements Top10CityDAO {
    @Override
    public void insert(Top10City top10City) {
        String sql = "insert into user_attr_region(website_id,region,user_num,date) values(?,?,?,?)";

        Object[] params = new Object[]{top10City.getPageid(),
                top10City.getCityName(),
                top10City.getUser_num(),
                top10City.getDate()};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);


    }
}
