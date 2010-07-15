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
	private static final int DATABASE_VERSION = 12;
 
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_BEER_TYPE = "beer_type";
	public static final String KEY_STYLE = "style";
	public static final String KEY_ABV = "abv";
	public static final String KEY_IBU = "ibu";
	public static final String KEY_COUNTRY = "country";
	public static final String KEY_SERVING = "serving";
	public static final String KEY_FAVORITE = "favorite";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_LINK = "link";
  
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

  	// Update a saved search.
  	public boolean updateQuery(long _rowIndex, String _query, String _city, String _category) {
  		ContentValues newValue = new ContentValues();
  		
  		//newValue.put(KEY_QUERY, _query);
  		//newValue.put(KEY_CITY, _city);
  		//newValue.put(KEY_CATEGORY, _category);
  		
  		return db.update(DATABASE_TABLE, newValue, KEY_ID + "=" + _rowIndex, null) > 0;
  	}
  	
  	boolean setFavorite(Integer id, Boolean favorite) {
  		int result = 0;
  		if (favorite == true) result = 1;
  		
  		ContentValues newValue = new ContentValues();
  		newValue.put(KEY_FAVORITE, result);
  		return db.update(DATABASE_TABLE, newValue, KEY_ID + "=" + id, null) > 0;
    }
  	

  	// Get a cursor to all items in the datastore. 
  	public Cursor getAllBeersCursor(Boolean onlyFavorites, String type, String country) {
  		String conditions = null;
  		String faveCondition = null;
  		String typeCondition = null;
  		String countryCondition = null;
  		
  		if (onlyFavorites) faveCondition = KEY_FAVORITE + "=" + "1";
  		if (type != null) typeCondition = KEY_STYLE + "= '" + type + "'";
  		if (country != null) countryCondition = KEY_COUNTRY + "= '" + country + "'";
  		
  		// Apply filtering conditions.
  		if (faveCondition != null || typeCondition != null || countryCondition != null) {
  			conditions = "";
  			if (faveCondition != null) conditions = faveCondition;
  			
  			if (typeCondition != null) {
  				conditions = (conditions.equals("") ? typeCondition : conditions + " AND " + typeCondition);
  			}
  			
  			if (countryCondition != null) {
  				conditions = (conditions.equals("") ? countryCondition : conditions + " AND " + countryCondition);
  			}
  		}
  		
  		return db.query(DATABASE_TABLE, 
  				new String[] { KEY_ID, KEY_NAME, KEY_BEER_TYPE, KEY_STYLE, KEY_ABV, KEY_IBU, KEY_COUNTRY, KEY_SERVING, KEY_FAVORITE, KEY_DESCRIPTION }, 
  				conditions, null, null, null, "name desc");
  	}

  	// Move the cursor to a specific saved search in the table.
  	public Cursor setCursorToSearchItem(long _rowIndex) throws SQLException {
  		Cursor result = db.query(true, DATABASE_TABLE, 
  				new String[] { KEY_ID, KEY_NAME, KEY_BEER_TYPE, KEY_STYLE, KEY_ABV, KEY_IBU, KEY_COUNTRY, KEY_SERVING, KEY_FAVORITE, KEY_DESCRIPTION, KEY_LINK },
  				KEY_ID + "=" + _rowIndex, null, null, null, null, null);
  		if ((result.getCount() == 0) || !result.moveToFirst()) {
  			throw new SQLException("No beer found for row: " + _rowIndex);
  		}
  		return result;
  	}

  	// Fetch a beer from the datastore.
  	public Beer getBeer(Integer beerID) throws SQLException {
  		Cursor cursor = db.query(true, DATABASE_TABLE, 
  				new String[] { KEY_ID, KEY_NAME, KEY_BEER_TYPE, KEY_STYLE, KEY_ABV, KEY_IBU, KEY_COUNTRY, KEY_SERVING, KEY_FAVORITE, KEY_DESCRIPTION, KEY_LINK },
  				KEY_ID + "=" + beerID, null, null, null, null, null);
  		
  		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
  			throw new SQLException("No beer found for row: " + beerID);
  		}

  		Integer id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
  		String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
  		String type = cursor.getString(cursor.getColumnIndex(KEY_BEER_TYPE));
  		String style = cursor.getString(cursor.getColumnIndex(KEY_STYLE));
  		String abv = cursor.getString(cursor.getColumnIndex(KEY_ABV));
  		Integer ibu = cursor.getInt(cursor.getColumnIndex(KEY_IBU));
  		String country = cursor.getString(cursor.getColumnIndex(KEY_COUNTRY));
  		String serving = cursor.getString(cursor.getColumnIndex(KEY_SERVING));
  		Integer favorite = cursor.getInt(cursor.getColumnIndex(KEY_FAVORITE));
  		String desc = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
  		String link = cursor.getString(cursor.getColumnIndex(KEY_LINK));
  		
  		Beer beer = new Beer(id, name, type, style, abv, ibu, country, serving, favorite, desc, link);
  		
  		return beer;  
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