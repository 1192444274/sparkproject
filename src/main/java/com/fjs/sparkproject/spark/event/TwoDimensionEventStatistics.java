package com.fjs.sparkproject.spark.event;

import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.domain.*;
import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.*;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.SparkUtils;
import com.fjs.sparkproject.util.sparkUtil.EventUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.Date;
import java.util.List;

public class TwoDimensionEventStatistics {
    public static void main(String[] args) {
        args = new String[]{"4"};
        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_SESSION1);
        SparkUtils.setMaster(conf);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());
        //如果在本地条件下运行，就要生成测试数据
        SparkUtils.mockData(sc, sqlContext);
        WebsiteMessageDAO websiteMessageDAO = DAOFactory.getWebsiteMessageDAO();
        EventMessageDAO eventMessageDAO = DAOFactory.getEventMessageDAO();
        EventAnalysisDAO eventAnalysisDAO = DAOFactory.getEventAnalysisDAO();
        DimensionDAO dimensionDAO = DAOFactory.getDimensionDAO();
        DimensionInformationDAO dimensionInformationDAO = DAOFactory.getDimensionInformationDAO();
        //从这里开始考虑多维度，先获取所有维度
        List<Dimension> dimensions = dimensionDAO.findAllDimensions();

        for (Dimension dimension : dimensions) {
            //获取维度信息
            List<DimensionInformation> dimensionInformations = dimensionInformationDAO.findDimensionInformationByDimensionId(dimension.getDimensionId());
            for (DimensionInformation dimensionInformation : dimensionInformations) {
                for (Dimension dimension1 : dimensions) {
                    if (!(dimension1.getDimensionId() == dimension.getDimensionId())) {
                        //如果两个维度不同，获取第二个维度的信息
                        List<DimensionInformation> dimensionInformations1 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimension1.getDimensionId());
                        //第二个维度的所有信息和第一个维度的信息结合分析
                        for (DimensionInformation dimensionInformation1 : dimensionInformations1) {
                            //获取数据库中所有的网站
                            List<WebsiteMessage> websiteMessages = websiteMessageDAO.findAllWebsite();
                            //对每个网站的每个事件进行统计
                            for (WebsiteMessage websiteMessage : websiteMessages) {
                                //获取对应维度信息的所有事件RDD
                                JavaRDD<Row> actionRDD = getActionRDD(sqlContext, websiteMessage.getDomain(), dimension, dimensionInformation, dimension1, dimensionInformation1);
                                //获取事件总数,没必要写到数据库，这个数据可以让后台自己去把所有事件加起来得到
//                                Long totalEvent = actionRDD.count();
                                //获取维度相关用户，如果没有用户维度，则不用执行
                                JavaRDD<Long> userRDD = null;
                                if (dimension.getDimensionType().equals("user")||dimension.getDimensionType().equals("user_group")||dimension1.getDimensionType().equals("user")||dimension1.getDimensionType().equals("user_group")){
                                    userRDD = getUserRDD(sqlContext,dimension,dimensionInformation,dimension1,dimensionInformation1,websiteMessage);
                                }
                                //获取网站的事件
                                List<EventMessage> eventMessages = eventMessageDAO.findMattersByWebsiteId(websiteMessage.getWebsiteId());
                                //对每个事件进行分析
                                if (eventMessages.size() != 0) {
                                    for (EventMessage eventMessage : eventMessages) {
                                        //计算次数，过滤出符合条件的RDD
                                        JavaRDD<Row> filterActionRDD = EventUtils.getFilterActionRDD(actionRDD, eventMessage);
                                        JavaRDD<Row> dimensionFilterActionRdd;
                                        if (dimension.getDimensionType().equals("user") || dimension1.getDimensionType().equals("user")||dimension1.getDimensionType().equals("user")||dimension1.getDimensionType().equals("user_group")) {
                                            dimensionFilterActionRdd = EventUtils.getDimensionFilterActionRdd(filterActionRDD, userRDD);
                                        } else {
                                            dimensionFilterActionRdd = filterActionRDD;
                                        }
                                        Long eventNum = dimensionFilterActionRdd.count();
                                        //找出所有独立ip
                                        JavaRDD<String> ipActionRDD = EventUtils.getIpActionRDD(dimensionFilterActionRdd);
                                        Long ipNum = ipActionRDD.count();
                                        //找出所有用户
                                        JavaRDD<Long> userActionRDD = EventUtils.getUserActionRDD(dimensionFilterActionRdd);
                                        Long userNum = userActionRDD.count();
                                        //插入数据库中
//                                        eventAnalysisDAO.insert(new EventAnalysis(eventMessage.getEventId(), dimension.getDimensionId(), dimensionInformation.getDimensionInformationId(), dimension1.getDimensionId(), dimensionInformation1.getDimensionInformationId(), eventNum, userNum, ipNum, new Date()));
                                    }

                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private static JavaRDD<Long> getUserRDD(SQLContext sqlContext, Dimension dimension, DimensionInformation dimensionInformation, Dimension dimension1, DimensionInformation dimensionInformation1, WebsiteMessage websiteMessage) {

        JavaRDD<Long> userIdRDD1 = null;

        if (dimension.getDimensionType().equals("user_group")){
            userIdRDD1 = EventUtils.getUserIdRDD(sqlContext,dimensionInformation,websiteMessage);
        }
        if (dimension1.getDimensionType().equals("user_group")){
            userIdRDD1 = EventUtils.getUserIdRDD(sqlContext,dimensionInformation1,websiteMessage);
        }
        String sql = "select user_id " +
                "from user_info " +
                "where website_url='" + websiteMessage.getDomain() + "' " +
                (dimension.getDimensionType().equals("user")?("and " + dimension.getDimensionName() + "='" + dimensionInformation.getDimensionInformation() + "' "):"") +
                (dimension1.getDimensionType().equals("user")?("and " + dimension1.getDimensionName() + "='" + dimensionInformation1.getDimensionInformation() + "' "):"");
        DataFrame actionDF = sqlContext.sql(sql);
        JavaRDD<Long> userIdRDD2 = actionDF.javaRDD().map(new Function<Row, Long>() {
            @Override
            public Long call(Row row) throws Exception {
                return row.getLong(0);
            }
        });
        userIdRDD2 = userIdRDD1 == null?userIdRDD2:userIdRDD2.intersection(userIdRDD1);
        return userIdRDD2;
    }

    private static JavaRDD<Row> getActionRDD(SQLContext sqlContext, String domain, Dimension dimension, DimensionInformation dimensionInformation, Dimension dimension1, DimensionInformation dimensionInformation1) {
        String sql = "select * "
                + "from user_action "
                + "where date='" + DateUtils.getTodayDate() + "' "
                + "and url like '" + domain + "%' "
                + (dimension.getDimensionType().equals("session")?("and " + dimension.getDimensionName() + "='" + dimensionInformation.getDimensionInformation() + "' "):"")
                + (dimension1.getDimensionType().equals("session")?("and " + dimension1.getDimensionName() + "='" + dimensionInformation1.getDimensionInformation() + "' "):"");
        DataFrame actionDF = sqlContext.sql(sql);
        Long s = actionDF.count();
        return actionDF.javaRDD();



    }
}
