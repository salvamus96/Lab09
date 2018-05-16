package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.Map;

public class CountryIdMap {

	private Map <Integer, Country> map;
	
	
	public CountryIdMap () {
		
		this.map = new HashMap <> ();
	}
	
	public Country get (int cCod) {
		return map.get(cCod);
	}
	
	public Country get (Country country) {
		Country old = map.get(country.getcCod());
		if (old == null) {
			map.put(country.getcCod(), country);
			return country;
		}
		return old;
	}
	
	public void put (Country country, int cCod) {
		map.put(cCod, country);
	}
	
}
