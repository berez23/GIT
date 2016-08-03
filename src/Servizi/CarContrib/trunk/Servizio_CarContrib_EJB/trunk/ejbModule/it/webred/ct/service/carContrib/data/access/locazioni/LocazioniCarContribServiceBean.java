package it.webred.ct.service.carContrib.data.access.locazioni;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import it.webred.ct.config.luoghi.LuoghiService;
import it.webred.ct.config.model.AmTabComuni;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.locazioni.LocazioniService;
import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.access.basic.tarsu.TarsuService;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuParCatDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.ici.SitTIciSogg;
import it.webred.ct.data.model.locazioni.LocazioneBPK;
import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.locazioni.LocazioniB;
import it.webred.ct.data.model.locazioni.LocazioniI;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;
import it.webred.ct.service.carContrib.data.access.common.CarContribServiceBaseBean;
import it.webred.ct.service.carContrib.data.access.common.GeneralService;
import it.webred.ct.service.carContrib.data.access.common.dto.LocazioniDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.utility.Constants;
import it.webred.ct.service.carContrib.data.access.common.utility.StringUtility;
@Stateless
public class LocazioniCarContribServiceBean extends CarContribServiceBaseBean
		implements LocazioniCarContribService, LocazioniCarContribLocalService {

	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/LocazioniServiceBean")
	private LocazioniService locazioniService;
	@EJB(mappedName = "java:global/CT_Service/CT_Config_Manager/LuoghiServiceBean")
	private LuoghiService luoghiService;
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CatastoServiceBean")
	private CatastoService catastoService;
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/TarsuServiceBean")
	private TarsuService tarsuService;

	@EJB
	private GeneralService genService;

	public List<LocazioniDTO> searchLocazioniBySogg(RicercaDTO dati) {
		logger.debug("---INIZIO RICERCA LOCAZIONI---");
		List<LocazioniDTO> lista = new ArrayList<LocazioniDTO>();
		Object entitySogg =dati.getObjEntity();
		Object filtroSogg =dati.getObjFiltro();
		if (entitySogg == null && (filtroSogg instanceof RicercaLocazioniDTO  )){
			logger.debug("Ricerca locazioni per accesso diretto");
			lista= getDatiLocazioni((RicercaLocazioniDTO )filtroSogg);
			return lista;
		}
		if (entitySogg==null || 
		    (!(entitySogg instanceof SitDPersona) && !(entitySogg instanceof SitTTarSogg) && !(entitySogg instanceof SitTIciSogg)))
			return null;
		logger.debug("Ricerca locazioni tramite indice correlazione");
		List<Object> listaIdSoggCorr = getSoggettiCorrelatiLocazioni(dati);
		if (listaIdSoggCorr.size() ==0)
			return null;
		for (Object ele: listaIdSoggCorr) {
			String ufficio= ((LocazioniB)ele).getUfficio();
			BigDecimal anno= ((LocazioniB)ele).getAnno();
			String serie=((LocazioniB)ele).getSerie();
			BigDecimal numero= ((LocazioniB)ele).getNumero();
			BigDecimal progSogg= ((LocazioniB)ele).getProgSoggetto();
			RicercaLocazioniDTO  rl =  new RicercaLocazioniDTO();
			rl.setEnteId(dati.getEnteId());
			rl.setUserId(dati.getUserId());
			LocazioneBPK key = new LocazioneBPK();
			key.setUfficio(ufficio);
			key.setAnno(anno);
			key.setSerie(serie);
			key.setNumero(numero);
			rl.setKey(key);
			rl.setCodFis(((RicercaLocazioniDTO)filtroSogg).getCodFis());
			List<LocazioniA> listaOgg= locazioniService.getLocazioniOggByKey(rl);
			LocazioniB locB = locazioniService.getLocazioneSoggByKeyCodFisc(rl);
			for (LocazioniA ogg: listaOgg) {
				List<LocazioniI> listaImm= locazioniService.getImmobiliByKey(rl);
				for (LocazioniI imm: listaImm) {
					LocazioniDTO locOgg= new LocazioniDTO();
					locOgg.setDatiOggLocazione(ogg);
					locOgg.setDatiOggLocImmobile(imm);
					locOgg.setIndirizzo(imm.getIndirizzo());
					AmTabComuni c = luoghiService.getComuneItaByBelfiore(imm.getCodiceCat());
					if(c != null)
						locOgg.setComune(c.getDenominazione());
					if("A".equals(locB.getTipoSoggetto()))
						locOgg.setTipoSoggetto("Inquilino");
					else locOgg.setTipoSoggetto("Proprietario");
					locOgg.setFps((imm.getFoglio()!=null ? imm.getFoglio() : "-")+" / "+
								  (imm.getParticella()!=null ? imm.getParticella(): "-")+" / "+
								  (imm.getSubalterno()!=null ? imm.getSubalterno() : "-"));
					//recupero superfici per tarsu
					RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
					ro.setEnteId(dati.getEnteId());
					ro.setUserId(dati.getUserId());
					ro.setCodEnte(dati.getEnteId());
					ro.setFoglio(imm.getFoglio());
					ro.setParticella(imm.getParticella());
					ro.setUnimm(imm.getSubalterno());
					ro.setDtVal(new Date());
					Sitiuiu datiUICat= catastoService.getDatiUiAllaData(ro);
					if(datiUICat != null && datiUICat.getSupCat() != null)
						locOgg.setSupCat(StringUtility.DF.format(datiUICat.getSupCat()));
					RicercaOggettoTarsuParCatDTO rot = new RicercaOggettoTarsuParCatDTO();
					rot.setEnteId(dati.getEnteId());
					rot.setUserId(dati.getUserId());
					rot.setSezione(imm.getSezUrbana());
					rot.setFoglio(imm.getFoglio());
					rot.setParticella(imm.getParticella());
					rot.setSubalterno(imm.getSubalterno());
					List<SitTTarOggetto> listaTarOgg = tarsuService.getListaDichiarazioniTarsu(rot);
					if(listaTarOgg != null && listaTarOgg.size() > 0)
						locOgg.setSupTarsu(StringUtility.DF.format(listaTarOgg.get(0).getSupTot()));
					lista.add(locOgg);
				}
				if(listaImm == null || listaImm.size() ==0){
					//non ha immobili, potrebbe essere il tracciato precedente al 2011
					LocazioniDTO locOgg= new LocazioniDTO();
					locOgg.setDatiOggLocazione(ogg);
					locOgg.setIndirizzo(ogg.getIndirizzo());
					AmTabComuni c = luoghiService.getComuneItaByBelfiore(ogg.getCodCatComune());
					if(c != null)
						locOgg.setComune(c.getDenominazione());
					if("A".equals(locB.getTipoSoggetto()))
						locOgg.setTipoSoggetto("Inquilino");
					else locOgg.setTipoSoggetto("Proprietario");
					lista.add(locOgg);
				}
			}
			
		}
		return lista;
	}

	private List<LocazioniDTO> getDatiLocazioni(RicercaLocazioniDTO rl) {
		List<LocazioniDTO> listaLoc=null;
		List<LocazioniA> lista= getLocazioni(rl);
		if (lista != null && lista.size() >0 ) {
			listaLoc = new ArrayList<LocazioniDTO>();
			for(LocazioniA loc: lista) {
				LocazioneBPK key = new LocazioneBPK();
				key.setUfficio(loc.getUfficio());
				key.setAnno(loc.getAnno());
				key.setSerie(loc.getSerie());
				key.setNumero(loc.getNumero());
				rl.setKey(key);
				//cerco la locazione soggetto
				LocazioniB locB = locazioniService.getLocazioneSoggByKeyCodFisc(rl);
				List<LocazioniI> listaImm= locazioniService.getImmobiliByKey(rl);
				for (LocazioniI imm: listaImm) {
					LocazioniDTO locOgg= new LocazioniDTO();
					locOgg.setDatiOggLocazione(loc);
					locOgg.setDatiOggLocImmobile(imm);
					locOgg.setIndirizzo(imm.getIndirizzo());
					AmTabComuni c = luoghiService.getComuneItaByBelfiore(imm.getCodiceCat());
					if(c != null)
						locOgg.setComune(c.getDenominazione());
					if("A".equals(locB.getTipoSoggetto()))
						locOgg.setTipoSoggetto("Inquilino");
					else locOgg.setTipoSoggetto("Proprietario");
					locOgg.setFps(imm.getFoglio()+"/"+imm.getParticella()+"/"+imm.getSubalterno());
					listaLoc.add(locOgg);
				}
				if(listaImm == null || listaImm.size() ==0){
					//non ha immobili, potrebbe essere il tracciato precedente al 2011
					LocazioniDTO locOgg= new LocazioniDTO();
					locOgg.setDatiOggLocazione(loc);
					locOgg.setIndirizzo(loc.getIndirizzo());
					AmTabComuni c = luoghiService.getComuneItaByBelfiore(loc.getCodCatComune());
					if(c != null)
						locOgg.setComune(c.getDenominazione());
					if("A".equals(locB.getTipoSoggetto()))
						locOgg.setTipoSoggetto("Inquilino");
					else locOgg.setTipoSoggetto("Proprietario");
					listaLoc.add(locOgg);
				}
			}
		}
		return listaLoc;
	}
	
	//Recupera le locazioni per il soggetto (cf-pi) valide alla data di riferimento - INPUT:
	//--> codicefiscale (valorizzare con partita iva nel caso di persona giuridica)
	//--> dtRif
	private List<LocazioniA> getLocazioni(RicercaLocazioniDTO rl) {
		return locazioniService.getLocazioniByCF(rl);
	}
	
	public List<Object> getSoggettiCorrelatiLocazioni(RicercaDTO dati) {
		String progEs = genService.getProgEs(dati); //recupera il progEs dalla tabella corrispondente all'entity
		return genService.getSoggettiCorrelati(dati,progEs, Constants.LOCAZIONI_ENTE_SORGENTE,Constants.LOCAZIONI_TIPO_INFO_SOGG );
				
	}
	
}
