package com.pico.picoinvoices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditClientInfo extends Activity
{

    private ClientAdapter _myDb = null;
    private SPAdapter _sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client_info);
        intialize();
    }

    private void intialize()
    {
        _myDb = new ClientAdapter(this);
        _sp = new SPAdapter(getApplicationContext());
        openDB();

        Cursor cursor = _myDb.getRow(_sp.getClientID());
        if (cursor.moveToFirst())
        {
            // First Name
            EditText fname = (EditText) findViewById(R.id.txt_edit_fname);
            System.out.println(cursor.getString(ClientAdapter.COL_FNAME));
            fname.setText(cursor.getString(ClientAdapter.COL_FNAME));
            
            // Last Name
            EditText lname = (EditText) findViewById(R.id.txt_edit_lname);
            lname.setText(cursor.getString(ClientAdapter.COL_LNAME),
                    TextView.BufferType.EDITABLE);
            
            // Address
            EditText address = (EditText) findViewById(R.id.txt_edit_address);
            address.setText(cursor.getString(ClientAdapter.COL_ADDRESS));
            
            // Phone
            EditText phone = (EditText) findViewById(R.id.txt_edit_phone);
            phone.setText(cursor.getString(ClientAdapter.COL_PHONE));
            
            // EMail
            EditText email = (EditText) findViewById(R.id.txt_edit_email);
            email.setText(cursor.getString(ClientAdapter.COL_EMAIL));
        }
        closeDB();
    }

    private void openDB()
    {
        _myDb.open();
    }

    private void closeDB()
    {
        _myDb.close();
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
        getMenuInflater().inflate(R.menu.edit_client_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            case R.id.action_cancelEdit:
                onClick_cancelEdit();
                return true;
            case R.id.action_acceptEdit:
                onClick_edit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onClick_edit()
    {
        new AlertDialog.Builder(this)
                .setTitle("Edit Client")
                .setMessage("Do you want to save the changes?")
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {

                                // First Name
                                EditText fname = (EditText) findViewById(R.id.txt_edit_fname);
                                final String fname_txt = fname.getText()
                                        .toString().trim()
                                        + " ";

                                // Last Name
                                EditText lname = (EditText) findViewById(R.id.txt_edit_lname);
                                final String lname_txt = lname.getText()
                                        .toString().trim();

                                // Address
                                EditText address = (EditText) findViewById(R.id.txt_edit_address);
                                final String address_txt = address.getText()
                                        .toString().trim();

                                // Phone
                                EditText phone = (EditText) findViewById(R.id.txt_edit_phone);
                                final String phone_txt = phone.getText()
                                        .toString().trim();

                                // EMail
                                EditText email = (EditText) findViewById(R.id.txt_edit_email);
                                final String email_txt = email.getText()
                                        .toString().trim();

                                // Check to make sure that the fields are filled
                                // out
                                // Do not need to do sqlinjection checking here
                                // because ContentValues are used to insert into
                                // DB
                                if (fname_txt.matches(" ")
                                        || lname_txt.matches("")
                                        || address_txt.matches("")
                                        || phone_txt.matches("")
                                        || email_txt.matches(""))
                                {
                                    Toast.makeText(getBaseContext(),
                                            "Please fill out all fields",
                                            Toast.LENGTH_LONG).show();
                                } else
                                {
                                    openDB();
                                    _myDb.updateRow(_sp.getClientID(),
                                            fname_txt, lname_txt, address_txt,
                                            phone_txt, email_txt, "");
                                    // Call finish() to prevent the flow of
                                    // activities from accessing this activity
                                    // from
                                    // the backstack
                                    Intent intent = new Intent(
                                            EditClientInfo.this,
                                            ClientInvoices.class);
                                    startActivity(intent);
                                    finish();
                                    closeDB();
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

    private void onClick_cancelEdit()
    {
        new AlertDialog.Builder(this)
                .setTitle("Edit Client")
                .setMessage("Are you sure you want to cancel?")
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
                                // Do nothing to go back to the current entry
                            }
                        }).setIcon(R.drawable.ic_launcher).show();
    }

}
