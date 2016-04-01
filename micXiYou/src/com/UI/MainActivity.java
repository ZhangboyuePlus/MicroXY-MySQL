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
 * ����ҳ�棬��ҳ�档�������fragment��
 */
public class MainActivity extends FragmentActivity
{
	//Ĭ����ʾ�ĵ���ҳ��Ĭ�ϱ�����ĵ�����ť
	public static final int DEFAULT_INDICATOR = 0;
	//����ҳ���µ��ĸ�fragments
	public static Fragment[] mainFragments;
	//����ҳ���µ�����fragments
	public static Fragment[] newsFragments;
	//�������� TextView
	public static ImageButton mgbtBack;
	
	private Indicator indicator = null;
	
	private MyStack myStack = new MyStack(this);
//	private MyInternet myInternet = new MyInternet(this);

	public static View onLoadingFooter;
	public static View haveGotAllDataFooter; 
	
	//�ĸ�����ķ���ջ
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
	 *function  ����activity�ڱ�Creatʱ�� ��������
	 *param
	 *return
	 */
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		//�����Զ��������
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
	 *function	�л�����ҳ���µ���ҳ�浽�±�Ϊ which ��fragment ҳ��  
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
	 *function 	�л�����ҳ���µ���ҳ�浽�±�Ϊ which ��fragmentҳ��  ���������Ƿ���Ҫ��ʾ���ؼ�
	 *param		int which
	 *return
	 */
	private void exchangeMainFragments(int which)
	{
/*		Log.i("tag", "current main page = " + Constant.CURRENT_MAIN_PAGE_STATE  + 
				"	current home page = " + Constant.CURRENT_HOME_PAGE_STATE + 
				"	current food page = " + Constant.CURRENT_XY_FOOD_PAGE_STATE);
*/
		
		//�õ�Ҫת������ҳ��ĵ�ǰfragment
		Fragment frgtCurr = new Fragment();
		if(which == 0)
			frgtCurr = MainActivity.frgtCurrentHome;
		else if(which == 1)
			frgtCurr = MainActivity.frgtCurrentNews;
		else if(which == 2)
			frgtCurr = MainActivity.frgtCurrentXYFood;
		else if(which == 3)
			frgtCurr = MainActivity.frgtCurrentMyInfo;
		
		//�����ĸ���ҳ��
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
			.hide(mainFragments[Constant.INDEX_HOME_FRAGMENT])
			.hide(mainFragments[Constant.INDEX_NEWS_FRAGMENT])
			.hide(mainFragments[Constant.INDEX_XY_FOOD_FRAGMENT])
			.hide(mainFragments[Constant.INDEX_MY_INFO_FRAGMENT])
		//�����ĸ���ҳ���µĵ�ǰҳ��
			.hide(MainActivity.frgtCurrentHome)
			.hide(MainActivity.frgtCurrentNews)
			.hide(MainActivity.frgtCurrentXYFood)
			.hide(MainActivity.frgtCurrentMyInfo)
		//��ʾҪת������ҳ�����ҳ���µĵ�ǰfragment
			.show(mainFragments[which])
			.show(frgtCurr);
		transaction.commit();

		if(NewsActivity.isFragmentFirstShow == true && which == Constant.INDEX_NEWS_FRAGMENT)
		{
			exchangeNewsFragments(DEFAULT_INDICATOR);
			NewsActivity.isFragmentFirstShow = false;
		}
		
		//������ÿ��fragment destroy��ʱ���Ѿ��ж��Ƿ���ʾ���ؼ�����������Ϊ����ҳ��ת��ʱ�ж��Ƿ���ʾ���ؼ�
		if(Constant.IS_FRAGMENT_HAS_BACK_BUTTON[which] == true)
			new BackTextViewController().setBackTextViewEnable(true);
		else
			new BackTextViewController().setBackTextViewEnable(false);
	}

	/*
	 *function	������ҳ���´�������¼�  
	 *param		
	 *return
	 */
	private void dealAction() 
	{
		//indicatorΪ����ҳ��ĵ������� ʵ����һ��LinearLayout
		indicator = (Indicator) findViewById(R.id.indicator);
		//����Ĭ����ʾ������ĵ�����ť
		indicator.setIndicator(DEFAULT_INDICATOR);
		
		//����indicator�Ľӿڣ�����ʵ���䷽��
		indicator.setOnIndicateListener
		(
				new OnIndicateListener()
				{
					/*
					 *function	������indicator�ĵ���¼��󣬸��ݵõ����±꣬�л�����ҳ�����ҳ��  
					 */
					public void onIndicate(int which) 
					{
						exchangeMainFragments(which);
					}
				}
		);

		//newsIndicatorΪ����ҳ��ĵ������� ʵ����һ��LinearLayout
		NewsIndicator newsIndicator = (NewsIndicator) findViewById(R.id.news_indicator);
		//����Ĭ����ʾ������ĵ�����ť
		newsIndicator.setNewsIndicator(DEFAULT_INDICATOR);
		//����NewsIndicator�Ľӿڣ�����ʵ���䷽��
		newsIndicator.setOnNewsIndicatorClickListener
		(
				new OnNewsIndicatorClickListener()
				{
					/*
					 *function	������NewsIndicator�ĵ���¼��󣬸��ݵõ����±꣬������ҳ�����ҳ��  
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
	 *function 	��ʼ������activityҳ�档    
	 *param
	 *return
	 */
	private void initActivity() 
	{
		haveGotAllDataFooter = this.getLayoutInflater().inflate(R.layout.footer_have_get_all, null);
		onLoadingFooter = getLayoutInflater().inflate(R.layout.footer_onloading, null);

		//�����TextView
		mgbtBack = (ImageButton) findViewById(R.id.mgbtBack);
		
		//�ҵ�����ҳ���µ��ĸ�fragments
		mainFragments = new Fragment[4];
		mainFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_home);
		mainFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_news);
		mainFragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_xy_food);
		mainFragments[3] = getSupportFragmentManager().findFragmentById(R.id.fragment_my_info);

		//��ҳ���л���Ĭ�ϵ�fragmentҳ�棬 Ĭ��Ϊ0�� ����ҳ
		exchangeMainFragments(DEFAULT_INDICATOR);

		//�ҵ�����ҳ���µ�����fragments
		newsFragments = new Fragment[3];
		newsFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_school_news);
		newsFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_school_announcement);
		newsFragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_school_info);

		//������ҳ���µ���ҳ���л���Ĭ��ҳ��		�������������activity��һ��show��ʱ�����
//		exchangeNewsFragments(DEFAULT_INDICATOR);
	}

	@Override
	protected void onStop() 
	{
		android.os.Process.killProcess(android.os.Process.myPid());
		
		super.onStop();
	}
}
