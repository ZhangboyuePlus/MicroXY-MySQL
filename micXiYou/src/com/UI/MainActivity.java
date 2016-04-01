package com.UI;

import java.util.ArrayList;
import java.util.List;

import com.UI.Indicator.OnIndicateListener;
import com.UI.micxiyou.NewsActivity;
import com.UI.micxiyou.news.NewsIndicator;
import com.UI.micxiyou.news.NewsIndicator.OnNewsIndicatorClickListener;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;
import com.project.Util.MyStack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 宿主页面，主页面。管理各个fragment。
 */
public class MainActivity extends FragmentActivity
{
	//默认显示的导航页，默认被点击的导航按钮
	public static final int DEFAULT_INDICATOR = 0;
	//宿主页面下的四个fragments
	public static Fragment[] mainFragments;
	//新闻页面下的三个fragments
	public static Fragment[] newsFragments;
	//标题栏的 TextView
	public static ImageButton mgbtBack;
	
	private Indicator indicator = null;
	
	private MyStack myStack = new MyStack(this);
//	private MyInternet myInternet = new MyInternet(this);

	public static View onLoadingFooter;
	public static View haveGotAllDataFooter; 
	
	//四个假设的返回栈
	public static List<Fragment> ryltHomeFragment = new ArrayList<Fragment>(); 
	public static List<Fragment> ryltNewsFragment = new ArrayList<Fragment>(); 
	public static List<Fragment> ryltXYFoodFragment = new ArrayList<Fragment>(); 
	public static List<Fragment> ryltMyInfoFragment = new ArrayList<Fragment>();
	public static int stacktopIndex_HomeFragment = -1; 
	public static int stacktopIndex_NewsFragment = -1; 
	public static int stacktopIndex_XYFoodFragment = -1; 
	public static int stacktopIndex_MyInfoFragment = -1; 
	public static Fragment frgtCurrentHome = new Fragment();
	public static Fragment frgtCurrentNews = new Fragment();
	public static Fragment frgtCurrentXYFood = new Fragment();
	public static Fragment frgtCurrentMyInfo = new Fragment();
	
	/*
	 *function  宿主activity在被Creat时， 软件的入口
	 *param
	 *return
	 */
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		//请求自定义标题栏
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_main);
		
		initActivity();
		dealAction();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			dealBackAction();
		}
		
		return	true;
	}

	public void dealBackAction()
	{
/*		Log.i("tag", "current main page = " + Constant.CURRENT_MAIN_PAGE_STATE  + 
				"	current home page = " + Constant.CURRENT_HOME_PAGE_STATE + 
				"	current news page = " + Constant.CURRENT_NEWS_PAGE_STATE);
*/
		
		if(Constant.CURRENT_MAIN_PAGE_STATE == Constant.PAGE_STATE_NEWS_ACTIVITY && 
				Constant.CURRENT_NEWS_PAGE_STATE == Constant.PAGE_STATE_SCHOOL_NEWS_CONTENT_FRAGMENT)
		{
			myStack.popNewsStack();
		}
		else if(Constant.CURRENT_MAIN_PAGE_STATE == Constant.PAGE_STATE_XY_FOOD_ACTIVITY && 
				(	Constant.CURRENT_XY_FOOD_PAGE_STATE == Constant.PAGE_STATE_RESTAURANT_FRAGMENT 
				||	Constant.CURRENT_XY_FOOD_PAGE_STATE == Constant.PAGE_STATE_MEAL_FRAGMENT))
		{
			myStack.popXYFoodStack();
		}
		else if(Constant.CURRENT_MAIN_PAGE_STATE == Constant.PAGE_STATE_MY_INFO_ACTIVITY &&
					(	Constant.CURRENT_MY_INFO_PAGE_STATE == Constant.PAGE_STATE_CONTACT_US_FRAGMENT 
					||	Constant.CURRENT_MY_INFO_PAGE_STATE == Constant.PAGE_STATE_DEVELOP_MEMBER))
		{
			myStack.popMyInfoStack();
		}
		else if(Constant.CURRENT_MAIN_PAGE_STATE == Constant.PAGE_STATE_HOME_ACTIVITY && 
					(	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_STUDY_ROOM_FRAGMENT 
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_RECUITMENT_TITLE_FRAGMENT
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_RECUITMENT_CONTENT_FRAGMENT
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_COURSE_TABLE_FRAGMENT
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_CHOOSE_COURSE_TABLE_INFO_FRAGMENT
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_XY_MAP_FRAGMENT
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_ARTICLES_FOUND_FRAGMENT
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_ARTICLES_FOUND_CONTENT_FRAGMENT 
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_MY_ARTICLE_FOUND_PUBLISHED_FRAGMENT
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_I_WANT_PUBLISH_ARTICLE_FOUND_FARGMENT
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_SECONDARY_MARKET_FRAGMENT
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_SECONDARY_MARKET_CONTENT_FRAGMENT
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_MY_TRADE_FRAGMENT
					||	Constant.CURRENT_HOME_PAGE_STATE == Constant.PAGE_STATE_I_WANT_PUBLISH_TRADE_FARGMENT))
		{
			myStack.popHomeStack();
		}	
		else if(Constant.CURRENT_MAIN_PAGE_STATE != Constant.PAGE_STATE_HOME_ACTIVITY)
		{
			exchangeMainFragments(Constant.INDEX_HOME_FRAGMENT);
			indicator.setIndicator(Constant.INDEX_HOME_FRAGMENT);
		}
		else
		{
			MainActivity.this.finish();
		}
	}

	/*
	 *function	切换新闻页面下的子页面到下标为 which 的fragment 页面  
	 *param		int which 
	 *return	
	 */
	private void exchangeNewsFragments(int which)
	{
		getSupportFragmentManager().beginTransaction()
				.hide(newsFragments[Constant.INDEX_SCHOOL_NEWS_FRAGMENT])
				.hide(newsFragments[Constant.INDEX_SCHOOL_ANNOUNCEMENT_FRAGMENT])
				.hide(newsFragments[Constant.INDEX_SCHOOL_INFO_FRAGMENT])
				.show(newsFragments[which]).commit();
	}

	/*
	 *function 	切换宿主页面下的子页面到下标为 which 的fragment页面  并且设置是否需要显示返回键
	 *param		int which
	 *return
	 */
	private void exchangeMainFragments(int which)
	{
/*		Log.i("tag", "current main page = " + Constant.CURRENT_MAIN_PAGE_STATE  + 
				"	current home page = " + Constant.CURRENT_HOME_PAGE_STATE + 
				"	current food page = " + Constant.CURRENT_XY_FOOD_PAGE_STATE);
*/
		
		//得到要转至的主页面的当前fragment
		Fragment frgtCurr = new Fragment();
		if(which == 0)
			frgtCurr = MainActivity.frgtCurrentHome;
		else if(which == 1)
			frgtCurr = MainActivity.frgtCurrentNews;
		else if(which == 2)
			frgtCurr = MainActivity.frgtCurrentXYFood;
		else if(which == 3)
			frgtCurr = MainActivity.frgtCurrentMyInfo;
		
		//隐藏四个主页面
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
			.hide(mainFragments[Constant.INDEX_HOME_FRAGMENT])
			.hide(mainFragments[Constant.INDEX_NEWS_FRAGMENT])
			.hide(mainFragments[Constant.INDEX_XY_FOOD_FRAGMENT])
			.hide(mainFragments[Constant.INDEX_MY_INFO_FRAGMENT])
		//隐藏四个主页面下的当前页面
			.hide(MainActivity.frgtCurrentHome)
			.hide(MainActivity.frgtCurrentNews)
			.hide(MainActivity.frgtCurrentXYFood)
			.hide(MainActivity.frgtCurrentMyInfo)
		//显示要转至的主页面和主页面下的当前fragment
			.show(mainFragments[which])
			.show(frgtCurr);
		transaction.commit();

		if(NewsActivity.isFragmentFirstShow == true && which == Constant.INDEX_NEWS_FRAGMENT)
		{
			exchangeNewsFragments(DEFAULT_INDICATOR);
			NewsActivity.isFragmentFirstShow = false;
		}
		
		//尽管在每个fragment destroy的时候已经判断是否显示返回键，但这里是为了在页面转换时判断是否显示返回键
		if(Constant.IS_FRAGMENT_HAS_BACK_BUTTON[which] == true)
			new BackTextViewController().setBackTextViewEnable(true);
		else
			new BackTextViewController().setBackTextViewEnable(false);
	}

	/*
	 *function	在宿主页面下处理各种事件  
	 *param		
	 *return
	 */
	private void dealAction() 
	{
		//indicator为宿主页面的导航器， 实际是一个LinearLayout
		indicator = (Indicator) findViewById(R.id.indicator);
		//设置默认显示被点击的导航按钮
		indicator.setIndicator(DEFAULT_INDICATOR);
		
		//调用indicator的接口，并且实现其方法
		indicator.setOnIndicateListener
		(
				new OnIndicateListener()
				{
					/*
					 *function	监听到indicator的点击事件后，根据得到的下标，切换宿主页面的子页面  
					 */
					public void onIndicate(int which) 
					{
						exchangeMainFragments(which);
					}
				}
		);

		//newsIndicator为新闻页面的导航器， 实际是一个LinearLayout
		NewsIndicator newsIndicator = (NewsIndicator) findViewById(R.id.news_indicator);
		//设置默认显示被点击的导航按钮
		newsIndicator.setNewsIndicator(DEFAULT_INDICATOR);
		//调用NewsIndicator的接口，并且实现其方法
		newsIndicator.setOnNewsIndicatorClickListener
		(
				new OnNewsIndicatorClickListener()
				{
					/*
					 *function	监听到NewsIndicator的点击事件后，根据得到的下标，切新闻页面的子页面  
					 */
					@Override
					public void onNewsIndicatorClick(int which) 
					{
						exchangeNewsFragments(which);
					}
				}
		);
		
		mgbtBack.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						dealBackAction();
					}
				}
		);
	}

	/*
	 *function 	初始化宿主activity页面。    
	 *param
	 *return
	 */
	private void initActivity() 
	{
		haveGotAllDataFooter = this.getLayoutInflater().inflate(R.layout.footer_have_get_all, null);
		onLoadingFooter = getLayoutInflater().inflate(R.layout.footer_onloading, null);

		//标题的TextView
		mgbtBack = (ImageButton) findViewById(R.id.mgbtBack);
		
		//找到宿主页面下的四个fragments
		mainFragments = new Fragment[4];
		mainFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_home);
		mainFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_news);
		mainFragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_xy_food);
		mainFragments[3] = getSupportFragmentManager().findFragmentById(R.id.fragment_my_info);

		//将页面切换到默认的fragment页面， 默认为0， 即首页
		exchangeMainFragments(DEFAULT_INDICATOR);

		//找到新闻页面下的三个fragments
		newsFragments = new Fragment[3];
		newsFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_school_news);
		newsFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_school_announcement);
		newsFragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_school_info);

		//将新闻页面下的子页面切换到默认页面		这项工作会在新闻activity第一次show的时候调用
//		exchangeNewsFragments(DEFAULT_INDICATOR);
	}

	@Override
	protected void onStop() 
	{
		android.os.Process.killProcess(android.os.Process.myPid());
		
		super.onStop();
	}
}
