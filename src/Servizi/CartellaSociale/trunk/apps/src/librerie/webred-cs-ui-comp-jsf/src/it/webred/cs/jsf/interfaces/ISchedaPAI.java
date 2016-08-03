package it.webred.cs.jsf.interfaces;

import it.webred.cs.data.model.CsDPai;
import it.webred.cs.data.model.CsDRelazione;

public interface ISchedaPAI {
	
	public void carica();
	public void salva();
	public CsDPai getPai();
	public void inizializzaDialog(CsDRelazione rel, CsDPai pai);
	public void elimina(CsDPai pai);
	public void reset();

}
