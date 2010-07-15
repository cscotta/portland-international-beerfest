package com.cscotta.portlandintlbeerfest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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
        	
        	Beer beer = beerDBAdapter.getBeer(beerID);
        	
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
        	beerIbu.setText(beerCountry.getText() + beer.getCountry()	);   
        	
        	TextView beerServing = (TextView) findViewById(R.id.beer_serving);
        	beerIbu.setText(beerServing.getText() + beer.getServing());
        	
        	TextView beerDescription = (TextView) findViewById(R.id.beer_description);
        	beerDescription.setText("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");        	
        }
	}
 }
