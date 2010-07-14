package com.cscotta.portlandintlbeerfest;

public class Beer {    
	String query;
	String category;
	String city;

	// Simple POJO for wrapping up all info about a saved search.
	public Beer(String _query, String _city, String _category) {
		query = _query;
		city = _city;
		category = _category; 
	}
	
	// Getters
	public String getQuery() {
		return this.query;
	}

	public String getCity() {
		return this.city;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	// Setters
	public void setQuery(String query) {
		this.query = query;
	}

	public void setCategory(String category) {
		this.category= category;
	}
	
	public void setCity(String city) {
		this.city= city;
	}
	
}
