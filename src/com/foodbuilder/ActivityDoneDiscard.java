package com.foodbuilder;

import java.io.File;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import uk.co.senab.photoview.PhotoViewAttacher;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ActivityDoneDiscard extends Activity {
	DisplayImageOptions options;
	private Bitmap picTaken;
	ImageView mImageView;
	PhotoViewAttacher mAttacher;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	String [] imageUrls = null;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FragmentPage3.getPic() != null){
        	picTaken = FragmentPage3.getPic();
        }
        
        setContentView(R.layout.activity_picture_done_discard);
     // Any implementation of ImageView can be used!
        mImageView = (ImageView) findViewById(R.id.iv_photo);

        // Set the Drawable displayed
        
        if(picTaken !=null){
        	Drawable bitmap = new BitmapDrawable(getResources(), picTaken);
            mImageView.setImageDrawable(bitmap);

            // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
            mAttacher = new PhotoViewAttacher(mImageView);
        } else if(ActivityGridViewOfImages.returnImages() != null){
        	imageUrls = new String[48];
        	imageUrls = ActivityGridViewOfImages.returnImages();
        	 options = new DisplayImageOptions.Builder()
             .showStubImage(R.drawable.android_logo)
             .displayer(new RoundedBitmapDisplayer(20))
             .build();
        Intent i=getIntent();
        int pos = Integer.parseInt(i.getStringExtra("pos"));
        imageLoader.displayImage(imageUrls[pos], mImageView, options, null);
        mAttacher = new PhotoViewAttacher(mImageView);
        }
        
        
        
        // Inflate a "Done/Discard" custom action bar view.
        LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done_discard, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Done"
                    	if(picTaken != null){
                    	FragmentPage3.setImageReceived();
                    	FragmentPage3.nullifyPic();
                    	finish(); // TODO: don't just finish()!
                        } 
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_discard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Discard"
                   	if(FragmentPage3.getFilePic() != null){
                    		File pic = FragmentPage3.getFilePic();
                    		if(pic.delete()){ //Esto no deberia ser ajuro
                        		finish(); // TODO: don't just finish()!
                        	}
                    	}
                   				FragmentPage3.nullifyPic();
                   				finish();
                    }
                    
                });

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        

        
       
   
    }

}
