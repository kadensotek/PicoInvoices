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
<<<<<<< HEAD
=======
import android.widget.Toast;
>>>>>>> b78ad611cf4d378f58f0354984cd91df4c2925ab

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
	public static long CLIENT_ID = 0;
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
<<<<<<< HEAD
	protected void onDestroy() 
=======
	protected void onDestroy()
>>>>>>> d29360a61d29711889f8f278783e7621b022288f
>>>>>>> b78ad611cf4d378f58f0354984cd91df4c2925ab
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
				
//				Cursor cursor = myDb.getRow(idInDB);
//				if (cursor.moveToFirst())
//				{
//					long idDB = cursor.getLong(ClientAdapter.COL_ROWID);
//					
					Intent goToInvoices = new Intent(ClientList.this, ClientInvoices.class);
<<<<<<< HEAD
					CLIENT_ID = idInDB;
					System.out.println(CLIENT_ID);
=======
					goToInvoices.putExtra("customerID", idDB);
					goToInvoices.putExtra("fname", fname);
					goToInvoices.putExtra("lname", lname);
					goToInvoices.putExtra("address", address);
					goToInvoices.putExtra("phone", phone);
					goToInvoices.putExtra("email", email);
>>>>>>> b78ad611cf4d378f58f0354984cd91df4c2925ab
					startActivity(goToInvoices);
//				}
//				else 
//					Toast.makeText(ClientList.this, "failed to load"+idInDB, Toast.LENGTH_SHORT).show();
//				cursor.close();
				
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

<<<<<<< HEAD
=======
	
	
	
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
	

>>>>>>> b78ad611cf4d378f58f0354984cd91df4c2925ab
}
