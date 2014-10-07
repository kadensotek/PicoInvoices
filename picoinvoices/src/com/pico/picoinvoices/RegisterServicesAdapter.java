package com.pico.picoinvoices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RegisterServicesAdapter  
{

    /////////////////////////////////////////////////////////////////////
    //  Constants & Data
    /////////////////////////////////////////////////////////////////////
    
    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE = "type";
    public static final String KEY_RATE = "rate";
    
    public static final int COL_NAME = 1;
    public static final int COL_TYPE= 2;
    public static final int COL_RATE= 3;

    
    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_RATE};
    
    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_TABLE = "services";
    
    // Context of application who uses us.
    private final Context context;
    
    private DatabaseHelper myDBHelper;
    private static SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //  Public methods:
    /////////////////////////////////////////////////////////////////////
    
    public RegisterServicesAdapter(Context ctx) 
    {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }
    
    // Open the database connection.
    public RegisterServicesAdapter open() 
    {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    
    // Close the database connection.
    public void close() 
    {
        myDBHelper.close();
    }
    
    // Add a new set of values to the database.
    public long insertRow(String name, String type, String rate) 
    {
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_TYPE, type);
        initialValues.put(KEY_RATE, rate);
        
        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) 
    {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }
    
    public void deleteAll() 
    {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) 
        {
            do 
            {
                deleteRow(c.getLong((int) rowId));              
            } while (c.moveToNext());
        }
        c.close();
    }
    
    // Return all data in the database.
    public Cursor getAllRows() 
    {
        String where = null;
        Cursor c =  db.query(true, DATABASE_TABLE, ALL_KEYS, 
                            where, null, null, null, null, null);
        if (c != null) 
        {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId)
    {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c =  db.query(true, DATABASE_TABLE, ALL_KEYS, 
                        where, null, null, null, null, null);
        if (c != null) 
        {
            c.moveToFirst();
        }
        return c;
    }
    
    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, String name, String type, String rate)
    {
        String where = KEY_ROWID + "=" + rowId;

        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME, name);
        newValues.put(KEY_TYPE, type);
        newValues.put(KEY_RATE, rate);
        
        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }
    
    
    
    /////////////////////////////////////////////////////////////////////
    //  Private Helper Classes:
    /////////////////////////////////////////////////////////////////////
    
    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) 
        {
            super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) 
        {
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) 
        {
        }
    }
}
