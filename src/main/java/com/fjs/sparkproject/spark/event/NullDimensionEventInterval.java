package com.fjs.sparkproject.spark.event;

import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.EventMessageDAO;
import com.fjs.sparkproject.dao.IntervalAnalysisDAO;
import com.fjs.sparkproject.dao.PageMessageDAO;
import com.fjs.sparkproject.dao.WebsiteMessageDAO;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.EventMessage;
import com.fjs.sparkproject.domain.IntervalAnalysis;
import com.fjs.sparkproject.domain.WebsiteMessage;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.SparkUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.*;

public class NullDimensionEventInterval {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_SESSION1);
        SparkUtils.setMaster(conf);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());
        //如果在本地条件下运行，就要生成测试数据
        SparkUtils.mockData(sc,sqlContext);

        WebsiteMessageDAO websiteMessageDAO = DAOFactory.getWebsiteMessageDAO();
        PageMessageDAO pageMessageDAO = DAOFactory.getPageMessageDAO();
        EventMessageDAO eventMessageDAO = DAOFactory.getEventMessageDAO();

        //获取数据库中所有的网站
        List<WebsiteMessage> websiteMessages = websiteMessageDAO.findAllWebsite();
        //对每个网站进行统计
        for (WebsiteMessage websiteMessage:websiteMessages){
            //获取该网站所有事件RDD
            JavaRDD<Row> actionRDD = getActionRDD(sqlContext,websiteMessage.getDomain());
            List<EventMessage> eventMessages = eventMessageDAO.findMattersByWebsiteId(websiteMessage.getWebsiteId());
            for (EventMessage eventMessage:eventMessages){
                for (EventMessage eventMessage1:eventMessages){
                    JavaRDD<Long> actionIntervalsRDD = getActionIntervals(actionRDD,eventMessage,eventMessage1);
                    //吧平均间隔时间，最大最小间隔时间计算出来放到数据库中
                    if (actionIntervalsRDD!=null)
                        calculateInterval(actionIntervalsRDD,eventMessage,eventMessage1);
                }
            }


        }


    }

    private static void calculateInterval(JavaRDD<Long> actionIntervalsRDD, EventMessage eventMessage, EventMessage eventMessage1) {
        IntervalAnalysisDAO intervalAnalysisDAO = DAOFactory.getIntervalTimeAnalysisDAO();
        Long total = actionIntervalsRDD.count();
        Long totalIntervaltime = actionIntervalsRDD.reduce(new Function2<Long, Long, Long>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Long call(Long aLong, Long aLong2) throws Exception {
                return aLong + aLong2;
            }
        });

        Double averageIntervalTime = (double)totalIntervaltime/total;

        Long minIntervalTime = actionIntervalsRDD.reduce(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) throws Exception {
                if (aLong<aLong2){
                    return aLong;
                }else {
                    return aLong2;
                }
            }
        });
        Long maxIntervalTime = actionIntervalsRDD.reduce(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) throws Exception {
                if (aLong<aLong2){
                    return aLong2;
                }else {
                    return aLong;
                }
            }
        });
        List<Long> intervalTimes = actionIntervalsRDD.collect();
        Collections.sort(intervalTimes, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return Integer.valueOf(o1.toString())-Integer.valueOf(o2.toString());
            }
        });
        Double median = null;
        if (intervalTimes.size()==1){
            median = (double)intervalTimes.get(0);
        }else {
            if (intervalTimes.size()%2==0){
                median = (double)intervalTimes.get(intervalTimes.size()/2) + intervalTimes.get(intervalTimes.size()/2+1);
                median = median/2;
            }else {
                median = (double)intervalTimes.get((intervalTimes.size()+1)/2);
            }
        }


        intervalAnalysisDAO.insert(new IntervalAnalysis(null,eventMessage.getEventId(),eventMessage1.getEventId(),null,null,
                null,null,median,averageIntervalTime,(double)maxIntervalTime,(double)minIntervalTime,Integer.valueOf(total.toString()),new Date()));
    }

    private static JavaRDD<Long> getActionIntervals(JavaRDD<Row> actionRDD, EventMessage eventMessage, EventMessage eventMessage1) {
            final String eventMethod1 = eventMessage.getEventMethod();
            final String eventMethod2 = eventMessage1.getEventMethod();
            JavaRDD<Row> filterActionRDD = actionRDD.filter(new Function<Row, Boolean>() {
                @Override
                public Boolean call(Row row) throws Exception {
                    String eventName = row.getString(6);

                    if (eventName.equals(eventMethod1)||eventName.equals(eventMethod2)){
                        return true;
                    }else {
                        return false;
                    }
                }
            });
            JavaPairRDD<String,Row> filterPairActionRDD = filterActionRDD.mapToPair(new PairFunction<Row, String, Row>() {
                @Override
                public Tuple2<String, Row> call(Row row) throws Exception {
                    return new Tuple2<String, Row>(row.getString(2),row);
                }
            });
            JavaPairRDD<String,Iterable<Row>> filterPairActionGroupRDD = filterPairActionRDD.groupByKey();

            JavaRDD<Long> intervalTimeRDD = filterPairActionGroupRDD.flatMap(new FlatMapFunction<Tuple2<String, Iterable<Row>>, Long>() {
                @Override
                public Iterable<Long> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                    Iterator<Row> iterator = tuple._2.iterator();
                    List<Row> rows = new ArrayList<Row>();

                    while (iterator.hasNext()){
                        rows.add(iterator.next());
                    }

                    Collections.sort(rows, new Comparator<Row>() {
                        @Override
                        public int compare(Row o1, Row o2) {
                            String actionTime1 = o1.getString(3);
                            String actionTime2 = o2.getString(3);

                            Date date1 = DateUtils.parseTime(actionTime1);
                            Date date2 = DateUtils.parseTime(actionTime2);

                            return (int)(date1.getTime() - date2.getTime());
                        }
                    });

                    List<Long> intervalTime = new ArrayList<Long>();

                    Row lastRow = null;

                    for (Row row:rows){

                        if (lastRow==null){
                            lastRow = row;
                            continue;
                        }
                        //如果lastrow和现在的row符合两个要求事件,计算间隔时间，加入到intervalTime里
                        if (lastRow.getString(6).equals(eventMethod1)&&row.getString(6).equals(eventMethod2)){
                            intervalTime.add((DateUtils.parseTime(row.getString(3)).getTime()-DateUtils.parseTime(lastRow.getString(3)).getTime())/1000);
                            lastRow=null;
                        }
                    }
                    return intervalTime;
                }
            });
        return intervalTimeRDD;
    }

    private static JavaRDD<Row> getActionRDD(SQLContext sqlContext, String domain) {
        String sql = "select * "
                + "from user_action "
                + "where date='" + DateUtils.getYesterdayDate() + "' "
                + "and url like '" + domain + "%' ";
        DataFrame actionDF = sqlContext.sql(sql);
        return actionDF.javaRDD();
    }

}
