package com.pico.picoinvoices;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class ShowDetailedInvoice extends Activity
{

    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> invoice;
    ExpandableListView expListView;
    private InvoiceAdapter myDb = null;
    private String invoiceID ,customerID = "";
    private String fname, lname, address, email, phone;
    private String issuedate, service, dateserviceperformed, priceservice, servicedesc, amountdue, status;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detailed_invoice);
        
        //get the invoice id and customer id from the previous activity
        Intent intent = getIntent();
        invoiceID = intent.getStringExtra("InvoiceID");
        customerID = intent.getStringExtra("CustomerID");
        
        System.out.println(invoiceID + "  " + customerID);
        
        openDB();
        
        populateValues();
        
        createGroupList();

        createCollection();

        expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        final ExpandableListViewAdapter expListAdapter = new ExpandableListViewAdapter(this, groupList, invoice);
        expListView.setAdapter(expListAdapter);

        // setGroupIndicatorToRight();

        expListView.setOnChildClickListener(new OnChildClickListener()
        {

            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id)
            {
                final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }
    
    @Override

    protected void onDestroy()
    {
        super.onDestroy();
        closeDB();
    }
    
    /*
     *  Database maintenance functions
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
    private void populateValues()
    {
        // Populate the values for the contact information.
        Cursor cursor = myDb.query(new String[] { customerID },ClientAdapter.DATABASE_TABLE);
        if (cursor.moveToFirst())
        {
            fname = cursor.getString(ClientAdapter.COL_FNAME);
            lname = cursor.getString(ClientAdapter.COL_LNAME);
            address = cursor.getString(ClientAdapter.COL_ADDRESS);
            email = cursor.getString(ClientAdapter.COL_EMAIL);
            phone = cursor.getString(ClientAdapter.COL_PHONE);
        } else
            Toast.makeText(ShowDetailedInvoice.this, "failed to load cursor",
                    Toast.LENGTH_SHORT).show();

        // Populate invoice specific information
        cursor = myDb.query(new String[] { invoiceID },InvoiceAdapter.DATABASE_TABLE);
        if (cursor.moveToFirst())
        {
            issuedate = cursor.getString(InvoiceAdapter.COL_ISSUEDATE);
            service = cursor.getString(InvoiceAdapter.COL_SERVICE);
            dateserviceperformed = cursor.getString(InvoiceAdapter.COL_DATESERVICEPERFORMED);
            priceservice = cursor.getString(InvoiceAdapter.COL_PRICESERVICE);
            servicedesc = cursor.getString(InvoiceAdapter.COL_SERVICEDESC);
            amountdue = cursor.getString(InvoiceAdapter.COL_AMOUNTDUE);
            status = cursor.getString(InvoiceAdapter.COL_STATUS);
        } else
            Toast.makeText(ShowDetailedInvoice.this, "failed to load cursor",
                    Toast.LENGTH_SHORT).show();
        cursor.close();

    }

    private void createGroupList()
    {
        groupList = new ArrayList<String>();
        groupList.add("Contact Info - " + fname + " " + lname);
        groupList.add("Invoice - Amount Due: $" + amountdue);
    }

 
    private void createCollection()
    {
        // preparing laptops collection(child)
        String[] contactInfo = { fname, lname, address, email, phone };
        String[] invoiceDetail = { issuedate, service, dateserviceperformed, priceservice, servicedesc, amountdue, status };
       

        invoice = new LinkedHashMap<String, List<String>>();

        for (String row : groupList)
        {
            if (row.equals("Contact Info - " + fname + " " + lname))
            {
                loadChild(contactInfo);
            } 
            else if (row.equals("Invoice - Amount Due: $" + amountdue))
                loadChild(invoiceDetail);
            

            invoice.put(row, childList);
        }
    }

    private void loadChild(String[] invoiceInfo)
    {
        childList = new ArrayList<String>();
        for (String line : invoiceInfo)
            childList.add(line);
    }

    @SuppressWarnings("unused")
    private void setGroupIndicatorToRight()
    {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_detailed_invoice, menu);
        return true;
    }
}