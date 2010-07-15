package com.cscotta.portlandintlbeerfest;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.Gravity;
import android.view.View;

public class PortlandBeerfest extends Activity {
	Cursor searchCursor;
	BeerDBAdapter beerDBAdapter;
	private BeerAdapter beerListAdapter;
	final ArrayList<Beer> beers = new ArrayList<Beer>();
	
	String currentBeerType = "All";
	String currentBeerCountry = "All";

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
            	populateBeerList();
            }

            @SuppressWarnings({ "unchecked" })
			public void onNothingSelected(AdapterView parent) { }
        }
        
        // Update the current category when the user selects one from the picker.
        class BeerCountryListener implements OnItemSelectedListener {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	currentBeerCountry = parent.getItemAtPosition(pos).toString();
            	populateBeerList();
            }

            @SuppressWarnings("unchecked")
			public void onNothingSelected(AdapterView parent) { }
        }
        
        final CheckBox onlyFavorites = (CheckBox) findViewById(R.id.favorites_checkbox);
        onlyFavorites.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	populateBeerList();
            }
        });
        
        // Bind listeners for changes in the category picker.
        beerTypeSpinner.setOnItemSelectedListener(new BeerTypeSpinnerListener());
        beerStyleSpinner.setOnItemSelectedListener(new BeerCountryListener());

    }
    
    // Get all the saved searches from the database.
    private void populateBeerList() {
    	final CheckBox onlyFavorites = (CheckBox) findViewById(R.id.favorites_checkbox);
    	String typeFilter = null;
    	String countryFilter = null;
    	
    	if (currentBeerType != null && !currentBeerType.equals("All")) typeFilter = currentBeerType;
    	if (currentBeerCountry != null && !currentBeerCountry.equals("All")) countryFilter = currentBeerCountry;
    	
    	searchCursor = beerDBAdapter.getAllBeersCursor(onlyFavorites.isChecked(), typeFilter, countryFilter);
        startManagingCursor(searchCursor);
        updateArray();
    }
    
    // Refresh the list of saved searches displayed on the screen.
    private void updateArray() {
    	searchCursor.requery();
    	beers.clear();

  	  	if (searchCursor.moveToFirst())
  	  		do { 
  	  			Integer id = searchCursor.getInt(searchCursor.getColumnIndex("_id"));
  	  			String name = searchCursor.getString(searchCursor.getColumnIndex("name"));
  	  			String type = searchCursor.getString(searchCursor.getColumnIndex("beer_type"));
  	  			String style = searchCursor.getString(searchCursor.getColumnIndex("style"));
  	  			Integer favorite = searchCursor.getInt(searchCursor.getColumnIndex("favorite"));
  	  			
  	  			Beer beer = new Beer(id, name, type, style, favorite);
  	  			beers.add(0, beer);
  	  		} while (searchCursor.moveToNext());
  	  	
  	  	if (beers.size() == 0) showToast("Sorry, no beers were found for this filter");
  	  	
  	  	beerListAdapter.notifyDataSetChanged();
  	}
    
    // Adds an item to the list of Saved Searches.
    void setFavorite(Integer id, String name, Boolean favorite) {
    	beerDBAdapter.setFavorite(id, favorite);
    	String addedOrRemoved = (favorite ? "added to" : "removed from");
    	showToast(name + " has been " + addedOrRemoved + " your favorites.");
    }
    
    // Toss up a toast!
    private void showToast(String message) {
    	Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
    	toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
 
 }
