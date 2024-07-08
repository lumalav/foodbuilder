package com.foodbuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;



public class ActivityBuilder extends FragmentActivity implements TablesDialogFragment.NoticeDialogListener, NumberPickerDialogFragment.NumberPickerDialogHandler  {
	private MyAdapter mAdapter;
    public static ViewPager mPager;
    public static Integer picture;
	public static DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView dbDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private EditText editText;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mTablesTitles;
    private int textLength = 0;
    private NumberPicker np;
    private Cursor c = null;
    
    private Button button1;
    private String[] food = null;
    ArrayList<String> text_sort = new ArrayList<String>();
    ArrayList<Integer> image_sort = new ArrayList<Integer>();
    private ArrayList<String> mArrayList = null;
    
    private Bundle args = null;
    private TablesDialogFragment newFragment = null;
    public static HashMap<String, Double> map = new HashMap<String, Double>();
    public static int counter = 0;
    private String[] selectedItems;			//Structure for the Selected Items coming from the SearchBar or Drawer when creating a new Plate
    
    public static MenuItem msearchMenuItem;
    
    public static String[] columnName = {"BABY_FOODS","BAKED_PRODUCTS_AND_CORN_TORTILLAS","BEEF_PRODUCTS","BEVERAGES","CEREAL_GRAINS_PASTA_AND_BREAKFAST_CEREALS","DAIRY_PRODUCTS",
    		"DESSERTS_PASTRY_AND_OTHER","EGG_PRODUCTS","FAST_FOODS_SNACKS_AND_OTHER","FATS_AND_OILS","FINFISH_AND_SHELLFISH_PRODUCTS","FRUITS_AND_FRUIT_JUICE","GAME_PRODUCTS",
    		"LEGUMES_AND_LEGUME_PRODUCTS","NUT_AND_SEED_PRODUCTS","PORK_PRODUCTS","POULTRY_PRODUCTS","SAUSAGES_AND_LUNCHEON_MEATS","SOUPS_SAUCES_AND_GRAVIES","SPICES_AND_HERBS",
    		"SWEETS", "VEGETABLES_AND_VEGETABLE_PRODUCTS"};
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_builder);
		
		new AsyncUpload(this, columnName).execute();
		System.out.println(counter);
		mAdapter = new MyAdapter(getSupportFragmentManager());
		 
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
		
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		setFragmentsParameters();
        
		mTitle = mDrawerTitle = getTitle();
		
		mTablesTitles = getResources().getStringArray(R.array.tables_array);
        
		
        
      //  editText = (EditText) findViewById(R.id.searchFoodType);
        
      //  dbDrawerList = (ListView) findViewById(R.id.db_drawer);
        
        mDrawerList.setAdapter(new CustomAdapter(mTablesTitles));
        
        
        
     //   np = (NumberPicker) findViewById(R.id.numberPicker1);
        
        
        //TextFilter
       /* editText.addTextChangedListener(new TextWatcher() {

        	public void afterTextChanged(Editable s) {

        	}

        	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        	}

        	public void onTextChanged(CharSequence s, int start, int before, int count) {

        		textLength = editText.getText().length();
        	    text_sort.clear();
        	    image_sort.clear();

        	    for (int i = 0; i < mTablesTitles.length; i++) {
        	    	if (textLength <= mTablesTitles[i].length()) {
        	    		if (editText.getText().toString().equalsIgnoreCase((String)mTablesTitles[i].subSequence(0,textLength))) {
        	    			text_sort.add(mTablesTitles[i]);
        	    		}
        	    	}
        	    }

        	    mDrawerList.setAdapter(new CustomAdapter(text_sort));

        	   }
        	  });
        */
        	//primer codigo
        
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
         
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            @Override
			public void onDrawerClosed(View view) {
    //            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
      //          invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        	mDrawerLayout.setDrawerListener(mDrawerToggle);
        	
       	handleIntent(getIntent());
        
        }
	
	//Part of searchBar
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		handleIntent(intent);
	}
	
	//SearchBar
    private void handleIntent(Intent intent) {
		// TODO Auto-generated method stub
    	 if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);
                //use the query to search your data somehow
                System.out.println(query);
                
                //new code for search
                DataBaseHelper myDbHelper = new DataBaseHelper(this);
               
                openDatabase(myDbHelper);
                
                setContentOfDialogFromSearch(query, myDbHelper, "ENGLISH_NAME");
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_menu, menu);
		
		// Associate searchable configuration with the SearchView
	    SearchManager searchManager =
	           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView =
	            (SearchView) menu.findItem(R.id.search).getActionView();
	    
	    searchView.setSearchableInfo(
	            searchManager.getSearchableInfo(getComponentName()));
	    
	    msearchMenuItem = menu.findItem(R.id.search);
	    
	   	    
	    msearchMenuItem.setVisible(false);
	    
		return true;
	}
	
	public static MenuItem getSearchMenuItem() {
        return msearchMenuItem;
    }
	
	public static ViewPager getViewPager(){
		return mPager;
	}
	
	public static DrawerLayout getLayout(){
		return mDrawerLayout;
	}
	
	public static Integer getDrawable(){
		return picture;
	}
	
    public void setFragmentsParameters(){
        FragmentPage1.isEnabled = false;
        FragmentPage3.isEnabled = false;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
    	 
    	 if (mDrawerToggle.onOptionsItemSelected(item)) {
             return true;
         }
   	 
    	switch (item.getItemId()) {
        case android.R.id.home:
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
    //	Toast.makeText(this, String.valueOf(fullNumber), Toast.LENGTH_LONG).show(); 
    	map.put(selectedItems[counter], fullNumber);
    	
    	System.out.println("MAP: Key = " + map.get(fullNumber) + " , y el value es = " + fullNumber);
    	
    	setImagesForView();
    	
		mPager.getAdapter().notifyDataSetChanged();
    
    	counter++;
    	
    }
    
    public static HashMap<String, Double> getMap(){
    	return map;
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
     /**
     * * 
     * @param position
     */
    

    private void selectItem(int position) {
    	
    	//Open DataBase
    	DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
    	openDatabase(myDbHelper);
		
    	switch(position){
    	case 0:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 0);
    		break;
    	case 1:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 1);
    		break;
    	case 2:
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 2);
    		break;
    	case 3:
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 3);
    		break;
    	case 4:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 4);
    		break;
    	case 5:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 5);
    		break;
    	case 6:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 6);
    		break;
    	case 7:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 7);
    		break;
    	case 8:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 8);
    		break;
    	case 9:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 9);
    		break;
    	case 10:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 10);
    		break;
    	case 11:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 11);
    		break;
    	case 12:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 12);
    		break;
    	case 13:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 13);
    		break;
    	case 14:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 14);
    		break;
    	case 15:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 15);
    		break;
    	case 16:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 16);
    		break;
    	case 17:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 17);
    		break;
    	case 18:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 18);
    		break;
    	case 19:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 19);
    		break;
    	case 20:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 20);
    		break;
    	case 21:
    		
    		setContentOfDialogFromDrawer(myDbHelper, c, "ENGLISH_NAME", columnName, 21);
    		break;
    		
    	}
    	
    }
    
    public static void openDatabase(DataBaseHelper myDbHelper) {
		// TODO Auto-generated method stub
    	try {
			 
            myDbHelper.createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}	try {

			myDbHelper.openDataBase();

		} catch(SQLException sqle){

        throw sqle;

		}
	}
    
    public void setImagesForView(){
    	
    	HashMap<String, ArrayList<String>> productsMap = AsyncUpload.getFoodMap();
    	for (Map.Entry<String, ArrayList<String>> e : productsMap.entrySet()) {
			String key = e.getKey();
			ArrayList<String> values = e.getValue();
			if(values.contains(selectedItems[counter])){	
				if       (key == columnName[0]){
					picture = Integer.valueOf(R.drawable.ic_baby);
				} else if(key == columnName[1]){
					picture = Integer.valueOf(R.drawable.ic_baked);
				} else if(key == ActivityBuilder.columnName[2]){
					picture = Integer.valueOf(R.drawable.ic_beef);
				} else if(key == columnName[3]){
					picture = Integer.valueOf(R.drawable.ic_beverage);
				} else if(key == columnName[4]){
					picture = Integer.valueOf(R.drawable.ic_cereal);
				} else if(key == columnName[5]){
					picture = Integer.valueOf(R.drawable.ic_dairy);
				} else if(key == columnName[6]){
					//picture = Integer.valueOf(R.drawable.ic_dessert);
				} else if(key == columnName[7]){
					picture = Integer.valueOf(R.drawable.ic_eggs);
				} else if(key == columnName[8]){
				//	picture = Integer.valueOf(R.drawable.ic_fast);
				} else if(key == columnName[9]){
				//	picture = Integer.valueOf(R.drawable.ic_fats);
				} else if(key == columnName[10]){
					picture = Integer.valueOf(R.drawable.ic_fish);
				} else if(key == columnName[11]){
					picture = Integer.valueOf(R.drawable.ic_fruit);
				} else if(key == columnName[12]){
					picture = Integer.valueOf(R.drawable.ic_game);
				} else if(key == columnName[13]){
					picture = Integer.valueOf(R.drawable.ic_legume);
				} else if(key == columnName[14]){
					picture = Integer.valueOf(R.drawable.ic_nut);
				} else if(key == columnName[15]){
					picture = Integer.valueOf(R.drawable.ic_pork);
				} else if(key == columnName[16]){
					picture = Integer.valueOf(R.drawable.ic_poultry);
				} else if(key == columnName[17]){
				//	picture = Integer.valueOf(R.drawable.ic_sausage);
				} else if(key == columnName[18]){
					picture = Integer.valueOf(R.drawable.ic_soup);
				} else if(key == columnName[19]){
					picture = Integer.valueOf(R.drawable.ic_spice);
				} else if(key == columnName[20]){
			//		picture = Integer.valueOf(R.drawable.ic_sweet);
				} else if(key == columnName[21]){
					picture = Integer.valueOf(R.drawable.ic_vegetable);
				} 
			}
	    
		}
    }

	public void setContentOfDialogFromDrawer(DataBaseHelper myDbHelper, Cursor c, String columnLanguage, String[] columnName, int numOfTable){
		
		mArrayList = new ArrayList<String>();
		
      	newFragment = new TablesDialogFragment();
    	
    	args = new Bundle();
    	
    	String columns[]= {columnLanguage}; 
    	
    	c = myDbHelper.query(columnName[numOfTable], columns, null, null, null, null, null);
    	
    	int id = c.getColumnIndex(columnLanguage);
    	    		
    	while(c.moveToNext()) {
    	   mArrayList.add(c.getString(id));
    	}
    	
    	food = mArrayList.toArray(new String[0]);
    	
    	args.putString("title", mTablesTitles[numOfTable]);
    	
    	args.putStringArray("key", food);
    	
    	newFragment.setArguments(args);
    	
    	newFragment.show(getSupportFragmentManager(), "Tables");
    	
    	c.close();
    	
    	myDbHelper.close();
    }
	
	public void setContentOfDialogFromSearch(String query, DataBaseHelper myDbHelper,  String columnLanguage){
		
		mArrayList = new ArrayList<String>();
		
		newFragment = new TablesDialogFragment();
        
		args = new Bundle();
        
		c = myDbHelper.queryForMultipleTablesEn(query);
		
		int id = c.getColumnIndex(columnLanguage);
		   		
		while(c.moveToNext()) {
		   mArrayList.add(c.getString(id));
		}
		
		food = mArrayList.toArray(new String[0]);
		
		args.putString("title", "Results for " + "\"" + query + "\"");
		
		args.putStringArray("key", food);
		
		newFragment.setArguments(args);
		
		newFragment.show(getSupportFragmentManager(), "Tables");
		
		c.close();
		
		myDbHelper.close();
	}    
     /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String[] selected, boolean listContainItems) {
		// TODO Auto-generated method stub
		selectedItems = selected;
		
		findViewById(android.R.id.empty).setVisibility(View.GONE);
		
		//Collapse Search
		getSearchMenuItem().collapseActionView();
		
		   
		FragmentPage2.setParams(selectedItems);
		
		//aqui va el numberpicker
		for(int i = 0; i < selectedItems.length; i++){
			NumberPickerBuilder npb = new NumberPickerBuilder()
            	.setFragmentManager(getSupportFragmentManager())
            	.setLabelText("g " + "(item " + (selectedItems.length - i) + ")")
            	.setPlusMinusVisibility(View.GONE)
            	.setStyleResId(R.style.BetterPickersDialogFragment);
			    System.out.println(selectedItems[i] + " Este es el orden " + i);
    		    npb.show();
		}
		counter = 0;
		FragmentPage2.counter = 0;
        mDrawerLayout.closeDrawers();
        
	}
	

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		getSearchMenuItem().collapseActionView();
	}
	
	

    /**
     * Adapter for the List in the Drawer and the Dialogs
     */
	public class CustomAdapter extends BaseAdapter {

  	  String[] data_text;

  	  CustomAdapter() {

  	  }

  	  CustomAdapter(String[] text) {
  		  data_text = text;
  	  }

  	  CustomAdapter(ArrayList<String> text) {

  		  data_text = new String[text.size()];

  	  for (int i = 0; i < text.size(); i++) {
  		  data_text[i] = text.get(i);
  	   }

  	  }

  	  @Override
		public int getCount() {
  		  return data_text.length;
  	  }

  	  @Override
		public String getItem(int position) {
  		  return null;
  	  }

  	  @Override
		public long getItemId(int position) {
  		  return position;
  	  }

  	  @Override
		public View getView(int position, View convertView, ViewGroup parent) {

  		  LayoutInflater inflater = getLayoutInflater();
  		  
  		  View row;
  	
  		  row = inflater.inflate(R.layout.drawer_list_item, parent, false);
  	
  		  TextView textview = (TextView) row.findViewById(R.id.text1);
  		 
  		  textview.setText(data_text[position]);

  	   return (row);

  	  }
  	 
}
	public static class MyAdapter extends FragmentPagerAdapter {
		
		
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        
        @Override
        public int getItemPosition(Object object) {
        	if(object != null && object instanceof FragmentPage2){
        	    FragmentPage2 f = (FragmentPage2) object;
        	    if (f != null) {
        	        f.update();
        	    } /*else if(object != null && object instanceof FragmentPage3){
            	    FragmentPage3 f2 = (FragmentPage3) object;
            	    if (f2 != null) {
            	        f2.update();
            	    }*/ //Prueba necesito hacer otra
        	    }
        	    return super.getItemPosition(object);
        }
        @Override
        public int getCount() {
            return 3;
        }
 
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
            case 0:
            	return new FragmentPage1();
            case 1:
                return new FragmentPage2();
            case 2:
            	return new FragmentPage3();
            default:
                return null;
            }
        }

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
		}
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(mPager.getCurrentItem() == 1){
			mPager.setCurrentItem(0);
		} else if(mPager.getCurrentItem() == 2){
			mPager.setCurrentItem(1);
		} else{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle("Are you sure you want to exit?");
		        builder.setMessage("You haven't finished your plate yet.");
		        builder.setPositiveButton("Let me go",  new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						counter =0;
						FragmentPage2.counter = 0;
						map.clear();
						FragmentPage2.itemsAdded = null;
						finish();
					}
		       });
		        builder.setNegativeButton("I'll stay!",  new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
		       });
		        builder.show();
		    } //Message to finish the App
	    }

	

   }
class AsyncUpload extends AsyncTask<String, String[], Void>{       
	
	public static HashMap<String, ArrayList<String>> productsMap = new HashMap<String, ArrayList<String>>();
	
	public static HashMap<String, ArrayList<String>> getFoodMap(){
		return productsMap;
	}
	
	Cursor c = null;
	
	String[] columns = null;
	
	DataBaseHelper myDbHelper;
	
	  public AsyncUpload(ActivityBuilder mainActivity, String[] columnName) {
		// TODO Auto-generated constructor stub
		  myDbHelper = new DataBaseHelper(mainActivity);
		  columns = columnName;
	  }

	public static void getProductNamesFromDB(DataBaseHelper myDbHelper, Cursor c, String columnLanguage, String[] columnName, int numOfTable){
		
		    ArrayList<String> arrayMap = new ArrayList<String>();
	        
	    	String columns[]= {columnLanguage}; 
	    	
	    	c = myDbHelper.query(columnName[numOfTable], columns, null, null, null, null, null);
	    	
	    	int id = c.getColumnIndex(columnLanguage);
	    	    		
	    	while(c.moveToNext()) {
	    		arrayMap.add(c.getString(id));
	    	}
	    	
	    	productsMap.put(columnName[numOfTable], arrayMap);
	    	
	        c.close();
	    	
	    	myDbHelper.close();
	    }

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		for(int i = 0; i <= 21; i++){
			ActivityBuilder.openDatabase(myDbHelper);
			getProductNamesFromDB(myDbHelper, c, "ENGLISH_NAME", columns, i);
		}
		
		return null;
	}
}