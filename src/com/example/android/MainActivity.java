/*******************************************************************************
 * Copyright (c) {2013} {Liew Yee Foo}
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    {Liew Yee Foo} - initial API and implementation
 *******************************************************************************/

package com.example.android;

import java.io.IOException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {
	//Declaration
	public TextView display;
	public JSONObject json;
	public TableLayout profile;
	
	
	
	/**Display JSON data in table format on the user interface of android app
	 * by clicking on the button 'Start'*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/**
		 * Declares TextView, Button and Tablelayout to retrieve the widgets 
		 * from User Interface. Insert the TableRow into Table and set the 
		 * gravity, font size  and id of table rows and columns.
		 * 
		 * Due to great amount of JSON data, 'for' loop method is used to insert 
		 * the new rows and columns in the table. In each loop, each of rows and 
		 * columns are set to has its own unique id. This purpose of doing this 
		 * is to allows the user to read and write the text of specific rows and 
		 * columns easily.
		 */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button StartDisplay= (Button)findViewById(R.id.btnDisplay);
		profile= (TableLayout)findViewById(R.id.tableLayout1);
		profile.setStretchAllColumns(true);
	    profile.bringToFront();
	    
	    for(int i = 1; i < 11; i++){
	        TableRow tr =  new TableRow(this);
	        TextView c1 = new TextView(this);
	        TextView c2 = new TextView(this);
	        c1.setId(i*10+1);      
	        c1.setTextSize(12);
	        c1.setGravity(Gravity.CENTER);
	        c2.setId(i*10+2);
	        c2.setTextSize(12);       
	        c2.setGravity(Gravity.CENTER);
	        tr.addView(c1);
	        tr.addView(c2);
	        tr.setGravity(Gravity.CENTER_HORIZONTAL);
	        profile.addView(tr);
	    }
	    
	    /**
		 * onClick: Executes the DownloadWebPageTask once OnClick event occurs. 
		 * When user click on the "Start" button, the JSON data will be
		 * read from URL and then displayed in table form.  
		 */
	    StartDisplay.setOnClickListener(new View.OnClickListener() {
			
		public void onClick(View v) {

			DownloadWebPageTask task = new DownloadWebPageTask();
			task.execute("http://private-ae335-pgserverapi.apiary.io/user/profile/234");

			}
		});
		
	}
	
	/**Reads all the data from the URL. Then, processes the data and display them 
	 * in table form. */
	private class DownloadWebPageTask extends AsyncTask<String, JSONObject, JSONObject> 
	{
		/**
		 * Calls the readJsonFromUrl Method from the JSONReader Class in order to 
		 * read the data from the url. Data read from the url is returned and passed to 
		 * the last step: OnPostExecute.*/
		private static final String TAG = "MyActivity";
	    @Override
	    protected JSONObject doInBackground(String... urls) {
	    	for (String url : urls)
	    	{
			  try {
				json = JSONReader.readJsonFromUrl(url);
			} catch (IOException e) {
				Log.e(TAG,Log.getStackTraceString(e)); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG,Log.getStackTraceString(e)); 
			}
				
	    	}
			return json;
	    }
		    
		/**
		 * OnPostExecute: Processes the data, which is read from URL, by reading the keys 
		 * and values of the JSON data lines by lines, and then replaces all the '_' in 
		 * the string of the key with " " in order to allow the user to read the data 
		 * clearly. All the processed data is displayed in table form.  */
		@Override
	    protected void onPostExecute(JSONObject json){
	    	Integer I=10;
		    Iterator<String> iter = json.keys();
		    while (iter.hasNext()) {
		    	TextView Col1=(TextView)findViewById(I+1);
		        String key = iter.next();
		        String Ckey=key.replace("_", " ");
		        Col1.setText(Ckey);
		        try {
		        	TextView Col2=(TextView)findViewById(I+2);
		            Object value = json.get(key);
		            Col2.setText(value.toString());
		            I=I+10;
		        } catch (JSONException e) {
		        	Log.e(TAG,Log.getStackTraceString(e)); 
		        }
		    }

	    }
	}

	/**
	 * Initialize the contents of the Activity's standard options menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
