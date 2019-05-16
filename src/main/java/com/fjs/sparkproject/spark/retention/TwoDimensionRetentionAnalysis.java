package com.fjs.sparkproject.spark.retention;

import com.alibaba.fastjson.JSONObject;
import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.*;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.ParamUtils;
import com.fjs.sparkproject.util.SparkUtils;
import com.fjs.sparkproject.util.sparkUtil.RetentionUtils;
import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.domain.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.text.ParseException;
import java.util.*;

public class TwoDimensionRetentionAnalysis {
    public static void main(String[] args) throws ParseException {
        args = new String[]{"2"};
        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_SESSION1);
        SparkUtils.setMaster(conf);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());
        //如果在本地条件下运行，就要生成测试数据
        SparkUtils.mockData(sc,sqlContext);


        ITaskDAO taskDAO = DAOFactory.getTaskDAO();
        KeepTypeDAO keepTypeDAO = DAOFactory.getKeepTypeDAO();
        WebsiteMessageDAO websiteMessageDAO = DAOFactory.getWebsiteMessageDAO();
        EventMessageDAO eventMessageDAO = DAOFactory.getEventMessageDAO();
        DimensionDAO dimensionDAO = DAOFactory.getDimensionDAO();
        DimensionInformationDAO dimensionInformationDAO = DAOFactory.getDimensionInformationDAO();

        long taskid = ParamUtils.getTaskIdFromArgs(args);
        Task task = taskDAO.findById(taskid);
        JSONObject taskParam = JSONObject.parseObject(task.getTaskParam());
        String startDate = ParamUtils.getParam(taskParam,Constants.PARAM_START_DATE);
        String endDate = ParamUtils.getParam(taskParam,Constants.PARAM_END_DATE);
        String date=startDate;
        String domain;
        WebsiteMessage websiteMessage;
        List<KeepType> keepTypes = keepTypeDAO.findAllKeepType();

        for (KeepType keepType:keepTypes){
            int keepTypeId = keepType.getKeepTypeId();
            int pageid = keepType.getPageId();
            websiteMessage = websiteMessageDAO.findById(pageid);
            domain = websiteMessage.getDomain();

            String needEvents = RetentionUtils.getNeedEvents(keepType.getEventIds(),eventMessageDAO);

            //从这里开始考虑多维度，先获取所有维度
            List<Dimension> dimensions = dimensionDAO.findAllDimensions();
            for (Dimension dimension:dimensions){
                //获取维度信息
                List<DimensionInformation> dimensionInformations = dimensionInformationDAO.findDimensionInformationByDimensionId(dimension.getDimensionId());
                for (DimensionInformation dimensionInformation:dimensionInformations){
                    //第二个维度
                    for (Dimension dimension1:dimensions){
                        //第二个维度不能是第一个维度
                        if (!(dimension1.getDimensionId()==dimension.getDimensionId())){
                            //如果两个维度不同，获取第二个维度的信息
                            List<DimensionInformation> dimensionInformations1 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimension1.getDimensionId());
                            //第二个维度的所有信息和第一个维度的信息结合分析
                            for (DimensionInformation dimensionInformation1:dimensionInformations1){
                                //获取起始时间的新增用户,获取用户的时候要考虑到维度了
                                String orginalUser = getOrginalUser(taskParam,sqlContext,dimensionInformation,dimensionInformation1,dimension,dimension1,websiteMessage);
                                //如果起始用户为空，那么后面其实都不用做了
                                if (!orginalUser.equals(",,")){
                                    int orginalUserCount = orginalUser.split(",").length-1;
                                    JavaRDD<Row> actionRDD;
                                    JavaPairRDD<Long,Row> userActionRDD;
                                    JavaPairRDD<Long,Iterable<Row>> userEventsRDD;
                                    JavaPairRDD<Long,Long> retentionUserRDD;
                                    date = startDate;
                                    int i = 1;
                                    while (!date.equals(endDate)){
                                        date = DateUtils.getNDate(date,i++);
                                        //获取date这天所有session的userid，event_id,action_time
                                        actionRDD = getActionRDDByDateRange(sqlContext,date,domain,dimensionInformation,dimensionInformation1,dimension,dimension1);
                                        Long count =actionRDD.count();
                                        //映射成<userid,row>
                                        userActionRDD = getUserActionRDD(actionRDD);
                                        //利用用户session中是否包含所需漏斗过滤，返回的过滤结果是满足漏斗条件的用户
                                        userEventsRDD = RetentionUtils.filterUserActionRDD(userActionRDD,needEvents);
                                        //利用用户是否包含在新增用户列表中进行过滤
                                        retentionUserRDD = RetentionUtils.filterRetentionUserRDD(userEventsRDD,orginalUser);
                                        //计算留存用户数和留存率
                                        calculateRetention((int) retentionUserRDD.count(),orginalUserCount,pageid,startDate,date,keepTypeId,
                                                dimension.getDimensionId(),dimensionInformation.getDimensionInformationId(),
                                                dimension1.getDimensionId(),dimensionInformation1.getDimensionInformationId());
                                    }
                                }else {
                                    calculateRetention(0,0,pageid,startDate,date,keepTypeId,
                                            dimension.getDimensionId(),dimensionInformation.getDimensionInformationId(),
                                            dimension1.getDimensionId(),dimensionInformation1.getDimensionInformationId());
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private static void calculateRetention(int retentionCount, int orginalUserCount,int pageid,String startdate,String date,int keepTypeId,
                                           Integer dimensionId,Integer dimensionInformationId,Integer dimensionId1,Integer dimensionInformationId1) {
        UserRetention userRetention = new UserRetention(DateUtils.parseDateKey(startdate),DateUtils.parseDateKey(date),pageid,keepTypeId,
                dimensionId,dimensionInformationId,dimensionId1,dimensionInformationId1,orginalUserCount,retentionCount);
        UserRetentionDAO userRetentionDAO = DAOFactory.getUserRetentionDAO();
        userRetentionDAO.insert(userRetention);

    }



    private static JavaPairRDD<Long,Row> getUserActionRDD(JavaRDD<Row> actionRDD) {

        JavaPairRDD<Long,Row> userActionRDD = actionRDD.mapToPair(new PairFunction<Row, Long, Row>() {
            @Override
            public Tuple2<Long, Row> call(Row row) throws Exception {
                return new Tuple2<Long, Row>(row.getLong(0),row);
            }
        });
        return userActionRDD;
    }

    private static JavaRDD<Row> getActionRDDByDateRange(SQLContext sqlContext, String date, String domain,
                                                        DimensionInformation dimensionInformation,DimensionInformation dimensionInformation1,
                                                        Dimension dimension,Dimension dimension1) {
        String sql =
                "select user_id,event_method,action_time "
                        + "from user_action "
                        + "where date='" + date + "' "
                        + "and url like '" + domain + "%' " +
                        (dimension.getDimensionType().equals("session")?("and " + dimension.getDimensionName() + "='" + dimensionInformation.getDimensionInformation() + "' "):"") +
                        (dimension1.getDimensionType().equals("session")?("and " + dimension1.getDimensionName() + "='" + dimensionInformation1.getDimensionInformation() + "'"):"");

        DataFrame actionDF = sqlContext.sql(sql);
        for (Row row:actionDF.take(1)){
            System.out.println(row);
        }
        return actionDF.javaRDD();
    }

    private static String getOrginalUser(JSONObject taskParam, SQLContext sqlContext,
                                         DimensionInformation dimensionInformation, DimensionInformation dimensionInformation1,
                                         Dimension dimension, Dimension dimension1, WebsiteMessage websiteMessage) {
        String startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE);
        JavaRDD<Long> userIdRDD1 = null;
        JavaRDD<Long> userIdRDD2 = null;
        if (dimension.getDimensionType().equals("user_group")){
            userIdRDD1 = RetentionUtils.getUserIdRDD(sqlContext,dimensionInformation,websiteMessage,startDate);
        }

        if (dimension1.getDimensionType().equals("user_group")){
            userIdRDD2 = RetentionUtils.getUserIdRDD(sqlContext,dimensionInformation1,websiteMessage,startDate);
        }

        //判断是否有维度条件，
        String sql = "select user_id " +
                "from user_info " +
                "where registration_time='" + startDate + "' " +
                "and website_url='" + websiteMessage.getDomain() + "' " +
                (dimension.getDimensionType().equals("user")?("and " + dimension.getDimensionName() + "='" + dimensionInformation.getDimensionInformation() + "' "):"") +
                (dimension1.getDimensionType().equals("user")?("and " + dimension1.getDimensionName() + "='" + dimensionInformation1.getDimensionInformation() + "'"):"");

        final DataFrame actionDF = sqlContext.sql(sql);
        JavaRDD<Long> userIdRDD3 = actionDF.javaRDD().map(new Function<Row, Long>() {
            @Override
            public Long call(Row row) throws Exception {
                return row.getLong(0);
            }
        });

        userIdRDD3 = userIdRDD1 == null?(userIdRDD2 == null?userIdRDD3:userIdRDD3.intersection(userIdRDD2)):userIdRDD3.intersection(userIdRDD1);

        JavaRDD<String> userIdRDD4 = userIdRDD3.map(new Function<Long, String>() {
            @Override
            public String call(Long aLong) throws Exception {
                return aLong.toString();
            }
        });
        String orginalUser = ",";
        if (userIdRDD4.count()>1){
            orginalUser = orginalUser + userIdRDD4.reduce(new Function2<String, String, String>() {
                @Override
                public String call(String s, String s2) throws Exception {
                    return s + "," + s2;
                }
            });
        }else if(userIdRDD4.count() == 1){
            orginalUser = orginalUser + userIdRDD4.take(1).get(0);
        }
        return orginalUser + ",";
    }


}
