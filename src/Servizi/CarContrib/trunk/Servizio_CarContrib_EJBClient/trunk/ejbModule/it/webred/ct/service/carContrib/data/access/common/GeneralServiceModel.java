package it.webred.ct.service.carContrib.data.access.common;

import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.KeyFabbricatoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.RicercaIndiceDTO;
import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.service.carContrib.data.access.common.dto.IndiciSoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.LocazioniDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.ParamAccessoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SitPatrimImmobileDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SitPatrimTerrenoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface GeneralServiceModel {
	
	public Set<SoggettoDTO> searchSoggetto(ParamAccessoDTO parms);
	
	//per indice correlazione in cartella contribuente
	public Object getEntityBean(RicercaSoggettoDTO sogg, String desTblProv);
	public String getProgEs(RicercaDTO dati);
	public List<Object> getSoggettiCorrelati(RicercaDTO dati, String progEs, String destFonte, String destProgEs ); 
	public List<Object> getSoggettiCorrelati(RicercaIndiceDTO soggetto); 
	//
	public IndiciSoggettoDTO getIndiciSoggetto(RicercaDTO dati ,Date dtRif);
	public List<BigDecimal> getIndiciAnagCat(RicercaDTO dati, Date dtRif) ;
	
 	
}
