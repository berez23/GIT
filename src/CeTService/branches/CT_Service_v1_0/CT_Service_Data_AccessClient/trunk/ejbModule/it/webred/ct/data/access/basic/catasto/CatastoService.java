package it.webred.ct.data.access.basic.catasto;

import it.webred.ct.data.access.basic.catasto.dto.CatastoSearchCriteria;
import it.webred.ct.data.access.basic.catasto.dto.DettaglioCatastaleImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.access.basic.catasto.dto.SintesiImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.catasto.Sitidstr;
import it.webred.ct.data.model.catasto.SitiediVani;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.persistence.Query;

@Remote
public interface CatastoService {
	
	//Suggerimenti 
	public List<Sitideco> getListaCategorieImmobile();
	
	public List<Sitideco> getListaCategorieImmobile(String codCategoria);
	
	public List<Sitidstr> getListaVie(String codNazionale, String via);
	
	public List<Siticivi> getListaIndirizzi(String codNazionale, BigDecimal pkIdStra);
	
	public List<Siticivi> getListaIndirizzi(String codNazionale, BigDecimal pkIdStra, String civico);
	
	public List<ConsSoggTab> getListaSoggettiF(String paramCognome);
	
	public List<ConsSoggTab> getListaSoggettiG(String paramDenom);
	
	public List<ConsSoggTab> getSoggettoByCF(String paramCF);
	
	public ConsSoggTab getSoggettoByPIVA(String paramPIVA);
	
	//Query di ricerca sul catasto
	public List<SintesiImmobileDTO> getListaImmobili(CatastoSearchCriteria criteria, int startm, int numberRecord);
	
	public Long getCatastoRecordCount(CatastoSearchCriteria criteria) ;
	
	public List<IndirizzoDTO> getListaIndirizziImmobile(String codNazionale, String idUiu);

	public List<SoggettoDTO> getListaSoggettiImmobile(String codNazionale, String idUiu);
	
	public DettaglioCatastaleImmobileDTO getDettaglioImmobile(String codNazionale, String idUiu);
	
	public List<PlanimetriaComma340DTO> getPlanimetrieComma340(String codNazionale, String foglio, String particella, String subalterno);

	public ArrayList<IndirizzoDTO> getLocalizzazioneCatastale(String codNazionale, String foglio, String particella, String subalterno);
	
	public List<SitiediVani> getDettaglioVaniC340(String codNazionale, String foglio, String particella, String unimm);

}
