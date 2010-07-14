package com.cscotta.portlandintlbeerfest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

public class PortlandBeerfest extends Activity {
	Cursor searchCursor;
	BeerDBAdapter searchDBAdapter;
	private BeerAdapter savedSearchAdapter;
	final ArrayList<Beer> savedSearches = new ArrayList<Beer>();
	
	String currentCity = "Portland";
	String currentCategory = "For Sale";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Initialize the user's list of saved searches.
    	ListView savedSearchListView = (ListView) findViewById(R.id.SearchListView);
        
        int resID = R.layout.savedsearch_item;
        savedSearchAdapter = new BeerAdapter(this, resID, savedSearches, PortlandBeerfest.this);
        savedSearchListView.setAdapter(savedSearchAdapter);
        
        searchDBAdapter = new BeerDBAdapter(this);
        searchDBAdapter.open();
        populateSearchList();
        
        // Bind event for adding to the list of Saved Searches via keypad or "Enter" button.
        final EditText searchInput = (EditText) findViewById(R.id.search_input);
        searchInput.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
              if (event.getAction() == KeyEvent.ACTION_DOWN)
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                	addSearchToList(searchInput, savedSearchAdapter);
                	return true;
                }
              return false;
            }
          });
        
        // Bind event for adding to the list of Saved Searches via the "Add" button.
        final Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	addSearchToList(searchInput, savedSearchAdapter);
            }
        });
    
        // Populate the City selector with the list of supported cities.
        Spinner citySpinner = (Spinner) findViewById(R.id.city_selector);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(
        		this, R.array.cities_array, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        
        // Populate the Category selector with the list of supported categories.
        Spinner categorySpinner = (Spinner) findViewById(R.id.category_selector);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
        		this, R.array.categories_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        
        // Update the current city when the user selects one from the picker.
        class CitySpinnerListener implements OnItemSelectedListener {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	currentCity = parent.getItemAtPosition(pos).toString();
            }

            @SuppressWarnings("rawtypes")
			public void onNothingSelected(AdapterView parent) { }
        }
        
        // Update the current category when the user selects one from the picker.
        class CategorySpinnerListener implements OnItemSelectedListener {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	currentCategory = parent.getItemAtPosition(pos).toString();
            }

            @SuppressWarnings("rawtypes")
			public void onNothingSelected(AdapterView parent) { }
        }
        
        // Bind listeners for changes in the category picker.
        citySpinner.setOnItemSelectedListener(new CitySpinnerListener());
        categorySpinner.setOnItemSelectedListener(new CategorySpinnerListener());

    }
    
    // Get all the saved searches from the database.
    private void populateSearchList() {
    	searchCursor = searchDBAdapter.getAllSearchItemsCursor();
        startManagingCursor(searchCursor);
        updateArray();
    }
    
    // Refresh the list of saved searches displayed on the screen.
    private void updateArray() {
    	searchCursor.requery();
    	savedSearches.clear();
  	    
  	  	if (searchCursor.moveToFirst())
  	  		do { 
  	  			String query = searchCursor.getString(searchCursor.getColumnIndex("query"));
  	  			String city = searchCursor.getString(searchCursor.getColumnIndex("city"));
  	  			String category = searchCursor.getString(searchCursor.getColumnIndex("category"));
  	  			
  	  			Beer savedSearch = new Beer(query, city, category);
  	  			savedSearches.add(0, savedSearch);
  	  		} while (searchCursor.moveToNext());
  	  
  	  	savedSearchAdapter.notifyDataSetChanged();
  	}
    
    // Adds an item to the list of Saved Searches.
    private void addSearchToList(EditText searchInput, ArrayAdapter<Beer> savedSearchAdapter) {
    	String query = searchInput.getText().toString();
    	
    	String apid = BeerfestUtils.getOrSetApid("get", null, getApplicationContext());
    	
    	if (!query.equals("") && apid != null) {
    		searchDBAdapter.insertQuery(query, BeerfestUtils.getCitySubdomain(currentCity), 
    				BeerfestUtils.getCategorySlug(currentCategory));
            updateArray();
        	searchInput.setText("");
        	hideKeyboard();
    	} else if (apid == null) {
    		showToast("Please wait a moment for your device to register with Urban Airship.");
    	} else {
    		showToast("Please type a search term before clicking 'Add'.");
    	}
    }

    // Delete a saved search from the database.
    public void removeQuery(String query) {
    	showToast("Removing your notification for '" + query + "' - just a moment.");
    	String apid = BeerfestUtils.getOrSetApid("get", null, getApplicationContext());
    	
    	if (apid != null) {
    		searchDBAdapter.removeQuery(query);
    		updateArray();
    	} else {
    		showToast("Please wait a moment for your device to register for Push Notifications.");
    	}    	
    }
    
    // Hide the Keyboard.
    private void hideKeyboard() {
    	final EditText searchInput = (EditText) findViewById(R.id.search_input);
    	InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	inputManager.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
    }
    
    // Toss up a toast!
    private void showToast(String message) {
    	Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
    	toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
 
 }
