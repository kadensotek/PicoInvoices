package com.pico.picoinvoices;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class AddNewClient extends Activity
{
    ClientAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_client);
        openDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_client, menu);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        closeDB();
    }

    private void openDB()
    {
        myDb = new ClientAdapter(this);
        myDb.open();
    }

    private void closeDB()
    {
        myDb.close();
    }

    /*
     * onClick functions defined.
     */
    public void onClick_addUser(View v)
    {
        EditText fname = (EditText) findViewById(R.id.txt_addNew_fname);
        String fname_txt = fname.getText().toString();

        EditText lname = (EditText) findViewById(R.id.txt_addNew_lname);
        String lname_txt = lname.getText().toString();

        EditText address = (EditText) findViewById(R.id.txt_addNew_address);
        String address_txt = address.getText().toString();

        EditText phone = (EditText) findViewById(R.id.txt_addNew_phone);
        String phone_txt = phone.getText().toString();

        EditText email = (EditText) findViewById(R.id.txt_addNew_email);
        String email_txt = email.getText().toString();

        myDb.insertRow(fname_txt, lname_txt, address_txt, phone_txt, email_txt);

        Intent intent = new Intent(this, ClientList.class);
        // EditText editText = (EditText) findViewById(R.id.edit_message);
        // String message = editText.getText().toString();
        // intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void onClick_cancelAdd(View v)
    {
        Intent intent = new Intent(this, ClientList.class);
        // EditText editText = (EditText) findViewById(R.id.edit_message);
        // String message = editText.getText().toString();
        // intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}
