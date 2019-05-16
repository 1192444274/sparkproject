package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.PageClickHeat;

import java.util.List;

public interface PageClickHeatDAO {

        void insert(PageClickHeat pageClickHeat);

        List<PageClickHeat> findById(int i);
}
