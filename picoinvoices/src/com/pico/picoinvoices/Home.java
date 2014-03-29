package com.pico.picoinvoices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends Activity
{

	DBAdapter _myDb = null;
	SPAdapter _sp = null;
	
	////////////////////////////////////////////////////////
    /////*
    /////*  Activity Lifecycle functions
    /////*
    ////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initialize();
	}
	@Override
    protected void onDestroy()
    {
        super.onDestroy();

        closeDB();
    }
	@Override
    protected void onResume()
    {
        super.onResume();
        initialize();
    }
	
	private void initialize()
	{
	    _sp = new SPAdapter(getApplicationContext());
        _sp.saveClientID("0");
        _sp.saveInvioceID("0");
        openDB();
        closeDB();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu)
//	{
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.home, menu);
//		return true;
//	}

	

    ////////////////////////////////////////////////////////
    /////*
    /////*  Database functions
    /////*
    ////////////////////////////////////////////////////////
	private void closeDB()
	{
		_myDb.close();
	}

	private void openDB()
	{
		_myDb = new DBAdapter(this);
		_myDb.open();

	}

    ////////////////////////////////////////////////////////
    /////*
    /////*  OnClick listener for starting new activities
    /////*
    ////////////////////////////////////////////////////////
	public void onClick_ToClients(View v)
	{
		Intent intent = new Intent(this, ClientList.class);
		startActivity(intent);
	}
	public void onClick_toSettings(View v)
    {
        Intent intent = new Intent(this, AddNewInvoice.class);
        startActivity(intent);
    }
	public void onClick_toManageInvoices(View v)
    {
        Intent intent = new Intent(this, ManageInvoices.class);
        startActivity(intent);
    }
	public void onClick_toServices(View v)
    {
        Intent intent = new Intent(this, RegisterServices.class);
        startActivity(intent);
    }
}
