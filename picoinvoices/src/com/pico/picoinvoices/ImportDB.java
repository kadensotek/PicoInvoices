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
import android.widget.Toast;

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

        path = Environment.getExternalStorageDirectory() + "/Android/data/com.pico.picoinvoices/";
        xmlImporter = new XmlImporter(this.db);
        stream = getInputStream(stream, path, "picodatabase.xml");

        try
        {
            invoices = xmlImporter.parseInvoices(stream);
        }
        catch(XmlPullParserException e)
        {
            Toast.makeText(getBaseContext(), "Error parsing file", Toast.LENGTH_LONG).show();
            return;
        }
        catch(IOException e)
        {
            Toast.makeText(getBaseContext(), "Error parsing file", Toast.LENGTH_LONG).show();
            return;
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

        csvImporter = new CsvImporter();

        try
        {
            fileStr = readFile(path + "picodatabase.csv");
        }
        catch(IOException e)
        {
            Toast.makeText(getBaseContext(), "File not found", Toast.LENGTH_LONG).show();
            return;
        }
        
        try
        {
            invoices = csvImporter.parseInvoices(new String(fileStr));
            clients = csvImporter.parseClients(new String(fileStr));
            services = csvImporter.parseServices(new String(fileStr));
        }
        catch(IOException e1)
        {
            Toast.makeText(getBaseContext(), "Error parsing file", Toast.LENGTH_LONG).show();
            return;
        }
        
        updateDatabase(invoices, clients, services);
        
        Toast.makeText(getBaseContext(), "Import completed.", Toast.LENGTH_LONG).show();
    }

    private void updateDatabase(List<InvoiceCSV> invoices,
            List<ClientCSV> clients, List<ServiceCSV> services)
    {
        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(this);
        ClientAdapter clientAdapter = new ClientAdapter(this);
        RegisterServicesAdapter serviceAdapter = new RegisterServicesAdapter(this);

        openAllTables(invoiceAdapter, clientAdapter, serviceAdapter);

        deleteAllRows(invoiceAdapter, clientAdapter, serviceAdapter);

        for(InvoiceCSV invoice : invoices)
        {
            invoiceAdapter.insertRow(invoice.issuedate, invoice.customer,
                    invoice.duedate, invoice.priceservice, invoice.service,
                    invoice.amountdue, invoice.status);
        }

        for(ClientCSV client : clients)
        {
            clientAdapter.insertRow(client.fname, client.lname, client.address,
                    client.phone, client.email, "");
        }

        for(ServiceCSV service : services)
        {
            serviceAdapter.insertRow(service.name, service.type, service.rate);
        }

        closeAllTables(invoiceAdapter, clientAdapter, serviceAdapter);
    }

    private void closeAllTables(InvoiceAdapter invoiceAdapter,
            ClientAdapter clientAdapter, RegisterServicesAdapter serviceAdapter)
    {
        invoiceAdapter.close();
        clientAdapter.close();
        serviceAdapter.close();
    }

    private void deleteAllRows(InvoiceAdapter invoiceAdapter,
            ClientAdapter clientAdapter, RegisterServicesAdapter serviceAdapter)
    {
        invoiceAdapter.deleteAll();
        clientAdapter.deleteAll();
        serviceAdapter.deleteAll();
    }

    private void openAllTables(InvoiceAdapter invoiceAdapter,
            ClientAdapter clientAdapter, RegisterServicesAdapter serviceAdapter)
    {
        invoiceAdapter.open();
        clientAdapter.open();
        serviceAdapter.open();
    }

    private String readFile(String pathname) throws IOException
    {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try
        {
            while(scanner.hasNextLine())
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
