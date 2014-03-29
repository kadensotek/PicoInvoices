package com.pico.picoinvoices;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Settings extends Activity 
{

	private InvoiceAdapter myDb = null;
	private SPAdapter _sp = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_invoices);
		
		_sp = new SPAdapter(getApplicationContext());
		openDB();
        refresh();
		
		TextView textView = (TextView) findViewById(R.id.client_invoices_txtClientName);
		String name = getClientName();
		textView.setText(name);
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
	        boolean result = super.onCreateOptionsMenu(menu);
	        return result;
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
        
        openDB();
        refresh();
        TextView textView = (TextView) findViewById(R.id.client_invoices_txtClientName);
        String name = getClientName();
        textView.setText(name);
    }
	/*
	 * 	Open/close functions for the DB
	 */
	private void closeDB() 
	{
		myDb.close();
	}
	private void openDB() 
	{
		myDb = new InvoiceAdapter(this);
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
		Cursor cursor = myDb.getAllRows();							//Create the list of items
		//	String array to use as a map for which db rows should be mapped to which element in the template layout
		String[] client_name_list = new String[]{InvoiceAdapter.KEY_ROWID, InvoiceAdapter.KEY_ISSUEDATE, InvoiceAdapter.KEY_STATUS};
		int[] ints = new int[] {R.id.invoice_listview_layout_template_txtInvoiceNumber, R.id.invoice_listview_layout_template_txtDate, 
				R.id.invoice_listview_layout_template_txtStatus};
	
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.invoice_listview_layout_template, cursor, client_name_list , ints);
		
		
		ListView list = (ListView) findViewById(R.id.client_invoices_listView);
		list.setAdapter(adapter);
		
	}
	
	private void registerClickCallback() 
	{
		ListView list = (ListView) findViewById(R.id.client_invoices_listView);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) 
			{
					
					Intent intent1 = new Intent(Settings.this, ShowDetailedInvoice.class);
                    _sp.saveInvioceID(Long.toString(idInDB));
                    startActivity(intent1);
			}
		});
	}
	
	
	/*
	 * 	onClickListeners are implemented here
	 */
	public void onClick_AddInvoice(View v)
	{
	    String issuedate = String.valueOf(new Date());
	    String customer = Long.toString(_sp.getClientID());
	    String dateserviceperformed = String.valueOf(new Date());
	    String priceservice = "300";
	    String service = "Mowing";
	    String servicedesc = "Front/back yard";
	    String amountdue = "100";
	    String status = "pending";
	    
	    myDb.insertRow(issuedate, customer, dateserviceperformed, priceservice, service, servicedesc, amountdue, status);
	    refresh();
//	    Intent goToInvoices = new Intent(this, AddNewInvoice.class);
//		goToInvoices.putExtra("customerID", customer_ID);
//		goToInvoices.putExtra("fname", customer_fname);
//		goToInvoices.putExtra("lname", customer_lname);
//		goToInvoices.putExtra("address", customer_address);
//		goToInvoices.putExtra("phone", customer_phone);
//		goToInvoices.putExtra("email", customer_email);
//		startActivity(goToInvoices);
	}
	
	private String getClientName()
	{
	    String name = "";
	    Cursor cursor = myDb.query(new String[] {Long.toString(_sp.getClientID())}, ClientAdapter.DATABASE_TABLE);
	    if (cursor.moveToFirst())
        {
            name+=cursor.getString(ClientAdapter.COL_FNAME);
            name+=" ";
            name+=cursor.getString(ClientAdapter.COL_LNAME);
        }
        else 
            Toast.makeText(Settings.this, "failed to load", Toast.LENGTH_SHORT).show();
        cursor.close();
	    return name;
//		Intent goToInvoices = new Intent(this, AddNewInvoice.class);
//		goToInvoices.putExtra("customerID", customer_ID);
//		goToInvoices.putExtra("fname", customer_fname);
//		goToInvoices.putExtra("lname", customer_lname);
//		goToInvoices.putExtra("address", customer_address);
//		goToInvoices.putExtra("phone", customer_phone);
//		goToInvoices.putExtra("email", customer_email);
//		startActivity(goToInvoices);
	}
}
