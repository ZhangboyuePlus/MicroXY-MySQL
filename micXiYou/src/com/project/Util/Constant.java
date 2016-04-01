package com.project.Util;

public class Constant
{
	public static final String NAMESPACE = "http://www.zby.com/";//113.140.86.66:8080
	public static final String WSDL_WITHOUT_SERVICE_NAME = "http://192.168.1.105:8080/xFireService/services/";
	public static final int HTTP_REQUEST_TIME_OUT = 10000;

/* ****************************** 数据库版本	表名称 ******************************************************* */	
	public static final String NAME_DB = "MICRO_XIYOU";
	public static final int BDVersion = 1;
	public static final String TABLE_NAME_VERSION = "version";
	public static final String TABLE_NAME_STUDY_ROOM = "studyRoom";
	
/*　******************************** NEWS SERVICES 的服务名称 和方法名 ******************************************************** */
	public static final String SERVICE_NEWS = "NewsService";
	public static final String METHOD_GET_SCHOOL_NEWS_COUNT = "getSchoolNewsCount";
	public static final String METHOD_GET_SCHOOL_ANNOUNCEMENT_COUNT = "getSchoolAnnouncementCount";
	public static final String METHOD_GET_SCHOOL_INFO_COUNT = "getSchoolInfoCount";
	
	public static final String METHOD_GET_SCHOOL_NEWS = "getSchoolNews";
	public static final String METHOD_GET_SCHOOL_ANNOUNCEMENT = "getSchoolAnnouncement";
	public static final String METHOD_GET_SCHOOL_INFO = "getSchoolInfo";
	
	public static final String METHOD_GET_SCHOOL_NEWS_CONTENT_BY_POSITION = "getSchoolNewsContentByPosition";
	public static final String METHOD_GET_SCHOOL_ANNOUNCEMENT_CONTENT_BY_POSITION = "getSchoolAnnouncementContentByPosition";
	public static final String METHOD_GET_SCHOOL_INFO_CONTENT_BY_POSITION = "getSchoolInfoContentByPosition";
	
/* ******************************** CLASS　ROOM 的服务名称和 方法名 ****************************** */
	public static final String 	SERVICE_CLASS_ROOM = "ClassRoomService";
	public static final String 	METHOD_GET_NEW_CLASS_ROOM_VERSION = "getNewClassRoomVersion";
	public static final String METHOD_GET_CLASS_ROOM = "getClassRoom";
	
/* ********************************* RECUITMENT 的服务名称和方法名  ******************************* */
	public static final String SERVICE_RECUITMENT = "RecuitmentService";
	public static final String METHOD_GET_RECUITMENT_COUNT = "getRecuitmentCount";
	public static final String METHOD_GET_RECUITMENT_TITLE = "getRecuitmentTitle";
	public static final String METHOD_GET_RECUITMENT_CONTENT_BY_POSITION = "getRecuitmentContentByPosition";

/* ********************************** COURSE TABLE 的服务名称和方法名 **************************************** */
	public static final String SERVICE_COURSE_TABLE = "CourseTableService";
	public static final String METHOD_GET_COURSE_TABLE_BY_CLASS = "getCourseTableByClass";
	
/* ********************************** RESTAURANT 的服务名称和方法名    ******************************** */
	public static final String SERVICE_MEAL_SERVICE = "MealService";
	public static final String METHOD_GET_RESTAURANT_BY_LOCATION = "getRestaurantByLocation";
	public static final String METHOD_GET_MEAL_BY_RESTAURANT_ID = "getMealByRestaurantId";
	
	
/* ********************************** LOGION OR REGISTER 的服务名称和方法名 ******************* */
	public static final String SERVICE_LOGIN_OR_REGISTER = "LoginOrRegisterService";
	public static final String METHOD_IS_ACCOUNT_REGISTERED = "isAccountRegistered";
	public static final String METHOD_REGISTER_NEW_ACCOUNT = "registerNewAccount";

/* ********************************** ACCOUNT 的服务名称和方法名*************************** */
	public static final String SERVICE_ACCOUNT = "AccountService";
	public static final String METHOD_MODIFY_USER_NAME = "modifyUserName";
	public static final String METHOD_MODIFY_PASSWORD = "modifyPassWord";
	public static final String METHOD_SEND_PASSWORD_TO_REGISTED_EMAIL = "sendPasswordToRegistedEmail";
	
/* ********************************** ARTICLE FOUND 的服务名称和方法名 ******************** */
	public static final String SERVICE_ARTICLE_FOUND = "ArticlesFoundService";
	public static final String METHOD_GET_ARTICLE_FOUND_TITLE = "getArticleFoundTitle";
	public static final String METHOD_GET_ATCFD_CONTENT_AND_TELENUMBER_BY_ID = "getAtcFdContentAndTeleNumberByid";
	public static final String METHOD_DEAL_THIS_PUBLISHED_BY_ID = "dealThisPublishedById";
	public static final String METHOD_GET_MY_ARTICLE_FOUND_PUBLISHED_TITLE = "getMyArticleFoundPublishedTitle";
	public static final String METHOD_PUBLISH_ARTICLE_FOUND = "publishArticleFound";
	
/* ********************************** SECONDARY MARKET 的服务名称和方法名  ******************************* */
	public static final String SERVICE_SECONDARY_MARKET = "SecondaryMarketService";
	public static final String METHOD_GET_SECONDARY_MARKET_TITLE = "getSecondaryMarketTitle";
	public static final String METHOD_GET_MY_SECONDARY_MARKET_PUBLISHED_TITLE = "getMySecondaryMarketPublishedTitle";
	public static final String METHOD_GET_SCDMKT_CONTENT_AND_TELENUMBER_BY_ID = "getScdMktContentAndTeleNumberByid";
	public static final String METHOD_DEAL_THIS_TRADE_BY_ID = "dealThisTradeById";
	public static final String METHOD_PUBLISH_SECONDARY_MARKET = "publishSecondaryMarket";
	
/* ****************************** 页面下标 ********************************************* */	
	public static final int INDEX_HOME_FRAGMENT = 0;
	public static final int INDEX_NEWS_FRAGMENT = 1;
	public static final int INDEX_XY_FOOD_FRAGMENT = 2;
	public static final int INDEX_MY_INFO_FRAGMENT = 3;
	
	public static final int INDEX_SCHOOL_NEWS_FRAGMENT = 0;
	public static final int INDEX_SCHOOL_ANNOUNCEMENT_FRAGMENT = 1;
	public static final int INDEX_SCHOOL_INFO_FRAGMENT = 2;
	
/* **************************** 页面管理	页面ID 管理  ******************************************* */
	//默认的CURRENT_PAGE_STATE和默认显示的主页面相等
	public static int CURRENT_MAIN_PAGE_STATE = 1;
	public static final int PAGE_STATE_HOME_ACTIVITY = 1;
	public static final int PAGE_STATE_NEWS_ACTIVITY = 2;
	public static final int PAGE_STATE_XY_FOOD_ACTIVITY = 3;
	public static final int PAGE_STATE_MY_INFO_ACTIVITY = 4;

	public static int CURRENT_NEWS_PAGE_STATE = 5;
	public static final int PAGE_STATE_NEWS_TITLE_FRAGMENT = 5;
	public static final int PAGE_STATE_SCHOOL_NEWS_CONTENT_FRAGMENT = 6;
	
	public static int CURRENT_XY_FOOD_PAGE_STATE = 3; 
	public static final int PAGE_STATE_XY_FOOD_FRAGMENT = 3;
	public static final int PAGE_STATE_RESTAURANT_FRAGMENT = 25;
	public static final int PAGE_STATE_MEAL_FRAGMENT = 13;

	public static int CURRENT_HOME_PAGE_STATE = 1;
	public static final int PAGE_STATE_STUDY_ROOM_FRAGMENT = 7;
	public static final int PAGE_STATE_RECUITMENT_TITLE_FRAGMENT = 8;
	public static final int PAGE_STATE_RECUITMENT_CONTENT_FRAGMENT = 9;
	public static final int PAGE_STATE_CHOOSE_COURSE_TABLE_INFO_FRAGMENT = 10;
	public static final int PAGE_STATE_COURSE_TABLE_FRAGMENT = 11;
	public static final int PAGE_STATE_XY_MAP_FRAGMENT = 12;
	public static final int PAGE_STATE_ARTICLES_FOUND_FRAGMENT = 15;
	public static final int PAGE_STATE_ARTICLES_FOUND_CONTENT_FRAGMENT = 16;
	public static final int PAGE_STATE_MY_ARTICLE_FOUND_PUBLISHED_FRAGMENT = 17;
	public static final int PAGE_STATE_I_WANT_PUBLISH_ARTICLE_FOUND_FARGMENT = 18;
	public static final int PAGE_STATE_SECONDARY_MARKET_FRAGMENT = 19;
	public static final int PAGE_STATE_SECONDARY_MARKET_CONTENT_FRAGMENT = 20;
	public static final int PAGE_STATE_MY_TRADE_FRAGMENT = 21;
	public static final int PAGE_STATE_I_WANT_PUBLISH_TRADE_FARGMENT = 22;
	
	public static int CURRENT_MY_INFO_PAGE_STATE = 4;
	public static final int PAGE_STATE_CONTACT_US_FRAGMENT = 23;
	public static final int PAGE_STATE_DEVELOP_MEMBER = 24;
	
/* ***************************** 管理四个主页面中是否需要显示返回按钮  ************************  */
	public static boolean IS_FRAGMENT_HAS_BACK_BUTTON[] = {false, false, false, false};	
	
/* ***************************** handler的信息标号   ******************************************** */
	public static final int MSG_FAULT_OCCUR = 13;

	public static final int MSG_SHOW_INTERNET_IS_ERROR = 0;
	public static final int MSG_NOTIFY_SCHOOL_NEWS_ADAPTER_DATA_CHANGE = 1;
	public static final int MSG_HAVE_GOT_ALL_SCHOOL_NEWS_DATA = -1;
	public static final int MSG_NOTIFY_SCHOOL_ANNOUNCEMENT_ADAPTER_DATA_CHANGE = 2;
	public static final int MSG_HAVE_GOT_ALL_SCHOOL_ANNOUNCEMENT_DATA = -2;
	public static final int MSG_NOTIFY_SCHOOL_INFO_ADAPTER_DATA_CHANGE = 3;
	public static final int MSG_HAVE_GOT_ALL_SCHOOL_INFO_DATA = -3;
	public static final int MSG_NOTIFY_RECUITMENT_ADAPTER_DATA_CHANGE = 4;
	public static final int MSG_HAVE_GOT_ALL_RECUITMENT_DATA = -4;
	public static final int MSG_HAVE_GOT_RECUITMENT_ARTICLE = 5;
	public static final int MSG_ERROR_GOT_RECUITMENT_ARTICLE = -5;
	public static final int MSG_NOTIFY_RESTAURANTS_ADAPTER_DATA_CHANGE = 6;
	public static final int MSG_NOTIFY_MEAL_ADAPTER_DATA_CHANGE = 7;
	public static final int MSG_HAVE_GOT_ALL_COURSE_TABLE = 8;
	
	public static final int MSG_ACCOUNT_HAS_REGISTERED = 9;
	public static final int MSG_ACCOUNT_HAS_NOT_REGISTERED = -9;
	
	public static final int MSG_SUCCESS_REGISTER_NEW_ACCOUNT = 10;
	public static final int MSG_USER_NAME_HAVE_BEEN_REGISTERED = 11;
	public static final int MSG_EMAIL_HAVE_BEEN_REGISTERED = 12;
	
	public static final int MSG_MODIFY_USER_NAME_SUCCESS = 13;
	public static final int MSG_MODIFY_USER_NAME_FAIL = 14;
	public static final int MSG_MODIFY_PASSWORD_SUCCESS = 15;
	public static final int MSG_MODIFY_PASSWORD_FAIL = 16;
	
	public static final int MSG_NOTIFY_ARTICLE_FOUND_ADAPTER_DATA_CHANGE = 17;
	public static final int MSG_HAVE_GOT_ALL_ARTICLE_FOUND_DATA = 18;
	public static final int MSG_HAVE_GOT_ARTICLE_FOUND_CONTENT = 19;
	public static final int MSG_SUCCESS_DEAL_THIS_PUBLISHED_BY_ID = 20;
	public static final int MSG_NOTIFY_MY_ARTICLE_FOUND_PUBLISHED_ADAPTER_DATA_CHANGE = 21;
	public static final int MSG_HAVE_GOT_ALL_MY_ARTICLE_FOUND_PUBLISHED_DATA = 22;
	public static final int MSG_SUCCESS_PUBLISH_ARTICLE_FOUND = 23;
	public static final int MSG_FAIL_PUBLISH_ARTICLE_FOUND = 24;
	
	public static final int MSG_NOTIFY_SECONDARY_MARKET_ADAPTER_DATA_CHANGE = 25;
	public static final int MSG_HAVE_GOT_ALL_SECONDARY_MARKET_DATA = 26;
	public static final int MSG_HAVE_GOT_SECONDARY_MARKET_CONTENT = 27;
	public static final int MSG_SUCCESS_DEAL_THIS_TRADE_BY_ID = 28;
	public static final int MSG_NOTIFY_MY_TRADE_ADAPTER_DATA_CHANGE = 29;
	public static final int MSG_HAVE_GOT_ALL_MY_TRADE_DATA = 30;
	public static final int MSG_SUCCESS_PUBLISH_TRADE = 31;
	public static final int MSG_FAIL_PUBLISH_TRADE = 32;
	
	public static final int MSG_SUCCESS_SEND_PASSWORD_TO_REGISTED_EMAIL = 33;
	public static final int MSG_FAIL_SEND_PASSWORD_TO_REGISTED_EMAIL = 34;
	
	/* *********************** sharedPreferneces 的文件名称 ******************************* */
	public static final String SHARED_PREFERNECES_FILE_NAME_ACCOUNT = "account";
	
	/* ************************ adapter Key 和 Name ***************************** */
	public static final String[] MAP_KEY_NEWS_KIND = {"NEWS_KIND_TITLE", "NEWS_KIND_READ_COUNT", 
							"NEWS_KIND_TIME"};
	public static final String[] MAP_KEY_PUBLISH_KIND = {"PUBLISH_KIND_TITLE", "PUBLISH_KIND_PUBLISHER", 
		"PUBLISH_KIND_TIME", "PUBLISH_KIND_STATE", "PUBLISH_KIND_ID"};
	public static final String MAP_KEY_RESTAURANT[] = {"RESTAURANT_NAME", "RESTAURANT_TELEPHONE"};
	public static final String MAP_KEY_MEAL[] = {"MEAL_NAME", "MEAL_PRICE"};
	
}
