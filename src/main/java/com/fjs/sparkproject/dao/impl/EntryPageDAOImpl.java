package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.EntryPageDAO;
import com.fjs.sparkproject.domain.EntryPage;

public class EntryPageDAOImpl implements EntryPageDAO {
    @Override
    public void intsert(EntryPage entryPage) {
        String sql = "insert into entry_page(page_id,url,website_id,visit_num,date) " +
                "values(?,?,?,?,?)";

        Object[] params = new Object[]{entryPage.getPageId(),
                entryPage.getUrl(),
                entryPage.getWebsiteId(),
                entryPage.getVisitNum(),
                entryPage.getDate()};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
