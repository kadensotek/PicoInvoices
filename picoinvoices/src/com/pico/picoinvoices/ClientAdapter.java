package com.pico.picoinvoices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClientAdapter  
{

	/////////////////////////////////////////////////////////////////////
	//	Constants & Data
	/////////////////////////////////////////////////////////////////////
	
	// DB Fields
	public static final String KEY_ROWID = "_id";
	public static final int COL_ROWID = 0;

	public static final String KEY_FNAME = "fname";
	public static final String KEY_LNAME = "lname";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_BUSINESS = "business";
	
	public static final int COL_FNAME = 1;
	public static final int COL_LNAME = 2;
	public static final int COL_ADDRESS = 3;
	public static final int COL_PHONE = 4;
	public static final int COL_EMAIL = 5;
	public static final int COL_BUSINESS = 6;

	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_FNAME, KEY_LNAME, KEY_ADDRESS, KEY_PHONE, KEY_EMAIL, KEY_BUSINESS};
	
	// DB info: it's name, and the table we are using (just one).
	public static final String DATABASE_TABLE = "contactInfo";
	
	// Context of application who uses us.
	private final Context context;
	
	private DatabaseHelper myDBHelper;
	private static SQLiteDatabase db;

	/////////////////////////////////////////////////////////////////////
	//	Public methods:
	/////////////////////////////////////////////////////////////////////
	
	public ClientAdapter(Context ctx) 
	{
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}
	
	// Open the database connection.
	public ClientAdapter open() 
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
	public long insertRow(String fname, String lname, String address, String phone, String email, String business) 
	{
	    //ContentValues use variable binding behind the scenes and therefore do not allow sql injection
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_FNAME, fname);
		initialValues.put(KEY_LNAME, lname);
		initialValues.put(KEY_ADDRESS, address);
		initialValues.put(KEY_PHONE, phone);
		initialValues.put(KEY_EMAIL, email);
		initialValues.put(KEY_BUSINESS, business);
		
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
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
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
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) 
		{
			c.moveToFirst();
		}
		return c;
	}
	
	// Change an existing row to be equal to new data.
	public boolean updateRow(long rowId, String fname, String lname, String address, String phone, String email, String business)
	{
		String where = KEY_ROWID + "=" + rowId;

		// Create row's data:
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_FNAME, fname);
		newValues.put(KEY_LNAME, lname);
		newValues.put(KEY_ADDRESS, address);
		newValues.put(KEY_PHONE, phone);
		newValues.put(KEY_EMAIL, email);
		newValues.put(KEY_BUSINESS, business);
		
		
		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}
	
	
	
	/////////////////////////////////////////////////////////////////////
	//	Private Helper Classes:
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
		    //This is handled in the DBAdapter on App startup to ensure that the database exists/is created on startup
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) 
		{
		    //This is handled in the DBAdapter on App startup to ensure that the database exists/is upgraded on startup
		}
	}
}
