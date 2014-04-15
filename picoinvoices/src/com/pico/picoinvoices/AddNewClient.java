package com.pico.picoinvoices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewClient extends Activity
{
    ClientAdapter myDb;

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Activity lifecycle functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_client);
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
        getMenuInflater().inflate(R.menu.add_new_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            case R.id.action_cancelNewClient:
                onClick_cancelAdd();
                return true;
            case R.id.action_acceptNewClient:
                onClick_addUser();
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
        myDb = new ClientAdapter(this);
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
    public void onClick_addUser()
    {

        new AlertDialog.Builder(this)
                .setTitle("Add Client")
                .setMessage("Do you want to add new client?")
                .setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,
                            int which)
                    {

                        // First Name
                        EditText fname = (EditText) findViewById(R.id.txt_addNew_fname);
                        final String fname_txt = fname.getText()
                                .toString().trim()
                                + " ";

                        // Last Name
                        EditText lname = (EditText) findViewById(R.id.txt_addNew_lname);
                        final String lname_txt = lname.getText()
                                .toString().trim();

                        // Address
                        EditText address = (EditText) findViewById(R.id.txt_addNew_address);
                        final String address_txt = address.getText()
                                .toString().trim();

                        // Phone
                        EditText phone = (EditText) findViewById(R.id.txt_addNew_phone);
                        final String phone_txt = phone.getText()
                                .toString().trim();

                        // EMail
                        EditText email = (EditText) findViewById(R.id.txt_addNew_email);
                        final String email_txt = email.getText()
                                .toString().trim();
                        
                        //Check to make sure that the fields are filled out
                        //Do not need to do sqlinjection checking here because ContentValues are used to insert into DB
                        if (fname_txt.matches(" ") || lname_txt.matches("") || address_txt.matches("") || phone_txt.matches("") || email_txt.matches(""))
                        {
                            Toast.makeText(getBaseContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            
                            myDb.insertRow(fname_txt, lname_txt,address_txt, phone_txt, email_txt,"");
                            // Call finish() to prevent the flow of
                            // activities from accessing this activity from
                            // the backstack
                            finish();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,
                            int which)
                    {
                        
                    }
                }).setIcon(R.drawable.ic_launcher).show();

    }
    public void onClick_cancelAdd()
    {
        new AlertDialog.Builder(this)
        .setTitle("Add Client")
        .setMessage("Cancel add?")
        .setPositiveButton(android.R.string.yes,
        new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,
                    int which)
            {
               finish();
            }
        })
        .setNegativeButton(android.R.string.no,
        new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,
                    int which)
            {
                //Do nothing to go back to the current entry
            }
        }).setIcon(R.drawable.ic_launcher).show();
    }

}
