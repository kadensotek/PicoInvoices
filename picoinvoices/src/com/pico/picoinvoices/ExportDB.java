package com.pico.picoinvoices;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ExportDB extends Activity
{

    DBAdapter _myDb = null;
    SPAdapter _sp = null;
    SQLiteDatabase db = null;

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Activity Lifecycle functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportdb);
        initialize();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        closeDB();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initialize();
    }

    private void initialize()
    {
        _sp = new SPAdapter(getApplicationContext());
        _sp.saveClientID("0");
        _sp.saveInvioceID("0");
        openDB();
        //closeDB();
    }
    ////////////////////////////////////////////////////////
    /////*
    /////*  Action bar functions
    /////*
    ////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exportdb, menu);
        return true;
    }
    
    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
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

//     @Override
//     public boolean onCreateOptionsMenu(Menu menu)
//     {
//     // Inflate the menu; this adds items to the action bar if it is present.
//     getMenuInflater().inflate(R.menu.home, menu);
//     return true;
//     }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Database functions
    // ///*
    // //////////////////////////////////////////////////////
    
    private void closeDB()
    {
        _myDb.close();
    }

    private void openDB()
    {
        _myDb = new DBAdapter(this);
        _myDb.open();

    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* OnClick listeners
    // ///*
    // //////////////////////////////////////////////////////
    public void onClick_ToExportXML(View v)
    {
        xmlConfirmation(v);
    }
    
    public void onClick_ToExportCSV(View v)
    {
        csvConfirmation(v);
    }
    
    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Exporting functions
    // ///*
    // //////////////////////////////////////////////////////
    public void exportToXML(View v)
    {
        String path = Environment.getExternalStorageDirectory() + "/Android/data/com.pico.picoinvoices/";
        createDirIfNotExists(path);

        XmlExporter xmlExporter = null;
        this.db = _myDb.getDB();
        xmlExporter = new XmlExporter(this.db);

        try
        {
            xmlExporter.export("picoinvoices", "picodatabase");
        }
        catch(IOException e)
        {
            Toast.makeText(getBaseContext(), "Error exporting database", Toast.LENGTH_LONG).show();
            return;
        }
        
        Toast.makeText(getBaseContext(), "Export completed.", Toast.LENGTH_LONG).show();
    }
    
    public void exportToCSV(View v)
    {
        String path = Environment.getExternalStorageDirectory() + "/Android/data/com.pico.picoinvoices/";
        createDirIfNotExists(path);

        CsvExporter csvExporter = null;
        this.db = _myDb.getDB();
        csvExporter = new CsvExporter(this.db);

        try
        {
            csvExporter.export("picoinvoices", "picodatabase");
        }
        catch(IOException e)
        {
            Toast.makeText(getBaseContext(), "Error exporting database", Toast.LENGTH_LONG).show();
            return;
        }
        
        Toast.makeText(getBaseContext(), "Export completed.", Toast.LENGTH_LONG).show();
    }

    public void xmlConfirmation(final View v)
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm export")
                .setMessage("Are you sure you want to export the database to XML?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        exportToXML(v);
                    }

                }).setNegativeButton("No", null).show();
    }
    
    public void csvConfirmation(final View v)
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm export")
                .setMessage("Are you sure you want to export the database to CSV?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        exportToCSV(v);
                    }

                }).setNegativeButton("No", null).show();
    }
    
    public static boolean createDirIfNotExists(String path)
    {
        boolean exists = true;

        File file = new File(path);
        //Toast.makeText(this.Context, "Exporting database complete.",Toast.LENGTH_SHORT).show();
        
        if (!file.exists())
        {
            System.out.println("directory does not exist; creating directory");
            
            if(!file.mkdirs())
            {
                System.out.println("Problem creating data storage folder");
                exists = false;
            }
        }
        else
        {
            System.out.println("directory exists");
        }
        
        return exists;
    }
}
