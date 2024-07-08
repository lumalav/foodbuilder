package com.foodbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class MyAddItemListener implements View.OnClickListener  {

	private View mView;
	private ViewGroup viewgroup;
	private Activity activity;
	private ArrayList<String> items;
	private String itemToBeRemoved;
	
    public MyAddItemListener(View v, ViewGroup vg, Activity _activity, ArrayList<String> itemsAdded, String newItems) {
        mView = v;
        viewgroup = vg;
        this.activity = _activity;
        items = itemsAdded;
        itemToBeRemoved = newItems;
    }

    @Override
    public void onClick(View v) {
    	
    	
    	viewgroup.removeView(mView);
        items.remove(itemToBeRemoved);
        FragmentPage2.itemsAdded = items;
        
      //New Code
    	
        ActivityBuilder.getMap().remove(itemToBeRemoved);
    	
    	Iterator<String> keySetIterator = ActivityBuilder.getMap().keySet().iterator();

    	while(keySetIterator.hasNext()){
    	  String key = keySetIterator.next();
    	  System.out.println("En mi mapa lo que hay es: key = " + key + " value = " + ActivityBuilder.getMap().get(key));
    	}
    	
    	//end new code
        
        if(items.size() >= 1){
            for(String itemLeft : items){
			    System.out.println(itemLeft);
			
		    }
        }
        
		System.out.println("Ahora me quedan " + items.size());
        
        
         // If there are no rows remaining, show the empty view.
         if (viewgroup.getChildCount() == 0) {
             this.activity.findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
             FragmentPage2.counter = 0;
             ActivityBuilder.counter = 0;
             ActivityBuilder.map.clear();
         }            
    }
}
