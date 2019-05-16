package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.KeepType;

import java.util.List;

public interface KeepTypeDAO {
    KeepType findById(int keepTypeId);
    List<KeepType> findAllKeepType();
}
