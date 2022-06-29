package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {

	//Coda degli eventi 
	private PriorityQueue <Event> queue; 
	
	//Parametri di simulazione 
	private int nInizialeMigranti; 
	private Country nazioneIniziale; 
	
	//Output della simulazione 
	private int nPassi; 
	private Map<Country, Integer> persone; 
	//oppure: List <CountryAndNumber> personeStanziali; 
	
	//Stato del mondo simulato 
	private Graph<Country, DefaultEdge> grafo; 
	//map persone Country -> Integer

	public Map<Country, Integer> getPersone() {
		return persone;
	}

	public Simulatore(Graph<Country, DefaultEdge> grafo) {
		super();
		this.grafo = grafo;
	}
	
	public void init(Country partenza, int migranti) {
		this.nazioneIniziale=partenza; 
		this.nInizialeMigranti=migranti; 
		
		this.persone=new HashMap<Country, Integer>(); 
		for(Country c: this.grafo.vertexSet()) {
			this.persone.put(c,  0); 
		}
		
		this.queue= new PriorityQueue<>(); 

		this.queue.add(new Event(1, this.nazioneIniziale, this.nInizialeMigranti)); 
		}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll(); 
			processEvent(e); 
		}
	}

	private void processEvent(Event e) {
		int stanziali= e.getPersone()/2; 
		int migranti= e.getPersone()-stanziali; 
		int confinanti=this.grafo.degreeOf(e.getNazione()); 
		int gruppiMigranti= migranti/confinanti; 
		stanziali= stanziali+ migranti %confinanti; 
		
		this.persone.put(e.getNazione(), this.persone.get(e.getNazione())+stanziali); 
		this.nPassi=e.getTime(); 
		
		if (gruppiMigranti!=0) {
		
		for (Country vicino: Graphs.neighborListOf(this.grafo, e.getNazione())) {
			this.queue.add(new Event(e.getTime()+1, vicino, gruppiMigranti)); 
		}
		}
		
	}

	public int getnPassi() {
		return nPassi;
	}
}
	

