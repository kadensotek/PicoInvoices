package com.pico.picoinvoices;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

	ClientAdapter _myDb = null;
	SPAdapter _sp = null;
	
    ////////////////////////////////////////////////////////
    /////*
    /////*  Activity lifecycle functions
    /////*
    ////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_list);
		
		//initialize SharedPreferences
		_sp = new SPAdapter(getApplicationContext());
		refresh();
		 System.out.println("Created..");
		 
		 //Make sure that there is a message if the listview is empty
		 ListView listView = (ListView) findViewById(R.id.client_listView);
		 listView.setEmptyView(findViewById(R.id.emptyClientList));
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		closeDB();
		System.out.println("Destroyed..");
	}
	@Override
    protected void onPause()
    {
        super.onPause();
        closeDB();
        System.out.println("Paused..");
    }
	@Override
    protected void onStop()
    {
        super.onStop();
        closeDB();
        System.out.println("Paused..");
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
	
    ////////////////////////////////////////////////////////
    /////*
    /////*  Action bar functions
    /////*
    ////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client_list, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_addClient:
                Intent intent = new Intent(this, AddNewClient.class);
                startActivity(intent);
                return true;
            case R.id.goto_Home:
                Intent home = new Intent(this, Home.class);
                startActivity(home);
                return true;
            case R.id.goto_Clients:
                Intent clients = new Intent(this, ClientList.class);
                startActivity(clients);
                return true;
            case R.id.goto_ManageInvoices:
                Intent manage = new Intent(this, ManageInvoices.class);
                startActivity(manage);
                return true;
            case R.id.goto_Services:
                Intent services = new Intent(this, RegisterServices.class);
                startActivity(services);
                return true;
            case R.id.goto_Settings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }


    ////////////////////////////////////////////////////////
    /////*
    /////*  Database access functions
    /////*
    ////////////////////////////////////////////////////////
	private void closeDB() 
	{
		_myDb.close();
	}
	private void openDB() 
	{
		_myDb = new ClientAdapter(this);
		_myDb.open();
	}
	
    ////////////////////////////////////////////////////////
    /////*
    /////*  Refresh functions
    /////*
    ////////////////////////////////////////////////////////
	private void refresh()
	{
		//Open database
	    openDB();
	    
	    //Create the list of items on in the listview
	    populateListView();
	    
	    //Set the functionality for selecting a given element in the list
		registerClickCallback();
		
		//Close database
		closeDB();
	}
	
	private void populateListView() 
	{
	    openDB();
	    //Create the list of items
	    Cursor cursor = _myDb.getAllRows(); 								
		
	    //Create the arrays for mapping DB columns to elements in the listview
		String[] client_name_list = new String[]{ClientAdapter.KEY_ROWID, ClientAdapter.KEY_FNAME, ClientAdapter.KEY_LNAME};
		int[] ints = new int[] {R.id.client_name_CustomerID, R.id.txt_clientFName, R.id.txt_clientLName};
		
		//Set the custom listviewadapter on the data and view
		ListViewAdapter adapter = new ListViewAdapter(this, R.layout.client_name, cursor,client_name_list , ints, 0);
		ListView list = (ListView) findViewById(R.id.client_listView);
		list.setAdapter(adapter);
		
		closeDB();
	}
	
	private void registerClickCallback() 
	{
		//Find listview that was created from populateListView
	    ListView list = (ListView) findViewById(R.id.client_listView);
	    
	    //Set the action for an element selection
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) 
			{
				//Create intent to send user to ClientInvoices activity
			    Intent goToInvoices = new Intent(ClientList.this, ClientInvoices.class);
			    
			    //Set the value of the CLIENT_ID value in the shared preferences
			    //This will be used to access which client was selected in subsequent activities (ClientInvoices and ShowDetailedInvoice)
			    _sp.saveClientID(Long.toString(idInDB));
				startActivity(goToInvoices);
			}
		});
	}
	
    ////////////////////////////////////////////////////////
    /////*
    /////*  OnClick Listeners for buttons
    /////*
    ////////////////////////////////////////////////////////
	public void onClick_AddRecord(View v)
	{
		//Create intent to send user to AddNewClient activity
	    Intent intent = new Intent(this, AddNewClient.class);
		startActivity(intent);
	}
}
