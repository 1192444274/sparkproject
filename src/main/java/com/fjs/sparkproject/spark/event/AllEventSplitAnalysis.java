package com.fjs.sparkproject.spark.event;

import com.fjs.sparkproject.dao.EventMessageDAO;
import com.fjs.sparkproject.dao.EventSplitAnalysisDAO;
import com.fjs.sparkproject.dao.WebsiteMessageDAO;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.SparkUtils;
import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.domain.EventMessage;
import com.fjs.sparkproject.domain.EventSplitAnalysis;
import com.fjs.sparkproject.domain.WebsiteMessage;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.*;

public class AllEventSplitAnalysis {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_SESSION1);
        SparkUtils.setMaster(conf);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());
        //如果在本地条件下运行，就要生成测试数据
        SparkUtils.mockData(sc,sqlContext);

        WebsiteMessageDAO websiteMessageDAO = DAOFactory.getWebsiteMessageDAO();
        EventMessageDAO eventMessageDAO = DAOFactory.getEventMessageDAO();

        List<WebsiteMessage> websiteMessages = websiteMessageDAO.findAllWebsite();

        for (WebsiteMessage websiteMessage:websiteMessages){
            //找出该页面的所有action
            JavaRDD<Row> actionRDD = getActionRDD(sqlContext,websiteMessage.getDomain());
            //以sessionid为键
            JavaPairRDD<String,Row> actionPairRDD = actionRDD.mapToPair(new PairFunction<Row, String, Row>() {
                @Override
                public Tuple2<String, Row> call(Row row) throws Exception { return new Tuple2<String, Row>(row.getString(2),row); }
            });
            //获取每个切片的转化次数
            JavaPairRDD<String,Integer> eventSplitRDD = getEventSplitRDD(actionPairRDD,websiteMessage.getWebsiteId());

            //汇总转化次数和转化率
            calculateSplitCountAndRate(eventSplitRDD,websiteMessage.getWebsiteId());

        }

    }

    private static void calculateSplitCountAndRate(JavaPairRDD<String, Integer> eventSplitRDD, Integer websiteId) {
        JavaPairRDD<String,Integer> eventSplitSum = eventSplitRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });

        Map<String,Integer> map = eventSplitSum.collectAsMap();
        EventSplitAnalysisDAO eventSplitAnalysisDAO = DAOFactory.getEventSplitAnalysisDAO();
        for (String s:map.keySet()){
            String splitName = s;
            Integer time = map.get(s);
            System.out.println(splitName.split("_").length==1);
            if (splitName.split("_").length==1){
                eventSplitAnalysisDAO.insert(new EventSplitAnalysis(null,websiteId,s,time,null,new Date()));
            }else {
                if (map.get(getLastSplit(s))!=null){
                    Double rate = (double)time/map.get(getLastSplit(s));
                    eventSplitAnalysisDAO.insert(new EventSplitAnalysis(null,websiteId,s,time,rate,new Date()));
                }
            }

        }

    }

    private static String getLastSplit(String s) {
        String[] s1 = s.split("_");
        String r = "";
        for (int i=0;i<s1.length-1;i++){
            r = r + s1[i] + "_";
        }
        System.out.println(r.substring(0,r.length()-1));
        return r.substring(0,r.length()-1);
    }

    private static JavaPairRDD<String,Integer> getEventSplitRDD(JavaPairRDD<String, Row> actionPairRDD, final Integer websiteId) {
        JavaPairRDD<String,Iterable<Row>> eventSplitGroupRDD = actionPairRDD.groupByKey();

        return eventSplitGroupRDD.flatMapToPair(new PairFlatMapFunction<Tuple2<String, Iterable<Row>>, String, Integer>() {
            @Override
            public Iterable<Tuple2<String, Integer>> call(Tuple2<String, Iterable<Row>> tuple2) throws Exception {
                EventMessageDAO eventMessageDAO = DAOFactory.getEventMessageDAO();

                List<EventMessage> eventMessages = eventMessageDAO.findMattersByWebsiteId(websiteId);

                List<Tuple2<String,Integer>> list = new ArrayList<Tuple2<String, Integer>>();

                Iterator<Row> iterator = tuple2._2.iterator();

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

                for (EventMessage eventMessage:eventMessages){
                    String eventMethod = eventMessage.getEventMethod();
                    Integer eventId = eventMessage.getEventId();
                    //单个事件的发生次数
                    for (Row row:rows){
                        if (eventMethod.equals(row.getString(6))){
                            list.add(new Tuple2<String, Integer>(eventId.toString(),1));
                        }
                    }
                    for (EventMessage eventMessage1:eventMessages){
                        String eventMethod1 = eventMessage.getEventMethod();
                        Integer eventId1 = eventMessage1.getEventId();
                        String lastEventMethod = null;
                        //两个事件的发生次数
                        for (Row row:rows){
                            if (lastEventMethod==null){
                                lastEventMethod = row.getString(6);
                                continue;
                            }
                            if (lastEventMethod.equals(eventMethod)&&row.getString(6).equals(eventMethod1)){
                                list.add(new Tuple2<String, Integer>(eventId + "_" + eventId1,1));
                            }
                            lastEventMethod = row.getString(6);
                        }


                        for (EventMessage eventMessage2:eventMessages){
                            String eventMethod2 = eventMessage2.getEventMethod();
                            Integer eventId2 = eventMessage2.getEventId();
                            String lastEventMethod1 = null;//第一个事件
                            String lastEventMethod2 = null;//第二个事件
                            //三个事件的发生次数
                            for(Row row:rows){
                                if (lastEventMethod1 == null){
                                    lastEventMethod1 = row.getString(6);
                                    continue;
                                }
                                if (lastEventMethod2 == null){
                                    lastEventMethod2 = row.getString(6);
                                    continue;
                                }
                                if (lastEventMethod1.equals(eventMethod)&&lastEventMethod2.equals(eventMethod1)&&row.getString(6).equals(eventMethod2)){
                                    list.add(new Tuple2<String, Integer>(eventId + "_" + eventId1 + "_" + eventId2,1));
                                }

                                lastEventMethod1 = lastEventMethod2;
                                lastEventMethod2 = row.getString(6);
                            }
                        }
                    }

                }
                for (Tuple2<String,Integer> tuple21:list){
                    System.out.println(tuple21);
                }
                return list;
            }
        });



    }

    private static JavaRDD<Row> getActionRDD(SQLContext sqlContext, String domain) {
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
