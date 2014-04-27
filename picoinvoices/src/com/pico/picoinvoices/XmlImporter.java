package com.pico.picoinvoices;

import java.io.IOException;
import java.io.StringReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XmlImporter
{
    private static final String DATASUBDIRECTORY = "/Android/data/com.pico.picoinvoices/";
    private static final String ns = null;
    private final SQLiteDatabase db;

    public XmlImporter(final SQLiteDatabase db)
    {
        this.db = db;
    }
    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Invoice functions
    // ///*
    // //////////////////////////////////////////////////////

    /* Parses through all invoices */
    public List<Invoice> parseInvoices(InputStream in) throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            
            return readDocumentInvoice(parser);
        }
        finally
        {
            in.close();
        }
    }

    private List<Invoice> readDocumentInvoice(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        List<Invoice> invoices = new ArrayList<Invoice>();

        parser.require(XmlPullParser.START_TAG, ns, "picoinvoices");
        
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            
            String name = parser.getName();
            
            /* Looks for the invoice tag; skips otherwise */
            if (name.equals("invoice"))
            {
                invoices.add(readInvoice(parser));
            }
            else
            {
                skip(parser);
            }
        }
        return invoices;
    }

    /* This class represents an individual invoice in the XML document. */
    public static class Invoice
    {
        public final String id;
        public final String issuedate;
        public final String customer;
        public final String duedate;
        public final String priceservice;
        public final String service;
        public final String amountdue;
        public final String status;

        private Invoice(String id, String issuedate, String customer, String duedate, String priceservice, String service, String amountdue, String status)
        {
            this.id = id;
            this.issuedate = issuedate;
            this.customer = customer;
            this.duedate = duedate;
            this.priceservice = priceservice;
            this.service = service;
            this.amountdue = amountdue;
            this.status = status;
        }
    }

    /* Parses the contents of an individual invoice */
    private Invoice readInvoice(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "invoice");
        String id = null;
        String issuedate = null;
        String customer = null;
        String duedate = null;
        String priceservice = null;
        String service = null;
        String amountdue = null;
        String status = null;
        
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            
            String name = parser.getName();
            
            if (name.equals("_id"))
            {
                id = readID(parser);
            }
            else if (name.equals("issuedate"))
            {
                issuedate = readIssueDate(parser);
            }
            else if (name.equals("customer"))
            {
                customer = readCustomer(parser);
            }
            else if (name.equals("duedate"))
            {
                duedate = readDueDate(parser);
            }
            else if (name.equals("priceservice"))
            {
                priceservice = readPriceService(parser);
            }
            else if (name.equals("service"))
            {
                service = readService(parser);
            }
            else if (name.equals("amountdue"))
            {
                amountdue = readAmountDue(parser);
            }
            else if (name.equals("status"))
            {
                status = readStatus(parser);
            }
            else
            {
                skip(parser);
            }
        }
        
        return new Invoice(id, issuedate, customer, duedate, priceservice, service, amountdue, status);
    }
    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Client functions
    // ///*
    // //////////////////////////////////////////////////////
    
    /* Parses through all clients */
    public List<Client> parseClients(InputStream in) throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            
            return readDocumentClient(parser);
        }
        finally
        {
            in.close();
        }
    }

    private List<Client> readDocumentClient(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        List<Client> clients = new ArrayList<Client>();

        parser.require(XmlPullParser.START_TAG, ns, "picoinvoices");
        
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            
            String name = parser.getName();
            
            /* Looks for the client tag; skips otherwise */
            if (name.equals("client"))
            {
                clients.add(readClient(parser));
            }
            else
            {
                skip(parser);
            }
        }
        return clients;
    }
    
    /* This class represents an individual client in the XML document. */
    public static class Client
    {
        public final String id;
        public final String fname;
        public final String lname;
        public final String address;
        public final String phone;
        public final String email;
        public final String business;

        private Client(String id, String fname, String lname, String address, String phone, String email, String business)
        {
            this.id = id;
            this.fname = fname;
            this.lname = lname;
            this.address = address;
            this.phone = phone;
            this.email = email;
            this.business = business;
        }
    }
    
    /* Parses the contents of an individual client */
    private Client readClient(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "client");
        String id = null;
        String fname = null;
        String lname = null;
        String address = null;
        String phone = null;
        String email = null;
        String business = null;
        
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            
            String name = parser.getName();
            
            if (name.equals("_id"))
            {
                id = readID(parser);
            }
            else if (name.equals("fname"))
            {
                fname = readFname(parser);
            }
            else if (name.equals("lname"))
            {
                lname = readLname(parser);
            }
            else if (name.equals("address"))
            {
                address = readAddress(parser);
            }
            else if (name.equals("phone"))
            {
                phone = readPhone(parser);
            }
            else if (name.equals("email"))
            {
                email = readEmail(parser);
            }
            else if (name.equals("business"))
            {
                business = readBusiness(parser);
            }
            else
            {
                skip(parser);
            }
        }
        
        return new Client(id, fname, lname, address, phone, email, business);
    }
    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Service functions
    // ///*
    // //////////////////////////////////////////////////////
    
    /* Parses through all clients */
    public List<Service> parseServices(InputStream in) throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            
            return readDocumentService(parser);
        }
        finally
        {
            in.close();
        }
    }

    private List<Service> readDocumentService(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        List<Service> services = new ArrayList<Service>();

        parser.require(XmlPullParser.START_TAG, ns, "picoinvoices");
        
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            
            String name = parser.getName();
            
            /* Looks for the service tag; skips otherwise */
            if (name.equals("service"))
            {
                services.add(readServiceTag(parser));
            }
            else
            {
                skip(parser);
            }
        }
        return services;
    }
    
    /* This class represents an individual service in the XML document. */
    public static class Service
    {
        public final String id;
        public final String name;
        public final String rate;
        public final String type;

        private Service(String id, String name, String rate, String type)
        {
            this.id = id;
            this.name = name;
            this.rate = rate;
            this.type = type;
        }
    }
    
    /* Parses the contents of an individual client */
    private Service readServiceTag(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "client");
        String id = null;
        String name = null;
        String rate = null;
        String type = null;
        
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            
            String tagname = parser.getName();
            
            if (tagname.equals("_id"))
            {
                id = readID(parser);
            }
            else if (tagname.equals("name"))
            {
                name = readName(parser);
            }
            else if (tagname.equals("rate"))
            {
                rate = readRate(parser);
            }
            else if (tagname.equals("type"))
            {
                type = readType(parser);
            }
            else
            {
                skip(parser);
            }
        }
        
        return new Service(id, name, rate, type);
    }
    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Tag processing functions
    // ///*
    // //////////////////////////////////////////////////////
    
    private String readID(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "_id");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "_id");
        
        return id;
    }

    private String readIssueDate(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "issuedate");
        String issuedate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "issuedate");
        
        return issuedate;
    }
    
    private String readCustomer(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "issuedate");
        String issuedate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "issuedate");
        
        return issuedate;
    }
    
    private String readDueDate(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "duedate");
        String duedate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "duedate");
        
        return duedate;
    }
    
    private String readPriceService(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "priceservice");
        String priceservice = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "priceservice");
        
        return priceservice;
    }
    
    private String readService(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "service");
        String service = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "service");
        
        return service;
    }
    
    private String readAmountDue(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "amountdue");
        String amountdue = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "amountdue");
        
        return amountdue;
    }
    
    private String readStatus(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "status");
        String status = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "status");
        
        return status;
    }
    
    private String readFname(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "fname");
        String fname = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "fname");
        
        return fname;
    }
    
    private String readLname(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "lname");
        String lname = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "lname");
        
        return lname;
    }
    
    private String readAddress(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "address");
        String address = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "address");
        
        return address;
    }
    
    private String readPhone(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "phone");
        String phone = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "phone");
        
        return phone;
    }
    
    private String readEmail(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "email");
        String email = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "email");
        
        return email;
    }
    
    private String readBusiness(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "business");
        String business = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "business");
        
        return business;
    }
    
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        
        return name;
    }
    
    private String readRate(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "rate");
        String rate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "rate");
        
        return rate;
    }
    
    private String readType(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "type");
        String type = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "type");
        
        return type;
    }

    /* Extracts text values between tags. */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String result = "";
        
        if (parser.next() == XmlPullParser.TEXT)
        {
            result = parser.getText();
            parser.nextTag();
        }
        
        return result;
    }

    /* Skips tags using depth to keep track of location */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        if (parser.getEventType() != XmlPullParser.START_TAG)
        {
            throw new IllegalStateException();
        }
        
        int depth = 1;
        
        while (depth != 0)
        {
            switch (parser.next())
            {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
