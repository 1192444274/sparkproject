package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.FormAnalysisDAO;
import com.fjs.sparkproject.domain.FormAnalysis;

public class FormAnalysisDAOImpl implements FormAnalysisDAO {

    @Override
    public void insert(FormAnalysis formAnalysis) {
        String sql = "insert into form_analysis(id,page_url,form_name,visit_num,submit_num," +
                "translate_rate,abandon_rate,write_time,average_blank_field,remark,date) values(?,?,?,?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{formAnalysis.getId(),
                formAnalysis.getPageUrl(),
                formAnalysis.getFormName(),
                formAnalysis.getVisitNum(),
                formAnalysis.getSubmitNum(),
                formAnalysis.getTranslateRate(),
                formAnalysis.getAbandonRate(),
                formAnalysis.getWriteTime(),
                formAnalysis.getAverageBlankField(),
                formAnalysis.getRemark(),
                formAnalysis.getDate()
                };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
