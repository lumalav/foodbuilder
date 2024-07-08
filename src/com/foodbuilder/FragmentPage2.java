package com.foodbuilder;


import java.util.ArrayList;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentPage2 extends Fragment implements Updateable{
    //New Test Code
	private static String[] newItems;
	
	private static ViewGroup group;
	
	private DrawerLayout mDrawerLayout;
	
	private static MenuItem msearchMenuItem;
	
	private static TextView text;
	
	private static String name;
	
	private static MenuItem nextMenuItem;
	
	private ViewPager mPager;
	
	public static ArrayList<String> itemsAdded = null;
	
	public static int counter = 0;
	
	public static void setParams(String[] items){
		newItems = items;
		
    }
	
	public static String getName(){
		return name;
	}

	public static void setName(String string) {
		// TODO Auto-generated method stub
		name = string;
	}
	
	public interface OnRefreshListener {
	    public void onRefresh();
	}
	
	
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
		
	//	mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		itemsAdded = new ArrayList<String>();
		ViewGroup view = (ViewGroup)inflater.inflate(R.layout.page2, container, false);
		group = (ViewGroup) view.findViewById (R.id.container);
		text = (TextView) view.findViewById(android.R.id.empty);
		return view;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		ViewGroup newViews;
		
		if(newItems.length != 0){
			
			itemsAdded.add(newItems[counter]);
			
			for(String itemAdded : itemsAdded){
				System.out.println(itemAdded);
				
			}
			System.out.println(itemsAdded.size());
			
		}
		
    		newViews = (ViewGroup) LayoutInflater.from(getActivity()).inflate(
    	              R.layout.animated_layout_item, group, false);
    		
    		// Set the text in each new row.
    		((ImageView) newViews.findViewById(R.id.plateImage)).setBackgroundResource(ActivityBuilder.getDrawable());
    		((TextView) newViews.findViewById(android.R.id.text1)).setText(newItems[counter]);
    		((TextView) newViews.findViewById(android.R.id.text2)).setText(String.valueOf(ActivityBuilder.getMap().get(newItems[counter]) + " g"));
    		// Because mContainerView has android:animateLayoutChanges set to true,
            // adding this view is automatically animated.
    		   		
    		group.addView(newViews, 0);
    		
    		// Set a click listener for the "X" button in the row that will remove the row.
    		OnClickListener listener = new MyAddItemListener(newViews, group, getActivity(), itemsAdded, newItems[counter]);
    		newViews.findViewById(R.id.delete_button).setOnClickListener(listener);
    		ActivityBuilder.picture = null;
    		counter++;
	}


	@Override
	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
		if(menuVisible){
			
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		    getActivity().getActionBar().setHomeButtonEnabled(true);
		    FragmentPage1.drawerIsEnabled(true);
		    FragmentPage3.drawerIsEnabled(true);
		    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		    text.setText("Add an item to \"" + name + "\" to get started.");
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		
	    msearchMenuItem = menu.findItem(R.id.search);

		msearchMenuItem.setVisible(true);
		
		inflater.inflate(R.menu.next_menu, menu);
			
		nextMenuItem = menu.findItem(R.id.nextPage);
   	   	nextMenuItem.setVisible(true);
	    
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
		case R.id.nextPage:
			if(itemsAdded.size() == 0){
				Toast.makeText(getActivity(), "There are no items for your food", Toast.LENGTH_LONG).show(); 
			} else{
				mPager.setCurrentItem(2);
				System.out.println(itemsAdded.size());
				//mandar los items donde se vayan a usar
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}


