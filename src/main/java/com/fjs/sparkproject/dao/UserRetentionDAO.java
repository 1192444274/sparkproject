package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.UserRetention;

import java.util.Date;
import java.util.List;

public interface UserRetentionDAO {
    void insert(UserRetention userRetention);

    int getRerentionSumByCondition(int id1,int id2,Date startDate,Date endDate,int type);
    int getRerentionSumByCondition1(Date startDate,Date endDate,int type);
    int getOriginalNumByDate(Date date);
    int getOrginalSumByCondition1(Date startDate,Date endDate,int type);
    int getOrginalSumByCondition(int id1,int id2,Date startDate,Date endDate,int type);
}
