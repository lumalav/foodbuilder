package com.foodbuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentPage3 extends Fragment  {
    //New Test Code
	
	private DrawerLayout mDrawerLayout;
	
    private ViewPager mPager;
	public static File imageF = null;
	public static boolean isEnabled = false;
	
	String extStorageDirectory = Environment.getExternalStorageDirectory().toString() + "/Food Builder/";
	File foodBuilderDirectory = new File(extStorageDirectory);
	
	//camera variables
	private static final int ACTION_TAKE_PHOTO_B = 1;

	private static ImageView mImageView;
	private String mCurrentPhotoPath;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static Bitmap rotatedBitmap = null;
    public static File getFilePic(){
    	return imageF;
    }
    public static Bitmap getPic(){
    	return rotatedBitmap;
    }
    public static void nullifyPic(){
    	rotatedBitmap = null;
    }
	abstract class AlbumStorageDirFactory {
		public abstract File getAlbumStorageDir(String albumName);
	}
	

	public interface OnRefreshListener {
	    public void onRefresh();
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
		
		ViewGroup view = (ViewGroup)inflater.inflate(R.layout.page3, container, false);
		mImageView = (ImageView) view.findViewById(R.id.foodImageView);
		
			
		//this is the one
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
	public static void drawerIsEnabled(boolean b) {
		// TODO Auto-generated method stub
		isEnabled = b;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.fragment3_menu, menu);

	}
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
		case R.id.openCamera:
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
			return true;
	//aqui estoy
		case R.id.searchImages:
			LayoutInflater linf = LayoutInflater.from(getActivity());            
			final View inflator = linf.inflate(R.layout.dialog_search, null);
			        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity()); 

			        alert.setView(inflator); 

			        final EditText editText = (EditText) inflator.findViewById(R.id.searchImage);
			        
			        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() { 
			        public void onClick(DialogInterface dialog, int whichButton) 
			        { 
			           String query = editText.getText().toString();
			           Intent i = new Intent(getActivity(), ActivityGridViewOfImages.class);
	            	   i.putExtra("query", query);
		               startActivity(i);
			        } 
			        }); 

			        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
			          public void onClick(DialogInterface dialog, int whichButton) { 
			            dialog.cancel(); 
			          } 
			        }); 

			        alert.show(); 
			return true;
		case R.id.nextPage:
			Toast.makeText(getActivity(), "Hey Im alive", Toast.LENGTH_LONG).show(); 
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	//1st method to be called. This one opens the camera.
	private void dispatchTakePictureIntent(int actionCode) {
		
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode) {
		case ACTION_TAKE_PHOTO_B:
			File f = null;
			
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				System.out.println(mCurrentPhotoPath);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}
			break;

		default:
			break;			
		} 

		startActivityForResult(takePictureIntent, actionCode);
	}
	
    private File setUpPhotoFile() throws IOException {
		
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		
		return f;
	}
    
    private File createImageFile() throws IOException {
    	// Create an image file name. CHange
    	String imageFileName = JPEG_FILE_PREFIX + FragmentPage2.getName();
    	File albumF = getAlbumDir();
    	imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
    	return imageF;
    }
    private File getAlbumDir(){
    	if(!foodBuilderDirectory.exists()){
    		foodBuilderDirectory.mkdirs();
		}
		return foodBuilderDirectory;
    }


    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) { //Test to see if its called
		// TODO Auto-generated method stub
		//Buscar el code the refresh
		System.out.println("sera que llego aqui?");
		switch (requestCode) {
		case ACTION_TAKE_PHOTO_B: {
			;
			getActivity();
			if (resultCode == Activity.RESULT_OK) {
				handleBigCameraPhoto();
			}
			break;
		} // ACTION_TAKE_PHOTO_B

		
		} 
	}
	
	private void handleBigCameraPhoto() {

		if (mCurrentPhotoPath != null) {
			setPic();
	//		galleryAddPic();
			mCurrentPhotoPath = null;
		}

	}
	
	private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		
		/*Rotate image*/
		
		try {
			ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
			 String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
		     int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
		     int rotationAngle = 0;
		     if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
		     if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
		     if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

		     Matrix matrix = new Matrix();
		     matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
		     rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmOptions.outWidth, bmOptions.outHeight, matrix, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* Associate the Bitmap to the ImageView */
	//	mImageView.setImageBitmap(rotatedBitmap);
		Intent i = new Intent(getActivity(), ActivityDoneDiscard.class);
	    startActivity(i);
		//mImageView.setVisibility(View.VISIBLE);
	}/*
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    getActivity().sendBroadcast(mediaScanIntent);
     }*/
    public static void setImageReceived(){
    	mImageView.setImageBitmap(rotatedBitmap);
    	
    }
	


	

}
 


