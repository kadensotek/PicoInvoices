package com.pico.picoinvoices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.xmlpull.v1.XmlPullParserException;

import com.pico.picoinvoices.XmlImporter.Client;
import com.pico.picoinvoices.XmlImporter.Invoice;
import com.pico.picoinvoices.XmlImporter.Service;
import com.pico.picoinvoices.CsvImporter.ClientCSV;
import com.pico.picoinvoices.CsvImporter.InvoiceCSV;
import com.pico.picoinvoices.CsvImporter.ServiceCSV;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

public class ImportDB extends Activity
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
        // closeDB();
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
        csvConfirmation(v);
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Importing functions
    // ///*
    // //////////////////////////////////////////////////////

    public void importFromXML(View v)
    {
        System.out.println("Importing from XML selected.");

        XmlImporter xmlImporter = null;
        List<Invoice> invoices = null;
        List<Client> clients = null;
        List<Service> services = null;
        InputStream stream = null;
        String path = null;
        this.db = _myDb.getDB();

        path = Environment.getExternalStorageDirectory()
                + "/Android/data/com.pico.picoinvoices/";
        xmlImporter = new XmlImporter(this.db);
        stream = getInputStream(stream, path, "picodatabase.xml");

        try
        {
            invoices = xmlImporter.parseInvoices(stream);
        } catch (XmlPullParserException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // for (Invoice invoice : invoices)
        // {
        // System.out.println("For loop");
        // invoice.toString();
        // }

        // File xmlFile = null;
        // String xmlFilepath = null;
        // xmlFilepath = getXML();
        // System.out.println("Filepath1 is " + xmlFilepath);

        // xmlFile = new File(xmlFilepath);

        // System.out.println("Final selected file is " + xmlFile.toString());
    }

    public void importFromCSV(View v)
    {
        System.out.println("Importing from CSV selected.");

        CsvImporter csvImporter = null;
        List<InvoiceCSV> invoices = null;
        List<ClientCSV> clients = null;
        List<ServiceCSV> services = null;
        String path = Environment.getExternalStorageDirectory() + "/Android/data/com.pico.picoinvoices/";
        String fileStr = null;
        this.db = _myDb.getDB();

        csvImporter = new CsvImporter(this.db);

        try
        {
            fileStr = readFile(path + "picodatabase.csv");
            invoices = csvImporter.parseInvoices(new String(fileStr));
            clients = csvImporter.parseClients(new String(fileStr));
            services = csvImporter.parseServices(new String(fileStr));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        for (InvoiceCSV invoice : invoices)
        {
            System.out.println(invoice.toString());
        }
        
        for(ClientCSV client : clients)
        {
            System.out.println(client.toString());
        }
        
        for(ServiceCSV service : services)
        {
            System.out.println(service.toString());
        }
    }

    private String readFile(String pathname) throws IOException
    {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try
        {
            while (scanner.hasNextLine())
            {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            
            return fileContents.toString();
        }
        finally
        {
            scanner.close();
        }
    }

    private InputStream getInputStream(InputStream stream, String path,
            String filename)
    {
        /* Opens the xml file as an input stream to be used with xmlImporter */
        try
        {
            stream = new FileInputStream(new File(path + filename));
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block; needs to be handled
            System.out.println("File not found");
            e.printStackTrace();
        }
        return stream;
    }

    public void xmlConfirmation(final View v)
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm import")
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
                .setTitle("Confirm import")
                .setMessage(
                        "Are you sure you want to import the database from CSV?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        importFromCSV(v);
                    }

                }).setNegativeButton("No", null).show();
    }

    // public String getXML()
    // {
    // File dir = new File(Environment.getExternalStorageDirectory(), "");
    // String xmlFilepath = null;
    //
    // FileDialog dialog = new FileDialog(this, dir);
    // dialog.setFileEndsWith(".xml");
    // dialog.addFileListener(new FileDialog.FileSelectedListener()
    // {
    // public void fileSelected(File file)
    // {
    // System.out.println("selected file is " + file.toString());
    // }
    // });
    //
    // dialog.showDialog();
    // xmlFilepath = dialog.getSelectedFilepath();
    //
    // System.out.println("Filepath2 is " + xmlFilepath);
    //
    // return xmlFilepath;
    // }

}
