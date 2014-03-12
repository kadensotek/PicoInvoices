package com.pico.picoinvoices;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter 
{

    private Activity activity;
    private ArrayList<Object> childtems;
    private LayoutInflater inflater;
    private ArrayList<String> parentItems;
    ArrayList<String[]> child;

    // constructor
    public ExpandableListViewAdapter(ArrayList<String> parents, ArrayList<Object> childern)
    {
        this.parentItems = parents;
        this.childtems = childern;
    }

    public void setInflater(LayoutInflater inflater, Activity activity) 
    {
        this.inflater = inflater;
        this.activity = activity;
    }
    
    // method getChildView is called automatically for each child view.
    //  Implement this method as per your requirement
    @SuppressWarnings("unchecked")
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) 
    {

        child = (ArrayList<String[]>) childtems.get(groupPosition);

        TextView textView = null;
        EditText editText = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_view, null);
        }
        final String[] values = child.get(childPosition);
         // get the textView reference and set the value
        
        textView = (TextView) convertView.findViewById(R.id.child_view_label);
        textView.setText(values[0]);

        editText = (EditText) convertView.findViewById(R.id.child_view_editText);
        editText.setText(values[1]);
        // set the ClickListener to handle the click event on child item
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(activity, values[1],
                        Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    // method getGroupView is called automatically for each parent item
    // Implement this method as per your requirement
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
    {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.parent_view, null);
        }

        ((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
        ((CheckedTextView) convertView).setChecked(isExpanded);

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) 
    {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) 
    {
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getChildrenCount(int groupPosition) 
    {
        return ((ArrayList<String>) childtems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) 
    {
        return null;
    }

    @Override
    public int getGroupCount() 
    {
        return parentItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) 
    {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) 
    {
        return 0;
    }

    @Override
    public boolean hasStableIds() 
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

}

//package com.pico.picoinvoices;
//
//import java.util.HashMap;
//import java.util.List;
//
//import android.content.Context;
//import android.graphics.Typeface;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.TextView;
//
//public class ExpandableListViewAdapter extends BaseExpandableListAdapter
//{
//
//	private Context _context;
//	private List<String> _listDataHeader; // header titles
//	// child data in format of header title, child title
//	private HashMap<String, List<String>> _listDataChild;
//
//	public ExpandableListViewAdapter(Context context,
//			List<String> listDataHeader,
//			HashMap<String, List<String>> listChildData)
//	{
//		this._context = context;
//		this._listDataHeader = listDataHeader;
//		this._listDataChild = listChildData;
//	}
//
//	@Override
//	public Object getChild(int groupPosition, int childPosititon)
//	{
//		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//				.get(childPosititon);
//	}
//
//	@Override
//	public long getChildId(int groupPosition, int childPosition)
//	{
//		return childPosition;
//	}
//
//	@Override
//	public View getChildView(int groupPosition, final int childPosition,
//			boolean isLastChild, View convertView, ViewGroup parent)
//	{
//
//		final String childText = (String) getChild(groupPosition, childPosition);
//
//		if (convertView == null)
//		{
//			LayoutInflater infalInflater = (LayoutInflater) this._context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = infalInflater.inflate(R.layout.list_item, null);
//		}
//
//		TextView txtListChild = (TextView) convertView
//				.findViewById(R.id.lblListItem);
//
//		txtListChild.setText(childText);
//		return convertView;
//	}
//
//	@Override
//	public int getChildrenCount(int groupPosition)
//	{
//		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//				.size();
//	}
//
//	@Override
//	public Object getGroup(int groupPosition)
//	{
//		return this._listDataHeader.get(groupPosition);
//	}
//
//	@Override
//	public int getGroupCount()
//	{
//		return this._listDataHeader.size();
//	}
//
//	@Override
//	public long getGroupId(int groupPosition)
//	{
//		return groupPosition;
//	}
//
//	@Override
//	public View getGroupView(int groupPosition, boolean isExpanded,
//			View convertView, ViewGroup parent)
//	{
//		String headerTitle = (String) getGroup(groupPosition);
//		if (convertView == null)
//		{
//			LayoutInflater infalInflater = (LayoutInflater) this._context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = infalInflater.inflate(R.layout.list_group, null);
//		}
//
//		TextView lblListHeader = (TextView) convertView
//				.findViewById(R.id.lblListHeader);
//		lblListHeader.setTypeface(null, Typeface.BOLD);
//		lblListHeader.setText(headerTitle);
//
//		return convertView;
//	}
//
//	@Override
//	public boolean hasStableIds()
//	{
//		return false;
//	}
//
//	@Override
//	public boolean isChildSelectable(int groupPosition, int childPosition)
//	{
//		return true;
//	}
//}
