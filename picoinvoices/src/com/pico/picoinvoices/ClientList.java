package com.pico.picoinvoices;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
	//this variable is used to select the correct user in ClientInvoices
	public static long CLIENT_ID = 0;                  
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_list);
		
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
	protected void onDestroy()
	{
		super.onDestroy();
		closeDB();
		System.out.println("Destroyed..");
	}
	
	@Override
    protected void onResume()
    {
        super.onResume();
        refresh();
        System.out.println("Resumed..");
    }
	@Override
    protected void onRestart()
    {
        super.onRestart();
        refresh();
        System.out.println("Restarted..");
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
		openDB();
	    populateListView();
		registerClickCallback();
		closeDB();
	}
	
	private void populateListView() 
	{
		openDB();
	    Cursor cursor = myDb.getAllRows(); 								//Create the list of items
		
		String[] client_name_list = new String[]{ClientAdapter.KEY_ROWID, ClientAdapter.KEY_FNAME, ClientAdapter.KEY_LNAME};
		int[] ints = new int[] {R.id.client_name_CustomerID, R.id.txt_clientFName, R.id.txt_clientLName};
		
		ListViewAdapter adapter = new ListViewAdapter(this, R.layout.client_name, cursor,client_name_list , ints, 0);
		
		ListView list = (ListView) findViewById(R.id.client_listView);
		list.setAdapter(adapter);
		closeDB();
		
	}
	
	private void registerClickCallback() 
	{
		ListView list = (ListView) findViewById(R.id.client_listView);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) 
			{
				Intent goToInvoices = new Intent(ClientList.this, ClientInvoices.class);
				CLIENT_ID = idInDB;
				startActivity(goToInvoices);
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

	
	
	

}
