package com.pico.picoinvoices;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ExportDB extends Activity
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
        setContentView(R.layout.activity_exportdb);
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
//        closeDB();
    }

//  @Override
//  public boolean onCreateOptionsMenu(Menu menu)
//  {
//      // Inflate the menu; this adds items to the action bar if it is present.
//      getMenuInflater().inflate(R.menu.home, menu);
//      return true;
//  }

    

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
    public void onClick_ToExportXML(View v)
    {
//        Intent intent = new Intent(this, ExportXML.class);
//        startActivity(intent);
    }
    
    public void onClick_ToExportCSV(View v)
    {
//        Intent intent = new Intent(this, ExportCSV.class);
//        startActivity(intent);
    }
}
