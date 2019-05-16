package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.PageBrowseHeatDAO;
import com.fjs.sparkproject.domain.PageBrowseHeat;

public class PageBrowseHeatDAOImpl implements PageBrowseHeatDAO {
    @Override
    public void insert(PageBrowseHeat pageBrowseHeat) {

        String sql = "insert into page_browse_heat(page_id,X,Y,num,date) values(?,?,?,?,?)";

        Object[] params = new Object[]{pageBrowseHeat.getPageId(),
                pageBrowseHeat.getX(),
                pageBrowseHeat.getY(),
                pageBrowseHeat.getNum(),
                pageBrowseHeat.getDate()};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
