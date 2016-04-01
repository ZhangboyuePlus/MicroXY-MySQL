package com.UI.micxiyou.xyFood;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.project.Util.MyStack;
import com.project.adapter.RestaurantAdapter;
import com.project.webServices.restaurantService.GetRestaurantByLocation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class RestaurantFragment extends Fragment
{
	private TextView txtvCanteen;
	private ListView lstvRestaurants;
	
	private String isInXRY;
	
//	public SimpleAdapter adapter;
	private RestaurantAdapter adapter;
	public List<Map<String, String>> restaurantList = new ArrayList<Map<String, String>>();
	public List<Map<String, String>> newList = new ArrayList<Map<String,String>>();
	
	public static boolean isDownLoadFinish = true;
	
	private MyInternet myInternet;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
		dealAction();
	}

	private void dealAction() 
	{
		lstvRestaurants.setOnItemClickListener
		(
				new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long l)
					{
						// ´«µÝ	isInXRY£¬ ·¹µêÃû³Æ£¬ µç»°ºÅÂë£¬position£»
						Map<String, String> map = new HashMap<String, String>();
						map = restaurantList.get(position);
						String name = map.get(Constant.MAP_KEY_RESTAURANT[0]);
						String telephone = map.get(Constant.MAP_KEY_RESTAURANT[1]);
						String[] s = name.split(" ");
						String id = s[0];
						
						Bundle bundle = new Bundle();
						bundle.putString("isInXRY", isInXRY);
						bundle.putString("restaurantName", name);
						bundle.putString("restaurantTele", telephone);
						bundle.putString("restaurantId", id);
						
						MealFragment frgtMeal = new MealFragment();
						frgtMeal.setArguments(bundle);
						
						android.support.v4.app.FragmentTransaction transaction = 
								getActivity().getSupportFragmentManager().beginTransaction();
						transaction.add(R.id.fragmentContainer, frgtMeal, null)
						.hide(RestaurantFragment.this)
						.show(frgtMeal);
						new MyStack(getActivity()).addToXYFoodStack(RestaurantFragment.this);
						transaction.commit();
					}
				}
		);
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			
			if(msg.what == Constant.MSG_NOTIFY_RESTAURANTS_ADAPTER_DATA_CHANGE)
			{
				restaurantList.addAll(newList);
				adapter.notifyDataSetChanged();
				newList.clear();
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
				myInternet.showInternetIsError();
		}
	};
	
	public void loadRestaurants(String location)
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
			new LoadRestaurantsThread(location).start();
		else
			myInternet.showInternetIsNotAvailable();
	}
	
	class LoadRestaurantsThread extends Thread
	{
		private String location;
		
		public LoadRestaurantsThread(String location)
		{
			this.location = location;
		}
		
		@Override
		public void run() 
		{
			getRestaurants(this.location);
		}
	}
	
	private void getRestaurants(String location) 
	{
		ExecutorService pool = Executors.newFixedThreadPool(1);
		Callable<List<Map<String, String>>> getRestaurantByLocation
				= new GetRestaurantByLocation(isInXRY);
		Future<List<Map<String, String>>> future = pool.submit(getRestaurantByLocation);

		newList.clear();
		try 
		{
			newList = future.get();

			Message msg = new Message();
			if(newList != null && newList.size() > 0)
				msg.what = Constant.MSG_NOTIFY_RESTAURANTS_ADAPTER_DATA_CHANGE;
			
			handler.sendMessage(msg);
		} catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			myInternet.sendMsgToHandlerInternetIsError(handler);
			return;
		} catch (ExecutionException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			myInternet.sendMsgToHandlerInternetIsError(handler);
			return;
		}
	}
	
	private void initFragment()
	{
		MainActivity.frgtCurrentXYFood = this;
		Constant.CURRENT_XY_FOOD_PAGE_STATE = Constant.PAGE_STATE_RESTAURANT_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_XY_FOOD_FRAGMENT] = true;
		
		myInternet = new MyInternet(getActivity());
		
		lstvRestaurants = (ListView) getActivity().findViewById(R.id.lstvRestaurants);
		adapter = new RestaurantAdapter(getActivity(), restaurantList);
		lstvRestaurants.setAdapter(adapter);
		lstvRestaurants.setDivider(null);
		
		txtvCanteen = (TextView) getActivity().findViewById(R.id.txtvCanteen);
		
		Bundle bundle = getArguments();
		String location = bundle.get("location").toString();
		if(location.equals("XRY"))
		{
			txtvCanteen.setText("ÐñÈÕÔ·²ÍÌü»¶Ó­Äú");
			isInXRY = "1";
		}
		else if(location.equals("MG"))
		{
			txtvCanteen.setText("ÃÀÊ³¹ã³¡»¶Ó­Äú");
			isInXRY = "0";
		}
				
		loadRestaurants(location);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_restaurant, container, false);
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
		Constant.CURRENT_XY_FOOD_PAGE_STATE = Constant.PAGE_STATE_XY_FOOD_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(false);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_XY_FOOD_FRAGMENT] = false;

		super.onDestroy();
	}
}
