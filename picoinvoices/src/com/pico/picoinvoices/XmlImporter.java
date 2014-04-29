package com.pico.picoinvoices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class XmlImporter
{
    public XmlImporter()
    {
    }
    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Invoice functions
    // ///*
    // //////////////////////////////////////////////////////

    /* Parses through all invoices */
    public List<Invoice> parseInvoices(String contents) throws IOException
    {        
        return readDocumentInvoice(contents);
    }

    private List<Invoice> readDocumentInvoice(String contents) throws IOException
    {
        String line = null;
        String invoiceContents = null;
        Scanner scanner = new Scanner(contents);
        List<Invoice> invoices = new ArrayList<Invoice>();
        
        line = scanner.nextLine();
        
        while(scanner.hasNextLine())
        {
            if(line.equals("<invoices>"))                         //
            {                                                     //    This reads everything between <invoices> and </invoices> tags
                while(!line.trim().equals("</invoices>"))         //
                {
                    line = scanner.nextLine();
                    
                    if(line.trim().equals("<invoice>"))            //
                    {                                              //
                        invoiceContents = "";                      //  This reads everything between <invoice> and </invoice> tags
                                                                   //
                        while(!line.trim().equals("</invoice>"))   //
                        {
                            line = scanner.nextLine();
                            
                            if(!line.trim().equals("</invoice>"))                 //
                            {                                                     //   Pulls all individual lines between <invoice> and </invoice>
                                invoiceContents = invoiceContents + line + "\n";  //   into a new string for separate processing
                            }                                                     //
                            else
                            {
                                break;   // Breaks out to check for next <invoice>
                            }
                        }
                        
                        invoices.add(readInvoice(invoiceContents));     // Sends the invoice contents for processing
                    }
                    else
                    {
                        break; // </invoice> reached, so jump out and discard rest of file
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
    private Invoice readInvoice(String invoice)
    {
        Scanner scanner = new Scanner(invoice);
        String line = "";
        
        String id = null;
        String issuedate = null;
        String customer = null;
        String duedate = null;
        String priceservice = null;
        String service = null;
        String amountdue = null;
        String status = null;
        
        /* This goes through each line, checking for appropriate tags, 
         * splitting the string based on the tags and making the assignment */
        while (scanner.hasNextLine())
        {            
            line = scanner.nextLine().trim();
            
            if (line.contains("<_id>"))
            {
                line = line.substring(5,line.length());
                line = line.substring(0,line.length()-6);
                id = line;
            }
            else if (line.contains("<issuedate>"))
            {
                line = line.substring(11,line.length());
                line = line.substring(0,line.length()-12);
                issuedate = line;
            }
            else if (line.contains("<customer>"))
            {
                line = line.substring(10,line.length());
                line = line.substring(0,line.length()-11);
                customer = line;
            }
            else if (line.contains("<duedate>"))
            {
                line = line.substring(9,line.length());
                line = line.substring(0,line.length()-10);
                duedate = line;
            }
            else if (line.contains("<priceservice>"))
            {
                line = line.substring(14,line.length());
                line = line.substring(0,line.length()-15);
                line = line.replace("&amp;","&");
                priceservice = line;
            }
            else if (line.contains("<service>"))
            {
                line = line.substring(9,line.length());
                line = line.substring(0,line.length()-10);
                line = line.replace("&amp;","&");
                service = line;
            }
            else if (line.contains("<amountdue>"))
            {
                line = line.substring(11,line.length());
                line = line.substring(0,line.length()-12);
                amountdue = line;
            }
            else if (line.contains("<status>"))
            {
                line = line.substring(8,line.length());
                line = line.substring(0,line.length()-9);
                status = line;
            }
        }
        
        scanner.close();
        
        return new Invoice(id, issuedate, customer, duedate, priceservice, service, amountdue, status);
    }
    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Client functions
    // ///*
    // //////////////////////////////////////////////////////
    
    /* Parses through all clients */
    public List<Client> parseClients(String contents) throws IOException
    {        
        return readDocumentClient(contents);
    }

    private List<Client> readDocumentClient(String contents) throws IOException
    {
        String line = null;
        String clientContents = null;
        Scanner scanner = new Scanner(contents);
        List<Client> clients = new ArrayList<Client>();
        
        line = scanner.nextLine();
        
        while(scanner.hasNextLine())
        {
            if(line.equals("<contactInfo>"))
            {
                while(!line.trim().equals("</contactInfo>"))
                {
                    line = scanner.nextLine();
                    
                    if(line.trim().equals("<contactInf>"))
                    {
                        clientContents = ""; /* resets client contents before building new one */
                        
                        while(!line.trim().equals("</contactInf>"))
                        {
                            line = scanner.nextLine();
                            
                            if(!line.trim().equals("</contactInf>"))
                            {
                                clientContents = clientContents + line + "\n";  /* Creates client subset for parsing */
                            }
                            else
                            {
                                break;
                            }
                        }
                        
                        clients.add(readClient(clientContents));
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
    private Client readClient(String client) throws IOException
    {
        Scanner scanner = new Scanner(client);
        String line = null;
        
        String id = null;
        String fname = null;
        String lname = null;
        String address = null;
        String phone = null;
        String email = null;
        String business = null;
        
        /* This goes through each line, checking for appropriate tags, 
         * splitting the string based on the tags and making the assignment */
        while (scanner.hasNextLine())
        {
            line = scanner.nextLine().trim();
            
            if (line.contains("<_id>"))
            {
                line = line.substring(5,line.length());
                line = line.substring(0,line.length()-6);
                id = line;
            }
            else if (line.contains("<fname>"))
            {
                line = line.substring(7,line.length());
                line = line.substring(0,line.length()-8);
                fname = line;
            }
            else if (line.contains("<lname>"))
            {
                line = line.substring(7,line.length());
                line = line.substring(0,line.length()-8);
                lname = line;
            }
            else if (line.contains("<address>"))
            {
                line = line.substring(9,line.length());
                line = line.substring(0,line.length()-10);
                address = line;
            }
            else if (line.contains("<phone>"))
            {
                line = line.substring(7,line.length());
                line = line.substring(0,line.length()-8);
                phone = line;
            }
            else if (line.contains("<email>"))
            {
                line = line.substring(7,line.length());
                line = line.substring(0,line.length()-8);
                email = line;
            }
            else if (line.contains("<business>"))
            {
                business = "";
            }
        }
        
        scanner.close();
        
        return new Client(id, fname, lname, address, phone, email, business);
    }
    
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Service functions
    // ///*
    // //////////////////////////////////////////////////////
    
    /* Parses through all services */
    public List<Service> parseServices(String contents) throws IOException
    {
        return readDocumentService(contents);
    }

    private List<Service> readDocumentService(String contents) throws IOException
    {
        String line = null;
        String serviceContents = null;
        Scanner scanner = new Scanner(contents);
        List<Service> services = new ArrayList<Service>();
        
        line = scanner.nextLine();
        
        while(scanner.hasNextLine())
        {
            if(line.equals("<services>"))
            {
                while(!line.trim().equals("</services>"))
                {
                    line = scanner.nextLine();
                    
                    if(line.trim().equals("<service>"))
                    {
                        serviceContents = ""; /* resets services contents before building new one */
                        
                        while(!line.trim().equals("</services>"))
                        {
                            line = scanner.nextLine();
                            
                            if(!line.trim().equals("</service>"))
                            {
                                serviceContents = serviceContents + line + "\n";  /* Creates service subset for parsing */
                            }
                            else
                            {
                                break;
                            }
                        }
                        
                        services.add(readService(serviceContents));
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
    
    /* Parses the contents of an individual service */
    private Service readService(String service) throws IOException
    {
        Scanner scanner = new Scanner(service);
        String line = null;
        
        String id = null;
        String name = null;
        String rate = null;
        String type = null;
        
        /* This goes through each line, checking for appropriate tags, 
         * splitting the string based on the tags and making the assignment */
        while (scanner.hasNextLine())
        {            
            line = scanner.nextLine().trim();
            
            if (line.contains("<_id>"))
            {
                line = line.substring(5,line.length());
                line = line.substring(0,line.length()-6);
                id = line;
            }
            else if (line.contains("<name>"))
            {
                line = line.substring(6,line.length());
                line = line.substring(0,line.length()-7);
                name = line;
            }
            else if (line.contains("<rate>"))
            {
                line = line.substring(6,line.length());
                line = line.substring(0,line.length()-7);
                rate = line;
            }
            else if (line.contains("<type>"))
            {
                line = line.substring(6,line.length());
                line = line.substring(0,line.length()-7);
                type = line;
            }
        }
        
        scanner.close();
        
        return new Service(id, name, rate, type);
    }
}
