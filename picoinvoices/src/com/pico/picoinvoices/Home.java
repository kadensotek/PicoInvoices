package com.pico.picoinvoices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

public class Home extends Activity
{

	private DBAdapter _vDb = null;
	private SPAdapter _sp = null;
	private InvoiceAdapter _myDb = null; 
	private boolean _showNotes = true;
	
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
		
		if (_showNotes == true)
		{
		      getNotifications();
		      _showNotes = false;
		}
		
		//Use shared preferences to make sure that the notification is only shown once per application life
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(!prefs.getBoolean("firstTime", false))
		{
		    
    		SharedPreferences.Editor editor = prefs.edit();
    		editor.putBoolean("firstTime", true);
    		editor.commit();
		}
		
	}
	@Override
    protected void onDestroy()
    {
        super.onDestroy();

        closevDB();
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
        openvDB();
        closevDB();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu)
//	{
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.home, menu);
//		return true;
//	}

    @SuppressLint("SimpleDateFormat")
    private void getNotifications() 
    {
       openDB();
       boolean displayNote = false;
       Date todayDate = new Date();
       String notice = "";
       Cursor cursor = _myDb.getAllRows();
//       Cursor cursor = _myDb.querySort2(new String[] { InvoiceAdapter.KEY_DUEDATE },InvoiceAdapter.DATABASE_TABLE);
       
       if (cursor.moveToFirst())
       {
          do 
          {
               Calendar c=Calendar.getInstance();
               DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               System.out.println(df.format(c.getTime()));      // This past Sunday [ May include today ]
               //changed to 60 for testing purposes should be 7 in production
               c.add(Calendar.DATE,60);
               String s = df.format(c.getTime());
               Date sevenDayDate = null;
               Date invoiceDate = null;
               
               try
               {
                    sevenDayDate = df.parse(s);
                    invoiceDate = df.parse(cursor.getString(InvoiceAdapter.COL_DUEDATE));
               } 
               catch (ParseException e)
               {
                   Toast.makeText(Home.this, "Error parsing the date.",Toast.LENGTH_SHORT).show();
               }
               
               //If the invoice duedate is between today and 7 days out, add that to the list to be displayed.
               if(invoiceDate.after(todayDate) && invoiceDate.before(sevenDayDate)) 
               {
                   notice+="Invoice #: " + cursor.getString(InvoiceAdapter.COL_ROWID) + " - " + returnDate(cursor.getString(InvoiceAdapter.COL_DUEDATE)) + "\n";
                   displayNote = true;
               }
          }while(cursor.moveToNext());
       } 
       else
       {
         //  Toast.makeText(Home.this, "No invoices have been found.",Toast.LENGTH_SHORT).show();
       }
           
       //Display the notification with the included invoices due within the following week
       if(displayNote == true)
       {
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setMessage("Upcoming payments due...\n" + notice)
                  .setCancelable(false)
                  .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                      @Override
                    public void onClick(DialogInterface dialog, int id) {
                           //do things
                      }
                  });
           AlertDialog alert = builder.create();
           alert.show();
       }

       cursor.close();
       closeDB();
        
    }
    private String returnDate(String s)
    {
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        String formattedDate = "";
        try
        {
            Date dated = originalFormat.parse(s);
            formattedDate = targetFormat.format(dated);
            System.out.println(formattedDate);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return formattedDate;
    }
    ////////////////////////////////////////////////////////
    /////*
    /////*  Database functions
    /////*
    ////////////////////////////////////////////////////////
	private void closevDB()
	{
		_vDb.close();
	}
	private void openvDB()
	{
		_vDb = new DBAdapter(this);
		_vDb.open();

	}
	
	
	private void closeDB()
    {
        _myDb.close();
    }
    private void openDB()
    {
        _myDb = new InvoiceAdapter(this);
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
        Intent intent = new Intent(this, Settings.class);
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
