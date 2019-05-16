package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.Source;

public interface SourceDAO {

    Source findByUrl(String url,int websiteId);

    Source findById(Integer integer);
}
