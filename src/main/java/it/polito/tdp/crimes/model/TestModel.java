package it.polito.tdp.crimes.model;

import java.time.LocalDate;

public class TestModel {

	public static void main(String[] args) {
		Model m = new Model();
		System.out.println(m.creaGrafo("drug-alcohol", LocalDate.of(2015, 01, 01)));
		System.out.println(m.getArchiInferiori());
	}

}
