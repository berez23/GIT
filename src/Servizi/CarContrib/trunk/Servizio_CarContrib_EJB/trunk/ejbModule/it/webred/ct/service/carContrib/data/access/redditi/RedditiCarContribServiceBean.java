package it.webred.ct.service.carContrib.data.access.redditi;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.access.basic.redditi.RedditiService;
import it.webred.ct.data.access.basic.redditi.dto.KeySoggDTO;
import it.webred.ct.data.access.basic.redditi.dto.RedditiDicDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.catasto.Siticomu;
import it.webred.ct.data.model.ici.SitTIciSogg;
import it.webred.ct.data.model.redditi.RedDatiAnagrafici;
import it.webred.ct.data.model.redditi.RedDomicilioFiscale;
import it.webred.ct.data.model.tarsu.SitTTarSogg;
import it.webred.ct.service.carContrib.data.access.common.CarContribServiceBaseBean;
import it.webred.ct.service.carContrib.data.access.common.GeneralService;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.utility.Constants;
import it.webred.ct.service.carContrib.data.access.common.utility.StringUtility;
import it.webred.ct.service.carContrib.data.access.ici.dto.DatiIciDTO;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
@Stateless
public class RedditiCarContribServiceBean extends CarContribServiceBaseBean implements RedditiCarContribService{
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/RedditiServiceBean")
	private RedditiService redditiService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CatastoServiceBean")
	private CatastoService catastoService;
	
	@EJB
	private GeneralService genService;
	
	public List<RedDatiAnagrafici> searchSoggettiCorrelatiRedditi(RicercaDTO dati) {
		logger.debug("searchSoggettiCorrelatiRedditi: inizio......");
		List<RedDatiAnagrafici> lista=new ArrayList<RedDatiAnagrafici>();
		Object entitySogg =dati.getObjEntity();
		Object filtroSogg =dati.getObjFiltro();
		logger.debug("entitySogg: " + entitySogg);
		logger.debug("filtroSogg: " + filtroSogg.getClass().getName());
		if (entitySogg == null && (filtroSogg instanceof RicercaSoggettoDTO  )){
			logger.debug("searchSoggettiCorrelatiRedditi: RICERCA SENZA INDICE CORRELAZIONE");
			lista= redditiService.getListaSoggettiUltDic((RicercaSoggettoDTO )filtroSogg);
			if (lista!=null) {
				for (RedDatiAnagrafici ele:lista) {
					String desComune ="";
					RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
					ro.setEnteId(dati.getEnteId());
					ro.setUserId(dati.getUserId());
					ro.setCodEnte(ele.getComuneNascita());
					desComune = decodificaComune(ro);
					ele.setDesComuneNascita(desComune );
				}
			}
			return lista;
		}
		if (entitySogg==null || 
		    (!(entitySogg instanceof SitDPersona) && !(entitySogg instanceof SitTTarSogg) && !(entitySogg instanceof SitTIciSogg)))
			return null;
		List<Object> listaIdSoggCorr = getSoggettiCorrelatiRedditi(dati);
		if (listaIdSoggCorr.size() ==0)
			return null;
	
		List<RedDatiAnagrafici> listaTemp=new ArrayList<RedDatiAnagrafici>();
		int ultimoAnnoImposta =0;
		for (Object ele: listaIdSoggCorr) {
			logger.debug("searchSoggettiCorrelatiRedditi: RICERZA CON INDICE CORRELAZIONE");
			String ideTel= ((RedDatiAnagrafici)ele).getIdeTelematico();
			String codFis= ((RedDatiAnagrafici)ele).getCodiceFiscaleDic();
			KeySoggDTO  key =  new KeySoggDTO();
			key.setEnteId(dati.getEnteId());
			key.setUserId(dati.getUserId());
			key.setIdeTelematico(ideTel);
			key.setCodFis(codFis);
			RedDatiAnagrafici soggRed= redditiService.getSoggettoByKey(key);
			if (soggRed!=null)
				logger.debug("searchSoggettiCorrelatiRedditi - sogg: " + soggRed.getIdeTelematico() + "|" + soggRed.getCodiceFiscaleDic());
			else
				logger.debug("searchSoggettiCorrelatiRedditi - NESSUN SOGGETTO: " + codFis);
				
	
			if (soggRed !=null) {
				listaTemp.add(soggRed);
				int annoImpostaCorr =0;
				try {
					annoImpostaCorr=(new Integer(soggRed.getAnnoImposta())).intValue();
					if (annoImpostaCorr> ultimoAnnoImposta )
						ultimoAnnoImposta=	annoImpostaCorr;
				}catch (NumberFormatException nfe) {	}
			}
				
			
		}
		logger.debug("searchSoggettiCorrelatiRedditi - ultimoAnnoImposta: " + ultimoAnnoImposta);
		if (listaTemp==null)
			return lista;
			
		for (RedDatiAnagrafici ele:listaTemp)  {
			if (ele.getAnnoImposta() != null) {
				int annoImpostaCorr =0;
				try {
					annoImpostaCorr=new Integer(ele.getAnnoImposta()).intValue();
					if (annoImpostaCorr== ultimoAnnoImposta )
						lista.add(ele);
				}catch (NumberFormatException nfe) {	}
			}
		}
		logger.debug("searchSoggettiCorrelatiRedditi - neleme" + lista.size());
		return lista;
	}
	
	private List<Object> getSoggettiCorrelatiRedditi(RicercaDTO dati) {
		String progEs = genService.getProgEs(dati); //recupera il progEs dalla tabella corrispondente all'entity
		return genService.getSoggettiCorrelati(dati,progEs, Constants.REDDITI_ENTE_SORGENTE,Constants.REDDITI_TIPO_INFO_SOGG );
	}

	public RedDomicilioFiscale getDomicilioByKey(KeySoggDTO key) {
		RedDomicilioFiscale domFis= redditiService.getDomicilioByKey(key);
		if (domFis!=null) {
			String desComune ="";
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			ro.setEnteId(key.getEnteId());
			ro.setUserId(key.getUserId());
			ro.setCodEnte(domFis.getCodiceCatDomFiscaleDic() );
			desComune = decodificaComune(ro);
			domFis.setDesComuneDomFiscaleDic(desComune );
			ro.setCodEnte(domFis.getCodiceCatDomFiscaleAttuale() );
			desComune = decodificaComune(ro);
			domFis.setDesComuneDomFiscaleAttuale(desComune);
		}
		return domFis;
	}

	public List<RedditiDicDTO> getRedditiByKey(KeySoggDTO key) {
		List<RedditiDicDTO> lista = redditiService.getRedditiByKey(key);
		if (lista !=null)  {
			for (RedditiDicDTO riga:lista ) {
				if (riga.getValoreContabile()!=null){
					//controllo ora probabilmente inutile, ved. commento seguente
					String valore = riga.getValoreContabile();
					if (valore.length()>0 &&  valore.charAt(0)== '+')
						valore =valore.substring(1);
					riga.setValoreContabile(StringUtility.removeLeadingZero(valore));
					if (!riga.getValoreContabile().equals("")) {
						//la stringa arriva già formattata
						try {
							//presumo che il formato sia quello impostato in RedditiServiceBean.elaboraListaRedditiDichiarati()
							long lngVal = new DecimalFormat("#,##0.00").parse(riga.getValoreContabile()).longValue();
							riga.setValoreContabileF(StringUtility.DF.format(new BigDecimal(lngVal)));							
						} catch (Exception e) {
							//provo direttamente con la stringa
							riga.setValoreContabileF(StringUtility.DF.format(new BigDecimal(riga.getValoreContabile())));
						}
					}						
				}
			}
		}
		return lista;
	}

	public RedDatiAnagrafici getSoggettoByKey(KeySoggDTO key) {
		RedDatiAnagrafici sogg = redditiService.getSoggettoByKey(key);
		if (sogg!=null) {
			String desComune ="";
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			ro.setEnteId(key.getEnteId());
			ro.setUserId(key.getUserId());
			ro.setCodEnte(sogg.getComuneNascita());
			desComune = decodificaComune(ro);
			sogg.setDesComuneNascita(desComune );
		}
		return sogg;
	}
	
	private String decodificaComune( RicercaOggettoCatDTO ro)  {
		String desComune ="";
		Siticomu comune = catastoService.getSitiComu(ro);
		if (comune!=null) {
			if (comune.getNome() !=null)
				desComune = comune.getNome() ;
			if (comune.getSiglaProv() !=null)
				desComune +=  " (" + comune.getSiglaProv() + ")";
		}
		return desComune;			
	}
	
	
}
