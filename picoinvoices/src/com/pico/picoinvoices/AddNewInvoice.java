package com.pico.picoinvoices;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class AddNewInvoice extends Activity {

	private InvoiceAdapter myDb = null;
	private long customer_ID = 0;
	private String customer_fname = "";
	private String customer_lname = "";
	private String customer_address = "";
	private String customer_phone = "";
	private String customer_email = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_invoice);
		
		Bundle extras = getIntent().getExtras(); 
		customer_ID = extras.getLong("customerID");
		customer_fname = extras.getString("fname");
		customer_lname = extras.getString("lname");
		customer_address = extras.getString("address");
		customer_phone = extras.getString("phone");
		customer_email = extras.getString("email");

		TextView customerName = (TextView) findViewById(R.id.add_new_invoice_txtInfo);
		customerName.setText(customer_fname +"\n"+ customer_lname +"\n"+ customer_address + "\n"+ customer_email + "\n" + customer_phone);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_invoice, menu);
		return true;
	}

	
//	String issuedate = new Date().toString();
//	Long customer = customer_ID;
//	String dateserviceperformed = new Date().toString();
//	String servicedesc = "Snow plowing";
//	String status = "Pending";
//	String priceservice = "$350";
//	String amountdue = "$350";
//	myDb.insertRow(issuedate, customer, dateserviceperformed, priceservice, servicedesc, amountdue, status);
//	
//	refresh();
}
