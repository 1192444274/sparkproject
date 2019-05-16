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

import java.util.*;

public class MyMockData1 {

    public static void mock(JavaSparkContext sc,
                            SQLContext sqlContext) {
        List<Row> rows = new ArrayList<Row>();

        String[] searchKeywords = new String[] {"国资驰援", "消费电子", "恒生电子", "独角兽",
                "富士康", "数字中国", "新能源", "边缘计算", "白马股", "新零售"};
        String date = DateUtils.getYesterdayDate();
        String[] parameters1 = new String[]{"1|2|3|5","1||2|","1||1|1"};
        String[] parameters2 = new String[]{"success","fail"};
        String[] actions = new String[]{"browse","form"};
        String[] versions = new String[]{"2.1.0", "2.1.1", "3.0.1", "3.0.2"};
        String[] resolutions = new String[]{"1920*1080", "1680*1050", "1600*900", "1440*900"};
        String[] platforms = new String[]{"windows", "linux", "android", "iphone"};
        String[] userAgents = new String[]{"Chrome/67.0.3396.99", "Firefox/61.0", "Edge/17.17134", "Tablet PC 2.0"};
        String[] languages = new String[]{"English", "Chinese", "Japanese", "French"};
        String[] websiteUrls = new String[]{"www.9696.com","www.4399.com"};
        String[] pageUrl = new String[]{"/wenjuan","/special/2458.html","/special/2453.html"};
        String[] eventNames = new String[]{"buy","submit-order","register","install","submit-ppq"};
        Random random = new Random();
        //假设100个用户，每个用户会打开10个session，每个session里会有若干个事件，且给个session对应一个浏览事件
        for(int i = 0; i < 100; i++) {
            //埋点传过来的uuid应该是个随机字符串，为了方便我把uuid作为一个属性，userid作为主键好了
            long userid = random.nextInt(100);

            for(int j = 0; j < 10; j++) {
                String sessionid = UUID.randomUUID().toString().replace("-", "");
                String baseActionTime = date + " " + random.nextInt(23);

                Long clickCategoryId = null;
                String referrerUrl = null;
                String version = versions[random.nextInt(4)];
                String sdk = "js";
                String platform = platforms[random.nextInt(4)];
                String ip = String.valueOf(random.nextInt(92)+100) + "." + String.valueOf(random.nextInt(92)+100) +
                        "." + String.valueOf(random.nextInt(92)+100) + "." + String.valueOf(random.nextInt(92)+100);
                String resolution = resolutions[random.nextInt(4)];
                String userAgent = userAgents[random.nextInt(4)];
                String language = languages[random.nextInt(4)];
                String websiteUrl = websiteUrls[random.nextInt(2)];
                for(int k = 0; k < random.nextInt(100); k++) {
                    //一个session对应一个网站的多个网页
                    String Url = websiteUrl + pageUrl[random.nextInt(3)];
                    String eventName = eventNames[random.nextInt(5)];
                    String actionTime = baseActionTime + ":" + StringUtils.fulfuill(String.valueOf(random.nextInt(59))) + ":" + StringUtils.fulfuill(String.valueOf(random.nextInt(59)));
                    String searchKeyword = null;
                    String parameter1 = null;
                    String parameter2 = null;
                    String parameter3 = null;
                    String parameter4 = null;

                    String action = actions[random.nextInt(2)];
                    if("form".equals(action)) {
                        parameter1 = parameters1[random.nextInt(3)];
                        parameter2 = parameters2[random.nextInt(2)];
                    } else if("browse".equals(action)) {
                        referrerUrl = getRandomDomain();
                    }

                    Row row = RowFactory.create(date, userid, sessionid, actionTime,action,
                            Url, eventName, searchKeyword,
                            parameter1, parameter2,
                            parameter3, parameter4,
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
                DataTypes.createStructField("version", DataTypes.StringType, true),
                DataTypes.createStructField("sdk", DataTypes.StringType, true),
                DataTypes.createStructField("platform", DataTypes.StringType, true),
                DataTypes.createStructField("ip", DataTypes.StringType, true),
                DataTypes.createStructField("resolution", DataTypes.StringType, true),
                DataTypes.createStructField("userAgent", DataTypes.StringType, true),
                DataTypes.createStructField("language", DataTypes.StringType, true),
                DataTypes.createStructField("referrerUrl", DataTypes.StringType, true)));

        DataFrame df = sqlContext.createDataFrame(rowsRDD, schema);

        df.registerTempTable("user_action");

        for(Row _row : df.take(50)) {
			System.out.println(_row);
        }

    }

    public static String getRandomDomain(){
        String[] strs = new String[]{"www.baidu.com/hehe?opwq=wjk","www.baidu.com/hehe?opwq=wjk","www.baidu.com/hqwe?oprwqwq=wwek","www.baidu.com/he123e?opwq=w11k","www.baidu.com/hehyye?wq=wjk",
                "www.google.com/oaps","www.google.com/oaps","www.google.com/oaps","www.google.com/oaps","www.google.com/oaps",
                "hao.360.cn","www.skdaj.qwe","www.saod.qq"};
        Random random = new Random();
        return strs[random.nextInt(13)];
    }
}
