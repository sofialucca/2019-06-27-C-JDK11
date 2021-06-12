package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private Graph<String,DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private List<Adiacenti> archiInferiori;
	private List<String> percorsoOttimo;
	private double pesoOttimo;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public String creaGrafo(String categoria, LocalDate data) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		archiInferiori = new ArrayList<>();
		
		Graphs.addAllVertices(grafo, dao.getVertici(categoria,data));
		List<Adiacenti> archi = dao.getArchi(data, categoria, new ArrayList<>(grafo.vertexSet()));
		if(!archi.isEmpty()) {
			double mediana = (archi.get(0).getPeso() + archi.get(archi.size()-1).getPeso())/2;
			for(Adiacenti a: archi) {
				double peso = a.getPeso();
				if(peso < mediana) {
					archiInferiori.add(a);
				}
				Graphs.addEdge(grafo, a.getR1(), a.getR2(), peso);
			}			
		}

		return "GRAFO CREATO:\n#VERTICI: "+ grafo.vertexSet().size() + "\n#ARCHI: " + archi.size();
	}
	
	public List<LocalDate> getDate(){
		return dao.getDate();
	}
	
	public List<String> getCategorie(){
		return dao.getCategorie();
	}
	
	public List<Adiacenti> getArchiInferiori(){
		return archiInferiori;
	}
	
	public List<String> calcolaPercorso(Adiacenti a){
		percorsoOttimo = new ArrayList<>();
		pesoOttimo = 0;
		List<String> parziale = new ArrayList<>();
		parziale.add(a.getR1());
		cerca(parziale, a.getR2(), 0);
		
		return this.percorsoOttimo;
	}

	private void cerca(List<String> parziale, String arrivo, double pesoParziale) {
		String ultimo = parziale.get(parziale.size() -1);
		if(ultimo.equals(arrivo)) {
			if(pesoParziale > pesoOttimo) {
				pesoOttimo = pesoParziale;
				percorsoOttimo = new ArrayList<>(parziale);
			}
			return;
		}else {
			for(String s : Graphs.neighborListOf(grafo, ultimo)) {
				if(!parziale.contains(s)) {
					parziale.add(s);
					DefaultWeightedEdge e = grafo.getEdge(ultimo, s);
					double costo = grafo.getEdgeWeight(e);
					cerca(parziale, arrivo, pesoParziale + costo);
					parziale.remove(s);
				}
			}
		}
	}
	
	public double getPesoOttimo() {
		return this.pesoOttimo;
	}
	
}
