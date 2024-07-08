package com.foodbuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;


import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ActivityGridViewOfImages extends Activity {
    
	protected AbsListView list;
    
	protected ImageLoader loader = ImageLoader.getInstance();
     
	private String query;
    
	final Context context=this;
	
 DisplayImageOptions op;
 
 public static String [] images = new String[48];
     
 @Override
 public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.gridview_layout);
     
     Bundle extras = getIntent().getExtras();
     if (extras != null) {
         query = extras.getString("query");
     }
     
     ActionBar actionBar = getActionBar();
     actionBar.setDisplayHomeAsUpEnabled(true);
     actionBar.setTitle("Food Builder");
     new AsyncUpload().execute();
     op = new DisplayImageOptions.Builder()
             .displayer(new RoundedBitmapDisplayer(20))
             .build();
     
     list = (GridView) findViewById(R.id.gridViewSearch);
     list.setAdapter(new ItemAdapter());
     list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     	
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           Intent i=new Intent(context,ActivityDoneDiscard.class);
           i.putExtra("pos",position+"");
           startActivity(i);
         }
     });
     
 }
 
 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId()) {
	        
  	case android.R.id.home:
  		goUp(); //Nullify Everything and go to previous Activity
         return true;

  }
		return super.onOptionsItemSelected(item);
	}
 
 @Override
 public void onBackPressed() {
 	goUp();   //Nullify Everything
     finish(); // Finish Activity
 }
 /***
  * Intent that will navigate to the Upper Activity and will Nullify the array of images.
  * */
 public void goUp(){
 	Intent intent = new Intent(this, ActivityBuilder.class);
  	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
  	Arrays.fill(images, null);
  	startActivity(intent);
     NavUtils.navigateUpTo(this, intent);
 }
 /***
  * Parser for 48 images coming from Bing
  * */
public static void gsonParser(String jsonText){
 	
 	   Gson gson = new Gson();
 	   Response response = gson.fromJson(jsonText, Response.class);
 	   Result[] results = new Result[49];
 	   for(int i = 0; i < 48; i++){
 		   results[i] = response.getD().getResults().get(i);
 		   images[i] = results[i].getThumbnails().getMediaUrl();
 		//   System.out.println("Size of original pic: " + results[i].getFileSize());
 		//   System.out.println("Size of thumbnail pic: " + results[i].getThumbnails().getFileSize());
 	   }
 	}
  /***
   * List Adapter
   * */
  class ItemAdapter extends BaseAdapter {

     private class ViewHolder {
         public ImageView image;
     }

     @Override
     public int getCount() {
         return images.length;
     }

     @Override
     public Object getItem(int position) {
         return position;
     }

     @Override
     public long getItemId(int position) {
         return position;
     }

     @Override
     public View getView(final int position, View convertView, ViewGroup parent) {
         View v = convertView;
         v = getLayoutInflater().inflate(R.layout.image_in_gridview_layout, parent, false);
         final ProgressBar spinner = (ProgressBar) v.findViewById(R.id.loading);
         final ViewHolder holder = new ViewHolder();
         holder.image = (ImageView) v.findViewById(R.id.grid_item_image);
         v.setTag(holder);

         loader.displayImage(images[position], holder.image, op, new SimpleImageLoadingListener() {
             @Override
             public void onLoadingStarted(String imageUri, View view) {
                 spinner.setVisibility(View.VISIBLE);
             }

             @Override
             public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                 spinner.setVisibility(View.GONE);
             }

             @Override
             public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                 spinner.setVisibility(View.GONE);
             }
         });

         return v;
     }
 }
 /***
  * Bing Image Retriever
  * */
 class AsyncUpload extends AsyncTask<String, String[], Void>{       
		
		public void SearchWithBing(String search){
	    	 //CodeFomBingSearch
			search = search.replaceAll(" ", "%20");
	        String accountKey="m3C+vOdMUWfEw08y9pEXVQOfWQfjFE1jg1AIwgQMEA4";
	      
	        byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
	        String accountKeyEnc = new String(accountKeyBytes);
	        URL url;
	        try {
	            url = new URL(
	                    "https://api.datamarket.azure.com/Bing/Search/Image?Query=%27" + search + "%27&$top=50&$format=json");
	            
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
	        conn.setRequestProperty("Accept", "application/json");
	        BufferedReader br = new BufferedReader(new InputStreamReader(
	                (conn.getInputStream())));
	        String output;
	        System.out.println("Output from Server .... \n");
	  
	        while ((output = br.readLine()) != null) {
	             	gsonParser(output);
	        }
	       
	        conn.disconnect();

	        } catch (MalformedURLException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	     
	       //EndOfBingSearch  
	    }

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			SearchWithBing(query);
			return null;
		}
	    }
 /***
  * Return Images for other Activities.
  * 
  * */
 public static String[] returnImages(){
 	return images;
 }
 
 /***
  * JSON Structure Classes
  * 
  * */
 public class Response{
     private D d;

 	public D getD(){
 		return this.d;
 	}
 	public void setD(D d){
 		this.d = d;
 	}
 	
 	@Override
    public String toString() {
        return d.toString();
    }
 }

 public class D{
     private String __next;
   	private List<Result> results;

 	public String get__next(){
 		return this.__next;
 	}
 	public void set__next(String __next){
 		this.__next = __next;
 	}
 	public List<Result> getResults(){
 		return this.results;
 	}
 	public void setResults(List<Result> results){
 		this.results = results;
 	}
 }

  public class Result{
   	private String ContentType;
 	   	private String DisplayUrl;
 	   	private String FileSize;
 	   	private String Height;
 	   	private String ID;
 	   	private String MediaUrl;
 	   	private String SourceUrl;
 	   	private Thumbnail Thumbnail;
 	   	private String Title;
 	   	private String Width;
 	   	

 	 	public String getContentType(){
 			return this.ContentType;
 		}
 		public void setContentType(String contentType){
 			this.ContentType = contentType;
 		}
 	 	public String getDisplayUrl(){
 			return this.DisplayUrl;
 		}
 		public void setDisplayUrl(String DisplayUrl){
 			this.DisplayUrl = DisplayUrl;
 		}
 	 	public String getFileSize(){
 			return this.FileSize;
 		}
 		public void setFileSize(String FileSize){
 			this.FileSize = FileSize;
 		}
 	 	public String getHeight(){
 			return this.Height;
 		}
 		public void setHeight(String Height){
 			this.Height = Height;
 		}
 	 	public String getID(){
 			return this.ID;
 		}
 		public void setID(String iD){
 			this.ID = iD;
 		}
 	 	public String getMediaUrl(){
 			return this.MediaUrl;
 		}
 		public void setMediaUrl(String mediaUrl){
 			this.MediaUrl = mediaUrl;
 		}
 	 	public String getSourceUrl(){
 			return this.SourceUrl;
 		}
 		public void setSourceUrl(String SourceUrl){
 			this.SourceUrl = SourceUrl;
 		}
 	 	public Thumbnail getThumbnails(){
 			return this.Thumbnail;
 		}
 		public void setThumbnail(Thumbnail thumbnail){
 			this.Thumbnail = thumbnail;
 		}
 	 	public String getTitle(){
 			return this.Title;
 		}
 		public void setTitle(String Title){
 			this.Title = Title;
 		}
 	 	public String getWidth(){
 			return this.Width;
 		}
 		public void setWidth(String Width){
 			this.Width = Width;
 		}
 	 	
 	}
  
  public class Thumbnail{
   	private String ContentType;
 	   	private String FileSize;
 	   	private String Height;
 	   	private String MediaUrl;
 	   	private String Width;

 	 	public String getContentType(){
 			return this.ContentType;
 		}
 		public void setContentType(String contentType){
 			this.ContentType = contentType;
 		}
 	 	public String getFileSize(){
 			return this.FileSize;
 		}
 		public void setFileSize(String FileSize){
 			this.FileSize = FileSize;
 		}
 	 	public String getHeight(){
 			return this.Height;
 		}
 		public void setHeight(String Height){
 			this.Height = Height;
 		}
 	 	public String getMediaUrl(){
 			return this.MediaUrl;
 		}
 		public void setMediaUrl(String mediaUrl){
 			this.MediaUrl = mediaUrl;
 		}
 	 	public String getWidth(){
 			return this.Width;
 		}
 		public void setWidth(String Width){
 			this.Width = Width;
 		}
 	}
  
} 