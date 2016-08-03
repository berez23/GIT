package it.webred.ct.data.access.basic.tarsu;

import it.webred.ct.data.access.basic.catasto.dto.ParametriCatastaliDTO;
import it.webred.ct.data.access.basic.tarsu.dto.DichiarazioniTarsuSearchCriteria;
import it.webred.ct.data.access.basic.tarsu.dto.InformativaTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SintesiDichiarazioneTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SoggettoTarsuDTO;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitidstr;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface TarsuService {
	
	
	//Suggerimenti 
	
	//public List<Sitidstr> getListaVie(String cod_nazionale, String via);
	
	//public List<Siticivi> getListaIndirizzi(String cod_nazionale, String cod_via);
	
	public List<SitTTarSogg> getListaSoggettiF(String paramCognome);
	
	public List<SitTTarSogg> getListaSoggettiG(String paramDenom);
	
	public List<SitTTarSogg> getSoggettoByCF(String paramCF);
	
	public List<SitTTarSogg> getSoggettoByPIVA(String paramPIVA);
	
	//Query di ricerca
	public List<SintesiDichiarazioneTarsuDTO> getListaDichiarazioniTarsu(DichiarazioniTarsuSearchCriteria criteria, int startm, int numberRecord);
	
	public Long getDichTarsuRecordCount(DichiarazioniTarsuSearchCriteria criteria) ;
	
	//Estrazione dettaglio informativo TARSU
	public InformativaTarsuDTO getInformativaTarsu(String chiave, boolean loadSoggetti);
	
	public List<InformativaTarsuDTO> getListaInformativaTarsu(ParametriCatastaliDTO params,  boolean loadSoggetti);
	
	public List<SitTTarOggetto> getListaDichiarazioniTarsu(ParametriCatastaliDTO params);
	
	public List<SoggettoTarsuDTO> getListaSoggettiDichiarazioneTarsu(String idExtOgg);

}
