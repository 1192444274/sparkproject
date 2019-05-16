package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.PageFlowSplitAnalysis;

import java.util.List;

public interface PageFlowSplitAnalysisDAO {
    List<PageFlowSplitAnalysis> findAll();
    void insert(PageFlowSplitAnalysis pageFlowSplitAnalysis);
    void updateConversionRate(PageFlowSplitAnalysis pageFlowSplitAnalysis);
}
