package com.pico.picoinvoices;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class AddNewInvoice extends Activity
{
    //Database adpater
    private InvoiceAdapter myDb = null;
    //Used to track the element to add new elements to
    private int nextBelowID = R.id.addNewInvoice_rateInput;
    private  int value=1;
    private String customerID="0", customerName="0";
    //Array lists are used to store the View, Spinner, and EditText that are created dynamically 
    //So they can be referenced for remove and data retrieval 
    private ArrayList<Integer> rIdStore = new ArrayList<Integer>(), customerId = new ArrayList<Integer>();
    private ArrayList<Spinner> rIdStore_spinners = new ArrayList<Spinner>();
    private ArrayList<EditText> rIdStore_editText = new ArrayList<EditText>();

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Activity Lifecycle functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_invoice);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_invoice, menu);
        return true;
    }
    //Create initial spinners and add items to them
    private void initialize()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        Intent intent = getIntent();
        if (intent != null)
            customerID = intent.getStringExtra("customerID");
        Spinner customerSpinner = (Spinner) findViewById(R.id.addNewInvoice_customerSpinner);
        Spinner serviceSpinner = (Spinner) findViewById(R.id.addNewInvoice_serviceSpinner);
        // load the services and customers into the proper spinner element
        addItemsOnSpinner(customerSpinner, "customer");
        addItemsOnSpinner(serviceSpinner, "services");
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
    public void addItemsOnSpinner(Spinner spinner,  String cs)
    {
        List<String> list = null;
        if (cs.equals("services"))
        {
            list = getServices();
        } else
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                    long id)
            {
                
            }

            public void onNothingSelected(AdapterView<?> adapterView)
            {
                return;
            }
        });
    }

    // Get the values from the services table to populate into an ArrayList to
    // load into the services spinner
    private ArrayList<String> getServices()
    {
        openDB();
        ArrayList<String> list = new ArrayList<String>();
        list.add("-");
        Cursor cursor = myDb.getAllRows(RegisterServicesAdapter.DATABASE_TABLE,
                RegisterServicesAdapter.ALL_KEYS);
        cursor.moveToFirst();
        if(cursor != null)
        {
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
        }
        
        cursor.close();
        closeDB();
        return list;
    }

    // Get the values from the customer table to populate into an ArrayList to
    // load into the customer spinner
    private ArrayList<String> getCustomers()
    {
        openDB();
        ArrayList<String> list = new ArrayList<String>();
        list.add("-");
        Cursor cursor = myDb.getAllRows(ClientAdapter.DATABASE_TABLE,ClientAdapter.ALL_KEYS);
        cursor.moveToFirst();
        if (cursor != null)
        {
            do
            {
                //only allow one of each instance into the dropdown
                if (list.contains(cursor.getString(ClientAdapter.COL_FNAME) + " " + cursor.getString(ClientAdapter.COL_LNAME)))
                {
    
                } 
                else
                {
                    list.add(cursor.getString(ClientAdapter.COL_FNAME) + " " + cursor.getString(ClientAdapter.COL_LNAME));
                    customerId.add(cursor.getInt(ClientAdapter.COL_ROWID));
//                    System.out.println(customerID);
//                    if(cursor.getLong(ClientAdapter.COL_ROWID) == Integer.parseInt(customerID) )
//                    {
//                        customerName = cursor.getString(ClientAdapter.COL_FNAME) + " " + cursor.getString(ClientAdapter.COL_LNAME);
//                        setSelection();
//                    }
                }
    
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return list;
    }
    private void setSelection()
    {
        Spinner spinner = (Spinner) findViewById(R.id.addNewInvoice_customerSpinner);
        
        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

        int spinnerPosition = myAdap.getPosition(customerName);

        //set the default according to value
        spinner.setSelection(spinnerPosition);
    }
    ////////////////////////////////////////////////////////
    /////*
    /////*  OnClick listener for a new dynamic service
    /////*
    ////////////////////////////////////////////////////////
    public void onClick_addInvoice(View v)
    {
        String services = "";
        String rates ="";
        
        //Makes sure that the original edittext and spinner are added as well
        Spinner s = (Spinner) findViewById(R.id.addNewInvoice_serviceSpinner);
        services = services + s.getSelectedItem().toString() + "||";
        
        EditText et = (EditText) findViewById(R.id.addNewInvoice_rateInput);
        rates = rates + et.getText().toString() + "||";
        
        for(int i = 0; i < rIdStore_spinners.size(); i++)
        {
            Spinner s2 = rIdStore_spinners.get(i);
            services = services + s2.getSelectedItem().toString() + "||";
        }
        for(int i = 0; i < rIdStore_editText.size(); i++)
        {
            EditText et2 = rIdStore_editText.get(i);
            rates = rates + et2.getText().toString() + "||";
        }
        
        System.out.println(services + "\n\n\n" + rates);
    }
    public void onClick_cancelInvoice(View v)
    {
        finish();
    }
    // remove the last element from the list
    public void onClick_removeServiceDyn(View v)
    {
        //  prevent the subtraction button from replying to any subtraction if there are no additional serverices added
        if(rIdStore.size() > 0)
        {
            //  This relative view references the internal relativelayout view inside scroll view
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.addNewInvoiceLayout);
            int size = rIdStore.size() - 1;
            
            //  Use the ArrayList of stored, newly added spinners to retrieve as a 'View' and remove them from last to first
            View view = (View) findViewById(rIdStore.get(size));
            layout.removeView(view);
            if(rIdStore.size() > 1)
            {
                rIdStore.remove(size);
                nextBelowID = rIdStore.get(size-1);
            }
            
            //  This else is used to set the default spinner view to the original view so that the add/cancel buttons are flush 
            else
            {
                rIdStore.remove(0);
                nextBelowID = R.id.addNewInvoice_rateInput;
            }
            moveButtons();
        }
    }
    
    //  Add new services to the view
    public void onClick_addServiceDyn(View v)
    {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.addNewInvoiceLayout);
        View l = LayoutInflater.from(getBaseContext()).inflate(R.layout.service_rows, null);
        int newID = generateViewId();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, nextBelowID);
        layout.addView(l, params);

        // assign the generated ViewID to the new spinner created
        l.setId(newID);
        nextBelowID = newID;
        //Add the element to the arraylist so they can be removed in the future
        rIdStore.add(nextBelowID);
        
        // Create new EditText and Spinner that corresponds to the newly created elements in the new layout.
        // This is done because by default the values are R.id.serviceRow_edit and R.id.serviceRow_spinner
        EditText et = (EditText) findViewById(R.id.serviceRow_edit);
        et.setId(generateViewId());
        rIdStore_editText.add(et);
        
        Spinner s = (Spinner) findViewById(R.id.serviceRow_spinner);
        s.setId(generateViewId());
        rIdStore_spinners.add(s);
        addItemsOnSpinner(s, "services");
        moveButtons();
    }

    //  Reorganize the buttons so align with the last entry
    private void moveButtons()
    {
        LinearLayout cancel = (LinearLayout) findViewById(R.id.buttonContainer);
        
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.BELOW, nextBelowID);
        cancel.setLayoutParams(p);
        
    }
    
    //  function for generating a random ID for the viewId. This is done so that devices on SDK < 17 can support this functionality
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public int generateViewId()
    {
        if (Build.VERSION.SDK_INT < 17)
        {
            int v = value;
            value++;
            return v;
        } else
        {
            return View.generateViewId();
        }
    }
    
}
