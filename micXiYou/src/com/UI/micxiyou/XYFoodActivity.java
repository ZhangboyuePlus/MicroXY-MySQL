package com.UI.micxiyou;

import com.UI.R;
import com.UI.micxiyou.xyFood.RestaurantFragment;
import com.project.Util.Constant;
import com.project.Util.MyStack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class XYFoodActivity extends Fragment
{
	private ImageButton mgbtXRY;
	private ImageButton mgbtMG;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
		
		dealAction();
	}

	private void dealAction()
	{
		mgbtXRY.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						startRestaurantFragment("XRY");
					}
				}
		);
		
		mgbtMG.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						startRestaurantFragment("MG");
					}
				}
		);
	}

	/**
	 * tag equals "MG" or "XRY"
	 */
	private void startRestaurantFragment(String tag)
	{
		Bundle bundle = new Bundle();
		bundle.putString("location", tag);

		RestaurantFragment frgtRestaurant = new RestaurantFragment();
		frgtRestaurant.setArguments(bundle);

		android.support.v4.app.FragmentTransaction transaction = 
				getActivity().getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.fragmentContainer, frgtRestaurant, null)
		.hide(XYFoodActivity.this)
		.show(frgtRestaurant);
		new MyStack(getActivity()).addToXYFoodStack(XYFoodActivity.this);
		transaction.commit();
	}

	private void initFragment() 
	{
		mgbtXRY = (ImageButton) getActivity().findViewById(R.id.mgbtXRY);
		mgbtMG = (ImageButton) getActivity().findViewById(R.id.mgbtMG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_xy_food, container, false);
		
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		
		if(hidden == false)
		{
			Constant.CURRENT_MAIN_PAGE_STATE = Constant.PAGE_STATE_XY_FOOD_ACTIVITY;
		}
	}

	public void onDestroy() 
	{
		super.onDestroy();
	}
}
