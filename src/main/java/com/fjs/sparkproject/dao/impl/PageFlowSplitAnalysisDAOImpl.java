package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.PageFlowSplitAnalysisDAO;
import com.fjs.sparkproject.domain.PageFlowSplitAnalysis;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PageFlowSplitAnalysisDAOImpl implements PageFlowSplitAnalysisDAO {

    @Override
    public List<PageFlowSplitAnalysis> findAll() {
        String sql = "select * from page_flow_split_analysis";
        final List<PageFlowSplitAnalysis> pageFlowSplitAnalyses = new ArrayList<PageFlowSplitAnalysis>();

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, null, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    PageFlowSplitAnalysis pageFlowSplitAnalysis = new PageFlowSplitAnalysis();
                    pageFlowSplitAnalysis.setId(rs.getInt(1));
                    pageFlowSplitAnalysis.setWebsiteId(rs.getInt(2));
                    pageFlowSplitAnalysis.setPageSplit(rs.getString(3));
                    pageFlowSplitAnalysis.setHappenTime(rs.getInt(4));
                    pageFlowSplitAnalysis.setDate(rs.getDate(6));
                    pageFlowSplitAnalyses.add(pageFlowSplitAnalysis);
                }
            }
        });
        return pageFlowSplitAnalyses;
    }

    @Override
    public void insert(PageFlowSplitAnalysis pageFlowSplitAnalysis) {
        String sql = "insert into page_flow_split_analysis(website_id,page_split,happen_time,conversion_rate,date)" +
                " values(?,?,?,?,?)";

        Object[] params = new Object[]{
                pageFlowSplitAnalysis.getWebsiteId(),
                pageFlowSplitAnalysis.getPageSplit(),
                pageFlowSplitAnalysis.getHappenTime(),
                pageFlowSplitAnalysis.getConversionRate(),
                pageFlowSplitAnalysis.getDate()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }

    @Override
    public void updateConversionRate(PageFlowSplitAnalysis pageFlowSplitAnalysis) {
        String sql = "update page_flow_split_analysis set conversion_rate = ? where id = ?";

        Object[] params = new Object[]{
                pageFlowSplitAnalysis.getConversionRate(),
                pageFlowSplitAnalysis.getId()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
