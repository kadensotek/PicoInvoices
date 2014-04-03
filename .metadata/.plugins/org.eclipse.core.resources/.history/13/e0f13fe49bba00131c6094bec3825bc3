package com.pico.picoinvoices;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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

    private List<String> _groupList;
    private List<String> _childList;
    private Map<String, List<String>> _invoice;
    private ExpandableListView _expListView;
    private InvoiceAdapter _myDb = null;
    private SPAdapter _sp = null;
    private String _fname, _lname, _address, _email, _phone;
    private String _issuedate, _service, _dateserviceperformed, _priceservice, _servicedesc, _amountdue, _status;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detailed_invoice);
                
        _sp = new SPAdapter(getApplicationContext());
        System.out.println("Client Id (ShowDetailedInvoices):" + _sp.getClientID());
        System.out.println("Invoice Id (ShowDetailedInvoices):" + _sp.getInvoiceID());
        openDB();
        
        populateValues();
        
        createGroupList();

        createCollection();

        _expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        final ExpandableListViewAdapter expListAdapter = new ExpandableListViewAdapter(this, _groupList, _invoice);
        _expListView.setAdapter(expListAdapter);

        // setGroupIndicatorToRight();

        _expListView.setOnChildClickListener(new OnChildClickListener()
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
        _myDb.close();
    }
    private void openDB() 
    {
        _myDb = new InvoiceAdapter(this);
        _myDb.open();
    }
    private void populateValues()
    {
        // Populate the values for the contact information.
        Cursor cursor = _myDb.query(new String[] { Integer.toString(_sp.getClientID()) },ClientAdapter.DATABASE_TABLE);
        if (cursor.moveToFirst())
        {
            _fname = cursor.getString(ClientAdapter.COL_FNAME);
            _lname = cursor.getString(ClientAdapter.COL_LNAME);
            _address = cursor.getString(ClientAdapter.COL_ADDRESS);
            _email = cursor.getString(ClientAdapter.COL_EMAIL);
            _phone = cursor.getString(ClientAdapter.COL_PHONE);
        } else
            Toast.makeText(ShowDetailedInvoice.this, "failed to load cursor for customerId " + _sp.getClientID(),
                    Toast.LENGTH_SHORT).show();

        // Populate invoice specific information
        cursor = _myDb.query(new String[] { Integer.toString(_sp.getInvoiceID()) },InvoiceAdapter.DATABASE_TABLE);
        if (cursor.moveToFirst())
        {
            _issuedate = cursor.getString(InvoiceAdapter.COL_ISSUEDATE);
            _service = cursor.getString(InvoiceAdapter.COL_SERVICE);
            _dateserviceperformed = cursor.getString(InvoiceAdapter.COL_DATESERVICEPERFORMED);
            _priceservice = cursor.getString(InvoiceAdapter.COL_PRICESERVICE);
            _servicedesc = cursor.getString(InvoiceAdapter.COL_SERVICEDESC);
            _amountdue = cursor.getString(InvoiceAdapter.COL_AMOUNTDUE);
            _status = cursor.getString(InvoiceAdapter.COL_STATUS);
        } else
            Toast.makeText(ShowDetailedInvoice.this, "failed to load cursor for invoiceID " + _sp.getInvoiceID(),
                    Toast.LENGTH_SHORT).show();
        cursor.close();

    }

    private void createGroupList()
    {
        _groupList = new ArrayList<String>();
        _groupList.add("Contact Info - " + _fname + " " + _lname);
        _groupList.add("Invoice - Amount Due: $" + _amountdue);
    }

 
    private void createCollection()
    {
        // preparing laptops collection(child)
        String[] contactInfo = { _fname, _lname, _address, _email, _phone };
        String[] invoiceDetail = { _issuedate, _service, _dateserviceperformed, _priceservice, _servicedesc, _amountdue, _status };
       

        _invoice = new LinkedHashMap<String, List<String>>();

        for (String row : _groupList)
        {
            if (row.equals("Contact Info - " + _fname + " " + _lname))
            {
                loadChild(contactInfo);
            } 
            else if (row.equals("Invoice - Amount Due: $" + _amountdue))
                loadChild(invoiceDetail);
            

            _invoice.put(row, _childList);
        }
    }

    private void loadChild(String[] invoiceInfo)
    {
        _childList = new ArrayList<String>();
        for (String line : invoiceInfo)
            _childList.add(line);
    }

//    @SuppressWarnings("unused")
//    private void setGroupIndicatorToRight()
//    {
//        /* Get the screen width */
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//
//        _expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
//                - getDipsFromPixel(5));
//    }
//
//    // Convert pixel to dip
//    public int getDipsFromPixel(float pixels)
//    {
//        // Get the screen's density scale
//        final float scale = getResources().getDisplayMetrics().density;
//        // Convert the dps to pixels, based on density scale
//        return (int) (pixels * scale + 0.5f);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_detailed_invoice, menu);
        return true;
    }
}