package com.cscotta.portlandintlbeerfest;

public class Beer {    
	String name;
	String type;
	String style;

	// Simple POJO for wrapping up all info about a beer.
	public Beer(String _name, String _type, String _style) {
		name = _name;
		style = _type;
		type = _style; 
	}
	
	// Getters
	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.style;
	}
	
	public String getStyle() {
		return this.type;
	}
	
	// Setters
	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setStyle(String style) {
		this.style = style;
	}
	
}
