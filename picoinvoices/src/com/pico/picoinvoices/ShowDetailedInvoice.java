<<<<<<< HEAD
package com.pico.picoinvoices;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class ShowDetailedInvoice extends ExpandableListActivity
{
    // Create ArrayList to hold parent Items and Child Items
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    private long customer_ID = ClientList.CLIENT_ID;
    private long invoice_ID = ClientInvoices.INVOICE_ID;
    private InvoiceAdapter myDb = null;
    private String fname, lname, address, email, phone;
    private String issuedate, service, dateserviceperformed, priceservice, servicedesc, amountdue, status;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {

        super.onCreate(savedInstanceState);

        openDB();
        populateValues();
        
        // Create Expandable List and set it's properties
        ExpandableListView expandableList = getExpandableListView(); 
        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        // Set the Items of Parent
        setGroupParents();
        // Set The Child Data
        setChildData();

        // Create the Adapter
        ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(parentItems, childItems);

        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        
        // Set the Adapter to expandableList
        expandableList.setAdapter(adapter);
        expandableList.setOnChildClickListener(this);
    }
    protected void onDestroy() 
    {
        super.onDestroy();
        closeDB();
    }
    protected void onFinish()
    {
        super.finish();
        
    }
    private void closeDB() 
    {
        myDb.close();
    }
    private void openDB() 
    {
        myDb = new InvoiceAdapter(this);
        myDb.open();
    }

    // method to add parent Items
    public void setGroupParents() 
    {
        parentItems.add("Contact Info - " + fname +" "+ lname);
        parentItems.add("Invoice - " + amountdue);
    }

    // method to set child data of each parent
    public void setChildData() 
    {

        // Add Child Items for Contact
        ArrayList<String[]> child = new ArrayList<String[]>();
        child.add(new String[] {"First", fname});
        child.add(new String[] {"Last", lname});
        child.add(new String[] {"Phone", phone});
        child.add(new String[] {"EMail",email});
        child.add(new String[] {"Address",address});
        
        childItems.add(child);

        // Add Child Items for Invoice
        child = new ArrayList<String[]>();
        child.add(new String[] {"Issue Date", issuedate});
        child.add(new String[] {"Date Performed", dateserviceperformed});
        child.add(new String[] {"Price Charged", priceservice});
        child.add(new String[] {"Service", service});
        child.add(new String[] {"Description",servicedesc});
        child.add(new String[] {"Status",status});
        child.add(new String[] {"Amount Due",amountdue});
        
        childItems.add(child);

    }
    private void populateValues()
    {
        //Populate the values for the contact information.
        Cursor cursor = myDb.query(new String[] {Long.toString(customer_ID)}, ClientAdapter.DATABASE_TABLE);
        if (cursor.moveToFirst())
        {
            fname  = cursor.getString(ClientAdapter.COL_FNAME);
            lname = cursor.getString(ClientAdapter.COL_LNAME);
            address = cursor.getString(ClientAdapter.COL_ADDRESS);
            email = cursor.getString(ClientAdapter.COL_EMAIL);
            phone = cursor.getString(ClientAdapter.COL_PHONE);
        }
        else 
            Toast.makeText(ShowDetailedInvoice.this, "failed to load cursor", Toast.LENGTH_SHORT).show();
        
       
        //Populate invoice specific information
        cursor = myDb.query(new String[] {Long.toString(invoice_ID)}, InvoiceAdapter.DATABASE_TABLE);
        if (cursor.moveToFirst())
        {
            issuedate  = cursor.getString(InvoiceAdapter.COL_ISSUEDATE);
            service = cursor.getString(InvoiceAdapter.COL_SERVICE);
            dateserviceperformed = cursor.getString(InvoiceAdapter.COL_DATESERVICEPERFORMED);
            priceservice = cursor.getString(InvoiceAdapter.COL_PRICESERVICE);
            servicedesc = cursor.getString(InvoiceAdapter.COL_SERVICEDESC);
            amountdue = cursor.getString(InvoiceAdapter.COL_AMOUNTDUE);
            status = cursor.getString(InvoiceAdapter.COL_STATUS);
        }
        else 
            Toast.makeText(ShowDetailedInvoice.this, "failed to load cursor", Toast.LENGTH_SHORT).show();
        cursor.close();
        
    }
}
=======
//package com.pico.picoinvoices;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.Menu;
//import android.widget.ExpandableListView;
//import android.widget.SimpleExpandableListAdapter;
//
//public class ShowDetailedInvoice extends Activity {
//	 
//    ExpandableListViewAdapter listAdapter;
//    ExpandableListView expListView;
//    List<String> listDataHeader;
//    HashMap<String, List<String>> listDataChild;
// 
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
// 
//        // get the listview
//        expListView = (ExpandableListView) findViewById(R.id.lvExp);
// 
//        // preparing list data
//        prepareListData();
// 
//        listAdapter = new ExpandableListViewAdapter(this, listDataHeader, listDataChild);
// 
//        // setting list adapter
//        expListView.setAdapter(listAdapter);
//    }
// 
//    /*
//     * Preparing the list data
//     */
//    private void prepareListData() {
//        listDataHeader = new ArrayList<String>();
//        listDataChild = new HashMap<String, List<String>>();
// 
//        // Adding child data
//        listDataHeader.add("Top 250");
//        listDataHeader.add("Now Showing");
//        listDataHeader.add("Coming Soon..");
// 
//        // Adding child data
//        List<String> top250 = new ArrayList<String>();
//        top250.add("The Shawshank Redemption");
//        top250.add("The Godfather");
//        top250.add("The Godfather: Part II");
//        top250.add("Pulp Fiction");
//        top250.add("The Good, the Bad and the Ugly");
//        top250.add("The Dark Knight");
//        top250.add("12 Angry Men");
// 
//        List<String> nowShowing = new ArrayList<String>();
//        nowShowing.add("The Conjuring");
//        nowShowing.add("Despicable Me 2");
//        nowShowing.add("Turbo");
//        nowShowing.add("Grown Ups 2");
//        nowShowing.add("Red 2");
//        nowShowing.add("The Wolverine");
// 
//        List<String> comingSoon = new ArrayList<String>();
//        comingSoon.add("2 Guns");
//        comingSoon.add("The Smurfs 2");
//        comingSoon.add("The Spectacular Now");
//        comingSoon.add("The Canyons");
//        comingSoon.add("Europa Report");
// 
//        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), nowShowing);
//        listDataChild.put(listDataHeader.get(2), comingSoon);
//    }
//}
>>>>>>> b78ad611cf4d378f58f0354984cd91df4c2925ab
