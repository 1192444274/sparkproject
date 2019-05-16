package com.fjs.sparkproject.spark.page;

import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.EventMessageDAO;
import com.fjs.sparkproject.dao.PageMessageDAO;
import com.fjs.sparkproject.dao.WebsiteMessageDAO;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.EventMessage;
import com.fjs.sparkproject.domain.PageMessage;
import com.fjs.sparkproject.domain.WebsiteMessage;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.SparkUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class PageStatistics {


    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_SESSION1);
        SparkUtils.setMaster(conf);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());
        //如果在本地条件下运行，就要生成测试数据
        SparkUtils.mockData(sc,sqlContext);

        //1.获取页面的浏览信息
        JavaRDD<Row> actionRDD = getActionRDDByDateRange(sqlContext);
        //2.url可能是 网址?信息 形式的，应该要去掉?后面的再去重
        JavaRDD<String> pageUrlRDD = getPageUrlRDD(actionRDD);
        //2.对返回来的页面进行操作，先判断是属于哪一个网站的，再判断是否记录在案
        statisticPage(pageUrlRDD);

    }

    private static JavaRDD<String> getPageUrlRDD(JavaRDD<Row> actionRDD) {
        JavaRDD<String> pageUrlRDD = actionRDD.map(new Function<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                String url = row.getString(0);
                return url.split("\\?")[0];
            }
        });
        return pageUrlRDD.distinct();
    }

    private static void statisticPage(JavaRDD<String> pageUrlRDD) {
        pageUrlRDD.count();
        pageUrlRDD.foreach(new VoidFunction<String>() {
            @Override
            public void call(String s) throws Exception {
                PageMessageDAO pageMessageDAO = DAOFactory.getPageMessageDAO();
                WebsiteMessageDAO websiteMessageDAO = DAOFactory.getWebsiteMessageDAO();
                EventMessageDAO eventMessageDAO = DAOFactory.getEventMessageDAO();
                //获取url
                String url = s;
                //根据url获取它属于哪个网站
                WebsiteMessage websiteMessage = websiteMessageDAO.findByUrl(s);
                //如果没有归属网站，就直接不处理
                if (websiteMessage.getDomain()==null){
                    return;
                }
                //查看当前页面是否存在，若存在，则结束，若不存在,则加到页面表里去,同时添加页面的浏览事件
               PageMessage pageMessage = pageMessageDAO.findByWebsiteIdAndUrl(websiteMessage.getWebsiteId(),url);
                if (pageMessage.getUrl()==null){
                    pageMessageDAO.insertPageMessage(new PageMessage(websiteMessage.getWebsiteId(),url));
                    eventMessageDAO.insert(new EventMessage(null,websiteMessage.getWebsiteId(),url,"browse_" + url,"browse",null));
                }
            }
        });
        pageUrlRDD.count();
    }

    private static JavaRDD<Row> getActionRDDByDateRange(SQLContext sqlContext) {
        String sql =
                "select url "
                        + "from user_action "
                        + "where date='" + DateUtils.getYesterdayDate() + "' "
                        + "and action = 'browse'";

        DataFrame actionDF = sqlContext.sql(sql);
        System.out.println(actionDF.count());
        for (Row row : actionDF.take(20)){
            System.out.println(row);
        }
        return actionDF.javaRDD().distinct();
    }

}
