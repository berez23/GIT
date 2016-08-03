package it.webred.ct.data.access.basic.tarsu;

import it.webred.ct.data.access.basic.catasto.dto.ParametriCatastaliDTO;
import it.webred.ct.data.access.basic.docfa.DocfaServiceException;
import it.webred.ct.data.access.basic.tarsu.dto.DichiarazioniTarsuSearchCriteria;
import it.webred.ct.data.access.basic.tarsu.dto.InformativaTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SintesiDichiarazioneTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SoggettoTarsuDTO;
import it.webred.ct.data.model.catasto.SitRepTarsu;
import it.webred.ct.data.model.common.SitEnte;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ejb.Stateless;

@Stateless
public class TarsuServiceBean extends TarsuBaseService implements TarsuService {

	
	public List<SitTTarSogg> getListaSoggettiF(String paramCognome){
		
		List<SitTTarSogg> result = new ArrayList<SitTTarSogg>();
		paramCognome = paramCognome.trim().toUpperCase();
		
		logger.debug("LISTA SOGGETTI F TARSU ["+paramCognome+"]");
			
		try{
			
			Query q = manager_diogene.createNamedQuery("SitTTarSogg.getListaSoggettiByCognome");
			q.setParameter("cognome", paramCognome);
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");
			
		}catch(Throwable t) {
			
			throw new TarsuServiceException(t);
		}
		return result;
		
	}
	
	public List<SitTTarSogg> getListaSoggettiG(String paramDenom){
		
		List<SitTTarSogg> result = new ArrayList<SitTTarSogg>();
		paramDenom = paramDenom.trim().toUpperCase();
		
		logger.debug("LISTA SOGGETTI G TARSU [Denominazione: "+paramDenom+"]");
			
		try{
			Query q = manager_diogene.createNamedQuery("SitTTarSogg.getListaSoggettiByDenominazione");
			q.setParameter("denominazione", paramDenom);
			result = q.getResultList();
			logger.debug("Result size ["+result.size()+"]");
					
		}catch(Throwable t) {
			throw new TarsuServiceException(t);
		}
		return result;
		
	}
	
	public List<SitTTarSogg> getSoggettoByCF(String paramCF){
		
		List<SitTTarSogg> result = new ArrayList<SitTTarSogg>();
		paramCF = paramCF.trim().toUpperCase();
		
		logger.debug("LISTA SOGGETTI F TARSU [CF: "+paramCF+"]");
			
		try{
			Query q = manager_diogene.createNamedQuery("SitTTarSogg.getSoggettoByCF");
			q.setParameter("codfisc", paramCF);
			result = q.getResultList();
			logger.debug("Result size ["+result.size()+"]");
					
		}catch(Throwable t) {
			throw new TarsuServiceException(t);
		}
		return result;
		
	}
	
	public List<SitTTarSogg> getSoggettoByPIVA(String paramPIVA){
		
		List<SitTTarSogg> result = new ArrayList<SitTTarSogg>();
		paramPIVA = paramPIVA.trim().toUpperCase();
		
		logger.debug("LISTA SOGGETTI G TARSU [PIVA: "+paramPIVA+"]");
			
		try{
			Query q = manager_diogene.createNamedQuery("SitTTarSogg.getSoggettoByPIVA");
	
			q.setParameter("piva", paramPIVA);
			result = q.getResultList();
			logger.debug("Result size ["+result.size()+"]");
		
		}catch(Throwable t) {
			throw new TarsuServiceException(t);
		}
		return result;
		
	}
	
	@Override
	public Long getDichTarsuRecordCount(DichiarazioniTarsuSearchCriteria criteria) {
		try {
			
			String sql = (new TarsuQueryBuilder(criteria)).createQuery(true);
			Query q = manager_diogene.createNativeQuery(sql);
			Object o = q.getSingleResult();
			
			return (Long) o;
			
		}catch(Throwable t) {
			throw new TarsuServiceException(t);
		}
	}
	
	@Override
	public List<SintesiDichiarazioneTarsuDTO> getListaDichiarazioniTarsu(DichiarazioniTarsuSearchCriteria criteria,int startm, int numberRecord) {
		
		ArrayList<SintesiDichiarazioneTarsuDTO> result = new ArrayList<SintesiDichiarazioneTarsuDTO>();
		
		try {
			
			String sql = (new TarsuQueryBuilder(criteria)).createQuery(false);
			
			Query q = manager_diogene.createNativeQuery(sql);
			q.setFirstResult(startm);
			q.setMaxResults(numberRecord);
			
			List<Object[]> list = q.getResultList();
			
			for(Object[] rs:list){
				
				SintesiDichiarazioneTarsuDTO dich = new SintesiDichiarazioneTarsuDTO();
				
				//TODO: Scansione lista e assegnazione a DTO
				
				result.add(dich);
			}
		}catch(Throwable t) {
			throw new TarsuServiceException(t);
		}
															
		return result;
	}

	public List<SoggettoTarsuDTO> getListaSoggettiDichiarazioneTarsu(String idExtOgg){
		
		List<SoggettoTarsuDTO> soggetti = new ArrayList<SoggettoTarsuDTO>();
		
		try{
			
			soggetti.addAll(this.getListaDichiarantiTarsu(idExtOgg));
			soggetti.addAll(this.getListaContribuentiTarsu(idExtOgg));
			soggetti.addAll(this.getListaAltriSoggettiTarsu(idExtOgg));
			
			
		}catch(Throwable t) {
			throw new TarsuServiceException(t);
		}
		
		return soggetti;
		
	}
	
	protected List<SoggettoTarsuDTO> getListaAltriSoggettiTarsu(String idExtOgg){
			
			List<SoggettoTarsuDTO> soggetti = new ArrayList<SoggettoTarsuDTO>();
			SoggettoTarsuDTO soggetto;
			
			try{
				//Ricerca i soggetti ulteriori
				logger.info("RICERCA ALTRI SOGGETTI TARSU [idExtOgg"+idExtOgg+"]");
				Query qult = manager_diogene.createNamedQuery("SitTTarSogg.getUlterioriSoggettiBy_IdExtOggRsu");
				qult.setParameter("idExtOggRsu", idExtOgg);
				List<Object[]> ultSogg = qult.getResultList();
				logger.debug("Result size ["+ultSogg.size()+"]");
				
				for(Object[] ult: ultSogg){
					
					soggetto = new SoggettoTarsuDTO();
					soggetto.setTitolo(ult[0].toString());
					soggetto.setSoggetto((SitTTarSogg)ult[1]);
					soggetti.add(soggetto);
				}
				
			}catch(Throwable t) {
				throw new TarsuServiceException(t);
			}
			
			return soggetti;
			
		}
		
	protected List<SoggettoTarsuDTO> getListaDichiarantiTarsu(String idExtOgg){
			
			List<SoggettoTarsuDTO> soggetti = new ArrayList<SoggettoTarsuDTO>();
			SoggettoTarsuDTO soggetto;
			
			try{
				
				//Ricerca i dichiaranti
				logger.info("RICERCA SOGGETTI DICHIARANTI TARSU [idExtOgg"+idExtOgg+"]");
				Query qdic = manager_diogene.createNamedQuery("SitTTarSogg.getListaDichiarantiBy_IdExtOggRsu");
				qdic.setParameter("idExtOggRsu", idExtOgg);
				List<SitTTarSogg> dichiaranti = qdic.getResultList();
				logger.debug("Result size ["+dichiaranti.size()+"]");
				
				for(SitTTarSogg dic: dichiaranti){
					
					soggetto = new SoggettoTarsuDTO();
					soggetto.setSoggetto(dic);
					soggetto.setTitolo("Dichiarante");
					soggetti.add(soggetto);
				}
				
			}catch(Throwable t) {
				throw new TarsuServiceException(t);
			}
			
			return soggetti;
			
		}
	
	protected List<SoggettoTarsuDTO> getListaContribuentiTarsu(String idExtOgg){
	
		List<SoggettoTarsuDTO> soggetti = new ArrayList<SoggettoTarsuDTO>();
		SoggettoTarsuDTO soggetto;
		
		try{
			
			//Ricerca i contribuenti
			logger.info("RICERCA SOGGETTI CONTRIBUENTI TARSU [idExtOgg"+idExtOgg+"]");
			Query qcnt = manager_diogene.createNamedQuery("SitTTarSogg.getListaContribuentiBy_IdExtOggRsu");
			qcnt.setParameter("idExtOggRsu", idExtOgg);
			List<SitTTarSogg> contrib = qcnt.getResultList();
			logger.debug("Result size ["+contrib.size()+"]");
			
			for(SitTTarSogg cnt: contrib){
				
				soggetto = new SoggettoTarsuDTO();
				soggetto.setSoggetto(cnt);
				soggetto.setTitolo("Contribuente");
				soggetti.add(soggetto);
			}
			
		}catch(Throwable t) {
			throw new TarsuServiceException(t);
		}
		
		return soggetti;
	
}
	
	public List<SitTTarOggetto> getListaDichiarazioniTarsu(ParametriCatastaliDTO params){
		
		List<SitTTarOggetto> listaOggetti = new ArrayList<SitTTarOggetto>();
		String foglio = params.getFoglio();
		String particella = params.getParticella();
		String unimm = params.getSubalterno();
		
		if
			(
					(foglio != null && !foglio.equals("")) &&
					(particella != null && !particella.equals("")) &&
					(unimm != null && !unimm.equals(""))
			)
			
			{
			try{
			
			logger.debug("RICERCA DICHIARAZIONI TARSU [" +
					"Foglio: "+foglio+", " +
					"Particella: "+particella+", " +
					"Subalterno: "+unimm+"]");
			
			
					Query q0 = manager_diogene.createNamedQuery("SitTTarOggetto.getListaOggettiTARSUBYFPS");
					q0.setParameter("foglio", foglio);
					q0.setParameter("particella", particella);
					q0.setParameter("unimm", unimm);
					listaOggetti = q0.getResultList();
					logger.debug("Result size ["+listaOggetti.size()+"]");
			
			}catch(Throwable t) {
				logger.error("", t);
				throw new TarsuServiceException(t);
			}
			
		}else{
			String message = "Parametri non validi per la ricerca TARSU [" +
			"Foglio: "+foglio+", " +
			"Particella: "+particella+", " +
			"Subalterno: "+unimm+"]";
			throw new TarsuServiceException(message);	
		}
		
		return listaOggetti;
	}
	
	public List<InformativaTarsuDTO> getListaInformativaTarsu(ParametriCatastaliDTO params, boolean loadSoggetti){
		
		List<InformativaTarsuDTO> infolista = new ArrayList<InformativaTarsuDTO>();	
		List<SitTTarOggetto> lista = this.getListaDichiarazioniTarsu(params);
		for(SitTTarOggetto o: lista){
			InformativaTarsuDTO info = new InformativaTarsuDTO();
			info.setOggettoTarsu(o);
			if(loadSoggetti)
				this.setSoggettiDichiarazioneTARSU(info);
			
			infolista.add(info);
		}
		
		return infolista;
	}
	
	public InformativaTarsuDTO setSoggettiDichiarazioneTARSU(InformativaTarsuDTO info){
		SitTTarOggetto o = info.getOggettoTarsu();
		
		if(o!=null){
			String idExtOgg = o.getIdExt();
			info.setDichiaranti(this.getListaDichiarantiTarsu(idExtOgg));
			info.setContribuenti(this.getListaContribuentiTarsu(idExtOgg));
			info.setUlterioriSoggetti(this.getListaAltriSoggettiTarsu(idExtOgg));
		}
		return info;
	}
	
	
	public InformativaTarsuDTO getInformativaTarsu(String chiave, boolean loadSoggetti){
			
		InformativaTarsuDTO info = new InformativaTarsuDTO();
		try {
		
			Query q0 = manager_diogene.createNamedQuery("SitTTarOggetto.getDettaglioOggettoTARSU");
    		q0.setParameter(1, chiave);
    		SitTTarOggetto oggettoTarsu = (SitTTarOggetto)q0.getSingleResult();
    		
			info.setOggettoTarsu(oggettoTarsu);
			if(loadSoggetti)
				this.setSoggettiDichiarazioneTARSU(info);
			
		}catch(NoResultException nre){
			logger.warn("Result size [0] " + nre.getMessage());
		}catch(Throwable t) {
			logger.error("", t);
			throw new TarsuServiceException(t);
		}
		return info;
		
	}
	
}
