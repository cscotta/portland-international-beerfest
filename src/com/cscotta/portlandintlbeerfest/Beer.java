package com.cscotta.portlandintlbeerfest;

public class Beer {
	
	Integer id;
	String name;
	String type;
	String style;
	String abv;
	Integer ibu;
	String country;
	String serving;
	Boolean favorite;
	String description;
	String link;

	// Simple POJO for wrapping up all info about a beer.
	public Beer(Integer _id, String _name, String _type, String _style, String _abv, Integer _ibu, String _country, 
				String _serving, Integer _favorite, String _description, String _link) {
		id = _id;
		name = _name;
		style = _type;
		type = _style;
		abv = _abv;
		ibu = _ibu;
		country = _country;
		serving = _serving;
		favorite = false;
		if (_favorite != null && _favorite == 1) favorite = true;
		description = _description;
		link = _link;
	}
	
	public Beer(Integer _id, String _name, String _type, String _style, Integer _favorite) {
		id = _id;
		name = _name;
		style = _type;
		type = _style;
		favorite = false;
		if (_favorite != null && _favorite == 1) favorite = true;
	}
	
	// Getters
	public Integer getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.style;
	}
	
	public String getStyle() {
		return this.type;
	}
	
	public String getAbv() {
		return this.abv;
	}
	
	public Integer getIbu() {
		return this.ibu;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public String getServing() {
		return this.serving;
	}
	
	public Boolean getFavorite() {
		return this.favorite;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getLink() {
		return this.link;
	}
	
	
	// Setters
	public void setFavorite(Boolean fave) {
		this.favorite = fave;
	}
	
}
