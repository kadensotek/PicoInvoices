package com.pico.picoinvoices;

import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ClientInvoices extends Activity {

	InvoiceAdapter myDb = null;
	long customerID = 0;
	String fname = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_invoices);
		
		Bundle extras = getIntent().getExtras(); 
		customerID = extras.getLong("customerID");
		fname = extras.getString("fname");
		TextView customerName = (TextView) findViewById(R.id.client_invoices_txtClientName);
		customerName.setText(fname);
		openDB();
		refresh();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client_invoices, menu);
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
	private void populateListView() {
		Cursor cursor = myDb.getCustomerInvoice(customerID);							//Create the list of items
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
				
				Cursor cursor = myDb.getRow(idInDB);
				if (cursor.moveToFirst())
				{
					long idDB = cursor.getLong(InvoiceAdapter.COL_ROWID);
					String customer = cursor.getString(InvoiceAdapter.COL_CUSTOMER);
					String issuedate = cursor.getString(InvoiceAdapter.COL_ISSUEDATE);
					String dateserviceperformed = cursor.getString(InvoiceAdapter.COL_DATESERVICEPERFORMED);
					String servicedesc = cursor.getString(InvoiceAdapter.COL_SERVICEDESC);
					String status = cursor.getString(InvoiceAdapter.COL_STATUS);
					String priceservice = cursor.getString(InvoiceAdapter.COL_PRICESERVICE);
					
					String message = "Rowid: " + idDB + "\n" + customer +" " + issuedate +"\n"+ dateserviceperformed + "\n" + 
							servicedesc + "\n" + status + "\n" + priceservice;
					Toast.makeText(ClientInvoices.this, message, Toast.LENGTH_SHORT).show();
				}
				else 
					Toast.makeText(ClientInvoices.this, "failed to load "+idInDB, Toast.LENGTH_SHORT).show();
				cursor.close();
				
			}
		});
		
	}
	
	
	
	/*
	 * 	onClickListeners are implemented here
	 */
	public void onClick_AddInvoice(View v)
	{
		String issuedate = new Date().toString();
		String customer = Long.toString(customerID);
		String dateserviceperformed = new Date().toString();
		String servicedesc = "Snow plowing";
		String status = "Pending";
		String priceservice = "$350";
		String amountdue = "$350";
		myDb.insertRow(issuedate, customer, dateserviceperformed, priceservice, servicedesc, amountdue, status);
		
		refresh();
	}

}
