package com.fjs.sparkproject.spark.Form;

import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.EventMessageDAO;
import com.fjs.sparkproject.dao.FormAnalysisDAO;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.FormAnalysis;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.SparkUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.*;

public class FormSubmitAnalysis {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_SESSION1);
        SparkUtils.setMaster(conf);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());
        //如果在本地条件下运行，就要生成测试数据
        SparkUtils.mockData(sc,sqlContext);

        EventMessageDAO eventMessageDAO = DAOFactory.getEventMessageDAO();
        FormAnalysisDAO formAnalysisDAO = DAOFactory.getFormAnalysisDAO();

        //获取所有的表单页面
        List<String> formPageUrls = eventMessageDAO.findEventsByType("form");
        String formPageUrlToString = ListToString(formPageUrls);
        //遍历所有表单页面
        for (String formPageUrl:formPageUrls){
            //获取所有表单页面的事件
            JavaRDD<Row> actionRDD = getActionRDD(sqlContext,formPageUrl);
            //获取表单页面的浏览事件
            JavaRDD<Row> actionBrowseRDD = getBrowseActionRDD(sqlContext,formPageUrl);
            //表单浏览人数
            Long visitNum = actionBrowseRDD.count();
            //获取表单页面的提交事件
            JavaRDD<Row> actionSubmitRDD = getActionSubmitRDD(sqlContext,formPageUrl);
            //表单提交次数
            Long submitNum = actionSubmitRDD.count();

            //根据sessionid
            JavaPairRDD<String,Row> actionPairRDD = actionRDD.mapToPair(new PairFunction<Row, String, Row>() {
                @Override
                public Tuple2<String, Row> call(Row row) throws Exception { return new Tuple2<String, Row>(row.getString(2),row); }});
            //groupbykey
            JavaPairRDD<String,Iterable<Row>> actionGroupPairRDD = actionPairRDD.groupByKey();
            //获取表单填写时长RDD,以及填写的成功率
            JavaPairRDD<Integer,Double> formSubmitInformation = getWriteTimes(actionGroupPairRDD);
            //获取每次表单提交内容
            JavaRDD<String> formContentRDD = actionSubmitRDD.map(new Function<Row, String>() {
                @Override
                public String call(Row v1) throws Exception { return v1.getString(8);
                }
            });




            //计算表单平均填写时长
            JavaRDD<Integer> writeTimes = formSubmitInformation.keys();
            Long count = writeTimes.count();
            Integer wholeTime = writeTimes.reduce(new Function2<Integer, Integer, Integer>() {
                @Override
                public Integer call(Integer v1, Integer v2) throws Exception {
                    return v1+v2;
                }
            }) ;
            Double wholeSuccessRate = formSubmitInformation.values().reduce(new Function2<Double, Double, Double>() {
                @Override
                public Double call(Double v1, Double v2) throws Exception { return v1+v2;
                }
            });
            Double averageWriteTime = (double)wholeTime/count;
            Double averageRate = wholeSuccessRate/count;
            String blankFieldInformation = getBlankFieldInformation(formContentRDD);
//
            formAnalysisDAO.insert(new FormAnalysis(null,formPageUrl,null,Integer.valueOf(visitNum.toString()),
                    Integer.valueOf(submitNum.toString()),averageRate,null,averageWriteTime,null,
                        blankFieldInformation,DateUtils.parseTime(DateUtils.getYesterdayDate() + " 00:00:00")));
        }






    }


    private static String getBlankFieldInformation(JavaRDD<String> values) {

        JavaPairRDD<Integer,Integer> blankRDD = values.flatMapToPair(new PairFlatMapFunction<String, Integer, Integer>() {
            @Override
            public Iterable<Tuple2<Integer, Integer>> call(String s) throws Exception {
                List<Tuple2<Integer,Integer>> list = new ArrayList<Tuple2<Integer, Integer>>();
                //把每个input的信息提取出来
                String[] inputs = s.split("\\|");

                for (int i=0;i<inputs.length;i++){
                    if (inputs[i].equals("")){
                        list.add(new Tuple2<Integer, Integer>(i+1,1));
                    }
                }
                return list;
            }
        });

        long s = blankRDD.count();

       JavaPairRDD<Integer,Integer> blankCountRDD = blankRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1+v2;
            }
        });

        long s1 = blankCountRDD.count();
        Map<Integer,Integer> map = blankCountRDD.collectAsMap();
        String blankFieldInformation = "";
        for (Integer integer:map.keySet()){
            blankFieldInformation = blankFieldInformation + "input" + integer + "=" + map.get(integer) + "|";
        }

        if (!blankFieldInformation.equals("")){
            blankFieldInformation = blankFieldInformation.substring(0,blankFieldInformation.length()-2);
        }
        return blankFieldInformation;
    }

    private static JavaPairRDD<Integer, Double> getWriteTimes(JavaPairRDD<String,Iterable<Row>> actionGroupPairRDD) {
        JavaPairRDD<Integer,Double> writeTimes = actionGroupPairRDD.flatMapToPair(new PairFlatMapFunction<Tuple2<String, Iterable<Row>>, Integer, Double>() {
            @Override
            public Iterable<Tuple2<Integer, Double>> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                // 定义返回list
                List<Tuple2<Integer,Double>> list = new ArrayList<Tuple2<Integer, Double>>();

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
                boolean flag = true;
                if (rows.size()>0){
                    flag = rows.get(0).getString(9)==null?true:false;
                }

                //开始浏览的时间
                String browseTime = null;
                //成功提交时间
                String submitTime = null;
                //提交次数
                int i = 0;
                //如果参数里没有是否提交成功
                if (flag){
                    for (Row row:rows){
                        //如果是浏览且两个时间都是空，就直接把浏览时间写好
                        if (row.getString(4).equals("browse")&&submitTime==null&&browseTime==null){
                            browseTime = row.getString(3);
                        }
                        //如果是浏览而两个时间都不是空，就计算填写时长，然后把浏览时间改成现在的，提交次数也改成0
                        if (row.getString(4).equals("browse")&&submitTime!=null){
                            Long writeTime = (DateUtils.parseTime(submitTime).getTime()-DateUtils.parseTime(browseTime).getTime())/1000;
                            double submitSuccessRate = (double)1/i;
                            list.add(new Tuple2<Integer, Double>(Integer.valueOf(writeTime.toString()),submitSuccessRate));
                            i = 0;
                            browseTime = row.getString(3);
                            submitTime = null;
                        }
                        //如果是表单提交，而且浏览时间不是空，就把提交时间写好，提交次数+1
                        if (row.getString(4).equals("form")&&browseTime != null){
                            submitTime = row.getString(3);
                            i++;
                        }
                    }
                    //最后一次提交
                    if (submitTime!=null){
                        Long writeTime = (DateUtils.parseTime(submitTime).getTime()-DateUtils.parseTime(browseTime).getTime())/1000;
                        double submitSuccessRate = (double)1/i;
                        list.add(new Tuple2<Integer, Double>(Integer.valueOf(writeTime.toString()),submitSuccessRate));
                    }
                }else {
                    //如果参数里有是否提交成功
                    for (Row row:rows){
                        //如果出现浏览事件且提交时间为空，浏览时间不为空的情况，
                        if (row.getString(4).equals("browse")&&submitTime==null&&browseTime!=null){
                            Long writeTime = (DateUtils.parseTime(row.getString(3)).getTime()-DateUtils.parseTime(browseTime).getTime())/1000;
                            double submitSuccessRate = 0;
                            list.add(new Tuple2<Integer, Double>(Integer.valueOf(writeTime.toString()),submitSuccessRate));
                            browseTime = row.getString(3);
                            i=0;
                        }

                        //如果是浏览且两个时间都是空，就直接把浏览时间写好
                        if (row.getString(4).equals("browse")&&submitTime==null&&browseTime!=null){
                            browseTime = row.getString(3);
                        }

                        //如果是表单提交成功，而且浏览时间不是空，就把提交时间写好，提交次数+1，计算提交时间以及成功率等
                        if (row.getString(4).equals("form")&&row.getString(4).equals("success")&&browseTime != null){
                            submitTime = row.getString(3);
                            i++;
                            Long writeTime = (DateUtils.parseTime(submitTime).getTime()-DateUtils.parseTime(browseTime).getTime())/1000;
                            double submitSuccessRate = (double)1/i;
                            list.add(new Tuple2<Integer, Double>(Integer.valueOf(writeTime.toString()),submitSuccessRate));
                            submitTime = null;
                            i = 0;
                            browseTime = null;
                        }

                        //如果表单提交失败，提交次数+1
                        if (row.getString(4).equals("form")&&row.getString(4).equals("fail")&&browseTime != null){
                            i++;
                        }
                    }
                    //最后，如果浏览时间不为空，而提交时间为空，且i不是0
                    if (browseTime!=null&&submitTime==null&&i!=0){
                        Long writeTime = (DateUtils.parseTime(rows.get(rows.size()-1).getString(3)).getTime()-DateUtils.parseTime(browseTime).getTime())/1000;
                        double submitSuccessRate = 0;
                        list.add(new Tuple2<Integer, Double>(Integer.valueOf(writeTime.toString()),submitSuccessRate));
                    }
                }
                return list;
            }
        });

        return writeTimes;
    }

    private static JavaRDD<Row> getActionSubmitRDD(SQLContext sqlContext, String formPageUrl) {
        String sql = "select * "
                + "from user_action "
                + "where date='" + DateUtils.getYesterdayDate() + "' "
                + "and url like '" + formPageUrl + "%' "
                + "and action= 'form'";
        DataFrame actionDF = sqlContext.sql(sql);
        return actionDF.javaRDD();
    }

    private static JavaRDD<Row> getBrowseActionRDD(SQLContext sqlContext, String formPageUrl) {
        String sql = "select * "
                + "from user_action "
                + "where date='" + DateUtils.getYesterdayDate() + "' "
                + "and url like '" + formPageUrl + "%' "
                + "and action= 'browse'";
        DataFrame actionDF = sqlContext.sql(sql);
        return actionDF.javaRDD();
    }

    private static JavaRDD<Row> getActionRDD(SQLContext sqlContext, String formPageUrl) {

        String sql = "select * "
                + "from user_action "
                + "where date='" + DateUtils.getYesterdayDate() + "' "
                + "and url like '" + formPageUrl + "%' ";
        DataFrame actionDF = sqlContext.sql(sql);
//        System.out.println(actionDF.count());
//        for (Row row : actionDF.take(20)){
//            System.out.println(row);
//        }
        return actionDF.javaRDD();

    }

    private static String ListToString(List<String> formPageUrls) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String s:formPageUrls){
            stringBuffer.append(s + "|");
        }
        return stringBuffer.toString();
    }

}
