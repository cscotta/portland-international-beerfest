package com.cscotta.portlandintlbeerfest;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import android.widget.LinearLayout;
import android.widget.TextView;

public class BeerAdapter extends ArrayAdapter<Beer> {

	int resource;
	PortlandBeerfest activity;
	
	// Initialize the array adapter with a reference to the main activity
	// so we can re-populate the list and update the server when one is deleted.
	public BeerAdapter(Context _context, int _resource, List<Beer> _items, PortlandBeerfest _activity) {
		super(_context, _resource, _items);
		resource = _resource;
		activity= _activity;
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		LinearLayout searchView;
		
	    final Beer beer = getItem(position);

	    // Inflate the view from the XML layout.
		if (convertView == null) {
			searchView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, searchView, true);
		} else {
			searchView = (LinearLayout) convertView;
		}
		
		// Draw the search text and delete button.
	    final TextView queryView = (TextView) searchView.findViewById(R.id.query);
	    final CheckBox faveButton = (CheckBox) searchView.findViewById(R.id.delete_button);
	      
	    queryView.setText(beer.getName());
	    faveButton.setChecked(beer.getFavorite());
		
	    // Alternate the background color with each row.
	    if (position % 2 == 0) {
	    	queryView.setBackgroundResource(R.color.bluewhite);
	    	faveButton.setBackgroundResource(R.color.bluewhite);
	    } else {
	    	queryView.setBackgroundResource(R.color.white);
	    	faveButton.setBackgroundResource(R.color.white);	    	
	    }
	    
	    // Add a listener to the checkbox to add/remove an item from favorites.
	    faveButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		activity.setFavorite(beer.getID(), beer.getName(), faveButton.isChecked());
            }
        });
	    
	    // Add a listener to the beer text to view details.
	    queryView.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent launchDetail = new Intent();
	    		launchDetail.setClassName("com.cscotta.portlandintlbeerfest", "com.cscotta.portlandintlbeerfest.BeerDetail");
	    		launchDetail.putExtra("com.cscotta.portlandintlbeerfest.beerID", beer.getID().toString());
	    		activity.startActivity(launchDetail);	    		
	    	}
	    });
	    
		return searchView;
	}

}
