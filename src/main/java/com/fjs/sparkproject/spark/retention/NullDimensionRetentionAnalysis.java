package com.fjs.sparkproject.spark.retention;

import com.alibaba.fastjson.JSONObject;
import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.KeepType;
import com.fjs.sparkproject.domain.Task;
import com.fjs.sparkproject.domain.UserRetention;
import com.fjs.sparkproject.domain.WebsiteMessage;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.ParamUtils;
import com.fjs.sparkproject.util.SparkUtils;
import com.fjs.sparkproject.util.sparkUtil.RetentionUtils;
import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.util.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.text.ParseException;
import java.util.List;

public class NullDimensionRetentionAnalysis {
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

        long taskid = ParamUtils.getTaskIdFromArgs(args);
        Task task = taskDAO.findById(taskid);
        JSONObject taskParam = JSONObject.parseObject(task.getTaskParam());
        String startDate = ParamUtils.getParam(taskParam,Constants.PARAM_START_DATE);
        String endDate = ParamUtils.getParam(taskParam,Constants.PARAM_END_DATE);
        String date=startDate;
        String domain;

        List<KeepType> keepTypes = keepTypeDAO.findAllKeepType();
        WebsiteMessage websiteMessage;

        for (KeepType keepType:keepTypes){
            int keepTypeId = keepType.getKeepTypeId();
            //keeptype里有website_id,我们要根据这个去获得他的域名
            int pageid = keepType.getPageId();
            websiteMessage = websiteMessageDAO.findById(pageid);
            domain = websiteMessage.getDomain();

            //行为数据中的event变成string了，所以要把keeptype里的eventids转化一下
            String needEvents = RetentionUtils.getNeedEvents(keepType.getEventIds(),eventMessageDAO);
            //获取起始时间的新增用户
            String orginalUser = getOrginalUser(taskParam,sqlContext);
            if (!orginalUser.equals(",")){
                int orginalUserCount = orginalUser.split(",").length-1;
                JavaRDD<Row> actionRDD;
                JavaPairRDD<Long,Row> userActionRDD;
                JavaPairRDD<Long,Iterable<Row>> userEventsRDD;
                JavaPairRDD<Long,Long> retentionUserRDD;
                int i = 1;
                date = startDate;
                while (!date.equals(endDate)){
                    date = DateUtils.getNDate(date,i++);
                    //获取date这天所有session的userid，event_name,action_time
                    actionRDD = getActionRDDByDateRange(sqlContext,date,domain);
                    //映射成<userid,row>
                    userActionRDD = getUserActionRDD(actionRDD);
                    Long s1 = userActionRDD.count();
                    //利用用户session中是否包含所需漏斗过滤，返回的过滤结果是满足漏斗条件的用户
                    userEventsRDD = RetentionUtils.filterUserActionRDD(userActionRDD,needEvents);
                    Long s = userEventsRDD.count();
                    //利用用户是否包含在新增用户列表中进行过滤
                    retentionUserRDD = RetentionUtils.filterRetentionUserRDD(userEventsRDD,orginalUser);
                    //计算留存用户数和留存率
                    calculateRetention((int)retentionUserRDD.count(),orginalUserCount,pageid,startDate,date,keepTypeId);
                }
            }else {
                calculateRetention(0,0,pageid,startDate,date,keepTypeId);
            }


        }


    }


    private static void calculateRetention(int retentionCount, int orginalUserCount,int pageid,String startdate,String date,int keepTypeId) {
           UserRetention userRetention = new UserRetention(DateUtils.parseDateKey(startdate),DateUtils.parseDateKey(date),pageid,keepTypeId,
                   null,null,null,null,orginalUserCount,retentionCount);
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

    private static JavaRDD<Row> getActionRDDByDateRange(SQLContext sqlContext, String date, String domain) {
        String sql =
                "select user_id,event_method,action_time "
                        + "from user_action "
                        + "where date='" + date + "' "
                        + "and url like '" + domain + "%'";

        DataFrame actionDF = sqlContext.sql(sql);
        Long s = actionDF.count();
        return actionDF.javaRDD().distinct();
    }

    private static String getOrginalUser(JSONObject taskParam, SQLContext sqlContext) {

        String startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE);
        String sql = "select user_id " +
                "from user_info " +
                "where registration_time='" + startDate + "'";
        DataFrame actionDF = sqlContext.sql(sql);
        Long s = actionDF.count();
        String orginalUser = ",";
        for (Row row : actionDF.take((int) actionDF.count())){
            orginalUser = orginalUser + row.getLong(0) + ",";
        }

        return orginalUser;
    }
}
