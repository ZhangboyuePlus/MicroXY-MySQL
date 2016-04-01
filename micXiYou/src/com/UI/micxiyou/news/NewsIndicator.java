package com.UI.micxiyou.news;

import com.UI.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * 该类是新闻页面的导航视图， 继承LinearLayout。 里面包括三个TextView
 */
public class NewsIndicator extends LinearLayout
{
	//导航按钮的数量
	private static final int NEWS_INDICATORS_COUNT = 3;
	//三个TextView的字符串资源ID
	private static final int[] NEWS_INDICATORS_TEXT_RES_ID = {R.string.indicator_school_news, 
			R.string.indicator_school_announcement, R.string.indicator_school_info};
	
	//三个TextView的tag	
	private static final int[] NEWS_INDICATORS_TEXT_TAGS = {0, 1, 2};
	
	//TextView没有被选择时的文本颜色
	private static final int TEXT_UNSELECTED_COLOR = Color.BLACK;
	//TextView被选择时的文本颜色
	private static final int TEXT_SELECTED_COLOR = Color.WHITE;
	//TextView被选择时的背景颜色
	private static final int TEXT_BACKGROUND_SELECTED_COLOR = Color.GRAY;
	//TextView没有被选择时的背景颜色
	private static final int TEXT_BACKGROUND_UNSELECTED_COLOR  =Color.WHITE;
	
	//当下显示的fragment的下标， 初始值为 -1
	public static int CURRENT_NEWS_FRAGMENT = -1;
	
	//以后用来判断是否为第一次切换或者设置NewsIndicator，  注意它的值与CURRENT_NEWS_FRAGMENT 相等
	private static final int FIRST_SET_NEWS_INDICATOR = -1;
	
	//导航栏点击监听器
	private OnNewsIndicatorClickListener indicatorClickListener= null;
	//导航栏的三个TextView
	private TextView[] txtvIndicators = null;
	
	public NewsIndicator(Context context) 
	{
		super(context);
	}

	/*
	 *function 带参构造方法  
	 *param
	 *return
	 */
	public NewsIndicator(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		init();
		dealAction();
	}
	
	/*
	 * NewsIndicator监听器的接口， 供宿主类实现方法
	 */
	public interface OnNewsIndicatorClickListener
	{
		public void onNewsIndicatorClick(int which);
	}
	
	/*
	 *function	 给NewsIndicator设置监听器， 里面需要一个OnNewsIndicatorClickListener接口， 并需要实现接口的方法
	 *			并且将实现了的监听器引用到本类中
	 *param		OnNewsIndicatorClickListener listener
	 *return
	 */
	public void setOnNewsIndicatorClickListener(OnNewsIndicatorClickListener listener)
	{
		indicatorClickListener = listener;
	}

	/*
	 *function  根据下标设置导航栏中该按钮的状态为选中状态，让上一个被选中的按钮设置为未选中状态
	 *param		int which 需要设置为选中状态的按钮的下标
	 *return
	 */
	public void setNewsIndicator(int which)
	{
		//如果点击的不是当下选中的
		if(CURRENT_NEWS_FRAGMENT != which)
		{
			//如果不是第一次设置按钮状态
			if(CURRENT_NEWS_FRAGMENT != FIRST_SET_NEWS_INDICATOR)
			{
				txtvIndicators[CURRENT_NEWS_FRAGMENT].setTextColor(TEXT_UNSELECTED_COLOR);
				txtvIndicators[CURRENT_NEWS_FRAGMENT].setBackgroundColor(TEXT_BACKGROUND_UNSELECTED_COLOR);
			}
			
			txtvIndicators[which].setTextColor(TEXT_SELECTED_COLOR);
			txtvIndicators[which].setBackgroundColor(TEXT_BACKGROUND_SELECTED_COLOR);
			
			//将当下被选中的fragment的下标值赋值给CURRENT_NEWS_FRAGMENT
			CURRENT_NEWS_FRAGMENT = which;
		}
	}
	
	/*
	 *function	处理事件  
	 *param
	 *return
	 */
	private void dealAction() 
	{
		//给导航栏的每个TextView设置监听器
		for(int i = 0; i < NEWS_INDICATORS_COUNT; i++)
			txtvIndicators[i].setOnClickListener
			(
					new OnClickListener()
					{
						@Override
						public void onClick(View view) 
						{
							//得到被点击的TextView 的tag， 本质是下标
							int tag = (Integer) view.getTag();
							//如果indicatorClickListenert已经被实现，并且被点击的不是当下已经选中的， 则继续
							if(indicatorClickListener != null && CURRENT_NEWS_FRAGMENT != tag)
							{
								//设置导航栏按钮状态
								setNewsIndicator(tag);
								//切换新闻页面下的子页面
								indicatorClickListener.onNewsIndicatorClick(tag);
							}
						}
					}
			);
	}

	/*
	 *function	初始化NewsIndicator  
	 *param
	 *return
	 */
	private void init() 
	{
		//设置背景颜色， 方向
		this.setBackgroundColor(Color.WHITE);
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER_HORIZONTAL);

		txtvIndicators = new TextView[NEWS_INDICATORS_COUNT];
		
		//给吗，每一个TextView设置文本， 文本大小， 文本颜色， 背景颜色， tag， 输出参数， gravity 
		for(int i = 0; i < NEWS_INDICATORS_COUNT; i++)
		{
			txtvIndicators[i] = new TextView(this.getContext());
			txtvIndicators[i].setText(NEWS_INDICATORS_TEXT_RES_ID[i]);
			txtvIndicators[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			txtvIndicators[i].setTextColor(TEXT_UNSELECTED_COLOR);
			txtvIndicators[i].setBackgroundColor(TEXT_BACKGROUND_UNSELECTED_COLOR);
			txtvIndicators[i].setTag(NEWS_INDICATORS_TEXT_TAGS[i]);
			txtvIndicators[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT, 1));
			txtvIndicators[i].setGravity(Gravity.CENTER_HORIZONTAL);

			//添加到LinearLayout上面
			this.addView(txtvIndicators[i]);
		}
	}
}
