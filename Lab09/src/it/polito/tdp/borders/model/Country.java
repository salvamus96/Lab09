package it.polito.tdp.borders.model;

public class Country implements Comparable <Country>{

	private int cCod;
	private String stateAbb;
	private String stateNme;
	
	public Country(int cCod, String stateAbb, String stateNme) {
		super();
		this.cCod = cCod;
		this.stateAbb = stateAbb;
		this.stateNme = stateNme;
	}

	public int getcCod() {
		return cCod;
	}

	public void setcCod(int cCod) {
		this.cCod = cCod;
	}

	public String getStateAbb() {
		return stateAbb;
	}

	public void setStateAbb(String stateAbb) {
		this.stateAbb = stateAbb;
	}

	public String getStateNme() {
		return stateNme;
	}

	public void setStateNme(String stateNme) {
		this.stateNme = stateNme;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cCod;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (cCod != other.cCod)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return stateNme;
	}

	@Override
	public int compareTo(Country c) {
		return this.getStateAbb().compareTo(c.getStateAbb());
	}
	
	



}
