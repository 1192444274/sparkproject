package com.fjs.sparkproject.test;

import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.util.SparkUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.mllib.util.MLUtils;

public class mllib {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("MLlib");
        SparkUtils.setMaster(conf);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());
        //如果在本地条件下运行，就要生成测试数据
        SparkUtils.mockData(sc,sqlContext);

        LogisticRegressionWithLBFGS logisticRegressionModel = new LogisticRegressionWithLBFGS().setNumClasses(10);

    }


}
