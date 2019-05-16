package com.fjs.sparkproject.spark.heat;

import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.PageAttentionHeatDAO;
import com.fjs.sparkproject.dao.PageBrowseHeatDAO;
import com.fjs.sparkproject.dao.PageClickHeatDAO;
import com.fjs.sparkproject.dao.PageMessageDAO;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.PageAttentionHeat;
import com.fjs.sparkproject.domain.PageBrowseHeat;
import com.fjs.sparkproject.domain.PageClickHeat;
import com.fjs.sparkproject.domain.PageMessage;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HeatChartAnalysis {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_SESSION1);
        SparkUtils.setMaster(conf);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());
        //如果在本地条件下运行，就要生成测试数据
        SparkUtils.mockData(sc,sqlContext);

        PageMessageDAO pageMessageDAO = DAOFactory.getPageMessageDAO();

        List<PageMessage> pageMessages = pageMessageDAO.findAllPageMessages();

        for (PageMessage pageMessage:pageMessages){
            //1.点击热图
            //获取所有的点击位置
            JavaRDD<Row> clickCoordinateRDD = getClickCoordinateRDD(sqlContext,pageMessage.getUrl());

            //把RDD写成<坐标，1>格式的
            JavaPairRDD<String,Integer> coordinateRDD = getCoordinateRDD(clickCoordinateRDD);

            //统计每个点的点击次数
            calculateClickHeat(coordinateRDD,pageMessage.getPageId());

            //2.浏览热图
            //获取所有的鼠标移动位置
            JavaRDD<Row> moveCoordinateRDD = getMoveCoordinateRDD(sqlContext,pageMessage.getUrl());

            //对row中坐标数据进行解析，根据|分割来获取所有的坐标点，在写成<坐标，1>格式
            JavaPairRDD<String,Integer> coordinateRDD1 = getCoordinateRDD1(moveCoordinateRDD);

            //统计每个点的浏览次数
            calculateBrowseHeat(coordinateRDD1,pageMessage.getPageId());
            //3.注意力热图
            //对row中坐标数据进行解析，根据|分割来获取所有的坐标点，假设每一次的数据占1秒，那么计算平均每个点占的时间就可以计算出来
            //最后写成<坐标，时间>的形式
            JavaPairRDD<String,Double> coordinateRDD2 = getCoordinateRDD2(moveCoordinateRDD);

            //统计每个点的浏览时长
            calculateAttentionHeat(coordinateRDD2,pageMessage.getPageId());

        }

    }

    private static void calculateAttentionHeat(JavaPairRDD<String, Double> coordinateRDD1,final Integer pageId) {
        JavaPairRDD<String,Double> coordinateCountRDD = coordinateRDD1.reduceByKey(new Function2<Double, Double, Double>() {
            @Override
            public Double call(Double aDouble, Double aDouble2) throws Exception {
                return aDouble + aDouble2;
            }
        });

        coordinateCountRDD.foreach(new VoidFunction<Tuple2<String, Double>>() {
            @Override
            public void call(Tuple2<String, Double> tuple) throws Exception {
                PageAttentionHeatDAO pageAttentionHeatDAO = DAOFactory.getPageAttentionHeatDAO();
                String coordinate = tuple._1;
                Integer X = Integer.valueOf(coordinate.split(",")[0]);
                Integer Y = Integer.valueOf(coordinate.split(",")[1]);
                Double time = tuple._2;
                pageAttentionHeatDAO.insert(new PageAttentionHeat(null,pageId,X,Y,time,new Date()));
            }
        });
    }

    private static JavaPairRDD<String,Double> getCoordinateRDD2(JavaRDD<Row> moveCoordinateRDD) {
        return moveCoordinateRDD.flatMapToPair(new PairFlatMapFunction<Row, String, Double>() {
            @Override
            public Iterable<Tuple2<String, Double>> call(Row row) throws Exception {

                List<Tuple2<String,Double>> list = new ArrayList<Tuple2<String, Double>>();

                String coordinates = row.getString(3);

                String[] coordinate = coordinates.split("\\|");

                Double time = (double)1/coordinate.length;

                for (int i=0;i<coordinate.length;i++){
                    list.add(new Tuple2<String, Double>(coordinate[i],time));
                }

                return list;
            }
        });

    }

    private static void calculateBrowseHeat(JavaPairRDD<String,Integer> coordinateRDD1,final Integer pageId) {
        JavaPairRDD<String,Integer> coordinateCountRDD = coordinateRDD1.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        coordinateCountRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> tuple) throws Exception {
                PageBrowseHeatDAO pageBrowseHeatDAO = DAOFactory.getPageBrowseHeatDAO();
                String coordinate = tuple._1;
                Integer X = Integer.valueOf(coordinate.split(",")[0]);
                Integer Y = Integer.valueOf(coordinate.split(",")[1]);
                Integer num = tuple._2;
                pageBrowseHeatDAO.insert(new PageBrowseHeat(null,pageId,X,Y,num,new Date()));
            }
        });

    }

    private static JavaPairRDD<String,Integer> getCoordinateRDD1(JavaRDD<Row> moveCoordinateRDD) {

        return moveCoordinateRDD.flatMapToPair(new PairFlatMapFunction<Row, String, Integer>() {
            @Override
            public Iterable<Tuple2<String, Integer>> call(Row row) throws Exception {
                List<Tuple2<String,Integer>> list = new ArrayList<Tuple2<String, Integer>>();
                String coordinates = row.getString(3);

                String[] coordinate = coordinates.split("\\|");

                for (int i=0;i<coordinate.length;i++){
                    list.add(new Tuple2<String, Integer>(coordinate[i],1));
                }

                return list;
            }
        });
    }

    private static JavaRDD<Row> getMoveCoordinateRDD(SQLContext sqlContext, String url) {

        String sql = "select * "
                + "from click_coordinate "
                + "where url like '" + url + "%'";
        DataFrame actionDF = sqlContext.sql(sql);
        return actionDF.javaRDD();

    }

    private static void calculateClickHeat(JavaPairRDD<String, Integer> coordinateRDD, final Integer pageId) {
        JavaPairRDD<String,Integer> coordinateCountRDD = coordinateRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        coordinateCountRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> tuple) throws Exception {
                PageClickHeatDAO pageClickHeatDAO = DAOFactory.getPageClickHeatDAO();
                String coordinate = tuple._1;
                Integer X = Integer.valueOf(coordinate.split(",")[0]);
                Integer Y = Integer.valueOf(coordinate.split(",")[1]);
                Integer click_num = tuple._2;
                pageClickHeatDAO.insert(new PageClickHeat(null,pageId,X,Y,click_num,new Date()));
            }
        });

    }

    private static JavaPairRDD<String,Integer> getCoordinateRDD(JavaRDD<Row> clickCoordinateRDD) {

        return clickCoordinateRDD.mapToPair(new PairFunction<Row, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Row row) throws Exception {
                return new Tuple2<String, Integer>(row.getString(3),1);
            }
        });

    }

    private static JavaRDD<Row> getClickCoordinateRDD(SQLContext sqlContext, String url) {
        String sql = "select * "
                + "from click_coordinate "
                + "where  url like '" + url + "%'";
        DataFrame actionDF = sqlContext.sql(sql);
        return actionDF.javaRDD();
    }

}
