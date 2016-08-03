package it.webred.cs.jsf.interfaces;

import it.webred.cs.jsf.manbean.ComuneNazioneNascitaMan;

import java.util.ArrayList;

import javax.faces.model.SelectItem;

public interface IDatiAna {
	
	public ArrayList<SelectItem> getLstSessi();
	public ArrayList<SelectItem> getLstCittadinanze();
	public ComuneNazioneNascitaMan getComuneNazioneNascitaMan();
	public Long getSoggettoId();
	
	/*public IDatiValiditaGestione getMediciGestBean();
	public IDatiValiditaGestione getStatusGestBean();
	public IDatiValiditaGestione getStatoCivileGestBean();*/

}
