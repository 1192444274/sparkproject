package com.fjs.sparkproject.constant;

/**
 * 常量接口
 * @author Administrator
 *
 */
public interface Constants {

	/**
	 * 项目配置相关的常量
	 */
	String JDBC_DRIVER = "jdbc.driver";
	String JDBC_DATASOURCE_SIZE = "jdbc.datasource.size";
	String JDBC_URL = "jdbc.url";
	String JDBC_USER = "jdbc.user";
	String JDBC_PASSWORD = "jdbc.password";
	String JDBC_URL_PROD = "jdbc.url.prod";
	String JDBC_USER_PROD = "jdbc.user.prod";
	String JDBC_PASSWORD_PROD = "jdbc.password.prod";
	String SPARK_LOCAL = "spark.local";
	String SPARK_LOCAL_TASKID_SESSION = "spark.local.taskid.session";
	String SPARK_LOCAL_TASKID_PAGE = "spark.local.taskid.page";
	String SPARK_LOCAL_TASKID_PRODUCT = "spark.local.taskid.product";
	String KAFKA_METADATA_BROKER_LIST = "kafka.metadata.broker.list";
	String KAFKA_TOPICS = "kafka.topics";

	/**
	 * Spark作业相关的常量
	 */
	String SPARK_APP_NAME_SESSION = "UserVisitSessionAnalyzeSpark";
	String SPARK_APP_NAME_SESSION1 = "UserVisitSessionAnalyzeSpark";
	String SPARK_APP_NAME_PAGE = "PageOneStepConvertRateSpark";
	String FIELD_SESSION_ID = "sessionid";
	String FIELD_SEARCH_KEYWORDS = "searchKeywords";
	String FIELD_CLICK_CATEGORY_IDS = "clickCategoryIds";
	String FIELD_AGE = "age";
	String FIELD_PROFESSIONAL = "professional";
	String FIELD_CITY = "city";
	String FIELD_SEX = "sex";
	String FIELD_VISIT_LENGTH = "visitLength";
	String FIELD_VISIT_DEPTH = "visitDepth";
	String FIELD_VISIT_BOUNCE = "visitBounce";
	String FIELD_VISIT_SECOND_SKIP = "secondSkip";
	String FIELD_VISIT_TOTAL = "totalEvent";
	String FIELD_VISIT_IP = "ipNum";
	String FIELD_VISIT_USER = "userNum";
	String FIELD_VISIT_URL = "url";
	String FIELD_QUIT = "quit";
	String FIELD_STEP_LENGTH = "stepLength";
	String FIELD_START_TIME = "startTime";
	String FIELD_CLICK_COUNT = "clickCount";
	String FIELD_ORDER_COUNT = "orderCount";
	String FIELD_PAY_COUNT = "payCount";
	String FIELD_CATEGORY_ID = "categoryid";
	String FIELD_COUNT = "fieldCount";
	String SESSION_COUNT = "session_count";

	String TIME_PERIOD_1s_3s = "1s_3s";
	String TIME_PERIOD_4s_6s = "4s_6s";
	String TIME_PERIOD_7s_9s = "7s_9s";
	String TIME_PERIOD_10s_30s = "10s_30s";
	String TIME_PERIOD_30s_60s = "30s_60s";
	String TIME_PERIOD_1m_3m = "1m_3m";
	String TIME_PERIOD_3m_10m = "3m_10m";
	String TIME_PERIOD_10m_30m = "10m_30m";
	String TIME_PERIOD_30m = "30m";

	String STEP_PERIOD_1_3 = "1_3";
	String STEP_PERIOD_4_6 = "4_6";
	String STEP_PERIOD_7_9 = "7_9";
	String STEP_PERIOD_10_30 = "10_30";
	String STEP_PERIOD_30_60 = "30_60";
	String STEP_PERIOD_60 = "60";

	String SEX = "sex";
	String SEX_MALE = "男";
	String SEX_FEMALE = "女";
	String AGE = "age";
	String AGE_PERIOD_0_17 = "age_0_17";
	String AGE_PERIOD_18_24 = "age_18_24";
	String AGE_PERIOD_25_34 = "age_25_34";
	String AGE_PERIOD_35_44 = "age_35_44";
	String AGE_PERIOD_45_54 = "age_45_54";
	String AGE_PERIOD_55_100 = "age_55_100";

	String PROFESSIONAL = "professional";
	String CITY = "city";

	/**
	 * 任务相关的常量
	 */
	String PARAM_START_DATE = "startDate";
	String PARAM_END_DATE = "endDate";
	String PARAM_ACTION = "action";
	String PARAM_START_AGE = "startAge";
	String PARAM_END_AGE = "endAge";
	String PARAM_PROFESSIONALS = "professionals";
	String PARAM_CITIES = "cities";
	String PARAM_SEX = "sex";
	String PARAM_KEYWORDS = "keywords";
	String PARAM_CATEGORY_IDS = "categoryIds";
	String PARAM_TARGET_PAGE_FLOW = "targetPageFlow";
	String PARAM_PAGE_ID = "page_id";
	String PARAM_EVENT_IDS = "event_ids";
	String PARAM_KEEP_TYPE = "keep_type_id";


}
