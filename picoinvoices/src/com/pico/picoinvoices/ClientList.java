package com.pico.picoinvoices;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/*
 * Steps to using the DB
 * 1. Instantiate the DB Adapter
 * 2. Open the DB
 * 3. Use the DB
 * 4. Close the DB
 * 
 */

public class ClientList extends Activity 
{

	ClientAdapter myDb;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_list);
		
		openDB();
		refresh();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client_list, menu);
		return true;
	}
	@Override
<<<<<<< HEAD
	protected void onDestroy() 
=======
	protected void onDestroy()
>>>>>>> d29360a61d29711889f8f278783e7621b022288f
	{
		super.onDestroy();
		
		closeDB();
	}
	
	
	
	
	/*
	 * 	Database maintenance functions
	 */
	private void closeDB() 
	{
		myDb.close();
	}
	private void openDB() 
	{
		myDb = new ClientAdapter(this);
		myDb.open();
	}
	
	
	
	/*
	 * 	refresh functions
	 */
	private void refresh()
	{
		populateListView();
		registerClickCallback();
	}
	
	@SuppressWarnings("deprecation")
	private void populateListView() 
	{
		Cursor cursor = myDb.getAllRows(); 								//Create the list of items
//		ArrayList<String> names = getRecordSet(cursor);
//		
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(		//Build the adapter
//				this,						//Context for adapter 
//				R.layout.client_textview, 	//Layout to use
//				names);						//List of items to use
//		
		String[] client_name_list = new String[]{ClientAdapter.KEY_ROWID, ClientAdapter.KEY_FNAME, ClientAdapter.KEY_LNAME};
		int[] ints = new int[] {R.id.txt_dbID, R.id.txt_clientFName, R.id.txt_clientLName};
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.client_name, cursor,client_name_list , ints);
		
		ListView list = (ListView) findViewById(R.id.client_listView);
		list.setAdapter(adapter);
		
	}
	
	private void registerClickCallback() 
	{
		ListView list = (ListView) findViewById(R.id.client_listView);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) 
			{
				
				Cursor cursor = myDb.getRow(idInDB);
				if (cursor.moveToFirst())
				{
					long idDB = cursor.getLong(ClientAdapter.COL_ROWID);
					String fname = cursor.getString(ClientAdapter.COL_FNAME);
					String lname = cursor.getString(ClientAdapter.COL_LNAME);
					String address = cursor.getString(ClientAdapter.COL_ADDRESS);
					String phone = cursor.getString(ClientAdapter.COL_PHONE);
					String email = cursor.getString(ClientAdapter.COL_EMAIL);
					
					Intent goToInvoices = new Intent(ClientList.this, ClientInvoices.class);
					goToInvoices.putExtra("customerID", idDB);
					goToInvoices.putExtra("fname", fname);
					goToInvoices.putExtra("lname", lname);
					goToInvoices.putExtra("address", address);
					goToInvoices.putExtra("phone", phone);
					goToInvoices.putExtra("email", email);
					startActivity(goToInvoices);
				}
				else 
					Toast.makeText(ClientList.this, "failed to load"+idInDB, Toast.LENGTH_SHORT).show();
				cursor.close();
				
			}
		});
		
	}
	
	
	
	/*
	 * 	onClickListeners are implemented here
	 */
	public void onClick_AddRecord(View v)
	{
		Intent intent = new Intent(this, AddNewClient.class);
		startActivity(intent);
	}
	
	public void onClick_Clear(View v)
	{
		myDb.deleteAll();
		refresh();
	}

	
	
	
	/*
	 * 	display an entire record set to the screen
	 */
//	private ArrayList<String> getRecordSet(Cursor cursor) 
//	{
//		ArrayList<String> myList = new ArrayList<String>();
//		//popluate the message from the cursor
//		
//		//reset cursor to start to check for data
//		if(cursor.moveToFirst())
//		{
//			do
//			{
//				//process the data:
//				int id  = cursor.getInt(DBAdapter.COL_ROWID);
//				String fname  = cursor.getString(DBAdapter.COL_FNAME);
//				String lname = cursor.getString(DBAdapter.COL_LNAME);
//				String address = cursor.getString(DBAdapter.COL_ADDRESS);
//				String phone = cursor.getString(DBAdapter.COL_PHONE);
//				String email = cursor.getString(DBAdapter.COL_EMAIL);
//				
//				//append data to the message
//				myList.add(id + "" + fname +" "+lname);
//			}while(cursor.moveToNext());
//		}
//		cursor.close();
//		return myList;
//	}
	

}
