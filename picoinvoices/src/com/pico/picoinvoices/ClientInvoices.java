package com.pico.picoinvoices;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ClientInvoices extends Activity
{

    private InvoiceAdapter _myDb = null;
    private SPAdapter _sp = null;

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Activity lifecycle functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_invoices);
        initialize();
    }

    @Override
    protected void onRestart()
    {
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
        getMenuInflater().inflate(R.menu.client_invoices, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            case R.id.action_report:
                onClick_Reports();
                return true;
            case R.id.action_editClient:
                Intent edit = new Intent(this, EditClientInfo.class);
                startActivity(edit);
                return true;
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
    // ///* Database open/close
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
        // Setup SharedPreferences to access ClientId and InvoiceID
        _sp = new SPAdapter(getApplicationContext());

        // Refresh function for populating new invoices to the listview
        refresh();

        // Set the TextView to the name of the current client the user is
        // viewing
        TextView textView = (TextView) findViewById(R.id.client_invoices_txtClientName);
        textView.setText(getClientName());

        // Make sure that there is a message if the listview is empty
        ListView listView = (ListView) findViewById(R.id.client_invoices_listView);
        listView.setEmptyView(findViewById(R.id.emptyInvoiceList));
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
        Cursor cursor = _myDb.getCustomerInvoice(_sp.getClientID());

        // String array to use as a map for which db rows should be mapped to
        // which element in the template layout
        String[] client_name_list = new String[] { InvoiceAdapter.KEY_ROWID,
                InvoiceAdapter.KEY_ISSUEDATE, InvoiceAdapter.KEY_DUEDATE,
                InvoiceAdapter.KEY_STATUS, InvoiceAdapter.KEY_CUSTOMER };

        System.out.println("Does this trip");

        int[] ints = new int[] {
                R.id.invoice_listview_layout_template_txtInvoiceNumber,
                R.id.invoice_listview_layout_template_txtDate,
                R.id.invoice_listview_layout_template_txtDateDue,
                R.id.invoice_listview_layout_template_txtStatus,
                R.id.invoice_listview_layout_template_CustomerID };

        // Set the custom adapter to the data from the client to create the list
        // of invoices
        ListViewAdapter adapter = new ListViewAdapter(this,
                R.layout.invoice_listview_layout_template, cursor,
                client_name_list, ints, 0);
        ListView list = (ListView) findViewById(R.id.client_invoices_listView);
        list.setAdapter(adapter);

        closeDB();
    }

    private void registerClickCallback()
    {
        ListView list = (ListView) findViewById(R.id.client_invoices_listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                    int position, long idInDB)
            {
                Intent intent1 = new Intent(ClientInvoices.this,
                        ShowDetailedInvoice.class);
                // Update the value of the invoiceID so that ShowDetailedInvoice
                // will display the correct invoice
                _sp.saveInvioceID(Long.toString(idInDB));
                // System.out.println("Client Id (ClientInvoices):" +
                // _sp.getClientID());
                // System.out.println("Invoice Id (ClientInvoices):" +
                // _sp.getInvoiceID());
                startActivity(intent1);
            }
        });
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Utility functions
    // ///*
    // //////////////////////////////////////////////////////

    private String getClientName()
    {
        openDB();
        String name = "";
        Cursor cursor = _myDb.query(
                new String[] { Long.toString(_sp.getClientID()) },
                ClientAdapter.DATABASE_TABLE);

        if (cursor.moveToFirst())
        {
            name += cursor.getString(ClientAdapter.COL_FNAME);
            name += " ";
            name += cursor.getString(ClientAdapter.COL_LNAME);
        } else
        {
            Toast.makeText(ClientInvoices.this, "Failed to load client name.",
                    Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        closeDB();
        return name;
    }

    // ////////////////////////////////////////////////////////
    // /////*
    // /////* OnClick listener for reports
    // /////*
    // ////////////////////////////////////////////////////////

    // TODO Implement method to disable this if no invoices. Crashes now
    public void onClick_Reports()
    {
        System.out.println("Reports");
        Intent intent = new Intent(this, Reports.class);
        startActivity(intent);
    }
}
