package com.pico.picoinvoices;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter
{

    private Activity _context;
    private Map<String, List<String>> _invoice;
    private List<String> _content;

    public ExpandableListViewAdapter(Activity context, List<String> content,
            Map<String, List<String>> invoice)
    {
        this._context = context;
        this._invoice = invoice;
        this._content = content;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return _invoice.get(_content.get(groupPosition)).get(
                childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent)
    {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = _context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.child_view, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.laptop);

        item.setText(laptop);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return _invoice.get(_content.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return _content.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return _content.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent)
    {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parent_view, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.laptop);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}