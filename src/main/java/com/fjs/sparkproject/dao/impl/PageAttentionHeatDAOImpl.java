package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.PageAttentionHeatDAO;
import com.fjs.sparkproject.domain.PageAttentionHeat;

public class PageAttentionHeatDAOImpl implements PageAttentionHeatDAO {
    @Override
    public void insert(PageAttentionHeat pageAttentionHeat) {
        String sql = "insert into page_attention_heat(page_id,X,Y,time,date) values(?,?,?,?,?)";

        Object[] params = new Object[]{pageAttentionHeat.getPageId(),
                pageAttentionHeat.getX(),
                pageAttentionHeat.getY(),
                pageAttentionHeat.getTime(),
                pageAttentionHeat.getDate()};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
