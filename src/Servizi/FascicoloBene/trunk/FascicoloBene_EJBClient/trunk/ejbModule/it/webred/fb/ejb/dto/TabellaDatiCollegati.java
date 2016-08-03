package it.webred.fb.ejb.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class TabellaDatiCollegati implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LinkedList<String> nomiColonne = new LinkedList<String>();
	private LinkedHashMap<String, LinkedList<DatoSpec>> righe = new  LinkedHashMap<String, LinkedList<DatoSpec>> ();
	private String nomeTabella = new String();
	
	public TabellaDatiCollegati(String nomeTabella) {
		this.setNomeTabella(nomeTabella);				
	}

	public void addNomeColonna(String nome) {
		nomiColonne.add(nome);
	}

	public String getNomeTabella() {
		return nomeTabella;
	}

	private void setNomeTabella(String nomeTabella) {
		this.nomeTabella = nomeTabella;
	}	
	
	public void addRiga(String chiave, DatoSpec... valori ) {
		LinkedList<DatoSpec>  riga =  new LinkedList<DatoSpec>();
		for (DatoSpec object : valori) {
			riga.add(object);
		}
		righe.put(chiave, riga);
	}

	public LinkedList<String> getNomiColonne() {
		return nomiColonne;
	}

	public LinkedHashMap<String, LinkedList<DatoSpec>> getRighe() {
		return righe;
	}
}
