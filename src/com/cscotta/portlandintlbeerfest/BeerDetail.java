package com.cscotta.portlandintlbeerfest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class BeerDetail extends Activity {
	
	BeerDBAdapter beerDBAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_detail);
        
        beerDBAdapter = new BeerDBAdapter(this);
        beerDBAdapter.open();
        
        Bundle extras = getIntent().getExtras(); 
        if (extras != null) {
        	String beerid = extras.getString("com.cscotta.portlandintlbeerfest.beerID");
        	Integer beerID = Integer.parseInt(beerid);
        	
        	final Beer beer = beerDBAdapter.getBeer(beerID);
        	
        	TextView beerName = (TextView) findViewById(R.id.beer_name);
        	beerName.setText(beer.getName());
        	
        	TextView beerType = (TextView) findViewById(R.id.beer_type);
        	beerType.setText(beerType.getText() + beer.getType());
        	
        	TextView beerStyle = (TextView) findViewById(R.id.beer_style);
        	beerStyle.setText(beerStyle.getText() + beer.getStyle());
        	
        	TextView beerAbv = (TextView) findViewById(R.id.beer_abv);
        	beerAbv.setText(beerAbv.getText() + beer.getAbv());
        	
        	TextView beerIbu = (TextView) findViewById(R.id.beer_ibu);
        	beerIbu.setText(beerIbu.getText() + beer.getIbu().toString());        	

        	TextView beerCountry = (TextView) findViewById(R.id.beer_country);
        	beerCountry.setText(beerCountry.getText() + beer.getCountry()	);   
        	
        	TextView beerServing = (TextView) findViewById(R.id.beer_serving);
        	beerServing.setText(beerServing.getText() + beer.getServing());
        	
        	TextView beerDescription = (TextView) findViewById(R.id.beer_description);
        	beerDescription.setText(beer.getDescription());
        	
        	final CheckBox faveButton = (CheckBox) findViewById(R.id.favorites_checkbox);
        	faveButton.setChecked(beer.getFavorite());
    	    faveButton.setOnClickListener(new View.OnClickListener() {
    	    	public void onClick(View v) {
    	    		setFavorite(beer.getID(), beer.getName(), faveButton.isChecked());
                }
            });
        }
	}
	
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
