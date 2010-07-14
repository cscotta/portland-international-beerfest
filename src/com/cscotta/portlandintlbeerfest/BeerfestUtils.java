package com.cscotta.portlandintlbeerfest;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.ArrayUtils;

import android.content.Context;
import android.util.Log;

public class BeerfestUtils {
	
	// Preference filename where APID is stored.
	private final static String APID_FILENAME = "apid";
	
	// List of cities available to search.
	public final static String[] cities = {"Portland","Atlanta","Austin","Boston","Chicago","Houston","Las Vegas","Los Angeles","Miami",
		"New York","Orange Co","Philadelphia","San Diego","Seattle","San Francisco","Seattle","Washington, D.C."};
	
	// List of subdomains for cities available to search.
	public final static String[] citySubdomains = {"portland","atlanta","austin","boston","chicago","houston","lasvegas","losangeles","miami",
		"newyork","orangeco","philadelphia","sandiego","seattle","sfbay","seattle","washingtondc"};
	
	// List of categories available to search.
	public final static String[] categories = {"For Sale", "Antiques","Appliances","Arts + Crafts","Auto Parts","Baby + Kids","Barter","Beauty + Health",
		"Bikes","Boats","Books","Business","CDs / DVDs / VHS","Cell Phones","Clothes + Accessories","Collectibles","Computer","Electronics","Farm + Garden",
		"Free","Furniture","Garage Sale","General","Household","Jewelry","Materials","Motorcycles","Musical Instruments","Photo + Video","RVs","Sporting",
		"Tickets","Tools","Toys + Games","Wanted","Video Gaming"};
	
	// List of slugs for categories available to search.
	public final static String[] categorySlugs = {"sss","atq","app","art","pts","bab","bar","hab","bik","boa","bks","bfs","emd","mob","clo","clt","sys","ele",
		"grd","zip","fua","gms","for","hsh","jwl","mat","mcy","msg","pho","rvs","spo","tix","tls","tag","wan","vgm"};
	
	// Synchronized method for retrieving or setting the APID for AirMail.
    public synchronized static String getOrSetApid(String command, String apid, Context context) {
    	String result = null;
    	
    	if (command.equals("get")) {
        	FileInputStream apidFile = null;
        	
        	try { apidFile = context.openFileInput(APID_FILENAME); } 
        	catch (FileNotFoundException e) { Log.d("PortlandBeerfest", "APID file does not exist"); }
    		
    		try {
    			// Get the object of DataInputStream
    			apid = "";
    			String strLine;
    			DataInputStream in = new DataInputStream(apidFile);
    			BufferedReader br = new BufferedReader(new InputStreamReader(in));
    			while ((strLine = br.readLine()) != null) apid += strLine;
    			in.close();
    		} catch (IOException e) {
    			Log.d("PortlandBeerfest", "Error reading data from APID file.");
    		}
    		
    		if (apid.equals("")) result = null;
    		else result = apid;
    		
    	} else {
    		FileOutputStream fos = null;
			try { fos = context.openFileOutput(APID_FILENAME, Context.MODE_PRIVATE); }
			catch (FileNotFoundException e) { Log.d("PortlandBeerfest", "APID file not found."); }
            
			try {
				fos.write(apid.getBytes());
				fos.close();
			} catch (IOException e) {
				Log.d("PortlandBeerfest", "Error writing to APID file.");
			}
    	}
    	
    	return result;
    }
    
    // Returns the subdomain for a city selected in the list.
    public static String getCitySubdomain(String cityName) {
    	String citySubdomain = "portland";
    	int cityIndex = ArrayUtils.indexOf(cities, cityName);
    	if (cityIndex != -1) citySubdomain = citySubdomains[cityIndex];
    	
    	return citySubdomain;
    }

    // Returns the slug for a category selected in the list.
    public static String getCategorySlug(String categoryName) {
    	String categorySlug = "sss";
    	int slugIndex = ArrayUtils.indexOf(categories, categoryName);
    	if (slugIndex != -1) categorySlug = categorySlugs[slugIndex];
    	
    	return categorySlug;
    }
    
}
