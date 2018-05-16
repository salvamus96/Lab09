package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

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
		
	public List <Country> trovaVicini(Country start) {
	
		List <Country> parziale = new ArrayList <Country> ();
		parziale.add(start);
		
		cerca(parziale, 1);
		
		System.out.println("Numero della componente connessa riferito a " + start + ": " + this.getNumberOfConnectedComponents(start));
		System.out.println("Numero degli stati raggiunti: " + parziale.size());
		return parziale;
		
	}

	private void cerca(List<Country> parziale, int livello) {
		
		// la condizione di terminazione è superflua poichè agisce il filtro e il cilco for
		
		Country precedent = parziale.get(parziale.size() - 1);
		List <Country> adiacenti = Graphs.neighborListOf(graph, precedent);
		
		for (Country c : adiacenti) {
			if (!parziale.contains(c)) {
				parziale.add(c);
				cerca(parziale, livello + 1);
// il backtracking non serve poichè sto cercando di scendere quanto più in profondità

			}
					
		}
	}
	
	public List <Country> trovaViciniAmpiezza(Country start) {
		
		List <Country> parziale = new ArrayList <Country> ();
		BreadthFirstIterator <Country, DefaultEdge> bfi = new BreadthFirstIterator <>(this.graph, start);
		while (bfi.hasNext())
			parziale.add(bfi.next());

		System.out.println("Numero della componente connessa riferito a " + start + ": " + this.getNumberOfConnectedComponents(start));
		System.out.println("Numero degli stati raggiunti: " + parziale.size());
				
		return parziale;
	}	

	public List <Country> trovaViciniProfondita(Country start) {
		
		List <Country> parziale = new ArrayList <Country> ();
		DepthFirstIterator <Country, DefaultEdge> bfi = new DepthFirstIterator <>(this.graph, start);
		while (bfi.hasNext())
			parziale.add(bfi.next());

		System.out.println("Numero della componente connessa riferito a " + start + ": " + this.getNumberOfConnectedComponents(start));
		System.out.println("Numero degli stati raggiunti: " + parziale.size());
				
		return parziale;
	}	
	

	public List <Country> trovaViciniIteratore (Country start){
		
		List <Country> parziale = new ArrayList <Country> ();

		List <Country> visitati = new ArrayList <Country> ();
		List <Country> daVisitare = new ArrayList <Country> ();
		daVisitare.add(start);
		
		parziale = aggiungiVicini (daVisitare, visitati);
		
		System.out.println("Numero della componente connessa riferito a " + start + ": " + this.getNumberOfConnectedComponents(start));
		System.out.println("Numero degli stati raggiunti: " + parziale.size());
		
		return parziale;
	}

	
	private List<Country> aggiungiVicini(List<Country> daVisitare, List<Country> visitati) {
		
		while (!daVisitare.isEmpty()) {
			// estrarre un nodo dalla lista daVisitare
			Country temp = daVisitare.remove(0);
			
			// il nodo estratto viene inserito nella lista visitati
			visitati.add(temp);
			
			List <Country> vicini = Graphs.neighborListOf(this.graph, temp);
			// rimuovere dalla lista di vicini tutti gli stati che ho visitato...
			vicini.removeAll(visitati);
			
			// ... e tutti gli stati che devo visitare
			vicini.removeAll(daVisitare);
			
			// aggiungere i rimanenti alla coda di quelli che devi vistare
			daVisitare.addAll(vicini);
		}
		
		return visitati;
	}

}

