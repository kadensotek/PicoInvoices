package com.pico.picoinvoices;

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
            if (customerID.equals("paid"))
            {
                textView.setTextColor(Color.rgb(200,0,0));
            }
        }
//        // check for odd or even to set alternate colors to the row background
//        if (position % 2 == 0)
//        {
//            view.setBackgroundColor(Color.rgb(238, 233, 233));
//        } else
//        {
//            view.setBackgroundColor(Color.rgb(255, 255, 255));
//        }
        return view;
    }

}
