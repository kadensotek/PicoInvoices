package com.pico.picoinvoices;

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
	private long customer_ID = 0;
	private String customer_fname = "";
	private String customer_lname = "";
	private String customer_address = "";
	private String customer_phone = "";
	private String customer_email = "";
	private int INSERT_ID = Menu.FIRST;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_invoices);
		
		Bundle extras = getIntent().getExtras(); 
		customer_ID = extras.getLong("customerID");
		customer_fname = extras.getString("fname");
		customer_lname = extras.getString("lname");
		customer_address = extras.getString("address");
		customer_phone = extras.getString("phone");
		customer_email = extras.getString("email");

		TextView customerName = (TextView) findViewById(R.id.client_invoices_txtClientName);
		customerName.setText(customer_fname);
		openDB();
		refresh();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
	        boolean result = super.onCreateOptionsMenu(menu);
	        menu.add(0, INSERT_ID, 0, "Add New");
	        return result;
	}
	@Override
	protected void onDestroy() 
	{
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
<<<<<<< HEAD
	private void populateListView() 
=======
	private void populateListView()
>>>>>>> d29360a61d29711889f8f278783e7621b022288f
	{
		Cursor cursor = myDb.getCustomerInvoice(customer_ID);							//Create the list of items
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
					long  customer = cursor.getLong(InvoiceAdapter.COL_CUSTOMER);
					String issuedate = cursor.getString(InvoiceAdapter.COL_ISSUEDATE);
					String dateserviceperformed = cursor.getString(InvoiceAdapter.COL_DATESERVICEPERFORMED);
					String servicedesc = cursor.getString(InvoiceAdapter.COL_SERVICEDESC);
					String status = cursor.getString(InvoiceAdapter.COL_STATUS);
					String priceservice = cursor.getString(InvoiceAdapter.COL_PRICESERVICE);
					
					String message = "Rowid: " + idDB + "\n" + customer +"\n" + issuedate +"\n"+ dateserviceperformed + "\n" + 
							servicedesc + "\n" + status + "\n" + priceservice + "Contact Info: " + customer_fname + " " +customer_lname
							+"\n" + customer_address + "\n" + customer_phone + "\n" + customer_email;
					Toast.makeText(ClientInvoices.this, message, Toast.LENGTH_LONG).show();
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
		Intent goToInvoices = new Intent(this, AddNewInvoice.class);
		goToInvoices.putExtra("customerID", customer_ID);
		goToInvoices.putExtra("fname", customer_fname);
		goToInvoices.putExtra("lname", customer_lname);
		goToInvoices.putExtra("address", customer_address);
		goToInvoices.putExtra("phone", customer_phone);
		goToInvoices.putExtra("email", customer_email);
		startActivity(goToInvoices);
	}

}
