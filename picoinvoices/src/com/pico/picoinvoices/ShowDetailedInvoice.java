package com.pico.picoinvoices;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class ShowDetailedInvoice extends ExpandableListActivity
{
    // Create ArrayList to hold parent Items and Child Items
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    private InvoiceAdapter myDb = null;
    private String fname, lname, address, email, phone;
    private String issuedate, service, dateserviceperformed, priceservice, servicedesc, amountdue, status;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {

        super.onCreate(savedInstanceState);

      
        openDB();
        populateValues();
        
        
        // Create Expandable List and set it's properties
        ExpandableListView expandableList = getExpandableListView(); 
        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        // Set the Items of Parent
        setGroupParents();
        // Set The Child Data
        setChildData();

        // Create the Adapter
        ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(parentItems, childItems);

        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        
        // Set the Adapter to expandableList
        expandableList.setAdapter(adapter);
        expandableList.setOnChildClickListener(this);
    }
    protected void onDestroy() 
    {
        super.onDestroy();
        closeDB();
    }
    protected void onStop() 
    {
        super.onStop();
        closeDB();
    }
    protected void onPause() 
    {
        super.onPause();
        closeDB();
    }
    private void closeDB() 
    {
        myDb.close();
    }
    private void openDB() 
    {
        myDb = new InvoiceAdapter(this);
        myDb.open();
    }

    // method to add parent Items
    public void setGroupParents() 
    {
        parentItems.add("Contact Info - " + fname +" "+ lname);
        parentItems.add("Invoice - " + amountdue);
    }

    // method to set child data of each parent
    public void setChildData() 
    {

        // Add Child Items for Contact
        ArrayList<String[]> child = new ArrayList<String[]>();
        child.add(new String[] {"First", fname});
        child.add(new String[] {"Last", lname});
        child.add(new String[] {"Phone", phone});
        child.add(new String[] {"EMail",email});
        child.add(new String[] {"Address",address});
        
        childItems.add(child);

        // Add Child Items for Invoice
        child = new ArrayList<String[]>();
        child.add(new String[] {"Issue Date", issuedate});
        child.add(new String[] {"Date Performed", dateserviceperformed});
        child.add(new String[] {"Price Charged", priceservice});
        child.add(new String[] {"Service", service});
        child.add(new String[] {"Description",servicedesc});
        child.add(new String[] {"Status",status});
        child.add(new String[] {"Amount Due",amountdue});
        
        childItems.add(child);

    }
    private void populateValues()
    {
        //Populate the values for the contact information.
        Cursor cursor = myDb.query(new String[] {Long.toString(ClientList.CLIENT_ID)}, ClientAdapter.DATABASE_TABLE);
        if (cursor.moveToFirst())
        {
            fname  = cursor.getString(ClientAdapter.COL_FNAME);
            lname = cursor.getString(ClientAdapter.COL_LNAME);
            address = cursor.getString(ClientAdapter.COL_ADDRESS);
            email = cursor.getString(ClientAdapter.COL_EMAIL);
            phone = cursor.getString(ClientAdapter.COL_PHONE);
        }
        else 
            Toast.makeText(ShowDetailedInvoice.this, "failed to load cursor", Toast.LENGTH_SHORT).show();
        
       
        //Populate invoice specific information
        cursor = myDb.query(new String[] {Long.toString(ClientInvoices.INVOICE_ID)}, InvoiceAdapter.DATABASE_TABLE);
        if (cursor.moveToFirst())
        {
            issuedate  = cursor.getString(InvoiceAdapter.COL_ISSUEDATE);
            service = cursor.getString(InvoiceAdapter.COL_SERVICE);
            dateserviceperformed = cursor.getString(InvoiceAdapter.COL_DATESERVICEPERFORMED);
            priceservice = cursor.getString(InvoiceAdapter.COL_PRICESERVICE);
            servicedesc = cursor.getString(InvoiceAdapter.COL_SERVICEDESC);
            amountdue = cursor.getString(InvoiceAdapter.COL_AMOUNTDUE);
            status = cursor.getString(InvoiceAdapter.COL_STATUS);
        }
        else 
            Toast.makeText(ShowDetailedInvoice.this, "failed to load cursor", Toast.LENGTH_SHORT).show();
        cursor.close();
        
    }
}
