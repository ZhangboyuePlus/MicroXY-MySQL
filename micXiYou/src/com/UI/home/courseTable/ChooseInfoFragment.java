package com.UI.home.courseTable;

import com.UI.MainActivity;
import com.UI.R;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;
import com.project.Util.MyStack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ChooseInfoFragment extends Fragment
{
	private Spinner spnnAdmissionYear = null;
	private Spinner spnnProfessional = null;
	private Spinner spnnClass = null;
	
	private Button bttnConfirmChooseCourseTable = null;
	private Button bttnCancelChooseCourseTable = null;
	
	private ArrayAdapter<CharSequence> yearAdapter = null;
	private ArrayAdapter<CharSequence> professionalAdapter = null;
	private ArrayAdapter<CharSequence> classAdapter = null;
	
	private final int[] CLASS_ADAPTER_ARRAY_ID = {R.array.classOf_1_Pro, R.array.calssOf_2_Pro};
	private int classArrayId = 0;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
		
		dealAction();
	}

	private void dealAction() 
	{
/*		spnnProfessional.setOnItemSelectedListener
		(
			new OnItemSelectedListener()
			{
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
				{
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0)
				{
					// TODO Auto-generated method stub
				}
			}
		);
*/
		
		bttnCancelChooseCourseTable.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_HOME_ACTIVITY;
						ChooseInfoFragment.this.getActivity().getSupportFragmentManager()
									.beginTransaction().remove(ChooseInfoFragment.this).commit();
					}
				}
		);

		bttnConfirmChooseCourseTable.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						String year = spnnAdmissionYear.getSelectedItem().toString();
						String originalProId = ("0" + (spnnProfessional.getSelectedItemPosition() + 1));
						String professionalId = originalProId.length() == 2 ? originalProId : originalProId.substring(1);
						String className = spnnClass.getSelectedItem().toString();
						
						Bundle bundle = new Bundle();
						bundle.putString("year", year);
						bundle.putString("professionalId", professionalId);
						bundle.putString("className", className);
						
						CourseTableFragment fragment = new CourseTableFragment();
						fragment.setArguments(bundle);

						android.support.v4.app.FragmentTransaction transaction = getActivity()
														.getSupportFragmentManager().beginTransaction();
						transaction.add(R.id.fragmentContainer , fragment, null)
						.hide(ChooseInfoFragment.this)
						.show(fragment);
						new MyStack(getActivity()).addToHomeStack(ChooseInfoFragment.this);
						transaction.commit();
					}
				}
		);
	}

	private void initFragment() 
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_CHOOSE_COURSE_TABLE_INFO_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;
		
		bttnConfirmChooseCourseTable = (Button) getActivity().findViewById(R.id.bttnConfirmChooseCourseTable);
		bttnCancelChooseCourseTable = (Button) getActivity().findViewById(R.id.bttnCancelChooseCourseTable);
		
		spnnAdmissionYear = (Spinner) getActivity().findViewById(R.id.spnnAdmissionYear);
		yearAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.admissionYear, 
				android.R.layout.simple_spinner_item);
		yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnnAdmissionYear.setAdapter(yearAdapter);
		spnnAdmissionYear.setSelection(2);

		spnnProfessional = (Spinner) getActivity().findViewById(R.id.spnnProfessional);
		professionalAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.professional,
					android.R.layout.simple_spinner_item);
		professionalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnnProfessional.setAdapter(professionalAdapter);
		
		spnnClass = (Spinner) getActivity().findViewById(R.id.spnnClass);
		classArrayId = CLASS_ADAPTER_ARRAY_ID[0];
		classAdapter = ArrayAdapter.createFromResource(getActivity(), classArrayId, 
					android.R.layout.simple_spinner_item);
		classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnnClass.setAdapter(classAdapter);
		spnnClass.setSelection(1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.fragment_choose_info, container, false);
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
