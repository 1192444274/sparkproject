package com.fjs.sparkproject.spark.event;

import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.EventAnalysisDAO;
import com.fjs.sparkproject.dao.EventMessageDAO;
import com.fjs.sparkproject.dao.EventParamAnalysisDAO;
import com.fjs.sparkproject.dao.WebsiteMessageDAO;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.EventAnalysis;
import com.fjs.sparkproject.domain.EventMessage;
import com.fjs.sparkproject.domain.EventParamAnalysis;
import com.fjs.sparkproject.domain.WebsiteMessage;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.SparkUtils;
import com.fjs.sparkproject.util.sparkUtil.EventUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.Date;
import java.util.List;

public class NullDimensionEventStatistics {

    public static void main(String[] args) {
        args = new String[]{"4"};
        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_SESSION1);
        SparkUtils.setMaster(conf);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());
        //如果在本地条件下运行，就要生成测试数据
        SparkUtils.mockData(sc,sqlContext);
        WebsiteMessageDAO websiteMessageDAO = DAOFactory.getWebsiteMessageDAO();
        EventMessageDAO eventMessageDAO = DAOFactory.getEventMessageDAO();
        EventAnalysisDAO eventAnalysisDAO = DAOFactory.getEventAnalysisDAO();
        //获取数据库中所有的网站
        List<WebsiteMessage> websiteMessages = websiteMessageDAO.findAllWebsite();
        //对每个网站的每个事件进行统计
        for (WebsiteMessage websiteMessage:websiteMessages){
            //获取该网站所有事件RDD
            JavaRDD<Row> actionRDD = getActionRDD(sqlContext,websiteMessage.getDomain());
            //获取事件总数,没必要写到数据库，这个数据可以让后台自己去把所有事件加起来得到
            Long totalEvent = actionRDD.count();
            //获取网站的事件
            List<EventMessage> eventMessages = eventMessageDAO.findMattersByWebsiteId(websiteMessage.getWebsiteId());
            //对每个事件进行分析
            if (eventMessages.size()!=0){
                for (EventMessage eventMessage:eventMessages){
                    //计算次数，过滤出符合条件的RDD
                    JavaRDD<Row> filterActionRDD = EventUtils.getFilterActionRDD(actionRDD,eventMessage);
                    final Long eventNum = filterActionRDD.count();
                    //找出所有独立ip
                    JavaRDD<String> ipActionRDD = EventUtils.getIpActionRDD(filterActionRDD);
                    Long ipNum = ipActionRDD.count();
                    //找出所有用户
                    JavaRDD<Long> userActionRDD = EventUtils.getUserActionRDD(filterActionRDD);
                    Long userNum = userActionRDD.count();
                    //找出所有会话
                    JavaRDD<String> sessionRDD = EventUtils.getSessionRDD(filterActionRDD);
                    Long sessionNum = sessionRDD.count();
                    Long money = null;
                    Long amount = null;
                    if (eventMessage.getEventType().equals("pay")){
                        money = getTotalMoney(filterActionRDD);
                        amount = getTotalAmount(filterActionRDD);

                        final String type = eventMessage.getParameter1();
                        final int eventId = eventMessage.getEventId();
                        paramStatistic(eventId,filterActionRDD,type,8, eventNum);

                        final String type2 = eventMessage.getParameter2();
                        paramStatistic(eventId,filterActionRDD,type2,9, eventNum);
                    }

                    if (eventMessage.getEventType().equals("click")){
                        final int eventId = eventMessage.getEventId();
                        String param1 = eventMessage.getParameter1();
                        String param2 = eventMessage.getParameter2();
                        String param3 = eventMessage.getParameter3();
                        String param4 = eventMessage.getParameter4();

                        if (param1 != null){
                            paramStatistic(eventId,filterActionRDD,param1,8,eventNum);
                        }
                        if (param2 != null){
                            paramStatistic(eventId,filterActionRDD,param1,9, eventNum);
                        }
                        if (param3 != null){
                            paramStatistic(eventId,filterActionRDD,param1,10, eventNum);
                        }
                        if (param4 != null){
                            paramStatistic(eventId,filterActionRDD,param1,11, eventNum);
                        }
                    }

                    Integer money1;
                    Integer amount1;
                    if (money != null){
                        money1 = Integer.valueOf(money.toString());
                    }else {
                        money1 = null;
                    }
                    if (amount != null){
                        amount1 = Integer.valueOf(amount.toString());
                    }else {
                        amount1 = null;
                    }

                    //插入数据库中
                    eventAnalysisDAO.insert(new EventAnalysis(null,eventMessage.getEventId(),null,
                            null,null,null,Integer.parseInt(String.valueOf(eventNum)),Integer.parseInt(String.valueOf(userNum)),
                            Integer.parseInt(String.valueOf(ipNum)),Integer.parseInt(String.valueOf(sessionNum)),money1,amount1,new Date()));
                }
            }
        }
    }

    private static void paramStatistic(final int eventId, JavaRDD<Row> filterActionRDD, final String param1, int i, final Long eventNum) {
        JavaPairRDD<String,Long> parameterRDD1 = getParameterRDDById(filterActionRDD,i);
        if (parameterRDD1!=null){
            JavaPairRDD<String,Long> parameterCountRDD1 = parameterRDD1.reduceByKey(new Function2<Long, Long, Long>() {
                @Override
                public Long call(Long v1, Long v2) throws Exception { return v1 + v2; }
            });
            parameterCountRDD1.foreach(new VoidFunction<Tuple2<String, Long>>() {
                @Override
                public void call(Tuple2<String, Long> tuple2) throws Exception {
                    EventParamAnalysisDAO eventParamAnalysisDAO = DAOFactory.getEventParamAnalysisDAO();
                    EventParamAnalysis eventParamAnalysis = new EventParamAnalysis(null,eventId,param1,
                            tuple2._1,null,null,null,null,Integer.valueOf(tuple2._2.toString()),(double)tuple2._2/eventNum,new Date());
                    eventParamAnalysisDAO.insert(eventParamAnalysis);
                }
            });
        }
    }

    private static JavaPairRDD<String,Long> getParameterRDDById(JavaRDD<Row> filterActionRDD, final int i) {
        filterActionRDD = filterActionRDD.filter(new Function<Row, Boolean>() {
            @Override
            public Boolean call(Row v1) throws Exception {
                if (v1.getString(i)==null||v1.getString(i).equals("")){
                    return false;
                }else {
                    return true;
                }
            }
        });
        return filterActionRDD.mapToPair(new PairFunction<Row, String, Long>() {
            @Override
            public Tuple2<String, Long> call(Row row) throws Exception {
                return new Tuple2<String, Long>(row.getString(i),1L);
            }
        });
    }

    private static JavaPairRDD<String,Long> getParameterRDD2(JavaRDD<Row> filterActionRDD) {

        filterActionRDD = filterActionRDD.filter(new Function<Row, Boolean>() {
            @Override
            public Boolean call(Row v1) throws Exception {
                if (v1.getString(9)==null||v1.getString(9).equals("")){
                    return false;
                }else {
                    return true;
                }
            }
        });

        return filterActionRDD.mapToPair(new PairFunction<Row, String, Long>() {
            @Override
            public Tuple2<String, Long> call(Row row) throws Exception {
                return new Tuple2<String, Long>(row.getString(9),1L);
            }
        });
    }

    private static JavaPairRDD<String,Long> getParameterRDD1(JavaRDD<Row> filterActionRDD) {

        filterActionRDD = filterActionRDD.filter(new Function<Row, Boolean>() {
            @Override
            public Boolean call(Row v1) throws Exception {
                if (v1.getString(8)==null||v1.getString(8).equals("")){
                    return false;
                }else {
                    return true;
                }
            }
        });
        return filterActionRDD.mapToPair(new PairFunction<Row, String, Long>() {
            @Override
            public Tuple2<String, Long> call(Row row) throws Exception {
                return new Tuple2<String, Long>(row.getString(8),1L);
            }
        });
    }

    private static Long getTotalAmount(JavaRDD<Row> filterActionRDD) {
        JavaRDD<Long> amountRDD = filterActionRDD.map(new Function<Row, Long>() {
            @Override
            public Long call(Row v1) throws Exception {
                if (v1.getString(11)==null||v1.getString(11).equals("")){
                    return 0L;
                }
                return Long.valueOf(v1.getString(11));
            }
        });

        return amountRDD.reduce(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        });
    }

    private static long getTotalMoney(JavaRDD<Row> filterActionRDD) {

        JavaRDD<Long> moneyRDD = filterActionRDD.map(new Function<Row, Long>() {
            @Override
            public Long call(Row v1) throws Exception {
                if (v1.getString(10)==null||v1.getString(10).equals("")){
                    return 0L;
                }
                return Long.valueOf(v1.getString(10));
            }
        });

        return moneyRDD.reduce(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        });
    }

    private static JavaRDD<Row> getActionRDD(SQLContext sqlContext,String domain) {
        String sql = "select * "
                + "from user_action "
                + "where date='" + DateUtils.getYesterdayDate() + "' "
                + "and url like '" + domain + "%' ";
        DataFrame actionDF = sqlContext.sql(sql);
//        System.out.println(actionDF.count());
//        for (Row row : actionDF.take(20)){
//            System.out.println(row);
//        }
        return actionDF.javaRDD();
    }

}