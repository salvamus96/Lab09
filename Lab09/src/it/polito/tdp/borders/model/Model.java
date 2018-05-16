package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;


import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private BordersDAO bdao;
	private List <Country> countries;
	
	private CountryIdMap countryIdMap;
	private Graph <Country, DefaultEdge> graph;
	
	public Model() {
	
		this.bdao = new BordersDAO ();
		this.countryIdMap = new CountryIdMap();
		this.countries = this.bdao.loadAllCountries(this.countryIdMap);
		
		System.out.println("Numero di paesi inseriti " + countries.size());
	}

	public List<Country> getCountries(){
		if (countries == null)
			return new ArrayList <> ();
		return this.countries;
	}
	
	public void createGraph (int anno) {
		
		List <Border> confini = bdao.getCountryPairs(anno, countryIdMap);
		
		System.out.println("Numero di confini trovati per l'anno " + anno + ": " + confini.size());
		
		this.graph = new SimpleGraph <> (DefaultEdge.class);
		
		for (Border b : confini) {
			
			graph.addVertex(b.getState1());
			graph.addVertex(b.getState2());
			graph.addEdge(b.getState1(), b.getState2());
		}
		
		System.out.println("Inseriti " + graph.vertexSet().size() + " vertici");
		System.out.println("Inseriti " + graph.edgeSet().size() + " archi");
	}
	
	public Map<Country, Integer> getNumStatiConfinanti (){
		Map <Country, Integer> map = new TreeMap <> ();
		
		for (Country c : this.graph.vertexSet()) 
			map.put(c, graph.degreeOf(c));
		
		return map;
	}
	
	public int getNumberOfConnectedComponents (){
		ConnectivityInspector <Country, DefaultEdge> ci = new ConnectivityInspector <>(graph);
		return ci.connectedSets().size();
	}

	public int getNumberOfConnectedComponents (Country country){
		ConnectivityInspector <Country, DefaultEdge> ci = new ConnectivityInspector <>(graph);
		return ci.connectedSetOf(country).size();
	}

	public boolean vertex(Country start) {
		if (!this.graph.containsVertex(start))
			return false;
		return true;
	}
	
}