package com.pico.picoinvoices;

import java.io.File;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

public class ImportDB extends Activity
{

    DBAdapter _myDb = null;
    SPAdapter _sp = null;

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Activity Lifecycle functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importdb);
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
        closeDB();
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu)
    // {
    // // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.home, menu);
    // return true;
    // }

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
    // ///* OnClick listener for starting new activities
    // ///*
    // //////////////////////////////////////////////////////
    public void onClick_ToImportXML(View v)
    {
        xmlConfirmation(v);
    }

    public void onClick_ToImportCSV(View v)
    {
        // Intent intent = new Intent(this, ImportCSV.class);
        // startActivity(intent);
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Importing functions
    // ///*
    // //////////////////////////////////////////////////////

    public void importFromXML(View v)
    {
        System.out.println("Importing selected.");
//        File xmlFile = null;
//        String xmlFilepath = null;
//        xmlFilepath = getXML();
//        System.out.println("Filepath1 is " + xmlFilepath);
        
//        xmlFile = new File(xmlFilepath);
        
//        System.out.println("Final selected file is " + xmlFile.toString());
    }

    public void xmlConfirmation(final View v)
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm export")
                .setMessage(
                        "Are you sure you want to import the database from XML?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        importFromXML(v);
                    }

                }).setNegativeButton("No", null).show();
    }

    public void csvConfirmation(final View v)
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm export")
                .setMessage(
                        "Are you sure you want to import the database from CSV?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // importFromCSV(v);
                    }

                }).setNegativeButton("No", null).show();
    }

//    public String getXML()
//    {
//        File dir = new File(Environment.getExternalStorageDirectory(), "");
//        String xmlFilepath = null;
//
//        FileDialog dialog = new FileDialog(this, dir);
//        dialog.setFileEndsWith(".xml");
//        dialog.addFileListener(new FileDialog.FileSelectedListener()
//        {
//            public void fileSelected(File file)
//            {
//                System.out.println("selected file is " + file.toString());
//            }
//        });
//
//        dialog.showDialog();
//        xmlFilepath = dialog.getSelectedFilepath();
//        
//        System.out.println("Filepath2 is " + xmlFilepath);
//
//        return xmlFilepath;
//    }

}
