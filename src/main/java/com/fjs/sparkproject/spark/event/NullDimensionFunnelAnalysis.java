package com.fjs.sparkproject.spark.event;

import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.EventMessageDAO;
import com.fjs.sparkproject.dao.FunnelMessageDAO;
import com.fjs.sparkproject.dao.FunnelSplitAnalysisDAO;
import com.fjs.sparkproject.dao.WebsiteMessageDAO;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.FunnelMessage;
import com.fjs.sparkproject.domain.FunnelSplitAnalysis;
import com.fjs.sparkproject.domain.WebsiteMessage;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.SparkUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.*;

public class NullDimensionFunnelAnalysis {

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
        FunnelMessageDAO funnelMessageDAO = DAOFactory.getFunnelMessageDAO();
        FunnelSplitAnalysisDAO funnelSplitAnalysisDAO = DAOFactory.getFunnelSplitAnalysisDAO();

        //获取所有的漏斗，进行一一分析
        List<FunnelMessage> funnelMessages = funnelMessageDAO.findAllFunnel();
        for (FunnelMessage funnelMessage:funnelMessages){
            WebsiteMessage websiteMessage = websiteMessageDAO.findById(funnelMessage.getWebsiteId());
            JavaRDD<Row> actionRDD = getActionRDD(sqlContext,websiteMessage.getDomain());
            JavaPairRDD<String,Row> actionPairRDD = getActionPairRDD(actionRDD);
            long l = actionPairRDD.count();
            String w =funnelMessage.getEventIds();
            JavaPairRDD<String,Integer> actionSplitRDD = getActionSplitRDD(sc,actionPairRDD,funnelMessage.getEventIds(),websiteMessage.getWebsiteId());
            long s = actionSplitRDD.count();

            Map<String,Object> countSplitMap = actionSplitRDD.countByKey();

            calculateConversionRate(countSplitMap,funnelMessage.getId());
        }

    }

    private static void calculateConversionRate(Map<String,Object> countSplitMap, Integer id) {
        FunnelSplitAnalysisDAO funnelSplitAnalysisDAO = DAOFactory.getFunnelSplitAnalysisDAO();
        for (Map.Entry<String,Object> splitMap:countSplitMap.entrySet()){
            String splitName = splitMap.getKey();
            Integer happenTime = Integer.valueOf(String.valueOf(splitMap.getValue()));
            Double conversionRate = null;
            if (splitName.split("_").length > 1){
                Integer frontTime = Integer.valueOf(String.valueOf(countSplitMap.get(getFrontSplit(splitName))));
                conversionRate =  Double.valueOf(happenTime.toString())/frontTime;
            }
            funnelSplitAnalysisDAO.insert(new FunnelSplitAnalysis(null,id,splitName,happenTime,conversionRate,new Date()));
        }
    }

    private static String getFrontSplit(String splitName) {

        return splitName.substring(0,splitName.lastIndexOf("_"));
    }

    private static JavaPairRDD<String,Integer> getActionSplitRDD(JavaSparkContext sc, JavaPairRDD<String, Row> actionPairRDD, final String eventIds, final Integer websiteId) {

        final Broadcast<String> targetEventFlowBroadcast = sc.broadcast(eventIds);

        JavaPairRDD<String,Iterable<Row>> actionGroupPairRDD = actionPairRDD.groupByKey();

        long l = actionGroupPairRDD.count();
        return actionGroupPairRDD.flatMapToPair(new PairFlatMapFunction<Tuple2<String, Iterable<Row>>, String, Integer>() {
            @Override
            public Iterable<Tuple2<String, Integer>> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                EventMessageDAO eventMessageDAO = DAOFactory.getEventMessageDAO();
                // 定义返回list
                List<Tuple2<String, Integer>> list =
                        new ArrayList<Tuple2<String, Integer>>();

                // 获取到当前session的访问行为的迭代器
                Iterator<Row> iterator = tuple._2.iterator();

                // 获取指定的事件流
                // 使用者指定的页面流，1,2,3,4,5,6,7
                // 1->2的转化率是多少？2->3的转化率是多少？
                String[] targetEvents = targetEventFlowBroadcast.value().split(",");

                //把row放进列表中排序
                List<Row> rows = new ArrayList<Row>();
                while(iterator.hasNext()) {
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



                for (int i=1;i<=targetEvents.length;i++){
                    //把切片组好
                    //比如说需求是1，2，3，4
                    //切片就有1  1，2  1，2，3  1，2，3，4
                    String targetEventSplit = null;
                    for (int j=0;j<i;j++){
                        if (targetEventSplit == null){
                            targetEventSplit = targetEvents[j];
                            continue;
                        }
                        targetEventSplit = targetEventSplit + "_" + targetEvents[j];
                    }
                    int o = rows.size();
                    String lastEventIds = null;
                    String eventSplit = null;
                    //然后去事件中找
                    for(Row row:rows){
                        //要把事件的id组合起来，具体组几个根据i来看
                        //如果是null，直接加，如果lastEventIds中包含的事件少于（不等于）i，也直接把事件id加上去
                        if (lastEventIds == null){
                            Integer eventId = eventMessageDAO.findIdByEventMethodAndWebsiteId(row.getString(6),websiteId);
                            lastEventIds = eventId + "";
                            continue;
                        }
                        if (lastEventIds.split("_").length<i-1){
                            Integer eventId = eventMessageDAO.findIdByEventMethodAndWebsiteId(row.getString(6),websiteId);
                            lastEventIds = lastEventIds + "_" + eventId;
                            continue;
                        }
                        //如果i=1，那么就看当前页面好了
                        if (i == 1){
                            eventSplit = eventMessageDAO.findIdByEventMethodAndWebsiteId(row.getString(6),websiteId) + "";
                        }else {
                            eventSplit = lastEventIds + "_" + eventMessageDAO.findIdByEventMethodAndWebsiteId(row.getString(6),websiteId);
                        }
                        if (eventSplit.equals(targetEventSplit)){
                            list.add(new Tuple2<String, Integer>(eventSplit,1));
                        }
                        lastEventIds = update(eventSplit);
                    }
                }
                return list;
            }
            private String update(String eventSplit) {
                return eventSplit.substring(eventSplit.indexOf("_")+1);
            }

        });
    }

    private static JavaPairRDD<String,Row> getActionPairRDD(JavaRDD<Row> actionRDD) {
        return actionRDD.mapToPair(new PairFunction<Row, String, Row>() {
            @Override
            public Tuple2<String, Row> call(Row row) throws Exception {
                return new Tuple2<String, Row>(row.getString(2),row);
            }
        });
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
