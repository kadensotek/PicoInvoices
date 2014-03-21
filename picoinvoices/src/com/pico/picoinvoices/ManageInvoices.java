package com.pico.picoinvoices;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ManageInvoices extends Activity
{

    private Spinner spinner2;
    private InvoiceAdapter myDb;
    public static long CLIENT_ID = 0;
    private int toggleOrder = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_invoices);
        openDB();
        addItemsOnSpinner();
        addListenerOnSpinnerItemSelection();
        refresh();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        closeDB();
    }
    protected void onResume()
    {
        super.onResume();
        openDB();
    }
    
    
    
    ////////////////////////////////////////////////////////
    /////*
    //// *  Database maintenance functions
    //// */
    ////////////////////////////////////////////////////////
    private void closeDB() 
    {
        myDb.close();
    }
    private void openDB() 
    {
        myDb = new InvoiceAdapter(this);
        myDb.open();
    }
    
    
    
    ////////////////////////////////////////////////////////
    //////*
    ///// *  refresh functions
    ///// */
    ///////////////////////////////////////////////////////
    private void refresh()
    {
        populateListView();
        registerClickCallback();
    }
    
    @SuppressWarnings("deprecation")
    private void populateListView() 
    {
        
        Cursor cursor = myDb.querySort2(new String[] {SpinnerAdapter.sort}, InvoiceAdapter.DATABASE_TABLE);                              //Create the list of items
//        Cursor cursor = myDb.getCustomerInvoice(1);

        //  String array to use as a map for which db rows should be mapped to which element in the template layout
        String[] client_name_list = new String[]{InvoiceAdapter.KEY_ROWID, InvoiceAdapter.KEY_ISSUEDATE, InvoiceAdapter.KEY_STATUS, InvoiceAdapter.KEY_CUSTOMER};
        int[] ints = new int[] {R.id.invoice_listview_layout_template_txtInvoiceNumber, R.id.invoice_listview_layout_template_txtDate, R.id.invoice_listview_layout_template_txtStatus, R.id.invoice_listview_layout_template_CustomerID};
    
        
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.invoice_listview_layout_template, cursor,client_name_list , ints);
        
        ListView list = (ListView) findViewById(R.id.manageInvoices_listView);
        list.setAdapter(adapter);
        
    }
    
    private void registerClickCallback() 
    {
        ListView list = (ListView) findViewById(R.id.manageInvoices_listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) 
            {
                    TextView textView = (TextView) findViewById(R.id.invoice_listview_layout_template_CustomerID);
                    String customerID = (String) textView.getText();
                    
                    Intent intent1 = new Intent(ManageInvoices.this, ShowDetailedInvoice.class);
                    intent1.putExtra("InvoiceID", Long.toString(idInDB));
                    intent1.putExtra("CustomerID", customerID);
                    
                    System.out.println(customerID);
                    startActivity(intent1);
            }
        });
    }
    
    
    ////////////////////////////////////////////////////////
    /////*
    //// * add items into spinner dynamically
    //// *
    ////////////////////////////////////////////////////////
    public void addItemsOnSpinner()
    {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add(InvoiceAdapter.KEY_CUSTOMER);
        list.add(InvoiceAdapter.KEY_ISSUEDATE);
        list.add(InvoiceAdapter.KEY_STATUS);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection()
    {
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new SpinnerAdapter());
    }
    
    ////////////////////////////////////////////////////////
    /////*
    //// * Use an inner class so that I can access the Invoice Adapter.
    //// *
    ////////////////////////////////////////////////////////
    public void onClick_SortInvoices(View v)
    {
        refresh();
    }
    

}