package com.fjs.sparkproject.dao.impl;

import com.fjs.sparkproject.domain.WebsiteMessage;
import com.fjs.sparkproject.jdbc.JDBCHelper;
import com.fjs.sparkproject.dao.UserRetentionDAO;
import com.fjs.sparkproject.domain.UserRetention;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserRetentionDAOImpl implements UserRetentionDAO {

    @Override
    public void insert(UserRetention userRetention) {
//        String sql = "insert into user_keep(start_date,end_date,website_id,keep_type_id,dimension_id_1,dimension_information_id_1,dimension_id_2,dimension_information_id_2,original_num,retention_num) values(?,?,?,?,?,?,?,?,?,?)";
        String sql = "insert into user_keep(start_date,end_date,website_id,keep_type_id,dimension_id_1,information_id_1,dimension_id_2,information_id_2,original_num,retention_num) values(?,?,?,?,?,?,?,?,?,?)";
        Object[] params = new Object[]{userRetention.getStartDate(),
                userRetention.getEndDate(),
                userRetention.getPageId(),
                userRetention.getKeepTypeId(),
                userRetention.getDimensionId1(),
                userRetention.getDimensionInformationId1(),
                userRetention.getDimensionId2(),
                userRetention.getDimensionInformationId2(),
                userRetention.getOriginal_num(),
                userRetention.getRetention_num()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }

    @Override
    public int getRerentionSumByCondition(int id1, int id2,Date startDate,Date endDate,int type) {
        final List<Integer> sum = new ArrayList<Integer>();

        String sql = "select sum(retention_num) from user_keep where (start_date=? and end_date=? and keep_type_id=? and dimension_id_1 = ? " +
                "and information_id_1=?) or (start_date=? and end_date=? and keep_type_id=? and dimension_id_2 = ? and information_id_2=?)";
        Object[] params = new Object[]{startDate,endDate,type,id1,id2,startDate,endDate,type,id1,id2};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if(rs.next()) {
                    sum.add(rs.getInt(1));
                }
            }
        });
        if (sum.size()!=0)
            return sum.get(0);
        else
            return 0;
    }
    @Override
    public int getRerentionSumByCondition1(Date startDate,Date endDate,int type) {
        final List<Integer> sum = new ArrayList<Integer>();

        String sql = "select sum(retention_num) from user_keep where start_date=? and end_date=? and keep_type_id=? ";
        Object[] params = new Object[]{startDate,endDate,type};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if(rs.next()) {
                    sum.add(rs.getInt(1));
                }
            }
        });
        if (sum.size()!=0)
            return sum.get(0);
        else
            return 0;
    }
    @Override
    public int getOrginalSumByCondition(int id1, int id2,Date startDate,Date endDate,int type) {
        final List<Integer> sum = new ArrayList<Integer>();

        String sql = "select sum(original_num) from user_keep where (start_date=? and end_date=? and keep_type_id=? and dimension_id_1 = ? " +
                "and information_id_1=?) or (start_date=? and end_date=? and keep_type_id=? and dimension_id_2 = ? and information_id_2=?)";
        Object[] params = new Object[]{startDate,endDate,type,id1,id2,startDate,endDate,type,id1,id2};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if(rs.next()) {
                    sum.add(rs.getInt(1));
                }
            }
        });
        if (sum.size()!=0)
            return sum.get(0);
        else
            return 0;
    }

    @Override
    public int getOrginalSumByCondition1(Date startDate,Date endDate,int type) {
        final List<Integer> sum = new ArrayList<Integer>();

        String sql = "select sum(original_num) from user_keep where start_date=? and end_date=? and keep_type_id=? ";
        Object[] params = new Object[]{startDate,endDate,type};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if(rs.next()) {
                    sum.add(rs.getInt(1));
                }
            }
        });
        if (sum.size()!=0)
            return sum.get(0);
        else
            return 0;
    }

    @Override
    public int getOriginalNumByDate(Date date) {
        final List<Integer> ori = new ArrayList<Integer>();

        String sql = "select original_num from user_keep where start_date=? limit 1";
        Object[] params = new Object[]{date};
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {

            @Override
            public void process(ResultSet rs) throws Exception {
                if(rs.next()) {
                    ori.add(rs.getInt(1));
                }
            }
        });
        return ori.get(0);
    }

}
