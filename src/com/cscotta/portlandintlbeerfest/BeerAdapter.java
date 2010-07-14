package com.cscotta.portlandintlbeerfest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
		
	    final Beer query = getItem(position);

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
	    ImageView deleteButton = (ImageView) searchView.findViewById(R.id.delete_button);
	      
	    queryView.setText(query.getQuery());
		
	    // Alternate the background color with each row.
	    if (position % 2 == 0) {
	    	queryView.setBackgroundResource(R.color.bluewhite);
	    	deleteButton.setBackgroundResource(R.color.bluewhite);
	    } else {
	    	queryView.setBackgroundResource(R.color.white);
	    	deleteButton.setBackgroundResource(R.color.white);	    	
	    }
	    
	    // Add a listener to the delete button to remove a saved search.
	    deleteButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		activity.removeQuery(query.getQuery());
            }
        });
	    
	    // Add a listener to the search text to view search results.
	    queryView.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		String urlQuery = query.getQuery();
	    		try { urlQuery = URLEncoder.encode(query.getQuery(), "UTF-8"); }
	    		catch (UnsupportedEncodingException e) { }
	    		
	    		Uri uri = Uri.parse("http://" + query.getCity() + ".craigslist.org/search/?catAbb=" + 
	    				query.getCategory()  + "&query=" + urlQuery);
	    		Intent myIntent = new Intent(Intent.ACTION_VIEW, uri); 
	    		activity.startActivity(myIntent);
	    	}
	    });
	    
		return searchView;
	}

}
