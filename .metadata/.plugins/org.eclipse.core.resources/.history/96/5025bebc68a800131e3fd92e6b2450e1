package com.pico.picoinvoices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Home extends Activity {
	
	DBAdapter myDb = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		open();
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		closeDB();
	}
	
	/*
	 * 	Open/close functions for the DB
	 */
	private void closeDB() 
	{
		myDb.close();
	}
	private void open() {
		myDb = new DBAdapter(this);
		myDb.open();
		
	}
	
	public void onClick_ToClients(View v)
	{
		Intent intent = new Intent(this, ClientList.class);
		startActivity(intent);
	}
}
