package com.fjs.sparkproject.spark.page;

import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.domain.*;
import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.*;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.SparkUtils;
import com.fjs.sparkproject.util.StringUtils;
import com.fjs.sparkproject.util.sparkUtil.EventUtils;
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

public class NullDimensionPageAnlysis {
    //website_source表，website_analysis表，page_analysis表，entry_page表
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
        //获取数据库中所有的网站
        List<WebsiteMessage> websiteMessages = websiteMessageDAO.findAllWebsite();
        //对每个网站进行统计
        for (WebsiteMessage websiteMessage:websiteMessages){
            //获取该网站所有浏览事件RDD
            JavaRDD<Row> actionBrowseRDD = getActionBrowseRDD(sqlContext,websiteMessage.getDomain());
            //网站页面浏览总次数
            Long totalEvent = actionBrowseRDD.count();
            //找出所有独立ip
            JavaRDD<String> ipActionRDD = EventUtils.getIpActionRDD(actionBrowseRDD);
            Long ipNum = ipActionRDD.count();
            //找出所有用户
            JavaRDD<Long> userActionRDD = EventUtils.getUserActionRDD(actionBrowseRDD);
            Long userNum = userActionRDD.count();


//            进行该网站的来源分析
            JavaPairRDD<Integer,Integer> channelActionRDD = getChannelActionRDD(actionBrowseRDD,websiteMessage.getWebsiteId());

//            calulateWebsiteSource(channelActionRDD,websiteMessage);
//            进行入口页面统计
//
//
//
//
//
//            //获取该网站所有事件RDD
            JavaRDD<Row> actionRDD = getActionRDD(sqlContext,websiteMessage.getDomain());

            JavaPairRDD<String,Integer> entryPageRDD = getEntryPageRDD(actionRDD);

            calulateEntryPage(entryPageRDD,websiteMessage.getWebsiteId());
            //计算该网站的浏览时长，步长
            JavaPairRDD<String,String> visitLengthRDD = getSessionIdActionRDD(actionRDD);
            System.out.println(visitLengthRDD.count());
            calulateAverage(visitLengthRDD,websiteMessage.getWebsiteId(),totalEvent,ipNum,userNum);

            //获取该网站所有的page
            List<PageMessage> pageMessages = pageMessageDAO.findAllPageMessages();
            List<String> pageAnalysisMessages = new ArrayList<String>();
            for (PageMessage pageMessage:pageMessages){
                //获取该网页所有浏览事件RDD
                JavaRDD<Row> actionBrowseRDD1 = getActionBrowseRDD1(sqlContext,pageMessage.getUrl());

                String pageAnalysisMessage = getPageAnalysisMessage(actionBrowseRDD1,pageMessage.getUrl());

                pageAnalysisMessages.add(pageAnalysisMessage);
            }

            JavaRDD<String> pageAnalysisMessageRDD = sc.parallelize(pageAnalysisMessages);

            JavaPairRDD<String,String> pageAnalysisMessagePairRDD = getPageAnalysisMessagePairRDD(pageAnalysisMessageRDD);

            //接下来计算该网站所有页面的浏览时长
            JavaPairRDD<String,String> visitLength2RDD = getVisitLength2RDD(actionRDD);
            //计算平均时长并把数据放进数据库
            calulateAverage1(visitLength2RDD,pageAnalysisMessagePairRDD);

        }
    }

    private static void calulateEntryPage(JavaPairRDD<String, Integer> entryPageRDD, final Integer websiteId) {
        JavaPairRDD<String,Integer> entryCountRDD = entryPageRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });
        entryCountRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> tuple) throws Exception {

                EntryPageDAO entryPageDAO = DAOFactory.getEntryPageDAO();
                PageMessageDAO pageMessageDAO = DAOFactory.getPageMessageDAO();
                String url = tuple._1;

                Integer pageId = pageMessageDAO.findByUrl(url).getPageId();

                Integer visitNum = tuple._2;

                entryPageDAO.intsert(new EntryPage(null,pageId,url,websiteId,visitNum,new Date()));

            }
        });


    }

    private static JavaPairRDD<String,Integer> getEntryPageRDD(JavaRDD<Row> actionRDD) {
        JavaPairRDD<String,Row> actionPairRDD = actionRDD.mapToPair(new PairFunction<Row, String, Row>() {
            @Override
            public Tuple2<String, Row> call(Row row) throws Exception {
                return new Tuple2<String, Row>(row.getString(2),row);
            }
        });

        JavaPairRDD<String,Iterable<Row>> actionPairGroupRDD = actionPairRDD.groupByKey();

        JavaPairRDD<String,Integer> entryPageRDD = actionPairGroupRDD.mapToPair(new PairFunction<Tuple2<String, Iterable<Row>>, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {

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

                Row row = rows.get(0);

                return new Tuple2<String, Integer>(row.getString(5).split("\\?")[0],1);

            }
        });
        return entryPageRDD;
    }

    private static void calulateWebsiteSource(JavaPairRDD<Integer, Integer> channelActionRDD, WebsiteMessage websiteMessage) {
        final Integer websiteId = websiteMessage.getWebsiteId();
        JavaPairRDD<Integer,Integer> channelCount = channelActionRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        channelCount.foreach(new VoidFunction<Tuple2<Integer, Integer>>() {
            @Override
            public void call(Tuple2<Integer, Integer> tuple) throws Exception {
                SourceDAO sourceDAO = DAOFactory.getSourceDAO();
                WebsiteSourceDAO websiteSourceDAO = DAOFactory.getWebsiteSourceDAO();
                Source source = sourceDAO.findById(tuple._1);
//                websiteSourceDAO.insert(new WebsiteSource(null,websiteId,source.getChannelId(),source.getId(),tuple._2,new Date()));
            }
        });

    }

    private static JavaPairRDD<Integer,Integer> getChannelActionRDD(JavaRDD<Row> actionBrowseRDD, final Integer websiteId) {
        JavaPairRDD<Integer,Integer> channelActionRDD = actionBrowseRDD.mapToPair(new PairFunction<Row, Integer, Integer>() {
            @Override
            public Tuple2<Integer, Integer> call(Row row) throws Exception {
                SourceDAO sourceDAO = DAOFactory.getSourceDAO();
                String currentUrl = row.getString(5);
                String referUrl = row.getString(19);
                if (referUrl.split("/")[0].equals(currentUrl.split("/")[0])){
                    return null;
                }else {
                    //判断是哪个渠道
                    Source source = sourceDAO.findByUrl(referUrl.split("\\?")[0],websiteId);
                    if (source.getId() == null){
                        return new Tuple2<Integer, Integer>(4,1);
                    }
                    return new Tuple2<Integer, Integer>(source.getId(),1);
                }
            }
        });

        return channelActionRDD;
    }

    private static void calulateAverage1(JavaPairRDD<String,String> visitLength2RDD,JavaPairRDD<String,String> pageAnalysisMessagePairRDD) {

        JavaPairRDD<String,String> totalLengths = visitLength2RDD.reduceByKey(new Function2<String, String, String>() {
            @Override
            public String call(String s, String s2) throws Exception {
                int visitLength1 = Integer.valueOf(StringUtils.getFieldFromConcatString(s,"\\|",Constants.FIELD_VISIT_LENGTH));
                int visitLength2 = Integer.valueOf(StringUtils.getFieldFromConcatString(s2,"\\|",Constants.FIELD_VISIT_LENGTH));
                int quitCount1 = Integer.valueOf(StringUtils.getFieldFromConcatString(s,"\\|",Constants.FIELD_QUIT));
                int quitCount2 = Integer.valueOf(StringUtils.getFieldFromConcatString(s2,"\\|",Constants.FIELD_QUIT));
                int count = Integer.valueOf(StringUtils.getFieldFromConcatString(s,"\\|",Constants.FIELD_COUNT));
                s = StringUtils.setFieldInConcatString(s,"\\|",Constants.FIELD_VISIT_LENGTH,(visitLength1+visitLength2)+"");
                s = StringUtils.setFieldInConcatString(s,"\\|",Constants.FIELD_COUNT,(count + 1 )+"");
                s = StringUtils.setFieldInConcatString(s,"\\|",Constants.FIELD_QUIT,(quitCount1+quitCount2)+"");
                return s;
            }
        });

        JavaPairRDD<String,Tuple2<String,String>> pageAnalysisCompleteMessagePairRDD = totalLengths.join(pageAnalysisMessagePairRDD);

        pageAnalysisCompleteMessagePairRDD.foreach(new VoidFunction<Tuple2<String, Tuple2<String, String>>>() {
            @Override
            public void call(Tuple2<String, Tuple2<String, String>> tuple) throws Exception {
                PageAnalysisDAO pageAnalysisDAO = DAOFactory.getPageAnalysisDAO();
                PageMessageDAO pageMessageDAO = DAOFactory.getPageMessageDAO();
                String partAggrInfo1 = tuple._2._1 + "|" + tuple._2._2;

                String url =tuple._1;
                Integer pageId = pageMessageDAO.findByUrl(url).getPageId();
                Long browseNum =  Long.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_TOTAL));
                Long userNum = Long.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_USER));
                Long ipNum = Long.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_IP));
                double x = Double.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_COUNT));
                double exitRate = Double.valueOf(Double.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_QUIT))
                        /Double.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_COUNT)));
                double averageVisitLength = Double.valueOf(Double.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_LENGTH))
                        /Double.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_COUNT)));
                pageAnalysisDAO.insert(new PageAnalysis(null,url,1,null,null,Integer.valueOf(browseNum.toString()),null,new Date(),Integer.valueOf(userNum.toString()),
                        Integer.valueOf(ipNum.toString()),exitRate,null,averageVisitLength,null));
//                pageAnalysisDAO.insert(new PageAnalysis(pageId,null,null,null,null,new Date(),userNum,ipNum,exitRate,null,averageVisitLength));

            }
        });

    }


    private static JavaPairRDD<String,String> getPageAnalysisMessagePairRDD(JavaRDD<String> pageAnalysisMessageRDD) {
        JavaPairRDD<String,String> pageAnalysisMessagePairRDD = pageAnalysisMessageRDD.mapToPair(new PairFunction<String, String, String>() {
            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                return new Tuple2<String, String>(StringUtils.getFieldFromConcatString(s,"\\|",Constants.FIELD_VISIT_URL),s);
            }
        });
        return pageAnalysisMessagePairRDD;
    }

    private static String getPageAnalysisMessage(JavaRDD<Row> actionBrowseRDD,String url) {
        //网站页面浏览总次数
        Long totalEvent = actionBrowseRDD.count();
        //找出所有独立ip
        JavaRDD<String> ipActionRDD = EventUtils.getIpActionRDD(actionBrowseRDD);
        Long ipNum = ipActionRDD.count();
        //找出所有用户
        JavaRDD<Long> userActionRDD = EventUtils.getUserActionRDD(actionBrowseRDD);
        Long userNum = userActionRDD.count();

        String partAggrInfo1 = Constants.FIELD_VISIT_URL + "=" + url + "|"
                + Constants.FIELD_VISIT_TOTAL + "=" + totalEvent + "|"
                + Constants.FIELD_VISIT_IP + "=" + ipNum + "|"
                + Constants.FIELD_VISIT_USER + "=" + userNum;
        return partAggrInfo1;
    }

    private static JavaRDD<Row> getActionBrowseRDD1(SQLContext sqlContext, String domain) {
        String sql = "select * "
                + "from user_action "
                + "where date='" + DateUtils.getYesterdayDate() + "' "
                + "and url like '" + domain + "%' "
                + "and action = 'browse'";
        DataFrame actionDF = sqlContext.sql(sql);
        return actionDF.javaRDD();
    }



    private static void calulateAverage(JavaPairRDD<String, String> visitLengthRDD, int websiteId, Long totalEvent, Long ipNum, Long userNum) {
        WebsiteAnalysisDAO websiteAnalysisDAO = DAOFactory.getWebsiteAnalysisDAO();
        Long sessionCount = visitLengthRDD.count();

        Tuple2<String,String> totalLength = visitLengthRDD.reduce(new Function2<Tuple2<String, String>, Tuple2<String, String>, Tuple2<String, String>>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, String> tuple1, Tuple2<String, String> tuple2) throws Exception {
                String partAggrInfo1 = tuple1._2;
                String partAggrInfo2 = tuple2._2;
                int visitLenth1 = Integer.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_LENGTH));
                int visitLenth2 = Integer.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo2,"\\|",Constants.FIELD_VISIT_LENGTH));
                int stepDepth1 = Integer.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_DEPTH));
                int stepDepth2 = Integer.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo2,"\\|",Constants.FIELD_VISIT_DEPTH));
                int bounce1 = Integer.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_BOUNCE));
                int bounce2 = Integer.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo2,"\\|",Constants.FIELD_VISIT_BOUNCE));
                int secondSkip1 = Integer.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_SECOND_SKIP));
                int secondSkip2 = Integer.valueOf(StringUtils.getFieldFromConcatString(partAggrInfo2,"\\|",Constants.FIELD_VISIT_SECOND_SKIP));
                partAggrInfo1 = StringUtils.setFieldInConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_LENGTH,(visitLenth1+visitLenth2)+"");
                partAggrInfo1 = StringUtils.setFieldInConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_DEPTH,(stepDepth1+stepDepth2)+"");
                partAggrInfo1 = StringUtils.setFieldInConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_BOUNCE,(bounce1+bounce2)+"");
                partAggrInfo1 = StringUtils.setFieldInConcatString(partAggrInfo1,"\\|",Constants.FIELD_VISIT_SECOND_SKIP,(secondSkip1+secondSkip2)+"");
                return new Tuple2<String, String>("key",partAggrInfo1);
            }
        });

        double averageVisitLength = Double.valueOf(StringUtils.getFieldFromConcatString(totalLength._2,"\\|",Constants.FIELD_VISIT_LENGTH))/sessionCount;
        double averageVisitDepth = Double.valueOf(StringUtils.getFieldFromConcatString(totalLength._2,"\\|",Constants.FIELD_VISIT_DEPTH))/sessionCount;
        double bounceRate = Double.valueOf(StringUtils.getFieldFromConcatString(totalLength._2,"\\|",Constants.FIELD_VISIT_BOUNCE))/sessionCount;
        double secondSkipRate =Double.valueOf(StringUtils.getFieldFromConcatString(totalLength._2,"\\|",Constants.FIELD_VISIT_SECOND_SKIP))/sessionCount;
        websiteAnalysisDAO.insert(new WebsiteAnalysis(websiteId,null,null,null,null,new Date(),userNum,ipNum,bounceRate,secondSkipRate,averageVisitLength,averageVisitDepth));

    }

    private static JavaPairRDD<String,String> getVisitLength2RDD(final JavaRDD<Row> actionRDD) {
        JavaPairRDD<String,Row> sessionIdActionRDD = actionRDD.mapToPair(new PairFunction<Row, String, Row>() {
            @Override
            public Tuple2<String, Row> call(Row row) throws Exception {
                return new Tuple2<String, Row>(row.getString(2),row);
            }
        });

        JavaPairRDD<String,Iterable<Row>> sessionId2ActionRDD = sessionIdActionRDD.groupByKey();

        JavaPairRDD<String,String> visitLength2RDD = sessionId2ActionRDD.flatMapToPair(new PairFlatMapFunction<Tuple2<String, Iterable<Row>>, String, String>() {
            @Override
            public Iterable<Tuple2<String, String>> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                List<Tuple2<String,String>> visitLenths = new ArrayList<Tuple2<String, String>>();
                List<PageTimePeriod> pageTimePeriods = new ArrayList<PageTimePeriod>();
                Iterator<Row> iterator = tuple._2.iterator();
                Date lastTime = null;
                while (iterator.hasNext()){
                    Row row = iterator.next();
                    String url = row.getString(5).split("\\?")[0];
                    Date actionTime = DateUtils.parseTime(row.getString(3));
                    PageTimePeriod pageTimePeriod = hasPage(pageTimePeriods,url);
                    //如果列表里没有，就直接加进去
                    if (pageTimePeriod==null){
                        pageTimePeriods.add(new PageTimePeriod(url,actionTime,actionTime));
                    }else {
                        //如果有了，就更新开始和结束时间
                        if(actionTime.before(pageTimePeriod.getStartTime())) {
                            pageTimePeriod.setStartTime(actionTime);
                        }
                        if(actionTime.after(pageTimePeriod.getEndTime())) {
                            pageTimePeriod.setEndTime(actionTime);
                        }
                    }

                    if (lastTime == null){
                        lastTime = actionTime;
                    }else {
                        if (actionTime.after(lastTime)){
                            lastTime = actionTime;
                        }
                    }

                }
                for (PageTimePeriod pageTimePeriod:pageTimePeriods){
                    long visitLength = (pageTimePeriod.getEndTime().getTime()-pageTimePeriod.getStartTime().getTime())/1000;
                    String partAggrInfo = Constants.FIELD_VISIT_LENGTH + "=" + visitLength + "|"
                            + Constants.FIELD_COUNT + "=" + 1+ "|"
                            + Constants.FIELD_QUIT + "=" + (pageTimePeriod.getEndTime()==lastTime?1:0);
                    visitLenths.add(new Tuple2<String, String>(pageTimePeriod.getUrl(),partAggrInfo));
                }
                return visitLenths;
            }

            private PageTimePeriod hasPage(List<PageTimePeriod> pageTimePeriods, String url) {
                for (PageTimePeriod pageTimePeriod:pageTimePeriods){
                    if (pageTimePeriod.getUrl().equals(url)){
                        return pageTimePeriod;
                    }
                }
                return null;
            }

        });
        return visitLength2RDD;
    }


    private static JavaPairRDD<String,String> getSessionIdActionRDD(JavaRDD<Row> actionRDD) {
        JavaPairRDD<String,Row> sessionIdActionRDD = actionRDD.mapToPair(new PairFunction<Row, String, Row>() {
            @Override
            public Tuple2<String, Row> call(Row row) throws Exception {
                return new Tuple2<String, Row>(row.getString(2),row);
            }
        });

        JavaPairRDD<String,Iterable<Row>> sessionId2ActionRDD = sessionIdActionRDD.groupByKey();

        JavaPairRDD<String,String> visitLengthRDD = sessionId2ActionRDD.mapToPair(new PairFunction<Tuple2<String, Iterable<Row>>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                String sessionId = tuple._1;
                Iterator<Row> iterator = tuple._2.iterator();

                // session的起始和结束时间
                Date startTime = null;
                Date endTime = null;
                // session的访问深度
                int stepDepth = 0;
                int bounce = 0;
                List<String> urls = new ArrayList<String>();
                int i = 0;
                // 遍历session所有的访问行为
                while(iterator.hasNext()) {
                    //如果一个session只有一个事件，那么循环结束后i=1，则认为用户浏览一个页面后直接跳出网站
                    i++;
                    Row row = iterator.next();
                    String url = row.getString(5).split("\\?")[0];
                    Date actionTime = DateUtils.parseTime(row.getString(3));
                    //计算开始时间和结束时间
                    if(startTime == null) {
                        startTime = actionTime;
                    }
                    if(endTime == null) {
                        endTime = actionTime;
                    }

                    if(actionTime.before(startTime)) {
                        startTime = actionTime;
                    }
                    if(actionTime.after(endTime)) {
                        endTime = actionTime;
                    }
                    if (!urls.contains(url)){
                        urls.add(url);
                    }
                }
                //如果只有一个浏览事件，说明用户直接跳出网站了
                if (i == 1){
                    bounce = 1;
                }else {
                    bounce = 0;
                }

                long visitLength = (endTime.getTime() - startTime.getTime()) / 1000;
                stepDepth = urls.size();
                String partAggrInfo = Constants.FIELD_VISIT_LENGTH + "=" + visitLength + "|"
                        + Constants.FIELD_VISIT_DEPTH + "=" + stepDepth + "|"
                        + Constants.FIELD_VISIT_SECOND_SKIP + "=" + (stepDepth==1?0:1) + "|"
                        + Constants.FIELD_VISIT_BOUNCE + "=" + bounce + "|"
                        + Constants.FIELD_START_TIME + "=" + DateUtils.formatTime(startTime);

                return new Tuple2<String, String>(sessionId,partAggrInfo);
            }
        });

        return visitLengthRDD;
    }

    private static JavaRDD<Row> getActionRDD(SQLContext sqlContext, String domain) {
        String sql = "select * "
                + "from user_action "
                + "where date='" + DateUtils.getYesterdayDate() + "' "
                + "and url like '" + domain + "%' ";
        DataFrame actionDF = sqlContext.sql(sql);
        return actionDF.javaRDD();
    }

    private static JavaRDD<Row> getActionBrowseRDD(SQLContext sqlContext, String domain) {
        String sql = "select * "
                + "from user_action "
                + "where date='" + DateUtils.getYesterdayDate() + "' "
                + "and url like '" + domain + "%' "
                + "and action = 'browse'";
        DataFrame actionDF = sqlContext.sql(sql);
        return actionDF.javaRDD();
    }

}
