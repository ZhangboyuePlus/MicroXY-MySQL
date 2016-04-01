package com.UI.home.XYMap;

import com.UI.MainActivity;
import com.UI.R;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class XYMapFragment extends Fragment
{
	private Button bttnSatellite;
	private Button bttnTraffic;
	
	private BMapManager mapManager;
	private MapView mapView;
	private MapController mapController;

	@Override 
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
		
		dealAction();
	}

	private void dealAction() 
	{
		bttnSatellite.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						mapView.setTraffic(false);
						mapView.setSatellite(true);
					}
				}
		);
		
		bttnTraffic.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						mapView.setSatellite(false);
						mapView.setTraffic(true);
					}
				}
		);
	}

	private void initFragment()
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_XY_MAP_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;

		bttnSatellite = (Button) getActivity().findViewById(R.id.bttnSatellite);
		bttnTraffic = (Button) getActivity().findViewById(R.id.bttnTraffic);
		
		mapView = (MapView) getActivity().findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		
		mapController = mapView.getController();
		
		GeoPoint gPoint = new GeoPoint((int)(34.161* 1E6),(int)(108.908* 1E6));
		mapController.setCenter(gPoint);
		mapController.setZoom(17);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		mapManager = new BMapManager(this.getActivity().getApplicationContext());
		mapManager.init(null);
		
		return inflater.inflate(R.layout.fragment_xy_map, container, false);
	}

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		super.onHiddenChanged(hidden);
		
		if(hidden == false)
		{
		}
	}

	@Override
	public void onDestroy() 
	{
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_HOME_ACTIVITY;
		new BackTextViewController().setBackTextViewEnable(false);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = false;
		
		super.onDestroy();
	}
	
}
