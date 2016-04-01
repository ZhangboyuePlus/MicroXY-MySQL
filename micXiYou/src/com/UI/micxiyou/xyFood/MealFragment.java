package com.UI.micxiyou.xyFood;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.UI.MainActivity;
import com.UI.R;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;
import com.project.Util.MyInternet;
import com.project.adapter.MealAdapter;
import com.project.webServices.restaurantService.GetMealByRestaurantId;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MealFragment extends Fragment
{
	private TextView txtvRestaurant;
	private TextView txtvRstrtTelePh;
	
	private ListView lstvMeal;
//	private SimpleAdapter adapter = null;
	private MealAdapter adapter;
	private List<Map<String, String>> mealList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
	
	public static boolean isDownLoadFinish = true;
	private MyInternet myInternet;

	private String restaurantName;
	private String restaurantTele;
	private String isInXRY;
	private String restaurantId;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
	}

	private void initFragment()
	{
		MainActivity.frgtCurrentXYFood = this;
		Constant.CURRENT_XY_FOOD_PAGE_STATE = Constant.PAGE_STATE_MEAL_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_XY_FOOD_FRAGMENT] = true;

		myInternet = new MyInternet(getActivity());
		
		Bundle bundle = getArguments();
		isInXRY = bundle.getString("isInXRY");
		restaurantId = bundle.getString("restaurantId");
		restaurantName = bundle.getString("restaurantName");
		restaurantTele = bundle.getString("restaurantTele");
		
		String location = isInXRY.equals("1") ? "旭日苑" : "美食广场"; 		
		txtvRestaurant = (TextView) getActivity().findViewById(R.id.txtvRestaurant);
		txtvRestaurant.setText("欢迎来到" + location + "的：\n" + restaurantName);
		
		txtvRstrtTelePh = (TextView) getActivity().findViewById(R.id.txtvRstrtTelePh);
		txtvRstrtTelePh.setText(restaurantTele);
		
		lstvMeal = (ListView) getActivity().findViewById(R.id.lstvMeal);
		adapter = new MealAdapter(getActivity(), mealList);
		lstvMeal.setAdapter(adapter);
		lstvMeal.setDivider(null);
		
		loadMeal();
	}

	public void loadMeal() 
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
			new GetMealThread().start(); 
		else 
			myInternet.showInternetIsNotAvailable();
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			if(msg.what == Constant.MSG_NOTIFY_MEAL_ADAPTER_DATA_CHANGE)
			{
				mealList.addAll(newList);
				adapter.notifyDataSetChanged();
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				myInternet.showInternetIsError();
			}
		}
	};

	private class GetMealThread extends Thread
	{
		@Override
		public void run() 
		{
			getMealList(restaurantId);
		}
	}
	
	private void getMealList(String restaurantId)
	{
		ExecutorService pool = Executors.newFixedThreadPool(1);
		Callable<List<Map<String, String>>> getMealByRestaurant = new GetMealByRestaurantId(restaurantId);
		Future<List<Map<String, String>>> future = pool.submit(getMealByRestaurant);
		
		try 
		{
			newList = future.get();

			Message msg = new Message();
			if(newList == null)
				msg.what = Constant.MSG_FAULT_OCCUR;
			else
				msg.what = Constant.MSG_NOTIFY_MEAL_ADAPTER_DATA_CHANGE;

			handler.sendMessage(msg);
		} catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		} catch (ExecutionException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			myInternet.sendMsgToHandlerInternetIsError(handler);
			return ;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_meal, container, false);
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() 
	{
		Constant.CURRENT_XY_FOOD_PAGE_STATE = Constant.PAGE_STATE_RESTAURANT_FRAGMENT;
		
		super.onDestroy();
	}

}
