package com.fjs.sparkproject.test;

import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import scala.util.parsing.combinator.testing.Str;

import java.util.*;

public class MyMockData {
    public static void mock(JavaSparkContext sc,
                            SQLContext sqlContext) {
        List<Row> rows = new ArrayList<Row>();

        String[] searchKeywords = new String[] {"国资驰援", "消费电子", "恒生电子", "独角兽",
                "富士康", "数字中国", "新能源", "边缘计算", "白马股", "新零售"};
        String date = DateUtils.getYesterdayDate();
        String[] actions = new String[]{"search", "click", "pay", "browse"};
        String[] versions = new String[]{"2.1.0", "2.1.1", "3.0.1", "3.0.2"};
        String[] resolutions = new String[]{"1920*1080", "1680*1050", "1600*900", "1440*900"};
        String[] platforms = new String[]{"windows", "linux", "android", "iphone"};
        String[] userAgents = new String[]{"Chrome/67.0.3396.99", "Firefox/61.0", "Edge/17.17134", "Tablet PC 2.0"};
        String[] languages = new String[]{"English", "Chinese", "Japanese", "French"};
        String[] websiteUrls = new String[]{"www.7k7k.com","www.4399.com","www.18183.com"};
        String[] pageUrl = new String[]{"/flash/2456.html","/special/2458.html","/special/2453.html","/flash/2444.html","/flash/2321.html"};
        String[] eventNames = new String[]{"buy","submit-order","register","install","submit-ppq"};
        String[] params1 = new String[]{"产品服务","解决方案","服务市场"};
        String[] params2 = new String[]{"涨停分析","行情策略","营销解决方案","智能资讯解决方案","项目服务"};
        String[] params3 = new String[]{"账户I","账户II","账户III"};
        Random random = new Random();
        //假设100个用户，每个用户会打开10个session，每个session里会有若干个事件，且给个session对应一个浏览事件
        for(int i = 0; i < 100; i++) {
            //埋点传过来的uuid应该是个随机字符串，为了方便我把uuid作为一个属性，userid作为主键好了
            long userid = random.nextInt(100);

            for(int j = 0; j < 10; j++) {
                String sessionid = UUID.randomUUID().toString().replace("-", "");
                String baseActionTime = date + " " + random.nextInt(23);


                String referrerUrl = null;
                String version = versions[random.nextInt(4)];
                String sdk = "js";
                String platform = platforms[random.nextInt(4)];
                String ip = String.valueOf(random.nextInt(92)+100) + "." + String.valueOf(random.nextInt(92)+100) +
                        "." + String.valueOf(random.nextInt(92)+100) + "." + String.valueOf(random.nextInt(92)+100);
                String resolution = resolutions[random.nextInt(4)];
                String userAgent = userAgents[random.nextInt(4)];
                String language = languages[random.nextInt(4)];
                String websiteUrl = websiteUrls[random.nextInt(3)];
                for(int k = 0; k < random.nextInt(100); k++) {
                    //一个session对应一个网站的多个网页
                    String Url = websiteUrl + pageUrl[random.nextInt(5)];
                    String eventName = eventNames[random.nextInt(5)];
                    String actionTime = baseActionTime + ":" + StringUtils.fulfuill(String.valueOf(random.nextInt(59))) + ":" + StringUtils.fulfuill(String.valueOf(random.nextInt(59)));
                    String searchKeyword = null;
                    String clickCategoryId = null;
                    String clickProductId = null;
                    String payCategoryIds = null;
                    String payProductIds = null;
                    String action = actions[random.nextInt(4)];
                    if (eventName.equals("buy")){
                        action = "pay";
                    }
                    if("search".equals(action)) {
                        searchKeyword = searchKeywords[random.nextInt(10)];
                    } else if("click".equals(action)) {
                        clickCategoryId = params3[random.nextInt(3)];
                    } else if("pay".equals(action)) {
                        clickCategoryId = params1[random.nextInt(3)];
                        clickProductId = params2[random.nextInt(5)];
                        payCategoryIds = String.valueOf(random.nextInt(100));
                        payProductIds = String.valueOf(random.nextInt(10));
                    }else if("browse".equals(action)) {
                        referrerUrl = getRandomDomain();
                    }

                    Row row = RowFactory.create(date, userid, sessionid, actionTime,action,
                            Url, eventName, searchKeyword,
                            clickCategoryId, clickProductId,
                            payCategoryIds, payProductIds,
                            version,sdk,platform,ip,resolution,userAgent,language,referrerUrl
                            );
                    rows.add(row);
                }
            }
        }

        JavaRDD<Row> rowsRDD = sc.parallelize(rows);

        StructType schema = DataTypes.createStructType(Arrays.asList(
                        DataTypes.createStructField("date", DataTypes.StringType, true),
                        DataTypes.createStructField("user_id", DataTypes.LongType, true),
                        DataTypes.createStructField("session_id", DataTypes.StringType, true),
                        DataTypes.createStructField("action_time", DataTypes.StringType, true),
                        DataTypes.createStructField("action", DataTypes.StringType, true),
                        DataTypes.createStructField("url", DataTypes.StringType, true),
                        DataTypes.createStructField("event_method", DataTypes.StringType, true),
                        DataTypes.createStructField("search_keyword", DataTypes.StringType, true),
                        DataTypes.createStructField("parameter1", DataTypes.StringType, true),
                        DataTypes.createStructField("parameter2", DataTypes.StringType, true),
                        DataTypes.createStructField("parameter3", DataTypes.StringType, true),
                        DataTypes.createStructField("parameter4", DataTypes.StringType, true),
                        DataTypes.createStructField("model", DataTypes.StringType, true),
                        DataTypes.createStructField("sdk", DataTypes.StringType, true),
                        DataTypes.createStructField("platform", DataTypes.StringType, true),
                        DataTypes.createStructField("ip", DataTypes.StringType, true),
                        DataTypes.createStructField("resolution", DataTypes.StringType, true),
                        DataTypes.createStructField("userAgent", DataTypes.StringType, true),
                        DataTypes.createStructField("language", DataTypes.StringType, true),
                        DataTypes.createStructField("referUrl", DataTypes.StringType, true)));

        DataFrame df = sqlContext.createDataFrame(rowsRDD, schema);

        df.registerTempTable("user_action");

        for(Row _row : df.take(50)) {
			System.out.println(_row);
        }


        /**
         * ==================================================================
         */

        rows.clear();
        String[] citys = new String[]{"浙江", "上海", "海南", "江西"};
        String[] sexes = new String[]{"male", "female"};
        for(int i = 0; i < 100; i ++) {
            long userid = i;
            String username = "user" + i;
            String name = "name" + i;
            int age = random.nextInt(60);
            String professional = "professional" + random.nextInt(100);
            String city = citys[random.nextInt(4)];
            String sex = sexes[random.nextInt(2)];
            String registration_time = random.nextInt(2)==0?DateUtils.getTodayDate():DateUtils.getYesterdayDate();
            String platform = platforms[random.nextInt(4)];
            String websiteUrl = websiteUrls[random.nextInt(3)];
            Row row = RowFactory.create(userid, username, name, age,
                    professional, city, sex,registration_time,platform,websiteUrl);
            rows.add(row);
        }

        rowsRDD = sc.parallelize(rows);

        StructType schema2 = DataTypes.createStructType(Arrays.asList(
                DataTypes.createStructField("user_id", DataTypes.LongType, true),
                DataTypes.createStructField("username", DataTypes.StringType, true),
                DataTypes.createStructField("name", DataTypes.StringType, true),
                DataTypes.createStructField("age", DataTypes.IntegerType, true),
                DataTypes.createStructField("professional", DataTypes.StringType, true),
                DataTypes.createStructField("city", DataTypes.StringType, true),
                DataTypes.createStructField("sex", DataTypes.StringType, true),
                DataTypes.createStructField("registration_time",DataTypes.StringType,true),
                DataTypes.createStructField("platform", DataTypes.StringType, true),
                DataTypes.createStructField("website_url", DataTypes.StringType, true)));

        DataFrame df2 = sqlContext.createDataFrame(rowsRDD, schema2);
        for(Row _row : df2.take(1)) {
//			System.out.println(_row);
        }

        df2.registerTempTable("user_info");

        /**
         * ==================================================================
         */
        rows.clear();

        int[] productStatus = new int[]{0, 1};

        for(int i = 0; i < 100; i ++) {
            long productId = i;
            String productName = "product" + i;
            String extendInfo = "{\"product_status\": " + productStatus[random.nextInt(2)] + "}";

            Row row = RowFactory.create(productId, productName, extendInfo);
            rows.add(row);
        }

        rowsRDD = sc.parallelize(rows);

        StructType schema3 = DataTypes.createStructType(Arrays.asList(
                DataTypes.createStructField("product_id", DataTypes.LongType, true),
                DataTypes.createStructField("product_name", DataTypes.StringType, true),
                DataTypes.createStructField("extend_info", DataTypes.StringType, true)));

        DataFrame df3 = sqlContext.createDataFrame(rowsRDD, schema3);
        for(Row _row : df3.take(1)) {
//			System.out.println(_row);
        }

        df3.registerTempTable("product_info");

        /**
         * ==================================================================点击热图数据
         */
        rows.clear();
        for (int i=0;i<=1000000;i++) {
            String coordinate = random.nextInt(100) + "," + random.nextInt(100);
            String url = websiteUrls[random.nextInt(3)] + pageUrl[random.nextInt(5)];
            String actionTime = date + " " + random.nextInt(23) + ":" + StringUtils.fulfuill(String.valueOf(random.nextInt(59))) + ":" + StringUtils.fulfuill(String.valueOf(random.nextInt(59)));

            Row row = RowFactory.create(date, url, actionTime, coordinate);
            rows.add(row);
        }
            rowsRDD = sc.parallelize(rows);

            StructType schema4 = DataTypes.createStructType(Arrays.asList(
                    DataTypes.createStructField("date", DataTypes.StringType, true),
                    DataTypes.createStructField("url", DataTypes.StringType, true),
                    DataTypes.createStructField("action_time", DataTypes.StringType, true),
                    DataTypes.createStructField("coordinate", DataTypes.StringType, true)));

            DataFrame df4 = sqlContext.createDataFrame(rowsRDD, schema4);

            for(Row _row : df4.take(100)) {
//    			System.out.println(_row);
            }

            df4.registerTempTable("click_coordinate");

        /**
         * ==================================================================浏览热图，注意力热图数据
         */
        rows.clear();
        for (int i=0;i<=100000;i++) {
            String coordinate = random.nextInt(100) + "," + random.nextInt(100);
            for (int j = 0;j<=random.nextInt(20);j++){
                coordinate = coordinate + "|" + random.nextInt(100) + "," + random.nextInt(100);
            }
            String url = websiteUrls[random.nextInt(3)] + pageUrl[random.nextInt(5)];
            String actionTime = date + " " + random.nextInt(23) + ":" + StringUtils.fulfuill(String.valueOf(random.nextInt(59))) + ":" + StringUtils.fulfuill(String.valueOf(random.nextInt(59)));

            Row row = RowFactory.create(date, url, actionTime, coordinate);
            rows.add(row);
        }
        rowsRDD = sc.parallelize(rows);

        StructType schema5 = DataTypes.createStructType(Arrays.asList(
                DataTypes.createStructField("date", DataTypes.StringType, true),
                DataTypes.createStructField("url", DataTypes.StringType, true),
                DataTypes.createStructField("action_time", DataTypes.StringType, true),
                DataTypes.createStructField("coordinate", DataTypes.StringType, true)));

        DataFrame df5 = sqlContext.createDataFrame(rowsRDD, schema5);

            for(Row _row : df4.take(100)) {
    			System.out.println(_row);
            }

        df5.registerTempTable("move_coordinate");
    }

    public static String getRandomDomain(){
        String[] strs = new String[]{"www.baidu.com/hehe?opwq=wjk","www.baidu.com/hehe?opwq=wjk","www.baidu.com/hqwe?oprwqwq=wwek","www.baidu.com/he123e?opwq=w11k","www.baidu.com/hehyye?wq=wjk",
                                        "www.google.com/oaps","www.google.com/oaps","www.google.com/oaps","www.google.com/oaps","www.google.com/oaps",
                                            "hao.360.cn","www.skdaj.qwe","www.saod.qq"};
        Random random = new Random();
        return strs[random.nextInt(13)];
    }

}
