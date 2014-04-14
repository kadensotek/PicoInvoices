package com.pico.picoinvoices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListViewAdapter extends SimpleCursorAdapter
{

    public ListViewAdapter(Context context, int layout, Cursor c,
            String[] from, int[] to, int flags)
    {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        // get reference to the row
        View view = super.getView(position, convertView, parent);
        
        if (view.findViewById(R.id.invoice_listview_layout_template_txtStatus) != null)
        {
            TextView textView = (TextView) view.findViewById(R.id.invoice_listview_layout_template_txtStatus);
            String customerID = textView.getText().toString();
            System.out.println(customerID);
            if (customerID.equals("Paid"))
            {
                textView.setTextColor(Color.rgb(0,200,0));
            }
            else if (customerID.equals("Quote"))
            {
                textView.setTextColor(Color.rgb(100,255,0));
            }
            else if (customerID.equals("Overdue"))
            {
                textView.setTextColor(Color.rgb(200,0,0));
            }
            else
            {
                textView.setTextColor(Color.rgb(0, 0, 0));
            }
        }
        
        if(view.findViewById(R.id.invoice_listview_layout_template_txtDateDue) != null && view.findViewById(R.id.invoice_listview_layout_template_txtDate) != null)
        {
            TextView date = (TextView) view.findViewById(R.id.invoice_listview_layout_template_txtDate);
            TextView dueDate = (TextView) view.findViewById(R.id.invoice_listview_layout_template_txtDateDue);
            
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            try
            {
                Date dated = originalFormat.parse(dueDate.getText().toString());
                String formattedDate = targetFormat.format(dated);
                System.out.println(formattedDate);
                dueDate.setText(formattedDate);
            } catch (ParseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
              // 20120821
        }
        return view;
    }

}
