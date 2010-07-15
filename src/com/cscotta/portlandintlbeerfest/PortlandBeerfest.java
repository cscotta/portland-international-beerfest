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
	BeerDBAdapter beerDBAdapter;
	private BeerAdapter beerListAdapter;
	final ArrayList<Beer> beers = new ArrayList<Beer>();
	
	String currentBeerType = "Ale";
	String currentBeerStyle = "IPA";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Initialize the user's list of saved searches.
    	ListView beerListView = (ListView) findViewById(R.id.SearchListView);
        
        int resID = R.layout.savedsearch_item;
        beerListAdapter = new BeerAdapter(this, resID, beers, PortlandBeerfest.this);
        beerListView.setAdapter(beerListAdapter);
        
        beerDBAdapter = new BeerDBAdapter(this);
        beerDBAdapter.open();
        populateBeerList();
        
        // Bind event for adding to the list of Saved Searches via the "Add" button.
        final Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// addToFavoriteList(searchInput, beerListAdapter);
            }
        });
    
        // Populate the City selector with the list of supported cities.
        Spinner beerTypeSpinner = (Spinner) findViewById(R.id.city_selector);
        ArrayAdapter<CharSequence> beerTypeAdapter = ArrayAdapter.createFromResource(
        		this, R.array.beer_types_array, android.R.layout.simple_spinner_item);
        beerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        beerTypeSpinner.setAdapter(beerTypeAdapter);
        
        // Populate the Category selector with the list of supported categories.
        Spinner beerStyleSpinner = (Spinner) findViewById(R.id.category_selector);
        ArrayAdapter<CharSequence> beerStyleAdapter = ArrayAdapter.createFromResource(
        		this, R.array.beer_countries_array, android.R.layout.simple_spinner_item);
        beerStyleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        beerStyleSpinner.setAdapter(beerStyleAdapter);
        
        // Update the current city when the user selects one from the picker.
        class BeerTypeSpinnerListener implements OnItemSelectedListener {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	currentBeerType = parent.getItemAtPosition(pos).toString();
            }

            @SuppressWarnings({ "unchecked" })
			public void onNothingSelected(AdapterView parent) { }
        }
        
        // Update the current category when the user selects one from the picker.
        class BeerStyleListener implements OnItemSelectedListener {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	currentBeerStyle = parent.getItemAtPosition(pos).toString();
            }

            @SuppressWarnings("unchecked")
			public void onNothingSelected(AdapterView parent) { }
        }
        
        // Bind listeners for changes in the category picker.
        beerTypeSpinner.setOnItemSelectedListener(new BeerTypeSpinnerListener());
        beerStyleSpinner.setOnItemSelectedListener(new BeerStyleListener());

    }
    
    // Get all the saved searches from the database.
    private void populateBeerList() {
    	searchCursor = beerDBAdapter.getAllBeersCursor();
        startManagingCursor(searchCursor);
        updateArray();
    }
    
    // Refresh the list of saved searches displayed on the screen.
    private void updateArray() {
    	searchCursor.requery();
    	beers.clear();

  	  	if (searchCursor.moveToFirst())
  	  		do { 
  	  			String name = searchCursor.getString(searchCursor.getColumnIndex("name"));
  	  			String type = searchCursor.getString(searchCursor.getColumnIndex("beer_type"));
  	  			String style = searchCursor.getString(searchCursor.getColumnIndex("style"));
  	  			
  	  			Beer beer = new Beer(name, type, style);
  	  			beers.add(0, beer);
  	  		} while (searchCursor.moveToNext());
  	  
  	  	beerListAdapter.notifyDataSetChanged();
  	}
    
    // Adds an item to the list of Saved Searches.
    private void addToFavoriteList(EditText searchInput, ArrayAdapter<Beer> savedSearchAdapter) {
    	String query = searchInput.getText().toString();
    	
    	if (!query.equals("")) {
    		beerDBAdapter.insertQuery(query, currentBeerType, currentBeerStyle);
            updateArray();
        	searchInput.setText("");
        	hideKeyboard();
    	} else {
    		showToast("Please type a search term before clicking 'Add'.");
    	}
    }

    // Delete a saved search from the database.
    public void removeFromFavorites(String query) {
    	showToast("Removing your favorite: '" + query + "' - just a moment.");
    	
    		beerDBAdapter.removeQuery(query);
    		updateArray();
    }
    
    // Hide the Keyboard.
    private void hideKeyboard() {
    	//final EditText searchInput = (EditText) findViewById(R.id.search_input);
    	//InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	//inputManager.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
    }
    
    // Toss up a toast!
    private void showToast(String message) {
    	Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
    	toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
 
 }
