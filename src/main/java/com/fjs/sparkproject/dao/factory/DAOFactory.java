package com.fjs.sparkproject.dao.factory;

import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.dao.impl.*;
import com.fjs.sparkproject.domain.EventParamAnalysis;

/**
 * DAO工厂类
 * @author Administrator
 *
 */
public class DAOFactory {


	public static ITaskDAO getTaskDAO() {
		return new TaskDAOImpl();
	}

	public static ISessionAggrStatDAO getSessionAggrStatDAO() { return new SessionAggrStatDAOImpl(); }

	public static ISessionRandomExtractDAO getSessionRandomExtractDAO() { return new SessionRandomExtractDAOImpl(); }

	public static ISessionDetailDAO getSessionDetailDAO() { return new SessionDetailDAOImpl(); }

	public static ITop10CategoryDAO getTop10CategoryDAO() { return new Top10CategoryDAOImpl(); }

	public static ITop10SessionDAO getTop10SessionDAO() { return new Top10SessionDAOImpl(); }

	public static IPageSplitConvertRateDAO getPageSplitConvertRateDAO() { return new PageSplitConvertRateDAOImpl(); }

	public static UserAttrSexDAO getUserAttrSexDAO(){ return new UserAttrSexDAOImpl(); }

	public static UserAttrAgeDAO getUserAttrAgeDAO(){ return new UserAttrAgeDAOImpl(); }

	public static Top10PorfessionDAO getTop10PorfessionDAO(){ return new Top10PorfessionDAOImpl(); }

	public static Top10CityDAO getTop10CityDAO(){ return new Top10CityDAOImpl(); }

	public static KeepTypeDAO getKeepTypeDAO(){ return new KeepTypeDAOImpl(); }

	public static UserRetentionDAO getUserRetentionDAO(){ return new UserRetentionDAOImpl(); }

	public static DimensionDAO getDimensionDAO(){ return new DimensionDAOImpl(); }

	public static DimensionInformationDAO getDimensionInformationDAO(){ return  new DimensionInformationDAOImpl(); }

	public static WebsiteMessageDAO getWebsiteMessageDAO(){ return new WebsiteMessageDAOImpl(); }

	public static EventMessageDAO getEventMessageDAO(){ return new EventMessageDAOImpl(); }

	public static PageMessageDAO getPageMessageDAO(){ return new PageMessageDAOImpl();}

	public static EventAnalysisDAO getEventAnalysisDAO(){ return new EventAnalysisDAOImpl(); }

	public static PageAnalysisDAO getPageAnalysisDAO(){ return new PageAnalysisDAOImpl(); }

	public static WebsiteAnalysisDAO getWebsiteAnalysisDAO(){ return new WebsiteAnalysisDAOImpl(); }

	public static IntervalAnalysisDAO getIntervalTimeAnalysisDAO(){ return new IntervalAnalysisDAOImpl(); }

	public static FunnelMessageDAO getFunnelMessageDAO(){ return new FunnelMessageDAOImpl(); }

	public static FunnelSplitAnalysisDAO getFunnelSplitAnalysisDAO(){ return new FunnelSplitAnalysisDAOImpl(); }

	public static SourceDAO getSourceDAO(){ return new SourceDAOImpl(); }

	public static  WebsiteSourceDAO getWebsiteSourceDAO(){ return new WebsiteSourceDAOImpl(); }

	public static EntryPageDAO getEntryPageDAO(){ return new EntryPageDAOImpl(); }

	public static PageFlowSplitAnalysisDAO getPageFlowSplitAnalysisDAO(){ return new PageFlowSplitAnalysisDAOImpl(); }

	public static PageClickHeatDAO getPageClickHeatDAO(){ return new PageClickHeatDAOImpl(); }

	public static PageBrowseHeatDAO getPageBrowseHeatDAO(){ return new PageBrowseHeatDAOImpl(); }

	public static PageAttentionHeatDAO getPageAttentionHeatDAO(){ return new PageAttentionHeatDAOImpl(); }

	public static UserGroupInformationDAO getUserGroupInformationDAO(){ return new UserGroupInformationDAOImpl();}

	public static FormAnalysisDAO getFormAnalysisDAO(){ return new FormAnalysisDAOImpl(); }

	public static EventSplitAnalysisDAO getEventSplitAnalysisDAO(){ return new EventSplitAnalysisDAOImpl(); }

	public static UserActiveDAO getUserActiveDAO(){ return new UserActiveDAOImpl();}

	public static EventSimiliarityDAO getEventSimiliarityDAO(){return new EventSimiliarityDAOImpl();}

	public static UserPortraitDAO getUserPortraitDAO(){return new UserPortraitDAOImpl();}

	public static EventParamAnalysisDAO getEventParamAnalysisDAO(){ return new EventParamAnalysisDAOImpl(); }
}
