package it.webred.ct.data.access.basic.indice.ricerca.dao;

import it.webred.ct.data.access.basic.indice.ricerca.KeyFabbricatoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.KeyUIDTO;
import it.webred.ct.data.model.indice.IndicePK;

import java.util.List;

public interface IndiceCorrelazioneDAO {
	//acquisizione dati indice con input entity 
	public List<IndicePK> getViaTotalePK(String key, String enteSorgente, String progES, String destFonte, String destProgr);
	
	public List<IndicePK> getSoggettoTotalePK(String key, String enteSorgente, String progES, String destFonte, String destProgr);
	
	public List<IndicePK> getCivicoTotalePK(String key, String enteSorgente, String progES, String destFonte, String destProgr);
	
	public List<IndicePK> getFabbricatoTotalePK(String key, String enteSorgente, String progES, String destFonte, String destProgr);
	
	//acquisizione dati indice con input chiave entità 
	public List<IndicePK> getOggettoTotalePKFromUI (KeyUIDTO keyUi, String destFonte, String destProgr);
	
	public List<IndicePK> getFabbricatoTotalePKFromUI (KeyFabbricatoDTO keyFabbr, String destFonte, String destProgr);
	

}
