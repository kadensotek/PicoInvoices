package com.pico.picoinvoices;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class AddNewInvoice extends Activity
{

    private InvoiceAdapter myDb = null;
    private Spinner customerSpinner = null, serviceSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_invoice);

        //  load the services and customers into the proper spinner element
        addItemsOnSpinner(customerSpinner, R.id.addNewInvoice_customerSpinner, "customer");
        addItemsOnSpinner(serviceSpinner, R.id.addNewInvoice_serviceSpinner, "services");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_invoice, menu);
        return true;
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Database open/close
    // ///*
    // //////////////////////////////////////////////////////
    private void closeDB()
    {
        myDb.close();
    }

    private void openDB()
    {
        myDb = new InvoiceAdapter(this);
        myDb.open();
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* add items into spinner dynamically
    // ///*
    // //////////////////////////////////////////////////////
    public void addItemsOnSpinner(Spinner spinner, int spinnerID, String cs)
    {
        List<String> list = null;
        spinner = (Spinner) findViewById(spinnerID);
        if (cs.equals("services"))
        {
             list = getServices();
        }
        else
        {
             list = getCustomers();
        }
        
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection(Spinner spinner, int spinnerID)
    {
        spinner = (Spinner) findViewById(spinnerID);
        spinner.setOnItemSelectedListener(new SpinnerAdapter());
    }

    //  Get the values from the services table to populate into an ArrayList to load into the services spinner
    private ArrayList<String> getServices()
    {
        openDB();
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = myDb.getAllRows(RegisterServicesAdapter.DATABASE_TABLE, RegisterServicesAdapter.ALL_KEYS);
        cursor.moveToFirst();
        do
        {
            if (list.contains(cursor.getString(RegisterServicesAdapter.COL_NAME)))
            {
                
            }
            else
            {
                list.add(cursor.getString(RegisterServicesAdapter.COL_NAME));
            }
        } while (cursor.moveToNext());
        cursor.close();
        closeDB();
        return list;
    }
    
    //  Get the values from the customer table to populate into an ArrayList to load into the customer spinner
    private ArrayList<String> getCustomers()
    {
        openDB();
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = myDb.getAllRows(ClientAdapter.DATABASE_TABLE, ClientAdapter.ALL_KEYS);
        cursor.moveToFirst();
        do
        {
            if (list.contains(cursor.getString(ClientAdapter.COL_FNAME) + " " + cursor.getString(ClientAdapter.COL_LNAME)))
            {
                
            }
            else
            {
                list.add(cursor.getString(ClientAdapter.COL_FNAME) + " " + cursor.getString(ClientAdapter.COL_LNAME));
            }
            
        } while (cursor.moveToNext());
        cursor.close();
        closeDB();
        return list;
    }
    
    public void onClick_addServiceDyn(View v)
    {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.addNewInvoiceLayout);
        Spinner t = new Spinner(AddNewInvoice.this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.addNewInvoice_rateInput);
        layout.addView(t, params);
    }

}
