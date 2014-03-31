package com.pico.picoinvoices;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String[]>
{
    
   private Context activity;
   private List<String[]> data;
   public Resources res;
   LayoutInflater inflater;
    
   /*************  CustomAdapter Constructor *****************/
   public SpinnerAdapter(Context cxt, List<String[]> list) 
    {
       super(cxt, 0);
        
       /********** Take passed values **********/
       activity = cxt;
       data     = list;
   
       /***********  Layout inflator to call external xml layout () **********************/
       inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
     }

   @Override
   public View getDropDownView(int position, View convertView,ViewGroup parent) {
       return getCustomView(position, convertView, parent);
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
       return getCustomView(position, convertView, parent);
   }

   // This funtion called for each row ( Called data.size() times )
   public View getCustomView(int position, View convertView, ViewGroup parent) {

       /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
       View row = inflater.inflate(R.layout.spinner_text_layout, parent, false);
        
        
       TextView label        = (TextView)row.findViewById(R.id.spinnerText1);
       TextView sub          = (TextView)row.findViewById(R.id.spinnerText2);
      
        
       if(position==0){
            
           // Default selected Spinner item 
           label.setText("Please select company");
           sub.setText("");
       }
       else
       {
           // Set values for spinner each row 
           label.setText(data.get(position)[0].toString());
           sub.setText(data.get(position)[1].toString());
       }   

       return row;
     }
}