package com.fjs.sparkproject.spark.session;

import com.alibaba.fastjson.JSONObject;
import com.fjs.sparkproject.constant.Constants;
import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.domain.*;
import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.*;
import com.fjs.sparkproject.util.ParamUtils;
import com.fjs.sparkproject.util.SparkUtils;
import com.fjs.sparkproject.util.StringUtils;
import com.fjs.sparkproject.util.ValidUtils;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.Date;
import java.util.List;

public class UserAttrAnalysis {
    public static void main(String[] args) {
        args = new String[]{"1"};
        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_SESSION1);
        SparkUtils.setMaster(conf);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());
        //如果在本地条件下运行，就要生成测试数据
        SparkUtils.mockData(sc,sqlContext);

        ITaskDAO taskDAO = DAOFactory.getTaskDAO();
        WebsiteMessageDAO websiteMessageDAO = DAOFactory.getWebsiteMessageDAO();
        long taskid = ParamUtils.getTaskIdFromArgs(args);
        Task task = taskDAO.findById(taskid);
        JSONObject taskParam = JSONObject.parseObject(task.getTaskParam());
        List<WebsiteMessage> websiteMessages = websiteMessageDAO.findAllWebsite();
        //对每个网站进行用户属性计算
        for (WebsiteMessage websiteMessage:websiteMessages){
            int pageid = websiteMessage.getWebsiteId();
            String domain = websiteMessage.getDomain();
            //获取到对应的页面的浏览信息
            JavaRDD<Row> actionRDD = getActionRDDByDateRange(sqlContext, taskParam,domain);
            //获取到浏览信息对应的用户，在获取对应的用户信息
            JavaPairRDD<Long,String> userInformation = getUserInformation(sqlContext,actionRDD);
            //对用户信息进行筛选，把数据存入数据库
            Accumulator<String> sessionAggrStatAccumulator = sc.accumulator(
                    "", new SessionAggrStatAccumulator());
            //filterSessionAndAggrStat这个函数要在返回的参数filteredSessionid2AggrInfoRDD会被使用的条件下才会执行。
            JavaPairRDD<Long, String> filteredUserInformation = filterSessionAndAggrStat(userInformation, taskParam, sessionAggrStatAccumulator);
            filteredUserInformation.count();
            PersistAggrStat(sessionAggrStatAccumulator.value(),taskid,pageid);
            System.out.println(sessionAggrStatAccumulator.value());
            //职业，城市，兴趣
            List<Tuple2<Long, String>> professionalList =
                    getProfessionalList(taskid,filteredUserInformation,pageid);

            List<Tuple2<Long,String>> cityList = getCityList(taskid,filteredUserInformation,pageid);
        }
    }

    private static List<Tuple2<Long,String>> getCityList(long taskid, JavaPairRDD<Long,String> filteredUserInformation, int pageid) {
        JavaPairRDD<String,Long> cityCountRDD = getCityCountRDD(filteredUserInformation);

        JavaPairRDD<Long,String> reCityCountRDD = cityCountRDD.mapToPair(new PairFunction<Tuple2<String, Long>, Long, String>() {
            @Override
            public Tuple2<Long, String> call(Tuple2<String, Long> tuple2) throws Exception {
                return new Tuple2<Long, String>(tuple2._2,tuple2._1);
            }
        });
        JavaPairRDD<Long,String> sortReCityCountRDD = reCityCountRDD.sortByKey(false);
        List<Tuple2<Long,String>> top10Citys = sortReCityCountRDD.take(10);

        Top10City top10City;
        Top10CityDAO top10CityDAO = DAOFactory.getTop10CityDAO();
        for (Tuple2<Long,String> tuple:top10Citys){
            top10City = new Top10City(pageid,tuple._2,Integer.parseInt(tuple._1.toString()),new Date());
            top10CityDAO.insert(top10City);

        }
        return top10Citys;
    }

    private static JavaPairRDD<String,Long> getCityCountRDD(JavaPairRDD<Long,String> filteredUserInformation) {
        JavaPairRDD<Long,String> cityRDD = filteredUserInformation.filter(new Function<Tuple2<Long, String>, Boolean>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Boolean call(Tuple2<Long, String> longStringTuple2) throws Exception {
                //因为模拟数据肯定有profession，而且前面做的时候我也没有考虑到professional为空的情况，所以这里就知识象征性过滤一下
                return true;
            }
        });

        JavaPairRDD<String,Long> cityFirstCountRDD = cityRDD.mapToPair(new PairFunction<Tuple2<Long, String>, String, Long>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Tuple2<String, Long> call(Tuple2<Long, String> tuple) throws Exception {
                String city = StringUtils.getFieldFromConcatString(tuple._2,"\\|",Constants.CITY);
                return new Tuple2<String, Long>(city,1L);
            }
        });
        JavaPairRDD<String,Long> cityCountRDD = cityFirstCountRDD.reduceByKey(new Function2<Long, Long, Long>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Long call(Long aLong, Long aLong2) throws Exception {
                return aLong + aLong2;
            }
        });
        return cityCountRDD;
    }

    private static List<Tuple2<Long,String>> getProfessionalList(long taskid, JavaPairRDD<Long,String> filteredUserInformation,int pageid) {

//        JavaPairRDD<String,String> professionalRDD = filteredUserInformation.mapToPair(new PairFunction<Tuple2<Long, String>, String, String>() {
//            @Override
//            public Tuple2<String, String> call(Tuple2<Long, String> tuple) throws Exception {
//                String professional = StringUtils.getFieldFromConcatString(tuple._2,"\\|",Constants.PROFESSIONAL);
//                return new Tuple2<String, String>(professional,professional);
//            }
//        });
//        //去重，第一步主要是为了找出有哪些职业
//        professionalRDD = professionalRDD.distinct();

        JavaPairRDD<String,Long> professionalCountRDD = getProfessionalCountRDD(filteredUserInformation);

        JavaPairRDD<Long,String> reProfessionalCountRDD = professionalCountRDD.mapToPair(new PairFunction<Tuple2<String, Long>, Long, String>() {
            @Override
            public Tuple2<Long, String> call(Tuple2<String, Long> tuple2) throws Exception {
                return new Tuple2<Long, String>(tuple2._2,tuple2._1);
            }
        });
        JavaPairRDD<Long,String> sortReProfessionalCountRDD = reProfessionalCountRDD.sortByKey(false);
        List<Tuple2<Long,String>> top10Professions = sortReProfessionalCountRDD.take(10);
        Top10Porfession top10Porfession;
        Top10PorfessionDAO top10PorfessionDAO = DAOFactory.getTop10PorfessionDAO();
        for (Tuple2<Long,String> tuple:top10Professions){
            top10Porfession = new Top10Porfession(pageid,tuple._2,Integer.parseInt(tuple._1.toString()),new Date());
            top10PorfessionDAO.insert(top10Porfession);

        }

        return top10Professions;
    }

    private static JavaPairRDD<String,Long> getProfessionalCountRDD(JavaPairRDD<Long,String> filteredUserInformation) {
        JavaPairRDD<Long,String> professionalRDD = filteredUserInformation.filter(new Function<Tuple2<Long, String>, Boolean>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Boolean call(Tuple2<Long, String> longStringTuple2) throws Exception {
                //因为模拟数据肯定有profession，而且前面做的时候我也没有考虑到professional为空的情况，所以这里就知识象征性过滤一下
                return true;
            }
        });
        JavaPairRDD<String,Long> professionalFirstCountRDD = professionalRDD.mapToPair(new PairFunction<Tuple2<Long, String>, String, Long>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Tuple2<String, Long> call(Tuple2<Long, String> tuple) throws Exception {
                String professional = StringUtils.getFieldFromConcatString(tuple._2,"\\|",Constants.PROFESSIONAL);
                return new Tuple2<String, Long>(professional,1L);
            }
        });
        JavaPairRDD<String,Long> professionalCountRDD = professionalFirstCountRDD.reduceByKey(new Function2<Long, Long, Long>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Long call(Long aLong, Long aLong2) throws Exception {
                return aLong + aLong2;
            }
        });
        return professionalCountRDD;
    }

    private static void PersistAggrStat(String value, long taskid,int pageid) {
        long session_count = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.SESSION_COUNT));
        int maleNum = Integer.valueOf(StringUtils.getFieldFromConcatString(value,"\\|",Constants.SEX_MALE));
        int femaleNum = Integer.valueOf(StringUtils.getFieldFromConcatString(value,"\\|",Constants.SEX_FEMALE));
        int user_age_0_17 = Integer.valueOf(StringUtils.getFieldFromConcatString(value,"\\|",Constants.AGE_PERIOD_0_17));
        int user_age_18_24 = Integer.valueOf(StringUtils.getFieldFromConcatString(value,"\\|",Constants.AGE_PERIOD_18_24));
        int user_age_25_34 = Integer.valueOf(StringUtils.getFieldFromConcatString(value,"\\|",Constants.AGE_PERIOD_25_34));
        int user_age_35_44 = Integer.valueOf(StringUtils.getFieldFromConcatString(value,"\\|",Constants.AGE_PERIOD_35_44));
        int user_age_45_54 = Integer.valueOf(StringUtils.getFieldFromConcatString(value,"\\|",Constants.AGE_PERIOD_45_54));
        int user_age_55_100 = Integer.valueOf(StringUtils.getFieldFromConcatString(value,"\\|",Constants.AGE_PERIOD_55_100));
        Date date = new Date();
        UserAttrSex maleUserAttr = new UserAttrSex(pageid,0,maleNum,date);
        UserAttrSex femaleUserAttr = new UserAttrSex(pageid,1,femaleNum,date);
        UserAttrAge userAttrAge = new UserAttrAge(pageid,user_age_0_17,user_age_18_24,user_age_25_34,user_age_35_44,user_age_45_54,user_age_55_100,date);
        UserAttrSexDAO userAttrSexDAO = DAOFactory.getUserAttrSexDAO();
        UserAttrAgeDAO userAttrAgeDAO = DAOFactory.getUserAttrAgeDAO();
        userAttrSexDAO.insert(maleUserAttr);
        userAttrSexDAO.insert(femaleUserAttr);
        userAttrAgeDAO.insert(userAttrAge);
    }

    private static JavaPairRDD<Long, String> filterSessionAndAggrStat(
                JavaPairRDD<Long,String> userInformation,
                final JSONObject taskParam,
                final Accumulator<String> sessionAggrStatAccumulator) {
        //筛选信息
        String startAge = ParamUtils.getParam(taskParam, Constants.PARAM_START_AGE);
        String endAge = ParamUtils.getParam(taskParam, Constants.PARAM_END_AGE);
        String professionals = ParamUtils.getParam(taskParam, Constants.PARAM_PROFESSIONALS);
        String cities = ParamUtils.getParam(taskParam, Constants.PARAM_CITIES);
        String sex = ParamUtils.getParam(taskParam, Constants.PARAM_SEX);
        String keywords = ParamUtils.getParam(taskParam, Constants.PARAM_KEYWORDS);
        String categoryIds = ParamUtils.getParam(taskParam, Constants.PARAM_CATEGORY_IDS);
        String _parameter = (startAge != null ? Constants.PARAM_START_AGE + "=" + startAge + "|" : "")
                + (endAge != null ? Constants.PARAM_END_AGE + "=" + endAge + "|" : "")
                + (professionals != null ? Constants.PARAM_PROFESSIONALS + "=" + professionals + "|" : "")
                + (cities != null ? Constants.PARAM_CITIES + "=" + cities + "|" : "")
                + (sex != null ? Constants.PARAM_SEX + "=" + sex + "|" : "")
                + (keywords != null ? Constants.PARAM_KEYWORDS + "=" + keywords + "|" : "")
                + (categoryIds != null ? Constants.PARAM_CATEGORY_IDS + "=" + categoryIds: "");
        if(_parameter.endsWith("\\|")) {
            _parameter = _parameter.substring(0, _parameter.length() - 1);
        }
        final String parameter = _parameter;

        JavaPairRDD<Long,String> filterUserInformation = userInformation.filter(

                new Function<Tuple2<Long, String>, Boolean>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Boolean call(Tuple2<Long, String> tuple) throws Exception {
                String aggrInfo = tuple._2;
                // 按照年龄范围进行过滤（startAge、endAge）
                if(!ValidUtils.between(aggrInfo, Constants.FIELD_AGE,
                        parameter, Constants.PARAM_START_AGE, Constants.PARAM_END_AGE)) {
                    return false;
                }

                // 按照职业范围进行过滤（professionals）
                if(!ValidUtils.in(aggrInfo, Constants.FIELD_PROFESSIONAL,
                        parameter, Constants.PARAM_PROFESSIONALS)) {
                    return false;
                }

                // 按照城市范围进行过滤（cities）
                if(!ValidUtils.in(aggrInfo, Constants.FIELD_CITY,
                        parameter, Constants.PARAM_CITIES)) {
                    return false;
                }

                // 按照性别进行过滤
                if(!ValidUtils.equal(aggrInfo, Constants.FIELD_SEX,
                        parameter, Constants.PARAM_SEX)) {
                    return false;
                }

                // 按照搜索词进行过滤
                if(!ValidUtils.in(aggrInfo, Constants.FIELD_SEARCH_KEYWORDS,
                        parameter, Constants.PARAM_KEYWORDS)) {
                    return false;
                }

                // 主要走到这一步，那么就是需要计数的user
                sessionAggrStatAccumulator.add(Constants.SESSION_COUNT);

                String sex = StringUtils.getFieldFromConcatString(aggrInfo,"\\|",Constants.SEX);
                int age = Integer.valueOf(StringUtils.getFieldFromConcatString(aggrInfo,"\\|",Constants.AGE));
                calculateSex(sex);
                calculateAge(age);
                return true;
            }

            private void calculateAge(int age) {
                if (age < 18){
                    sessionAggrStatAccumulator.add(Constants.AGE_PERIOD_0_17);
                }else if (age >=18 && age < 25){
                    sessionAggrStatAccumulator.add(Constants.AGE_PERIOD_18_24);
                }else if (age >=25 && age < 35){
                    sessionAggrStatAccumulator.add(Constants.AGE_PERIOD_25_34);
                }else if (age >=35 && age < 45){
                    sessionAggrStatAccumulator.add(Constants.AGE_PERIOD_35_44);
                }else if (age >=45 && age < 55){
                    sessionAggrStatAccumulator.add(Constants.AGE_PERIOD_45_54);
                }else if (age >=55){
                    sessionAggrStatAccumulator.add(Constants.AGE_PERIOD_55_100);
                }

            }

            private void calculateSex(String sex) {
                if (sex.equals("male")){
                    sessionAggrStatAccumulator.add(Constants.SEX_MALE);
                }else if (sex.equals("female")){
                    sessionAggrStatAccumulator.add(Constants.SEX_FEMALE);
                }
            }
        });
        return filterUserInformation;
    }

    private static JavaPairRDD<Long,String> getUserInformation(SQLContext sqlContext, JavaRDD<Row> actionRDD) {
        JavaPairRDD<Long,String> useridsession = actionRDD.mapToPair(new PairFunction<Row, Long, String>() {
            @Override
            public Tuple2<Long, String> call(Row row) throws Exception {
                return new Tuple2<Long, String>(row.getLong(0),row.getString(1));
            }
        });
        // 查询所有用户数据，并映射成<userid,Row>的格式
        String sql = "select * from user_info";
        final JavaRDD<Row> userInfoRDD = sqlContext.sql(sql).javaRDD();
        JavaPairRDD<Long, Row> userid2InfoRDD = userInfoRDD.mapToPair(

                new PairFunction<Row, Long, Row>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Tuple2<Long, Row> call(Row row) throws Exception {
                        return new Tuple2<Long, Row>(row.getLong(0), row);
                    }

                });
        JavaPairRDD<Long, Tuple2<String, Row>> userid2FullInfoRDD =
                useridsession.join(userid2InfoRDD);

        JavaPairRDD<Long,String> userInformation = userid2FullInfoRDD.mapToPair(new PairFunction<Tuple2<Long, Tuple2<String, Row>>, Long, String>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Tuple2<Long, String> call(Tuple2<Long, Tuple2<String, Row>> tuple) throws Exception {
                Row userInfoRow = tuple._2._2;
                Long userid = tuple._1;
                int age = userInfoRow.getInt(3);
                String professional = userInfoRow.getString(4);
                String city = userInfoRow.getString(5);
                String sex = userInfoRow.getString(6);
                //这里没有考虑到city，professional这些为空的情况
                String fullAggrInfo = Constants.FIELD_AGE + "=" + age + "|"
                        + Constants.FIELD_PROFESSIONAL + "=" + professional + "|"
                        + Constants.FIELD_CITY + "=" + city + "|"
                        + Constants.FIELD_SEX + "=" + sex;
                return new Tuple2<Long, String>(userid,fullAggrInfo);
            }
        });

        return userInformation;
    }

    private static JavaRDD<Row> getActionRDDByDateRange(SQLContext sqlContext, JSONObject taskParam,String websiteName) {
        String startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE);
        String endDate = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE);
        String action = ParamUtils.getParam(taskParam, Constants.PARAM_ACTION);
        String sql =
                "select user_id,session_id "
                        + "from user_action "
                        + "where date>='" + startDate + "' "
                        + "and date<='" + endDate + "'"
                        + "and action = '" + action + "'"
                        + "and url like '" + websiteName + "%'";

        DataFrame actionDF = sqlContext.sql(sql);
        System.out.println(actionDF.count());
        for (Row row : actionDF.take(20)){
            System.out.println(row);
        }
        return actionDF.javaRDD().distinct();
    }



}
