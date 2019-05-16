package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.DimensionInformation;

import java.util.List;

public interface DimensionInformationDAO {
    List<DimensionInformation> findDimensionInformationByDimensionId(int id);
}
