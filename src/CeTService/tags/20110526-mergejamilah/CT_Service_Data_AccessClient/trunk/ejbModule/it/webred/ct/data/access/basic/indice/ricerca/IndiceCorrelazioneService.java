package it.webred.ct.data.access.basic.indice.ricerca;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface IndiceCorrelazioneService {
	
	public List<Object>  getSoggettiCorrelati(RicercaIndiceDTO soggetto);
	
	public List<Object>  getVieCorrelate(RicercaIndiceDTO via);

	public List<Object>  getCiviciCorrelati(RicercaIndiceDTO via);
	
	public List<Object>  getFabbricatiCorrelati(RicercaIndiceDTO via);
	
	//METODI DI RICERCA CORRELAZIONI INDIRETTE
	public List<Object>  getCiviciCorrelatiFromFabbricato(RicercaIndiceDTO fabbricato);

	public List<Object> getCiviciCorrelatiFromUI(RicercaIndiceDTO rf) ;
	
	//METODI DI RICERCA DI TUTTE LE CORRELAZIONI
	public List<Object> getCorrelazioniOggettiFromUI(RicercaIndiceDTO rf);
	public Hashtable<String,  List<Object>> getCorrelazioniOggettiFromUI(RicercaIndiceDTO rf, boolean dueListe);
	public Hashtable<String,  List<Object>> getCorrelazioniFabbricatoFromFabbricato(RicercaIndiceDTO rf, boolean dueListe); 
}
