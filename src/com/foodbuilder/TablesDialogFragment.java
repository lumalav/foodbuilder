package com.foodbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.content.DialogInterface;
import android.os.Bundle;

public class TablesDialogFragment extends DialogFragment implements OnLongClickListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final ArrayList<String> mSelectedItems = new ArrayList<String>(); 
		String title = getArguments().getString("title");
		final String[] tables = getArguments().getStringArray("key");
		ContextThemeWrapper ctw = new ContextThemeWrapper( getActivity(), R.style.AlertDialogCustom);
		  
		AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
		
		if(tables.length !=0){
		    builder.setTitle(title)
		          .setMultiChoiceItems(tables, null,
                      new DialogInterface.OnMultiChoiceClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which,
                       boolean isChecked) {
                   if (isChecked) {
                       // If the user checked the item, add it to the selected items
                       mSelectedItems.add(tables[which]);
                   } else if (!isChecked) {
                       // Else, if the item is already in the array, remove it 
                      mSelectedItems.remove(tables[which]);
                   }
               }
           })
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // User clicked OK, so save the mSelectedItems results somewhere
	                   // or return them to the component that opened the dialog
	            	   boolean listContainsItems = false;
	            	   
	            	   String[] selected = mSelectedItems.toArray(new String[mSelectedItems.size()]);
	            	   
	            	   if(selected.length >= 1){
	            		   listContainsItems = true;
	            	   }
	            	   
	            	   mListener.onDialogPositiveClick(TablesDialogFragment.this, selected, listContainsItems);
	            	   
	            	   
	            	   
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	            	   	   //Collapse SearchView
	            	       ActivityBuilder.getSearchMenuItem().collapseActionView();
	            	   
	               }
	           });
		} else{
			builder.setTitle("Sorry to tell you this, but... :(");
			builder.setMessage("Why don't you try something different?")
			
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
				public void onClick(DialogInterface dialog, int id) {
                	//Collapse SearchView
         	       ActivityBuilder.getSearchMenuItem().collapseActionView();
                }
            });
           
		}
		    return builder.create();
	}
	
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String[] selected, boolean listContainsItems);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

	
}
