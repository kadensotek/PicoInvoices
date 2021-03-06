package com.pico.picoinvoices;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ManageInvoices extends Activity
{

    private Spinner _spinner;
    private InvoiceAdapter _myDb;
    private SPAdapter _sp = null;
    private String _sort = "customer";

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Activity lifecycle functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_invoices);
        initialize();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initialize();
    }
    @Override
    protected void onResume() 
    {
        super.onResume();
        initialize();
    }
    @Override
    protected void onDestroy() 
    {
        super.onDestroy();
        closeDB();
    }
    @Override
    protected void onPause() 
    {
        super.onPause();
        closeDB();
    }
    @Override
    protected void onStop() 
    {
        super.onStop();
        closeDB();
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Action bar functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manage_invoices, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            case R.id.action_addInvoice:
                Intent intent = new Intent(this, AddNewInvoice.class);
                startActivity(intent);
                return true;
            case R.id.goto_Home:
                Intent home = new Intent(this, Home.class);
                startActivity(home);
                return true;
            case R.id.goto_Clients:
                Intent clients = new Intent(this, ClientList.class);
                startActivity(clients);
                return true;
            case R.id.goto_ManageInvoices:
                Intent manage = new Intent(this, ManageInvoices.class);
                startActivity(manage);
                return true;
            case R.id.goto_Services:
                Intent services = new Intent(this, RegisterServices.class);
                startActivity(services);
                return true;
            case R.id.goto_Settings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Database maintenance functions
    // ///*
    // //////////////////////////////////////////////////////
    private void closeDB()
    {
        _myDb.close();
    }

    private void openDB()
    {
        _myDb = new InvoiceAdapter(this);
        _myDb.open();
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Refresh functions
    // ///*
    // //////////////////////////////////////////////////////
    private void initialize()
    {
        _sp = new SPAdapter(getApplicationContext());
        addItemsOnSpinner();
        
        //Make sure that there is a message if the listview is empty
        ListView listView = (ListView) findViewById(R.id.manageInvoices_listView);
        listView.setEmptyView(findViewById(R.id.emptyManageInvoices));
        refresh();
    }
    private void refresh()
    {
        openDB();
        populateListView();
        registerClickCallback();
        closeDB();
    }

    private void populateListView()
    {
        openDB();

        // Create the list of items
        Cursor cursor = _myDb.querySort2(new String[] { _sort },
                InvoiceAdapter.DATABASE_TABLE);

        // String array to use as a map for which db rows should be mapped to
        // which element in the template layout
        
        // client_name_list corresponds to the database columns that should be
        // mapped to the corresponding xml element as specified in ints. NOTE:
        // these mappings are done on directly (ie: InvoiceAdapter.KEY_ROWID maps to
        // R.id.invoice_listview_layout_template_txtInvoiceNumber)
        String[] client_name_list = new String[] { InvoiceAdapter.KEY_ROWID,
                InvoiceAdapter.KEY_ISSUEDATE, InvoiceAdapter.KEY_DUEDATE, InvoiceAdapter.KEY_STATUS,
                InvoiceAdapter.KEY_CUSTOMER };
        int[] ints = new int[] {
                R.id.invoice_listview_layout_template_txtInvoiceNumber,
                R.id.invoice_listview_layout_template_txtDate,
                R.id.invoice_listview_layout_template_txtDateDue,
                R.id.invoice_listview_layout_template_txtStatus,
                R.id.invoice_listview_layout_template_CustomerID };

        // Create the adapter that will bind the data from the DB to the listview
        ListViewAdapter adapter = new ListViewAdapter(this,R.layout.invoice_listview_layout_template, cursor,client_name_list, ints, 0);

        ListView list = (ListView) findViewById(R.id.manageInvoices_listView);
        list.setAdapter(adapter);
        closeDB();
    }

    private void registerClickCallback()
    {
        ListView list = (ListView) findViewById(R.id.manageInvoices_listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                    int position, long idInDB)
            {
                // Note that the viewClicked needs to be specified here so that
                // the selected item in the list will be used - not just the
                // first in the lists
                // Instead of passing the client's id number from activity to
                // activity, the clientID will be coupled with the invoice
                // directly
                TextView textView = (TextView) viewClicked
                        .findViewById(R.id.invoice_listview_layout_template_CustomerID);
                String customerID = textView.getText().toString();

                // Now use the intent to send the information to the final
                // activity - ShowDetailedInvoice)
                Intent intent1 = new Intent(ManageInvoices.this,
                        ShowDetailedInvoice.class);
                _sp.saveClientID(customerID);
                _sp.saveInvioceID(Long.toString(idInDB));

                System.out.println(customerID);
                startActivity(intent1);
            }
        });
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* add items into spinner dynamically
    // ///*
    // //////////////////////////////////////////////////////
    public void addItemsOnSpinner()
    {
        _spinner = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add(InvoiceAdapter.KEY_CUSTOMER);
        list.add(InvoiceAdapter.KEY_DUEDATE);
        list.add(InvoiceAdapter.KEY_ISSUEDATE);
        list.add(InvoiceAdapter.KEY_STATUS);
        
        List<String> shownList = new ArrayList<String>();
        shownList.add("Client");
        shownList.add("Date Due");
        shownList.add("Date Issued");
        shownList.add("Current Status");
        
        MyAdapter dataAdapter = new MyAdapter(this,
                R.layout.spinner_text_layout,shownList,list);
        _spinner.setAdapter(dataAdapter);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        _spinner.setAdapter(dataAdapter);
        addListenerOnSpinnerItemSelection(_spinner);
    }

    public void addListenerOnSpinnerItemSelection(Spinner spinner)
    {
        spinner = (Spinner) findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int pos, long id)
            {
                System.out.println("You selected: " + pos);
                TextView tv = (TextView) view.findViewById(R.id.spinnerText2);
                System.out.println("TextView is: " + tv.getText().toString());
                _sort = tv.getText().toString();
                refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                return;
            }
        });
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* On_Click method that was specified in the XML properties
    // ///*
    // //////////////////////////////////////////////////////
    public void onClick_SortInvoices(View v)
    {
        refresh();
    }

    private class MyAdapter extends ArrayAdapter<String>
    {
        ArrayList<String> l = null, l2 =null;

        public MyAdapter(Context context, int textViewResourceId,List<String> list, List<String> list2)
        {
            super(context, textViewResourceId, list);
            l = (ArrayList<String>) list;
            l2 = (ArrayList<String>) list2;
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent)
        {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView,
                ViewGroup parent)
        {
            //Only return a row if there are elements (clients and services have been registered) in the arraylists
           if (l.size() > 0 && l2.size() > 0)
           {
                LayoutInflater inflater = getLayoutInflater();
                View row = inflater.inflate(R.layout.spinner_text_layout, parent,false);
                TextView label = (TextView) row.findViewById(R.id.spinnerText1);
                // if (position > 0)
                // label.setText(l.get(position-1));
                // else
                label.setText(l.get(position));
    
                TextView sub = (TextView) row.findViewById(R.id.spinnerText2);
                
                if (position > 0)
                    sub.setText(l2.get(position));
                else
                    sub.setText(l2.get(position));
    
                return row;
           }
           else
           {
               return null;
           }
        }

    }
}