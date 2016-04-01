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
 * ����������ҳ��ĵ�����ͼ�� �̳�LinearLayout�� �����������TextView
 */
public class NewsIndicator extends LinearLayout
{
	//������ť������
	private static final int NEWS_INDICATORS_COUNT = 3;
	//����TextView���ַ�����ԴID
	private static final int[] NEWS_INDICATORS_TEXT_RES_ID = {R.string.indicator_school_news, 
			R.string.indicator_school_announcement, R.string.indicator_school_info};
	
	//����TextView��tag	
	private static final int[] NEWS_INDICATORS_TEXT_TAGS = {0, 1, 2};
	
	//TextViewû�б�ѡ��ʱ���ı���ɫ
	private static final int TEXT_UNSELECTED_COLOR = Color.BLACK;
	//TextView��ѡ��ʱ���ı���ɫ
	private static final int TEXT_SELECTED_COLOR = Color.WHITE;
	//TextView��ѡ��ʱ�ı�����ɫ
	private static final int TEXT_BACKGROUND_SELECTED_COLOR = Color.GRAY;
	//TextViewû�б�ѡ��ʱ�ı�����ɫ
	private static final int TEXT_BACKGROUND_UNSELECTED_COLOR  =Color.WHITE;
	
	//������ʾ��fragment���±꣬ ��ʼֵΪ -1
	public static int CURRENT_NEWS_FRAGMENT = -1;
	
	//�Ժ������ж��Ƿ�Ϊ��һ���л���������NewsIndicator��  ע������ֵ��CURRENT_NEWS_FRAGMENT ���
	private static final int FIRST_SET_NEWS_INDICATOR = -1;
	
	//���������������
	private OnNewsIndicatorClickListener indicatorClickListener= null;
	//������������TextView
	private TextView[] txtvIndicators = null;
	
	public NewsIndicator(Context context) 
	{
		super(context);
	}

	/*
	 *function ���ι��췽��  
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
	 * NewsIndicator�������Ľӿڣ� ��������ʵ�ַ���
	 */
	public interface OnNewsIndicatorClickListener
	{
		public void onNewsIndicatorClick(int which);
	}
	
	/*
	 *function	 ��NewsIndicator���ü������� ������Ҫһ��OnNewsIndicatorClickListener�ӿڣ� ����Ҫʵ�ֽӿڵķ���
	 *			���ҽ�ʵ���˵ļ��������õ�������
	 *param		OnNewsIndicatorClickListener listener
	 *return
	 */
	public void setOnNewsIndicatorClickListener(OnNewsIndicatorClickListener listener)
	{
		indicatorClickListener = listener;
	}

	/*
	 *function  �����±����õ������иð�ť��״̬Ϊѡ��״̬������һ����ѡ�еİ�ť����Ϊδѡ��״̬
	 *param		int which ��Ҫ����Ϊѡ��״̬�İ�ť���±�
	 *return
	 */
	public void setNewsIndicator(int which)
	{
		//�������Ĳ��ǵ���ѡ�е�
		if(CURRENT_NEWS_FRAGMENT != which)
		{
			//������ǵ�һ�����ð�ť״̬
			if(CURRENT_NEWS_FRAGMENT != FIRST_SET_NEWS_INDICATOR)
			{
				txtvIndicators[CURRENT_NEWS_FRAGMENT].setTextColor(TEXT_UNSELECTED_COLOR);
				txtvIndicators[CURRENT_NEWS_FRAGMENT].setBackgroundColor(TEXT_BACKGROUND_UNSELECTED_COLOR);
			}
			
			txtvIndicators[which].setTextColor(TEXT_SELECTED_COLOR);
			txtvIndicators[which].setBackgroundColor(TEXT_BACKGROUND_SELECTED_COLOR);
			
			//�����±�ѡ�е�fragment���±�ֵ��ֵ��CURRENT_NEWS_FRAGMENT
			CURRENT_NEWS_FRAGMENT = which;
		}
	}
	
	/*
	 *function	�����¼�  
	 *param
	 *return
	 */
	private void dealAction() 
	{
		//����������ÿ��TextView���ü�����
		for(int i = 0; i < NEWS_INDICATORS_COUNT; i++)
			txtvIndicators[i].setOnClickListener
			(
					new OnClickListener()
					{
						@Override
						public void onClick(View view) 
						{
							//�õ��������TextView ��tag�� �������±�
							int tag = (Integer) view.getTag();
							//���indicatorClickListenert�Ѿ���ʵ�֣����ұ�����Ĳ��ǵ����Ѿ�ѡ�еģ� �����
							if(indicatorClickListener != null && CURRENT_NEWS_FRAGMENT != tag)
							{
								//���õ�������ť״̬
								setNewsIndicator(tag);
								//�л�����ҳ���µ���ҳ��
								indicatorClickListener.onNewsIndicatorClick(tag);
							}
						}
					}
			);
	}

	/*
	 *function	��ʼ��NewsIndicator  
	 *param
	 *return
	 */
	private void init() 
	{
		//���ñ�����ɫ�� ����
		this.setBackgroundColor(Color.WHITE);
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER_HORIZONTAL);

		txtvIndicators = new TextView[NEWS_INDICATORS_COUNT];
		
		//����ÿһ��TextView�����ı��� �ı���С�� �ı���ɫ�� ������ɫ�� tag�� ��������� gravity 
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

			//��ӵ�LinearLayout����
			this.addView(txtvIndicators[i]);
		}
	}
}
