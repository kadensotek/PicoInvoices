package com.pico.picoinvoices;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewService extends Activity
{
    RegisterServicesAdapter myDb;

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Activity lifecycle functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_service);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        openDB();
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
        getMenuInflater().inflate(R.menu.add_new_service, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            case R.id.action_cancelNewService:
                onClick_cancelAdd();
                return true;
            case R.id.action_acceptNewService:
                onClick_addService();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Database functions
    // ///*
    // //////////////////////////////////////////////////////
    private void openDB()
    {
        myDb = new RegisterServicesAdapter(this);
        myDb.open();
    }

    private void closeDB()
    {
        myDb.close();
    }
    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* OnClick functions
    // ///*
    // //////////////////////////////////////////////////////
    public void onClick_addService()
    {

        new AlertDialog.Builder(this)
                .setTitle("Add Service")
                .setMessage("Do you want to add new service?")
                .setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,
                            int which)
                    {

                        // Service name
                        EditText sname = (EditText) findViewById(R.id.txt_addNew_sname);
                        final String sname_txt = sname.getText()
                                .toString().trim()
                                + " ";

                        // Service type
                        EditText type = (EditText) findViewById(R.id.txt_addNew_type);
                        final String type_txt = type.getText()
                                .toString().trim();

                        //Check to make sure that the fields are filled out
                        //Do not need to do sqlinjection checking here because ContentValues are used to insert into DB
                        if (sname_txt.matches("") || type_txt.matches(""))
                        {
                            Toast.makeText(getBaseContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            
                            myDb.insertRow(sname_txt, type_txt,"");
                            // Call finish() to prevent the flow of
                            // activities from accessing this activity from
                            // the backstack
                            Intent intent =  new Intent(AddNewService.this, RegisterServices.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,
                            int which)
                    {
                        
                    }
                }).setIcon(R.drawable.ic_launcher).show();

    }
    
    public void onClick_cancelAdd()
    {
        new AlertDialog.Builder(this)
        .setTitle("Add Service")
        .setMessage("Cancel add?")
        .setPositiveButton(android.R.string.yes,
        new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog,
                    int which)
            {
               finish();
            }
        })
        .setNegativeButton(android.R.string.no,
        new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog,
                    int which)
            {
                //Do nothing to go back to the current entry
            }
        }).setIcon(R.drawable.ic_launcher).show();
    }
}
