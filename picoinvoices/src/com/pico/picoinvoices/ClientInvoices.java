package com.pico.picoinvoices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ClientInvoices extends Activity 
{

	private InvoiceAdapter _myDb = null;
	private SPAdapter _sp = null;
	
    ////////////////////////////////////////////////////////
    /////*
    /////*  Activity lifecycle functions
    /////*
    ////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_invoices);
		initialize();
	}
	@Override
    protected void onRestart() {
        super.onRestart();
        initialize();
    }
    @Override
    protected void onResume() 
    {
        super.onResume();
        initialize();
    }
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		closeDB();
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
	        getMenuInflater().inflate(R.menu.client_invoices, menu);
	        return true;
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle presses on the action bar items
	        switch (item.getItemId()) {
	            case R.id.action_settings:
	                System.out.println("Settings selected");
	                return true;
	            case R.id.action_addInvoice:
	                Intent intent = new Intent(this, AddNewInvoice.class);
	                startActivity(intent);
	                return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
	
    ////////////////////////////////////////////////////////
    /////*
    /////*  Database open/close
    /////*
    ////////////////////////////////////////////////////////
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
    /////*  Refresh functions
    /////*
    ////////////////////////////////////////////////////////
	private void initialize()
	{
	   _sp = new SPAdapter(getApplicationContext());
	   refresh();
       TextView textView = (TextView) findViewById(R.id.client_invoices_txtClientName);
       String name = getClientName();
       textView.setText(name);
	}
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
	    //Create the list of items
	    Cursor cursor = _myDb.getCustomerInvoice(_sp.getClientID());							
		
		//String array to use as a map for which db rows should be mapped to which element in the template layout
		String[] client_name_list = new String[]{InvoiceAdapter.KEY_ROWID, InvoiceAdapter.KEY_ISSUEDATE, InvoiceAdapter.KEY_STATUS, InvoiceAdapter.KEY_CUSTOMER};
		int[] ints = new int[] {R.id.invoice_listview_layout_template_txtInvoiceNumber, R.id.invoice_listview_layout_template_txtDate, R.id.invoice_listview_layout_template_txtStatus, R.id.invoice_listview_layout_template_CustomerID};
	
		ListViewAdapter adapter = new ListViewAdapter(this, R.layout.invoice_listview_layout_template, cursor, client_name_list , ints, 0);
		ListView list = (ListView) findViewById(R.id.client_invoices_listView);
		list.setAdapter(adapter);
		
		closeDB();
		
	}
	
	private void registerClickCallback() 
	{
		ListView list = (ListView) findViewById(R.id.client_invoices_listView);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) 
			{
					Intent intent1 = new Intent(ClientInvoices.this, ShowDetailedInvoice.class);
					//Update the value of the invoiceID so that ShowDetailedInvoice will display the correct invoice
					_sp.saveInvioceID(Long.toString(idInDB));
					System.out.println("Client Id (ClientInvoices):" + _sp.getClientID());
					System.out.println("Invoice Id (ClientInvoices):" + _sp.getInvoiceID());
                    startActivity(intent1);
			}
		});
	}
	
    ////////////////////////////////////////////////////////
    /////*
    /////*  Utility functions 
    /////*
    ////////////////////////////////////////////////////////
	private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    private String getClientName()
    {
        openDB();
        String name = "";
        Cursor cursor = _myDb.query(new String[] {Long.toString(_sp.getClientID())}, ClientAdapter.DATABASE_TABLE);
        if (cursor.moveToFirst())
        {
            name+=cursor.getString(ClientAdapter.COL_FNAME);
            name+=" ";
            name+=cursor.getString(ClientAdapter.COL_LNAME);
        }
        else 
            Toast.makeText(ClientInvoices.this, "failed to load", Toast.LENGTH_SHORT).show();
        cursor.close();
        closeDB();
        return name;
    }
	
    ////////////////////////////////////////////////////////
    /////*
    /////*  OnClick listener for adding an invoice as described in the xml
    /////*
    ////////////////////////////////////////////////////////
    private String getType()
    {
        String[] types = new String[] {"Education", "Packaging", "Construction", "Insurance", "Finance", "Repair", "Installation", "Advertising", "Business Management", "Medical Services",
                "Legal Services"};
        Random randomGenerator = new Random();
        int rand = randomGenerator.nextInt(11);
        
        return types[rand];
        
    }
    private String getName()
    {
        String[] types = new String[] {"Lawn Mow", "Spring Cleanup", "Mulch", "Trim", "Clean Gutters", "Plow Driveway", "Weed", "Hedge Clipping"};
        Random randomGenerator = new Random();
        int rand = randomGenerator.nextInt(8);
        
        return types[rand];
    }
    private String getPrice() {
        Random randomGenerator = new Random();
        int rand = randomGenerator.nextInt(100) + 20;
        String value = Integer.toString(rand);
        
        return value;
    }
    private String getStatus() {
        String[] types = new String[] {"Open", "Pending", "Paid"};
        Random randomGenerator = new Random();
        int rand = randomGenerator.nextInt(3);
        
        return types[rand];
    }
	public void onClick_AddInvoice(View v)
	{
	    openDB();
	    String issuedate = getDateTime();
	    String customer = Long.toString(_sp.getClientID());
	    String duedate = getDateTime();
	    String priceservice = getPrice();
	    String service = getName();
	    String amountdue = getPrice();
	    String status = getStatus();
	    
	    _myDb.insertRow(issuedate, customer, duedate, priceservice, service, amountdue, status);
	    closeDB();
	    refresh();
//	    Intent intent = new Intent(this, AddNewInvoice.class);
//        startActivity(intent);
	}
	
}
