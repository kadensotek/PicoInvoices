package com.pico.picoinvoices;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPAdapter
{
    public static final String CLIENT_ID = "0-0";
    private static final String APP_SHARED_PREFS = SPAdapter.class
            .getSimpleName(); // Name of the file -.xml
    private SharedPreferences _sharedPrefs;
    private Editor _prefsEditor;

    public SPAdapter(Context context)
    {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
                Context.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Getters
    // ///*
    // //////////////////////////////////////////////////////
    public int getClientID()
    {
        String[] values = _sharedPrefs.getString(CLIENT_ID, "0-0").split("-");
        return Integer.parseInt(values[0]);
    }

    public int getInvoiceID()
    {
        String[] values = _sharedPrefs.getString(CLIENT_ID, "0-0").split("-");
        return Integer.parseInt(values[1]);
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Setters
    // ///*
    // //////////////////////////////////////////////////////
    public void saveClientID(String id)
    {
        String[] values = _sharedPrefs.getString(CLIENT_ID, "0-0").split("-");
        values[0] = id;
        String fin = values[0] + "-" + values[1];
        _prefsEditor.putString(CLIENT_ID, fin);
        _prefsEditor.commit();
    }

    public void saveInvioceID(String string)
    {
        String[] values = _sharedPrefs.getString(CLIENT_ID, "0-0").split("-");
        values[1] = string;
        String fin = values[0] + "-" + values[1];
        _prefsEditor.putString(CLIENT_ID, fin);
        _prefsEditor.commit();

    }

}
