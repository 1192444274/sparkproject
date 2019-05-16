package com.fjs.sparkproject.util.sparkUtil;

import com.fjs.sparkproject.dao.UserGroupInformationDAO;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.DimensionInformation;
import com.fjs.sparkproject.domain.EventMessage;
import com.fjs.sparkproject.domain.UserGroupInformation;
import com.fjs.sparkproject.domain.WebsiteMessage;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.codehaus.janino.Java;
import scala.Tuple2;

public class EventUtils {
    public static JavaRDD<Long> getUserActionRDD(JavaRDD<Row> filterActionRDD) {
        JavaRDD<Long> userActionRDD = filterActionRDD.map(new Function<Row, Long>() {
            @Override
            public Long call(Row row) throws Exception {
                return row.getLong(1);
            }
        });
        return userActionRDD.distinct();
    }

    public static JavaRDD<String> getIpActionRDD(JavaRDD<Row> filterActionRDD) {
        JavaRDD<String> ipActionRDD = filterActionRDD.map(new Function<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return row.getString(15);
            }
        });
        return ipActionRDD.distinct();
    }
    public static JavaRDD<String> getSessionRDD(JavaRDD<Row> filterActionRDD) {
        JavaRDD<String> sessionRDD = filterActionRDD.map(new Function<Row, String>() {
            @Override
            public String call(Row v1) throws Exception {
                return v1.getString(2);
            }
        });
        return sessionRDD.distinct();
    }
    public static JavaRDD<Row> getFilterActionRDD(JavaRDD<Row> actionRDD,EventMessage eventMessage) {
        final String eventMethod = eventMessage.getEventMethod();
        final String eventtype = eventMessage.getEventType();
        JavaRDD<Row> filterActionRDD = actionRDD.filter(new Function<Row, Boolean>() {
            @Override
            public Boolean call(Row row) throws Exception {
                String method = row.getString(6);
                String type = row.getString(4);
                if (method.equals(eventMethod)&&type.equals(eventtype)){
                    return true;
                }else
                    return false;
            }
        });
        return filterActionRDD;
    }

    public static JavaRDD<Row> getDimensionFilterActionRdd(JavaRDD<Row> filterActionRDD, JavaRDD<Long> userRDD) {
        JavaPairRDD<Long,Row> userActionPairRDD = filterActionRDD.mapToPair(new PairFunction<Row, Long, Row>() {
            @Override
            public Tuple2<Long, Row> call(Row row) throws Exception {
                return new Tuple2<Long, Row>(row.getLong(1),row);
            }
        });
        JavaPairRDD<Long,Long> userPairRDD = userRDD.mapToPair(new PairFunction<Long, Long, Long>() {
            @Override
            public Tuple2<Long, Long> call(Long l) throws Exception {
                return new Tuple2<Long, Long>(l,l);
            }
        });
        //左边是满足了session维度的session，右边是满足了user维度的user，我们需要的是两者都满足的session，所以用join,然后map成JavaRDD<Row>
        JavaRDD<Row> dimensionFilterActionRDD = userActionPairRDD.join(userPairRDD).map(new Function<Tuple2<Long, Tuple2<Row, Long>>, Row>() {
            @Override
            public Row call(Tuple2<Long, Tuple2<Row, Long>> tuple) throws Exception {
                return tuple._2._1;
            }
        });
        return dimensionFilterActionRDD;
    }

    public static JavaRDD<Long> getUserIdRDD(SQLContext sqlContext, DimensionInformation dimensionInformation, WebsiteMessage websiteMessage) {

        UserGroupInformationDAO userGroupInformationDAO = DAOFactory.getUserGroupInformationDAO();
        UserGroupInformation userGroupInformation = userGroupInformationDAO.findByGroupNameAndWebsiteId(dimensionInformation.getDimensionInformation(),websiteMessage.getWebsiteId());
        if (userGroupInformation.getId()==null){
            return null;
        }
        String needEvents = RetentionUtils.getNeedEvents(userGroupInformation.getEventIds(),DAOFactory.getEventMessageDAO());
        //找出对应时间段内该网站的所有session
        JavaRDD<Row> actionRDD = getActionRDDUserGroupInformation(sqlContext,userGroupInformation,websiteMessage.getDomain());
        //按照userid groupbykey
        JavaPairRDD<Long,Row> actionUserRDD = actionRDD.mapToPair(new PairFunction<Row, Long, Row>() {
            @Override
            public Tuple2<Long, Row> call(Row row) throws Exception { return new Tuple2<Long, Row>(row.getLong(0),row); }
        });
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
        JavaRDD<Row> userRDD1 = getUserRDD(sqlContext,userGroupInformation);
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

    public static JavaRDD<Row> getUserRDD(SQLContext sqlContext, UserGroupInformation userGroupInformation) {
        //判断是否有维度条件，
        String sql = "select user_id "
                + "from user_info "
                + "where " + userGroupInformation.getUserAttr();
        DataFrame actionDF = sqlContext.sql(sql);

        return actionDF.javaRDD();
    }


}
