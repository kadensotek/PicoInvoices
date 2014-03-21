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


public class ClientInvoices extends Activity 
{

	private InvoiceAdapter myDb = null;
	String customerID = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_invoices);
		
		
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
	    //Create the list of items
	    Cursor cursor = myDb.getCustomerInvoice(ClientList.CLIENT_ID);							
		
		//	String array to use as a map for which db rows should be mapped to which element in the template layout
		String[] client_name_list = new String[]{InvoiceAdapter.KEY_ROWID, InvoiceAdapter.KEY_ISSUEDATE, InvoiceAdapter.KEY_STATUS, InvoiceAdapter.KEY_CUSTOMER};
		int[] ints = new int[] {R.id.invoice_listview_layout_template_txtInvoiceNumber, R.id.invoice_listview_layout_template_txtDate, R.id.invoice_listview_layout_template_txtStatus, R.id.invoice_listview_layout_template_CustomerID};
	
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
					TextView textView = (TextView) findViewById(R.id.invoice_listview_layout_template_CustomerID);
					customerID = (String) textView.getText();
					Intent intent1 = new Intent(ClientInvoices.this, ShowDetailedInvoice.class);
					intent1.putExtra("InvoiceID", Long.toString(idInDB));
					intent1.putExtra("CustomerID", customerID);
					 System.out.println(customerID);
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
	    String customer = Long.toString(ClientList.CLIENT_ID);
	    String dateserviceperformed = String.valueOf(new Date());
	    String priceservice = "400";
	    String service = "Mowing";
	    String servicedesc = "Front/back yard";
	    String amountdue = "100";
	    String status = "paid";
	    
	    myDb.insertRow(issuedate, customer, dateserviceperformed, priceservice, service, servicedesc, amountdue, status);
	    refresh();
	}
	
	private String getClientName()
	{
	    String name = "";
	    Cursor cursor = myDb.query(new String[] {Long.toString(ClientList.CLIENT_ID)}, ClientAdapter.DATABASE_TABLE);
	    if (cursor.moveToFirst())
        {
            name+=cursor.getString(ClientAdapter.COL_FNAME);
            name+=" ";
            name+=cursor.getString(ClientAdapter.COL_LNAME);
        }
        else 
            Toast.makeText(ClientInvoices.this, "failed to load", Toast.LENGTH_SHORT).show();
        cursor.close();
	    return name;
	}
}
