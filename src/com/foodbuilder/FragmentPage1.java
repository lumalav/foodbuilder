package com.foodbuilder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class FragmentPage1 extends Fragment {
   	
	private static Button button;
	private ViewPager mPager;
	private static EditText editText;
	public static boolean isEnabled;
	private DrawerLayout mDrawerLayout;
		
	//end of new code
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mDrawerLayout = ActivityBuilder.getLayout();
		mPager = ActivityBuilder.getViewPager();
		
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		ViewGroup view = (ViewGroup)inflater.inflate(R.layout.page1, container, false);
	    button = (Button) view.findViewById(R.id.nextButton1);
	    editText = (EditText) view.findViewById(R.id.nameEditText);
	    editText.clearFocus();
	    
	    button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    
		        if(editText.length() == 0){
		        	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			        builder.setTitle("Don't you have imagination?");
			        builder.setMessage("Give a name to your plate!");
			        builder.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
	
			        });
			        builder.show();
		        }else{
		        	System.out.println(editText.getText().toString());
		        	FragmentPage2.setName(editText.getText().toString()); //Cambiar
		        	InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
		        	inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
		        	
				    mPager.setCurrentItem(1, true);
		        }
		   }
			
		});
		
		return view;
	}
	
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
		if(isEnabled){
			
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		    getActivity().getActionBar().setHomeButtonEnabled(false);
		    
		}
	}
	//Drawer shouldn't be displayed in this Fragment
	public static void drawerIsEnabled(boolean b) {
		// TODO Auto-generated method stub
		isEnabled = b;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.nextPage:
        	if(mPager.getCurrentItem() == 0 && editText.getText().length() !=0){
        		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        		mPager.setCurrentItem(1);
        	}
        	
        	return true;
		}
		
		
		return super.onOptionsItemSelected(item);
	}

}

