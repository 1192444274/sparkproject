package com.fjs.sparkproject.util.sparkUtil;

import com.fjs.sparkproject.dao.EventMessageDAO;
import com.fjs.sparkproject.dao.UserGroupInformationDAO;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.DimensionInformation;
import com.fjs.sparkproject.domain.UserGroupInformation;
import com.fjs.sparkproject.domain.WebsiteMessage;
import com.fjs.sparkproject.util.DateUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.*;

public class RetentionUtils {

    //把userActionRDD按照所要求的事件过滤
    public static JavaPairRDD<Long,Iterable<Row>> filterUserActionRDD(JavaPairRDD<Long, Row> userActionRDD, final String needEvents) {
        JavaPairRDD<Long,Iterable<Row>> userEventRDD = userActionRDD.groupByKey();
        JavaPairRDD<Long,Iterable<Row>> fileredUserEventRDD = userEventRDD.filter(new Function<Tuple2<Long, Iterable<Row>>, Boolean>() {
            @Override
            public Boolean call(Tuple2<Long, Iterable<Row>> tuple) throws Exception {
                Iterator<Row> iterator = tuple._2.iterator();
                Long userid = tuple._1;

                //先把iterator中的row都放到list里面去，然后用自定义排序按时间排序好
                List<Row> rowList = new ArrayList<Row>();
                while (iterator.hasNext()){
                    //判断事件是否在eventids中，如果在，记下时间
                    rowList.add(iterator.next());
                }
                Collections.sort(rowList, new Comparator<Row>() {
                    @Override
                    public int compare(Row o1, Row o2) {
                        String actionTime1 = o1.getString(2);
                        String actionTime2 = o2.getString(2);

                        Date date1 = DateUtils.parseTime(actionTime1);
                        Date date2 = DateUtils.parseTime(actionTime2);

                        return (int)(date1.getTime() - date2.getTime());
                    }
                });
                //排序好以后把event连起来
                String events = ",";
                for (Row row:rowList){
                    events = events + row.getString(1) + ",";
                }

                //看eventIds中是否包含了needEventIds,这样会有一个问题，就是如果needEventIds只有一个，比如说1，
                // 那么假如eventIds中有个11那么他也是成立的，所以1前面后面应该还要加个分隔符逗号
                if (events.indexOf(needEvents)!=-1){
                    return true;
                }else {
                    return false;
                }
            }
        });
        return fileredUserEventRDD;
    }

    //把需求事件id变成需求事件名字
    public static String getNeedEvents(String needEventIds, EventMessageDAO eventMessageDAO) {

        String splitNeedEvents = ",";
        if (needEventIds.equals("")||needEventIds == null){

        }else {
            String[] splitNeedEventIds = needEventIds.split(",");
            for (String splitNeedEventId:splitNeedEventIds){
                splitNeedEvents = splitNeedEvents + (eventMessageDAO.findById(Integer.parseInt(splitNeedEventId)).getEventMethod()==null?"":(eventMessageDAO.findById(Integer.parseInt(splitNeedEventId)).getEventMethod() + ","));
            }
        }
        System.out.println(splitNeedEvents);
        return splitNeedEvents;
    }

    public static JavaPairRDD<Long,Long> filterRetentionUserRDD(JavaPairRDD<Long,Iterable<Row>> userEventsRDD, final String orginalUser) {
        JavaPairRDD<Long,Long> retentionUserRDD = userEventsRDD.mapToPair(new PairFunction<Tuple2<Long, Iterable<Row>>, Long, Long>() {
            @Override
            public Tuple2<Long, Long> call(Tuple2<Long, Iterable<Row>> tuple) throws Exception {
                return new Tuple2<Long, Long>(tuple._1,tuple._1);
            }
        });

        retentionUserRDD = retentionUserRDD.filter(new Function<Tuple2<Long, Long>, Boolean>() {
            @Override
            public Boolean call(Tuple2<Long, Long> tuple) throws Exception {
                Long userid = tuple._1;
                String userid1 = "," + userid + ",";
                if (orginalUser.indexOf(userid1)!=-1){
                    return true;
                }else {
                    return false;
                }
            }
        });

        return retentionUserRDD;
    }

    public static JavaRDD<Long> getUserIdRDD(SQLContext sqlContext, DimensionInformation dimensionInformation, WebsiteMessage websiteMessage, String startDate) {
        UserGroupInformationDAO userGroupInformationDAO = DAOFactory.getUserGroupInformationDAO();
        UserGroupInformation userGroupInformation = userGroupInformationDAO.findByGroupNameAndWebsiteId(dimensionInformation.getDimensionInformation(),websiteMessage.getWebsiteId());
        if (userGroupInformation.getId()==null){
            return null;
        }
        String needEvents = RetentionUtils.getNeedEvents(userGroupInformation.getEventIds(),DAOFactory.getEventMessageDAO());
        //找出对应时间段内该网站的所有session
        JavaRDD<Row> actionRDD = getActionRDDUserGroupInformation(sqlContext,userGroupInformation,websiteMessage.getDomain());
        //按照userid groupbykey
        JavaPairRDD<Long,Row> actionUserRDD = getUserActionRDD(actionRDD);
        //遍历每个session，判断该session里是否有对应事件，如果有，把这个session对应的用户返回，如果没有，就返回null
        JavaPairRDD<Long,Iterable<Row>> userEventsRDD = RetentionUtils.filterUserActionRDD(actionUserRDD,needEvents);
        JavaRDD<Long> userRDD = userEventsRDD.map(new Function<Tuple2<Long, Iterable<Row>>, Long>() {
            @Override
            public Long call(Tuple2<Long, Iterable<Row>> tuple) throws Exception {
                return tuple._1;
            }
        });
        //得到的用户去重
        userRDD = userRDD.distinct();
        //找出对应属性的用户
        JavaRDD<Row> userRDD1 = getUserRDD(sqlContext,userGroupInformation,startDate);
        JavaRDD<Long> userIdRDD = userRDD1.map(new Function<Row, Long>() {
            @Override
            public Long call(Row row) throws Exception {
                return row.getLong(0);
            }
        });
        //找出两个用户rdd的交集
        JavaRDD<Long> userIdRDD1 = userRDD.intersection(userIdRDD);

        return userIdRDD1;
    }

    public static JavaRDD<Row> getActionRDDUserGroupInformation(SQLContext sqlContext, UserGroupInformation userGroupInformation, String domain) {
        String sql =
                "select user_id,event_method,action_time "
                        + "from user_action "
                        + "where date>='" + userGroupInformation.getStartDate() + "' "
                        + "and date<='" + userGroupInformation.getEndDate() + "' "
                        + "and url like '" + domain + "%' ";

        DataFrame actionDF = sqlContext.sql(sql);
        return actionDF.javaRDD();
    }


    public static JavaPairRDD<Long,Row> getUserActionRDD(JavaRDD<Row> actionRDD) {

        JavaPairRDD<Long,Row> userActionRDD = actionRDD.mapToPair(new PairFunction<Row, Long, Row>() {
            @Override
            public Tuple2<Long, Row> call(Row row) throws Exception {
                return new Tuple2<Long, Row>(row.getLong(0),row);
            }
        });
        return userActionRDD;
    }

    public static JavaRDD<Row> getUserRDD(SQLContext sqlContext, UserGroupInformation userGroupInformation, String startDate) {
        //判断是否有维度条件，
        String sql = "select user_id "
                + "from user_info "
                + "where " + userGroupInformation.getUserAttr()
                + " and registration_time='" + startDate + "'";
        DataFrame actionDF = sqlContext.sql(sql);

        return actionDF.javaRDD();
    }


}
