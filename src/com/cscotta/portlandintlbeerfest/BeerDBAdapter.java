package com.cscotta.portlandintlbeerfest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

// Provides an interface to the local SQLite database.
public class BeerDBAdapter {
	
	private static final String DATABASE_NAME = "beer.db";
	private static final String DATABASE_TABLE = "beer";
	private static final int DATABASE_VERSION = 4;
 
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_BEER_TYPE = "beer_type";
	public static final String KEY_STYLE = "style";
	public static final String KEY_ABV = "abv";
	public static final String KEY_IBU = "ibu";
	public static final String KEY_COUNTRY = "country";
	public static final String KEY_SERVING = "serving";
  
	private SQLiteDatabase db;
  	private final Context context;
  	private searchDBOpenHelper dbHelper;

  	public BeerDBAdapter(Context _context) {
  		this.context = _context;
  		dbHelper = new searchDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
  	}
  
  	public void close() {
  		db.close();
  	}
  
  	public void open() throws SQLiteException {  
  		try {
  			db = dbHelper.getWritableDatabase();
  		} catch (SQLiteException ex) {
  			db = dbHelper.getReadableDatabase();
  		}
  	}  
  
  	// Insert a new saved search into the database.
  	public long insertQuery(String _query, String _city, String _category) {
  		
  		// Create a new row of values to insert.
  		ContentValues newQueryValues = new ContentValues();
  		
  		// Assign values for each row and insert.
  		newQueryValues.put(KEY_NAME, _query);
  		newQueryValues.put(KEY_BEER_TYPE, _city);
  		newQueryValues.put(KEY_STYLE, _category);
  		
  		return db.insert(DATABASE_TABLE, null, newQueryValues);
  	}

  	// Remove a saved search based on its index.
  	public boolean removeQuery(long _rowIndex) {
  		return db.delete(DATABASE_TABLE, KEY_ID + "=" + _rowIndex, null) > 0;
  	}
  	
  	// Remove a saved search based on its name.
  	public boolean removeQuery(String _query) {
  		return db.delete(DATABASE_TABLE, KEY_NAME + "='" + _query+ "'", null) > 0;
  	}

  	// Update a saved search.
  	public boolean updateQuery(long _rowIndex, String _query, String _city, String _category) {
  		ContentValues newValue = new ContentValues();
  		
  		//newValue.put(KEY_QUERY, _query);
  		//newValue.put(KEY_CITY, _city);
  		//newValue.put(KEY_CATEGORY, _category);
  		
  		return db.update(DATABASE_TABLE, newValue, KEY_ID + "=" + _rowIndex, null) > 0;
  	}

  	// Get a cursor to all items in the datastore. 
  	public Cursor getAllBeersCursor() {
  		return db.query(DATABASE_TABLE, 
  				new String[] { KEY_ID, KEY_NAME, KEY_BEER_TYPE, KEY_STYLE, KEY_ABV, KEY_IBU, KEY_COUNTRY, KEY_SERVING }, 
  				null, null, null, null, null);
  	}

  	// Move the cursor to a specific saved search in the table.
  	public Cursor setCursorToSearchItem(long _rowIndex) throws SQLException {
  		Cursor result = db.query(true, DATABASE_TABLE, 
  				new String[] { KEY_ID, KEY_NAME, KEY_BEER_TYPE, KEY_STYLE, KEY_ABV, KEY_IBU, KEY_COUNTRY, KEY_SERVING },
  				KEY_ID + "=" + _rowIndex, null, null, null, null, null);
  		if ((result.getCount() == 0) || !result.moveToFirst()) {
  			throw new SQLException("No beer found for row: " + _rowIndex);
  		}
  		return result;
  	}

  	// Fetch a single search query from the datastore.
  	public String getSearchItem(long _rowIndex) throws SQLException {
  		Cursor cursor = db.query(true, DATABASE_TABLE, 
  				new String[] { KEY_ID, KEY_NAME, KEY_BEER_TYPE, KEY_STYLE, KEY_ABV, KEY_IBU, KEY_COUNTRY, KEY_SERVING },
  				KEY_ID + "=" + _rowIndex, null, null, null, null, null);
  		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
  			throw new SQLException("No beer found for row: " + _rowIndex);
  		}

  		String query = cursor.getString(cursor.getColumnIndex(KEY_NAME));
  		return query;  
  	}

  	private static class searchDBOpenHelper extends SQLiteOpenHelper {

  		public searchDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
  			super(context, name, factory, version);
  		}

  		// SQL Statement to create a new database.
  		@Override
  		public void onCreate(SQLiteDatabase _db) {
  			Log.w("BEERFEST", "CALLED POPULATE DB");
  			for (String statement : BeerDBSQL.POPULATE_DB) {
  				_db.execSQL(statement);
  			}
  		}

  		@Override
  		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
  			Log.w("QueryDBAdapter", "Upgrading from version " + 
  					_oldVersion + " to " +
  					_newVersion + ", which will destroy all old data");
  			
  			// Drop the old table and create a new one.
  			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
  			onCreate(_db);
  		}
  	}
}