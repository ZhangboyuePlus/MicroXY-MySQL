package com.UI;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Indicator extends LinearLayout
{
	//导航按钮数量
	private static final int INDICATOR_COUNT = 4;
	
	//没有被选中的图标的资源ID
	private static final int INDICATOR_UNSELECTED_ICON_RES_ID[] = {R.drawable.home_unselected,
					R.drawable.news_unselected,R.drawable.food_unselected, R.drawable.myinfo_unselected};
	//被选中的图标的资源ID
	private static final int INDICATOR_SELECTED_ICON_RES_ID[] = {R.drawable.home_selected,
					R.drawable.news_selected, R.drawable.food_selected, R.drawable.myinfo_selected};
	//导航按钮Text文本的资源ID
	private static final int INDICATOR_STR_RES_ID[] = {R.string.indicator_home,
					R.string.indicator_news, R.string.indicator_XYfood, R.string.indicator_myinfo};
	
	//按钮图标的tag
	private static final String indicatorIconTag[] = {"icon_tag_0", "icon_tag_1", "icon_tag_2", "icon_tag_3"};
	//按钮文本的tag
	private static final String indicatorTextTag[] = {"text_tag_0", "text_tag_1", "text_tag_2", "text_tag_3"};
	
	//没有被选中的颜色
	private static final int COLOR_UNSELECTED = Color.parseColor("#0093DD");
	//被选中的颜色
	private static final int COLOR_SELECTED = Color.GRAY;
	
	//当下被选中的导航按钮的下标， 初始值 为 -1
	public static int CURRENT_INDICATOR = -1;
	//是否是第一次设置导航按钮的状态
	public static final int FIRST_SET_INDICATOR = -1;
	
	//导航栏中放置每个导航按钮的LinearLayout
	private View[] viewIndicators;			
	//导航栏中放置导航按钮的图标的ImageView
	private ImageView[] mgvwIcons;
	//导航栏中放置导航按钮的Text文本的ImageView
	private TextView[] txtvTexts;
	
	//本类中对导航栏的监听器
	private OnIndicateListener myOnIndicateListener = null;
	
	public Indicator(Context context)
	{
		super(context);
	}

	/*
	 *function  带参构造方法
	 *param
	 *return
	 */
	public Indicator(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		initIndicator();
		
		dealAction();
	}

	/*
	 *function  处理事件：当点击导航按键时，设置该导航按键的图标， 并做 fragment的转换工作
	 *param
	 *return
	 */
	private void dealAction() 
	{
		for(int i = 0; i < INDICATOR_COUNT; i++)
			viewIndicators[i].setOnClickListener
			(
					new OnClickListener()
					{
						public void onClick(View view) 
						{
							if(myOnIndicateListener != null)
							{
								int tag = (Integer) view.getTag();
								if(tag != CURRENT_INDICATOR)
								{
									setIndicator(tag);
									myOnIndicateListener.onIndicate(tag);
								}
							}
						}
					}
			);
	}
	
	/*
	 *function  实现不同的indicators被点击时图形、文字的转换
	 *param
	 *return
	 */
	public void setIndicator(int which)
	{
		if(which != CURRENT_INDICATOR)
		{
			if(CURRENT_INDICATOR != FIRST_SET_INDICATOR)
			{
				mgvwIcons[CURRENT_INDICATOR].setImageResource(INDICATOR_UNSELECTED_ICON_RES_ID[CURRENT_INDICATOR]);
				txtvTexts[CURRENT_INDICATOR].setTextColor(COLOR_UNSELECTED);
			}
			mgvwIcons[which].setImageResource(INDICATOR_SELECTED_ICON_RES_ID[which]);
			txtvTexts[which].setTextColor(COLOR_SELECTED);
		}

		CURRENT_INDICATOR = which;
	}

	//该接口由调用类实现
	public interface OnIndicateListener
	{
		public void onIndicate(int which);
	}

	//该方法有主类调用，并且将实现后的 OnIndicateListener 接口 以参数方式传进来
	public void setOnIndicateListener(OnIndicateListener listener)
	{
		//用本类的Listener接收调用类实现的接口
		myOnIndicateListener = listener;
	}

	/*
	 *function  初始化导航栏，共四个导航按键
	 *param
	 *return
	 */
	private void initIndicator()
	{
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setBackgroundColor(Color.WHITE);	//TODO
		
		mgvwIcons = new ImageView[INDICATOR_COUNT];
		txtvTexts = new TextView[INDICATOR_COUNT];
		viewIndicators = new View[INDICATOR_COUNT];
		
		for(int i = 0; i < INDICATOR_COUNT; i++)
		{
			viewIndicators[i] = 
					createIndicator(INDICATOR_UNSELECTED_ICON_RES_ID[i],
					indicatorIconTag[i], INDICATOR_STR_RES_ID[i],
					indicatorTextTag[i], i);
			viewIndicators[i].setTag(Integer.valueOf(i));
			this.addView(viewIndicators[i]);
		}

		this.setGravity(Gravity.BOTTOM);
	}

	/*
	 *function  根据传过来的	图标资源id， iconTag， 文本资源id， textTag， 生成一个ImageView和TextView，
	 *			加入到LinearLayout中
	 *param		图标资源id， iconTag， 文本资源id， textTag
	 *return	 返回	LinearLayout
	 */
	private View createIndicator(int iconResId, String iconTag, int strResId, String textTag, int i) 
	{
		LinearLayout linearlayout = new LinearLayout(this.getContext());
		linearlayout.setOrientation(LinearLayout.VERTICAL);
		linearlayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
		linearlayout.setGravity(Gravity.CENTER);
		linearlayout.setPadding(0, 0, 0, 0);

		mgvwIcons[i] = new ImageView(this.getContext());
		mgvwIcons[i].setImageResource(iconResId);
		mgvwIcons[i].setTag(iconTag);
//		mgvwIcons[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		mgvwIcons[i].setLayoutParams(new LinearLayout.LayoutParams(60, 60));
		mgvwIcons[i].setPadding(0, 0, 0, 0);
			
		txtvTexts[i] = new TextView(this.getContext());
		txtvTexts[i].setTag(textTag);
		txtvTexts[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		txtvTexts[i].setText(strResId);
		txtvTexts[i].setTextColor(COLOR_UNSELECTED);
		txtvTexts[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		txtvTexts[i].setPadding(0, 0, 0, 0);
		
		linearlayout.addView(mgvwIcons[i]);
		linearlayout.addView(txtvTexts[i]);

		return linearlayout;
	}
}
