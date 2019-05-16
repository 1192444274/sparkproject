package com.fjs.sparkproject.spark.page;

import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.PageFlowSplitAnalysisDAO;
import com.fjs.sparkproject.dao.PageMessageDAO;
import com.fjs.sparkproject.dao.WebsiteMessageDAO;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.PageFlowSplitAnalysis;
import com.fjs.sparkproject.domain.PageMessage;
import com.fjs.sparkproject.domain.WebsiteMessage;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.SparkUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.*;

public class pageConvertRateAnalysis {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_SESSION1);
        SparkUtils.setMaster(conf);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());
        //如果在本地条件下运行，就要生成测试数据
        SparkUtils.mockData(sc,sqlContext);

        WebsiteMessageDAO websiteMessageDAO = DAOFactory.getWebsiteMessageDAO();

        //获取数据库中所有的网站
        List<WebsiteMessage> websiteMessages = websiteMessageDAO.findAllWebsite();
        //对每个网站进行统计
        for (WebsiteMessage websiteMessage:websiteMessages){
            //获取该网站所有浏览事件RDD
            JavaRDD<Row> browseActionRDD = getBrowseActionRDD(sqlContext,websiteMessage.getDomain());

            JavaPairRDD<String,Row> browseActionPairRDD = getBrowseActionPairRDD(browseActionRDD);

            JavaPairRDD<String,Integer> pageSplitRDD = getPageSplitRDD(browseActionPairRDD,websiteMessage.getWebsiteId());

            calculateConvert(pageSplitRDD,websiteMessage.getWebsiteId());

//            calculateConvertRate();
        }

    }

    private static void calculateConvertRate() {
        PageFlowSplitAnalysisDAO pageFlowSplitAnalysisDAO = DAOFactory.getPageFlowSplitAnalysisDAO();
        List<PageFlowSplitAnalysis> pageFlowSplitAnalyses = pageFlowSplitAnalysisDAO.findAll();
        for (PageFlowSplitAnalysis pageFlowSplitAnalysis:pageFlowSplitAnalyses){
            PageFlowSplitAnalysis frontPageSplit = null;

            if (pageFlowSplitAnalysis.getPageSplit().split("_").length>1){
                for (PageFlowSplitAnalysis pageFlowSplitAnalysis1:pageFlowSplitAnalyses){
                    if (pageFlowSplitAnalysis1.getPageSplit().equals(pageFlowSplitAnalysis.getPageSplit().substring(0,pageFlowSplitAnalysis.getPageSplit().lastIndexOf("_")))){
                        frontPageSplit = pageFlowSplitAnalysis1;
                    }
                }

                pageFlowSplitAnalysis.setConversionRate((double) pageFlowSplitAnalysis.getHappenTime()/frontPageSplit.getHappenTime());

                pageFlowSplitAnalysisDAO.updateConversionRate(pageFlowSplitAnalysis);
            }

        }

    }

    private static void calculateConvert(JavaPairRDD<String, Integer> pageSplitRDD, final Integer websiteId) {

        final JavaPairRDD<String,Integer> pageSplitCountRDD = pageSplitRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        pageSplitCountRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> tuple) throws Exception {
                PageFlowSplitAnalysisDAO pageFlowSplitAnalysisDAO = DAOFactory.getPageFlowSplitAnalysisDAO();

                String pageSplit = tuple._1;
                Integer happenTime = tuple._2;
                pageFlowSplitAnalysisDAO.insert(new PageFlowSplitAnalysis(null,websiteId,pageSplit,happenTime,null,new Date()));

            }
        });

    }

    private static JavaPairRDD<String,Integer> getPageSplitRDD(JavaPairRDD<String, Row> browseActionPairRDD, final Integer websiteId) {
        JavaPairRDD<String,Iterable<Row>> groupPairRDD = browseActionPairRDD.groupByKey();

        final JavaPairRDD<String,Integer> pageSplitRDD = groupPairRDD.flatMapToPair(new PairFlatMapFunction<Tuple2<String, Iterable<Row>>, String, Integer>() {
            @Override
            public Iterable<Tuple2<String, Integer>> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                PageMessageDAO pageMessageDAO = DAOFactory.getPageMessageDAO();
                // 定义返回list
                List<Tuple2<String, Integer>> list =
                        new ArrayList<Tuple2<String, Integer>>();
                // 获取到当前session的访问行为的迭代器
                Iterator<Row> iterator = tuple._2.iterator();

                List<Row> rows = new ArrayList<Row>();
                List<PageMessage> pageMessages = pageMessageDAO.findByWebsiteId(websiteId);

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

                //第一层循环，找出每个网页的浏览次数
                for (PageMessage pageMessage:pageMessages){

                    Integer pageId = pageMessage.getPageId();
                    //统计单页面跳转
                    for (Row row:rows){
                        Integer pageId1 = pageMessageDAO.findByWebsiteIdAndUrl(websiteId,row.getString(5).split("\\?")[0]).getPageId();
                        if (pageId == pageId1){
                            list.add(new Tuple2<String, Integer>(pageId.toString(),1));
                        }
                    }
                    //第二层循环，统计两个网页的转化率
                    for (PageMessage pageMessage1:pageMessages){
                        if (pageMessage1.getPageId()==pageMessage.getPageId()){
                            continue;
                        }
                        String targetPageSplit = pageMessage.getPageId() + "_" + pageMessage1.getPageId();
                        String lastPageId = null;
                        String pageSplit = null;
                        for (Row row:rows){
                            if (lastPageId == null){
                                lastPageId = pageMessageDAO.findByWebsiteIdAndUrl(websiteId,row.getString(5).split("\\?")[0]).getPageId() + "";
                                continue;
                            }
                            pageSplit = lastPageId + "_" + pageMessageDAO.findByWebsiteIdAndUrl(websiteId,row.getString(5).split("\\?")[0]).getPageId();

                            if (targetPageSplit.equals(pageSplit)){
                                list.add(new Tuple2<String, Integer>(pageSplit,1));
                            }

                            lastPageId = pageSplit.substring(pageSplit.indexOf("_")+1);
                        }
                        for (PageMessage pageMessage2:pageMessages){
                            if (pageMessage2.getPageId()==pageMessage1.getPageId()){
                                continue;
                            }
                            targetPageSplit = targetPageSplit + "_" + pageMessage2.getPageId();
                            lastPageId = null;
                            pageSplit = null;
                            for (Row row:rows){
                                if (lastPageId == null){
                                    lastPageId = pageMessageDAO.findByWebsiteIdAndUrl(websiteId,row.getString(5).split("\\?")[0]).getPageId() + "";
                                    continue;
                                }
                                if (lastPageId.split("_").length<2){
                                    lastPageId = lastPageId + "_" + pageMessageDAO.findByWebsiteIdAndUrl(websiteId,row.getString(5).split("\\?")[0]).getPageId();
                                    continue;
                                }
                                pageSplit = lastPageId + "_" + pageMessageDAO.findByWebsiteIdAndUrl(websiteId,row.getString(5).split("\\?")[0]).getPageId();

                                if (targetPageSplit.equals(pageSplit)){
                                    list.add(new Tuple2<String, Integer>(pageSplit,1));
                                }

                                lastPageId = pageSplit.substring(pageSplit.indexOf("_")+1);
                            }

                        }

                    }

                }
                return list;
            }
        });

        return pageSplitRDD;

    }

    private static JavaPairRDD<String,Row> getBrowseActionPairRDD(JavaRDD<Row> browseActionRDD) {

        JavaPairRDD<String,Row> browseActionPairRDD = browseActionRDD.mapToPair(new PairFunction<Row, String, Row>() {
            @Override
            public Tuple2<String, Row> call(Row row) throws Exception {
                return new Tuple2<String, Row>(row.getString(2),row);
            }
        });
        return browseActionPairRDD;
    }

    private static JavaRDD<Row> getBrowseActionRDD(SQLContext sqlContext, String domain) {
        String sql = "select * "
                + "from user_action "
                + "where date='" + DateUtils.getYesterdayDate() + "' "
                + "and url like '" + domain + "%' "
                + "and action = 'browse'";
        DataFrame actionDF = sqlContext.sql(sql);
        return actionDF.javaRDD();
    }

}
