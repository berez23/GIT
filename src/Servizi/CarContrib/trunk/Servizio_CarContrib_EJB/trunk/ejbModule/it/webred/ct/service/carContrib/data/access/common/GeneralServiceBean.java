package it.webred.ct.service.carContrib.data.access.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import it.webred.ct.data.access.basic.anagrafe.AnagrafeService;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.SoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.access.basic.ici.IciService;
import it.webred.ct.data.access.basic.ici.dto.RicercaSoggettoIciDTO;
import it.webred.ct.data.access.basic.indice.ricerca.IndiceCorrelazioneService;
import it.webred.ct.data.access.basic.indice.ricerca.KeyFabbricatoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.RicercaIndiceDTO;
import it.webred.ct.data.access.basic.tarsu.TarsuService;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaSoggettoTarsuDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.ici.SitTIciSogg;
import it.webred.ct.data.model.tarsu.SitTTarSogg;
import it.webred.ct.service.carContrib.data.access.anagrafe.AnagrafeCarContribService;
import it.webred.ct.service.carContrib.data.access.catasto.CatastoCarContribService;
import it.webred.ct.service.carContrib.data.access.cc.CarContribException;
import it.webred.ct.service.carContrib.data.access.common.dto.IndiciSoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.LocazioniDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.ParamAccessoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.utility.Constants;


import javax.ejb.EJB;
import javax.ejb.Stateless;


@Stateless
public class GeneralServiceBean extends CarContribServiceBaseBean implements GeneralService, GeneralLocalService{

	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/AnagrafeServiceBean")
	private AnagrafeService anagrafeService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/IciServiceBean")
	private IciService iciService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/TarsuServiceBean")
	private TarsuService tarsuService;
		
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/IndiceCorrelazioneServiceBean")
	private IndiceCorrelazioneService indiceService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CatastoServiceBean")
	private CatastoService catastoService;


/*
 * Restituisce la lista ordinata dei soggetti (da anagrafe-ici-tarsu) che corrispondono ai criteri passati
 *  Metodo da chiamare per creare la lista a partire dal filtro
 */
	public Set<SoggettoDTO> searchSoggetto(ParamAccessoDTO parms) {
		logger.debug("ricerca del soggetto dai parametri di filtro");
		Set<SoggettoDTO> listaSogg=null;
		ArrayList<SoggettoDTO> listaTemp = new ArrayList<SoggettoDTO>();
		try {
			if (parms.getTipoSogg().equals("F")) {
				RicercaSoggettoAnagrafeDTO rsa = parms.valRicercaAnag();
				//ricerca i soggetti in anagrafe
				List<SoggettoAnagrafeDTO> listaSoggAna = anagrafeService.searchSoggetto(rsa);	
				for (SoggettoAnagrafeDTO sa:listaSoggAna) {
					SoggettoDTO sogg = new SoggettoDTO();
					sogg.valDaAnagrafe(sa);
					listaTemp.add(sogg);
				}
			}
			//if (listaTemp.size() == 0) {//ricerca in Ici: si fa se PG oppure PF NON TROVATA IN ANAGRAFE
			//ricerca i soggetti in ICI
				RicercaSoggettoIciDTO rsi = parms.valRicercaIci();
				if (parms.getTipoSogg().equals("G"))
					rsi.setOperDenom("LIKE");
				List<SitTIciSogg> listaSoggIci = iciService.searchSoggetto(rsi);
				for (SitTIciSogg si:listaSoggIci) {
					if (parms.getTipoSogg().equals("G")) 
						logger.debug("trovato PG IN ICI: " + si.getCogDenom() + "-" + si.getPartIva());
					SoggettoDTO sogg = new SoggettoDTO();
					sogg.valDaIci(si);
					listaTemp.add(sogg);
				}
			//}
			//if (listaTemp.size() == 0) {//ricerca in Tarsu: si fa se non trovato né in ANAGRAFE né in TARSU
				//ricerca i soggetti in TARSU
				RicercaSoggettoTarsuDTO rst = parms.valRicercaTarsu();
				if (parms.getTipoSogg().equals("G"))
					rst.setOperDenom("LIKE");
				List<SitTTarSogg> listaSoggTarsu = tarsuService.searchSoggetto(rst);
				for (SitTTarSogg st:listaSoggTarsu) {
					if (parms.getTipoSogg().equals("G")) 
						logger.debug("trovato PG IN TARSU: " + st.getCogDenom() + "-" + st.getPartIva());
					SoggettoDTO sogg = new SoggettoDTO();
					sogg.valDaTarsu(st);
					listaTemp.add(sogg);
				}
			//}
			//CREA IL SET
			listaSogg = new TreeSet(listaTemp);
		
		}catch (Throwable t) {
			logger.error("",t);
			throw new CarContribException(t);
		} 
		return listaSogg;
	}
	
			
	//metodi per l'utilizzo dell'indice di correlazione. Sono richiamati dai vari servizi
	public Object getEntityBean(RicercaSoggettoDTO sogg, String desTblProv) {
		Object obj = null;
		List listaPers =null;
		String enteId = sogg.getEnteId();
		String userId = sogg.getUserId();
		if (desTblProv.equalsIgnoreCase(Constants.DEMOGRAFIA_SOGGETTI_TABLE)) {
			RicercaSoggettoAnagrafeDTO rsa = new RicercaSoggettoAnagrafeDTO();
			rsa.val(sogg);
			listaPers = anagrafeService.getListaPersoneByDatiAna(rsa);
		}
		else if (desTblProv.equalsIgnoreCase(Constants.TRIBUTI_ICI_SOGGETTI_TABLE)) {
			RicercaSoggettoIciDTO rsi =new RicercaSoggettoIciDTO();
			rsi.val(sogg);
			rsi.setTipoRicerca("all");
			listaPers = iciService.searchSoggetto(rsi);	
		}
		else if (desTblProv.equalsIgnoreCase(Constants.TRIBUTI_TARSU_SOGGETTI_TABLE)) {
			logger.debug("recupero entity tarsu - enteId/user: " + enteId + "/" + userId);
			RicercaSoggettoTarsuDTO rst = new RicercaSoggettoTarsuDTO();
			rst.val(sogg);
			rst.setTipoRicerca("all");
			listaPers =tarsuService.searchSoggetto((RicercaSoggettoTarsuDTO)rst);	
		}
		if (listaPers!=null && listaPers.size()>0)
			obj=listaPers.get(0);
		return obj;
	}
		
	//metodo che andrà chiamato al posto di getIndiciSoggetto(SoggettoDTO, dtRif)
	public IndiciSoggettoDTO getIndiciSoggetto(RicercaDTO dati, Date dtRif) {
		IndiciSoggettoDTO indSogg=new IndiciSoggettoDTO();
		indSogg.setListaIdSoggAnagCat(getIndiciAnagCat(dati, dtRif));
		indSogg.setListaIdSoggAnagGen(getIndiciAnagGen(dati, dtRif));
		return indSogg;
	}
	public List<Object> getSoggettiCorrelati(RicercaDTO dati, String progEs,String destFonte, String destProgEs) {
		RicercaIndiceDTO ri = new RicercaIndiceDTO();
		Object sogg=dati.getObjEntity();
		ri.setEnteId(dati.getEnteId());
		ri.setUserId(dati.getUserId());
		ri.setObj(sogg);
		ri.setProgressivoEs(progEs); 
		ri.setDestFonte(destFonte);
		ri.setDestProgressivoEs(destProgEs);
		logger.debug("Avvia la ricerca nell'indice di correlazione per ENTITY " + sogg.getClass().getName()  + " PROG_ES: " + progEs  + " DEST FONTE/PROG " + destFonte + "/" + destProgEs );
		return getSoggettiCorrelati(ri);
	}
	
	public List<Object> getSoggettiCorrelati(RicercaIndiceDTO soggetto) {
		return indiceService.getSoggettiCorrelati(soggetto) ;
	}
	
	public String getProgEs(RicercaDTO dati) {
		String progEs=null;
		Object sogg=dati.getObjEntity();
		if (sogg instanceof SitTIciSogg)
			progEs = Constants.TRIBUTI_ICI_TIPO_INFO_SOGG; 
		if (sogg instanceof SitTTarSogg)
			progEs = Constants.TRIBUTI_TARSU_TIPO_INFO_SOGG; 
		if (sogg instanceof SitDPersona)
			progEs =Constants.DEMOGRAFIA_TIPO_INFO_PERSONA; 
		return progEs;
	}
	 
	public List<String> getIndiciAnagGen(RicercaDTO dati, Date dtRif) {
		List<String> indAna=new ArrayList<String>();
		Object entitySogg =dati.getObjEntity();
		Object filtroSogg =dati.getObjFiltro();
		if (entitySogg == null && (filtroSogg instanceof SoggettoDTO )){//senza indice di correlazione
			return getIndiciAnagGen((SoggettoDTO)filtroSogg, dtRif);
		}
		List<Object> listaIdSoggCorr = getSoggettiCorrelati(dati,getProgEs(dati), Constants.DEMOGRAFIA_ENTE_SORGENTE,Constants.DEMOGRAFIA_TIPO_INFO_PERSONA) ;
		for (Object ele: listaIdSoggCorr) {
			String id = ((SitDPersona)ele).getId();
			boolean scarta=false;
			//applico le condizioni di filtro, se ci sono
			if (dtRif!=null){
				RicercaSoggettoAnagrafeDTO ra = new RicercaSoggettoAnagrafeDTO();
				ra.setEnteId(dati.getEnteId());
				ra.setUserId(dati.getUserId());
				ra.setIdVarSogg(id);
				SitDPersona pers = anagrafeService.getPersonaById(ra);
				//CONDIZ1: data inizio non successiva alla data rif e data fine non precedente alla data rif
				if(pers!=null){
					if (pers.getDtFineVal()==null  )  {
						if (pers.getDtInizioVal().compareTo(dtRif) > 0)
							scarta=true;
					}
					else {
						if (pers.getDtInizioVal().compareTo(dtRif) > 0 || pers.getDtFineVal().compareTo(dtRif) <0    )
							scarta=true;
					}
				}else{
					scarta=true;
				}
			}
			if(!scarta)
			   indAna.add(id);
		}	
		
		
		return indAna;
	}

	private List<String> getIndiciAnagGen(SoggettoDTO sogg, Date dtRif) {
		List<String> indAna=null;
		RicercaSoggettoAnagrafeDTO rsa = new RicercaSoggettoAnagrafeDTO();
		rsa.setEnteId(sogg.getEnteId());
		rsa.setUserId(sogg.getUserId());
		List<SitDPersona> listaPers=null;
		if(sogg.getCodFis()!= null && !sogg.getCodFis().equals("")) {
			rsa.setCodFis(sogg.getCodFis());
			rsa.setDtRif(dtRif);
			listaPers = anagrafeService.getListaPersoneByCF(rsa);
				
		}
		//CF non valorizzato oppure ricerca per cf non trovato
		if(listaPers == null) {
			if (rsa.getCognome() != null && !rsa.getCognome().equals("") && rsa.getNome() != null && !rsa.getNome().equals("") && rsa.getDtNas()!= null ) {
				rsa.setCognome(sogg.getCognome());;
				rsa.setNome(sogg.getNome());
				rsa.setDtNas(sogg.getDtNas());
				rsa.setCodComNas(sogg.getCodComNas());
				listaPers = anagrafeService.getListaPersoneByDatiAna(rsa);	
			}
		}
		if(listaPers != null && listaPers.size() >0) {
			String idSoggAna=null;
			for(SitDPersona pers: listaPers) {
				idSoggAna=pers.getId();
				indAna = new ArrayList<String>();
				indAna.add(idSoggAna);
			}
		}
		return indAna;
	}
	
	public List<BigDecimal> getIndiciAnagCat(RicercaDTO dati, Date dtRif) {
		List<BigDecimal> indCat=new ArrayList<BigDecimal>();
		Object entitySogg =dati.getObjEntity();
		Object filtroSogg =dati.getObjFiltro();
		if (entitySogg == null && (filtroSogg instanceof SoggettoDTO )){//senza indice di correlazione
			return getIndiciAnagCat((SoggettoDTO)filtroSogg, dtRif);
		}
		List<Object> listaIdSoggCorr = getSoggettiCorrelati(dati,getProgEs(dati), Constants.CATASTO_ENTE_SORGENTE,Constants.CATASTO_TIPO_INFO_CONDUZ);
		for (Object ele: listaIdSoggCorr) {
			BigDecimal id = ((ConsSoggTab)ele).getPkid();
			RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
			rs.setEnteId(dati.getEnteId());
			rs.setUserId(dati.getUserId());
			rs.setPkid(id);
			ConsSoggTab sogg = catastoService.getSoggettoByID(rs);
			boolean scarta=false;
			//CONDIZIONI DI FILTRO
			if (dtRif!=null){
				if(sogg!=null){
				if (sogg.getDataFine()==null  )  {
					if (sogg.getDataInizio().compareTo(dtRif) > 0)
						scarta=true;
				}
				else {
					if (sogg.getDataInizio().compareTo(dtRif) > 0 || sogg.getDataFine().compareTo(dtRif) <0    )
						scarta=true;
				}
			  }else scarta=true;
			}
			if(!scarta)
			   indCat.add(sogg.getPkCuaa());
		}	
		return indCat;
	}
	
	private List<BigDecimal> getIndiciAnagCat(SoggettoDTO sogg,	Date dtRif) {
		List<BigDecimal> listaIndSogg = new ArrayList<BigDecimal>();
		RicercaSoggettoCatDTO rsCat  =new RicercaSoggettoCatDTO(sogg.getCodEnte(),dtRif);
		rsCat.setEnteId(sogg.getEnteId());
		rsCat.setUserId(sogg.getUserId());
		if (sogg.getTipoSogg().equals("F")) {
			if(sogg.getCodFis()!= null && !sogg.getCodFis().equals("")) {
				rsCat.setCodFis(sogg.getCodFis());
				if (dtRif!=null) {
					BigDecimal id = catastoService.getIdSoggByCFAllaData(rsCat);
					if (id != null)
						listaIndSogg.add(id);
				}else {
					listaIndSogg= catastoService.getIdSoggByCF(rsCat);
				}
			}
			//CF non valorizzato oppure ricerca per cf non trovato
			if(listaIndSogg.size()==0) {
				rsCat.setDenom(sogg.getCognome() + " " + sogg.getNome());
				rsCat.setDtNas(sogg.getDtNas());
				listaIndSogg = catastoService.getIdSoggByDatiAnag(rsCat);
			}
		} else {
			if(sogg.getParIva() != null && !sogg.getParIva().equals("")) {
				rsCat.setPartIva(sogg.getParIva());
				if (dtRif!=null) {
					BigDecimal id = catastoService.getIdSoggByPIAllaData(rsCat);
					if (id != null)
						listaIndSogg.add(id);
				}else {
					listaIndSogg= catastoService.getIdSoggByPI(rsCat);
				}
			}
			//PI non valorizzato oppure ricerca per PI non trovato
			if(listaIndSogg.size()==0) {
				rsCat.setDenom(sogg.getDenom());
				listaIndSogg = catastoService.getIdSoggPGByDatiAnag(rsCat);
			}
		}
		if (listaIndSogg.size() == 0)
			listaIndSogg=null;
		return listaIndSogg;
	}

	
}
