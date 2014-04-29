package com.pico.picoinvoices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvImporter
{
    public CsvImporter()
    {
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Invoice functions
    // ///*
    // //////////////////////////////////////////////////////

    /* Parses through all invoices */
    public List<InvoiceCSV> parseInvoices(String contents) throws IOException
    {
        return readDocumentInvoice(contents);
    }

    private List<InvoiceCSV> readDocumentInvoice(String contents) throws IOException
    {
        String line = null;
        Scanner scanner = new Scanner(contents);
        List<InvoiceCSV> invoices = new ArrayList<InvoiceCSV>();
        
        line = scanner.nextLine();
        
        while(scanner.hasNextLine())
        {
            if(line.equals("invoices"))
            {
                while(scanner.hasNextLine())
                {
                    line = scanner.nextLine();
                    
                    if(!line.equals(""))
                    {
                        invoices.add(readInvoice(line));
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                line = scanner.nextLine();
            }
        }
        
        scanner.close();
        return invoices;
    }

    /* This class represents an individual invoice in the XML document. */
    public static class InvoiceCSV
    {
        public final String id;
        public final String issuedate;
        public final String customer;
        public final String duedate;
        public final String priceservice;
        public final String service;
        public final String amountdue;
        public final String status;

        private InvoiceCSV(String id, String issuedate, String customer,
                String duedate, String priceservice, String service,
                String amountdue, String status)
        {
            System.out.println("Creating an invoice");
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
    private InvoiceCSV readInvoice(String line)
    {
        String id = null;
        String issuedate = null;
        String customer = null;
        String duedate = null;
        String priceservice = null;
        String service = null;
        String amountdue = null;
        String status = null;
        
        String[] segments = line.split(",");
        
        id            = segments[0];
        issuedate     = segments[1];
        customer      = segments[2];
        duedate       = segments[3];
        priceservice  = segments[4];
        service       = segments[5];
        amountdue     = segments[6];
        status        = segments[7];

        return new InvoiceCSV(id, issuedate, customer, duedate, priceservice,
                service, amountdue, status);
    }

    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Client functions
    // ///*
    // //////////////////////////////////////////////////////
    
    /* Parses through all clients */
    public List<ClientCSV> parseClients(String contents) throws IOException
    {
        return readDocumentClient(contents);
    }

    private List<ClientCSV> readDocumentClient(String contents) throws IOException
    {
        String line = null;
        Scanner scanner = new Scanner(contents);
        List<ClientCSV> clients = new ArrayList<ClientCSV>();
        
        line = scanner.nextLine();
        
        while(scanner.hasNextLine())
        {
            if(line.equals("contactInfo"))
            {
                while(scanner.hasNextLine())
                {
                    line = scanner.nextLine();
                    
                    if(!line.equals(""))
                    {
                        clients.add(readClient(line));
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                line = scanner.nextLine();
            }
        }
        
        scanner.close();
        return clients;
    }
    
    public class ClientCSV
    {
        public final String id;
        public final String fname;
        public final String lname;
        public final String address;
        public final String phone;
        public final String email;
        public final String business;

        private ClientCSV(String id, String fname, String lname, String address, String phone, String email, String business)
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
    
    /* Parses the contents of an individual invoice */
    private ClientCSV readClient(String line)
    {
        String id = null;
        String fname = null;
        String lname = null;
        String address = null;
        String phone = null;
        String email = null;
        String business = null;
        
        String[] segments = line.split(",");
        
        int i = 0;
        id        = segments[i++];
        fname     = segments[i++];
        lname     = segments[i++];
        address   = segments[i++];
        
        if(address.startsWith("\""))
        {
            while(!address.endsWith("\""))
            {
                address = address + "," + segments[i++];  /* Appends next segment until closing quote is found */
            }
            address = address.substring(1,address.length()-1);  /* Trims leading and trailing quotes */
        }
        
        phone     = segments[i++];
        email     = segments[i++];
//        business  = segments[i];

        return new ClientCSV(id, fname, lname, address, phone, email, business);
    }

    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Service functions
    // ///*
    // //////////////////////////////////////////////////////
    
    /* Parses through all services */
    public List<ServiceCSV> parseServices(String contents) throws IOException
    {
        return readDocumentService(contents);
    }

    private List<ServiceCSV> readDocumentService(String contents) throws IOException
    {
        String line = null;
        Scanner scanner = new Scanner(contents);
        List<ServiceCSV> services = new ArrayList<ServiceCSV>();
        
        line = scanner.nextLine();
        
        while(scanner.hasNextLine())
        {
            if(line.equals("services"))
            {
                while(scanner.hasNextLine())
                {
                    line = scanner.nextLine();
                    
                    if(!line.equals(""))
                    {
                        services.add(readService(line));
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                line = scanner.nextLine();
            }
        }
        
        scanner.close();
        return services;
    }
    
    public static class ServiceCSV
    {
        public final String id;
        public final String name;
        public final String rate;
        public final String type;

        private ServiceCSV(String id, String name, String rate, String type)
        {
            this.id = id;
            this.name = name;
            this.rate = rate;
            this.type = type;
        }
    }
    
    private ServiceCSV readService(String line)
    {
        String id   = null;
        String name = null;
        String rate = null;
        String type = null;
        
        String[] segments = line.split(",");
        
        id      = segments[0];
        name    = segments[1];
        rate    = segments[2];
        type    = segments[3];

        return new ServiceCSV(id, name, rate, type);
    }
}
