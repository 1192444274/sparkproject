package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.Dimension;

import java.util.List;

public interface DimensionDAO {

    List<Integer> findAllDimensionIds();

    List<Dimension> findAllDimensions();
}
