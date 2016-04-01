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
	//������ť����
	private static final int INDICATOR_COUNT = 4;
	
	//û�б�ѡ�е�ͼ�����ԴID
	private static final int INDICATOR_UNSELECTED_ICON_RES_ID[] = {R.drawable.home_unselected,
					R.drawable.news_unselected,R.drawable.food_unselected, R.drawable.myinfo_unselected};
	//��ѡ�е�ͼ�����ԴID
	private static final int INDICATOR_SELECTED_ICON_RES_ID[] = {R.drawable.home_selected,
					R.drawable.news_selected, R.drawable.food_selected, R.drawable.myinfo_selected};
	//������ťText�ı�����ԴID
	private static final int INDICATOR_STR_RES_ID[] = {R.string.indicator_home,
					R.string.indicator_news, R.string.indicator_XYfood, R.string.indicator_myinfo};
	
	//��ťͼ���tag
	private static final String indicatorIconTag[] = {"icon_tag_0", "icon_tag_1", "icon_tag_2", "icon_tag_3"};
	//��ť�ı���tag
	private static final String indicatorTextTag[] = {"text_tag_0", "text_tag_1", "text_tag_2", "text_tag_3"};
	
	//û�б�ѡ�е���ɫ
	private static final int COLOR_UNSELECTED = Color.parseColor("#0093DD");
	//��ѡ�е���ɫ
	private static final int COLOR_SELECTED = Color.GRAY;
	
	//���±�ѡ�еĵ�����ť���±꣬ ��ʼֵ Ϊ -1
	public static int CURRENT_INDICATOR = -1;
	//�Ƿ��ǵ�һ�����õ�����ť��״̬
	public static final int FIRST_SET_INDICATOR = -1;
	
	//�������з���ÿ��������ť��LinearLayout
	private View[] viewIndicators;			
	//�������з��õ�����ť��ͼ���ImageView
	private ImageView[] mgvwIcons;
	//�������з��õ�����ť��Text�ı���ImageView
	private TextView[] txtvTexts;
	
	//�����жԵ������ļ�����
	private OnIndicateListener myOnIndicateListener = null;
	
	public Indicator(Context context)
	{
		super(context);
	}

	/*
	 *function  ���ι��췽��
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
	 *function  �����¼����������������ʱ�����øõ���������ͼ�꣬ ���� fragment��ת������
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
	 *function  ʵ�ֲ�ͬ��indicators�����ʱͼ�Ρ����ֵ�ת��
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

	//�ýӿ��ɵ�����ʵ��
	public interface OnIndicateListener
	{
		public void onIndicate(int which);
	}

	//�÷�����������ã����ҽ�ʵ�ֺ�� OnIndicateListener �ӿ� �Բ�����ʽ������
	public void setOnIndicateListener(OnIndicateListener listener)
	{
		//�ñ����Listener���յ�����ʵ�ֵĽӿ�
		myOnIndicateListener = listener;
	}

	/*
	 *function  ��ʼ�������������ĸ���������
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
	 *function  ���ݴ�������	ͼ����Դid�� iconTag�� �ı���Դid�� textTag�� ����һ��ImageView��TextView��
	 *			���뵽LinearLayout��
	 *param		ͼ����Դid�� iconTag�� �ı���Դid�� textTag
	 *return	 ����	LinearLayout
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
