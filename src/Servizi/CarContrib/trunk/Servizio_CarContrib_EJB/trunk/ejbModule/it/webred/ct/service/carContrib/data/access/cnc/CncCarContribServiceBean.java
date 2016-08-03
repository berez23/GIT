package it.webred.ct.service.carContrib.data.access.cnc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import it.webred.ct.data.access.basic.cnc.CNCCommonService;
import it.webred.ct.data.access.basic.cnc.CNCDTO;
import it.webred.ct.data.access.basic.cnc.flusso750.Flusso750Service;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.AnnoTributoDTO;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.Flusso750SearchCriteria;
import it.webred.ct.data.access.basic.cnc.statoriscossione.StatoRiscossioneService;
import it.webred.ct.data.access.basic.cnc.statoriscossione.dto.StatoRiscossioneSearchCriteria;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.model.cnc.flusso750.VAnagrafica;
import it.webred.ct.data.model.cnc.flusso750.VArticolo;
import it.webred.ct.data.model.cnc.statoriscossione.SRiscossioni;
import it.webred.ct.service.carContrib.data.access.cnc.dto.DatiImportiCncDTO;
import it.webred.ct.service.carContrib.data.access.cnc.dto.RicercaCncDTO;
import it.webred.ct.service.carContrib.data.access.common.CarContribServiceBaseBean;
import it.webred.ct.service.carContrib.data.access.common.dto.VersamentoDTO;

import it.webred.ct.support.datarouter.CeTBaseObject;
/**
 * Session Bean implementation class CncCarContribBean
 */
@Stateless
public class CncCarContribServiceBean extends CarContribServiceBaseBean implements	CncCarContribService, CncCarContribLocalService {
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CommonServiceBean")
	private CommonService  commonService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CNCCommonServiceBean")
	private CNCCommonService cncCommonService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/Flusso750ServiceBean")
	private Flusso750Service  cnc750Service;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/StatoRiscossioneServiceBean")
	private StatoRiscossioneService cncRiscService;
/*
 * INPUT 
 * --> codFis
 */
 public List<VAnagrafica> getAnagraficaDebitore(RicercaCncDTO rc) {
	    logger.debug("CNC- Ricerca record anagrafico debitore. CF: " +rc.getCodFis());
		if (rc.getCodFis() == null ||  rc.getCodFis().equals("") ) 
			return null;
		List<VAnagrafica> listaAnaDeb=null;
		String codEnteCreditore="";
		//RECUPERA L'ENTE CREDITORE
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rc.getEnteId());
		cet.setUserId(rc.getUserId());
		String codBelfioreEnte = rc.getCodEnteCreditore();
		if (codBelfioreEnte==null || codBelfioreEnte.equals(""))
			codBelfioreEnte= commonService.getEnte(cet).getCodent();
		logger.debug("sta per chiamare il servizio per recuperare l'ente creditore. Chiave lettura cnc: "+ codBelfioreEnte);
		CNCDTO cncDTO = new CNCDTO();
		cncDTO.setEnteId(rc.getEnteId());
		cncDTO.setUserId(rc.getUserId());
		cncDTO.setCodiceEnte(codBelfioreEnte);
		codEnteCreditore= cncCommonService.getCodiceEnte(cncDTO) ; 
		Flusso750SearchCriteria criteria750 = new Flusso750SearchCriteria();
		criteria750.setEnteId(rc.getEnteId());
		criteria750.setUserId(rc.getUserId());
		criteria750.getChiaveRuolo().setCodEnteCreditore(codEnteCreditore);
		criteria750.setCodiceFiscale(rc.getCodFis());
		listaAnaDeb= cnc750Service.getAnagraficaDebitore(criteria750);
		return listaAnaDeb;
	}

	public List<DatiImportiCncDTO> getDatiCNC(RicercaCncDTO rc,	int annoRif) {
		String[] anni = {annoRif +"" , annoRif-1+ "", annoRif-2+"", annoRif-3 +"", annoRif-4 +""};
		List<DatiImportiCncDTO> listaDatiRet=null;
		List<DatiImportiCncDTO> listaDati=null;
		DatiImportiCncDTO dati= null;	
		String codEnteCreditore="";
		//RECUPERA L'ENTE CREDITORE
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rc.getEnteId());
		cet.setUserId(rc.getUserId());
		String codBelfioreEnte = rc.getCodEnteCreditore();
		if (codBelfioreEnte==null || codBelfioreEnte.equals(""))
			codBelfioreEnte= commonService.getEnte(cet).getCodent();
		CNCDTO cncDTO = new CNCDTO();
		cncDTO.setEnteId(rc.getEnteId());
		cncDTO.setUserId(rc.getUserId());
		cncDTO.setCodiceEnte(codBelfioreEnte);
		codEnteCreditore= cncCommonService.getCodiceEnte(cncDTO);
		Flusso750SearchCriteria criteria750 = new Flusso750SearchCriteria();
		criteria750.setEnteId(rc.getEnteId());
		criteria750.setUserId(rc.getUserId());
		criteria750.getChiaveRuolo().setCodEnteCreditore(codEnteCreditore);
		criteria750.setCodiceFiscale(rc.getCodFis());
		List<AnnoTributoDTO> listaAnniTributi = cnc750Service.getAnnoETributo(criteria750);
		if (listaAnniTributi== null || listaAnniTributi.size()==0) 
			return null; 
		listaDati= new ArrayList<DatiImportiCncDTO>();
		//preparo la lista con gli anni-tributi di cui poi calcolerà i relativi importi 
		for (int i=0; i < anni.length; i++) {
			for (int j=0; j<listaAnniTributi.size(); j++) {
				String anno= listaAnniTributi.get(j).getAnnoRif();
				String codTipoTrib= listaAnniTributi.get(j).getCodTipoTributo();
				if(anno.equals(anni[i])) {
					dati=new DatiImportiCncDTO();
					dati.setAnnoRif(anno);
					dati.setCodTipoTributo(codTipoTrib);
					//DECODIFICA DEL TIPO TRIBUTO
					cncDTO = new CNCDTO();
					cncDTO.setEnteId(rc.getEnteId());
					cncDTO.setUserId(rc.getUserId());
					cncDTO.setCodTipoEntrata(codTipoTrib);
					String des  = cncCommonService.getCodiceTipoEntrataDescr(cncDTO); 
					dati.setDesTipoTributo(des);
					dati.setImpTotRuolo(new BigDecimal(0));
					dati.setImpTotRiscosso(new BigDecimal(0));
					listaDati.add(dati);
				}
			}
		}
		logger.debug("LISTA ANNI-TRIBUTI N.ELE. (listaDati.SIZE()): " + listaDati.size());
		listaDatiRet = getImportiCnc(rc, listaDati);
	
		return listaDatiRet;
	}
	
	/*
	 * Aggiunge alla lista versamenti per anno passata a parametro, gli importi a ruolo e quelli riscossi per il cf e il codice tipo tributo
	 * INPUT:  
	 * --> RicercaCncDTO: codiceTipoTributo (se null considera tutti i tributi), codFis  
	 * 
	 * N.B. questo metodo era all'inizio utilizzat da ici e tarsu per acquisire gli importi dei versamenti da cnc. E' stato sostituito da getImporti().
	 * ma al momento non lo elimino, perché  fino  ad ora non si è potuto fare un test su dati veri
	 */
	public List<VersamentoDTO> getVersamenti(RicercaCncDTO rc, List<VersamentoDTO> listaVersamentiDaAggiornare) {
		List<VersamentoDTO> listaVersamenti=new ArrayList<VersamentoDTO>();
		if (listaVersamentiDaAggiornare ==null || listaVersamentiDaAggiornare.size()==0)
			return null;
		for (VersamentoDTO ver: listaVersamentiDaAggiornare) {
			VersamentoDTO ver1 = new VersamentoDTO(ver.getAnnoRif(), ver.getImpTotVer(), ver.getImpTotRuolo(), ver.getImpTotRiscosso());
			listaVersamenti.add(ver1);
		}
		for (int i = 0 ; i < listaVersamenti.size(); i++){
			VersamentoDTO ele =  listaVersamenti.get(i);
			//logger.debug(" -- ANNO -->" + ele.getAnnoRif());
			//logger.debug(" -- importo -->" + ele.getImpTotVer());
		}
		//ISCRIZIONI A RUOLO CNC
		if (rc.getCodFis() != null && ! rc.getCodFis().equals("") ) {
			String codEnteCreditore="";
			//RECUPERA L'ENTE CREDITORE
			CeTBaseObject cet = new CeTBaseObject();
			cet.setEnteId(rc.getEnteId());
			cet.setUserId(rc.getUserId());
			String codBelfioreEnte = rc.getCodEnteCreditore();
			if (codBelfioreEnte==null || codBelfioreEnte.equals(""))
				codBelfioreEnte= commonService.getEnte(cet).getCodent();
			CNCDTO cncDTO = new CNCDTO();
			cncDTO.setEnteId(rc.getEnteId());
			cncDTO.setUserId(rc.getUserId());
			cncDTO.setCodiceEnte(codBelfioreEnte);
			codEnteCreditore= cncCommonService.getCodiceEnte(cncDTO);
			Flusso750SearchCriteria criteria750 = new Flusso750SearchCriteria();
			criteria750.setEnteId(rc.getEnteId());
			criteria750.setUserId(rc.getUserId());
			criteria750.getChiaveRuolo().setCodEnteCreditore(codEnteCreditore);
			criteria750.setCodiceFiscale(rc.getCodFis());
			criteria750.setCodiceTipoTributo(rc.getCodiceTipoTributo());
			List<VArticolo> listaArticoli= cnc750Service.getArticoliByComuneECF(criteria750);
			if (listaArticoli != null ) {
				for (int i = 0 ; i < listaVersamentiDaAggiornare.size(); i++){
					String annoRif = listaVersamentiDaAggiornare.get(i).getAnnoRif();
					for (int j = 0; j < listaArticoli.size(); j++ ) {
						VArticolo articolo= listaArticoli.get(j);
						if (articolo.getChiavePartita().getAnnoRiferimento().equals(annoRif)) {
							BigDecimal imp= null;
							BigDecimal impRuolo= listaVersamenti.get(i).getImpTotRuolo();
							try  {
								imp= new BigDecimal(articolo.getImportoCarico());
								impRuolo=impRuolo.add(imp);
								listaVersamenti.get(i).setImpTotRuolo(impRuolo);
							}catch(NumberFormatException nfe) {	}
						}
					}
				}
			}
			if (listaArticoli == null || listaArticoli.size()==0){
				logger.debug("NESSUNA ISCRIZIONE A RUOLO CNC TROVATA- COD_ENTE_CREDITOTE/CF: " +codEnteCreditore + "/" + rc.getCodFis()) ;
			}
			
			//STATO DELLA RISCOSSIONE CNC: bisogna scorrere la lista per poter acquisire lo stato della riscossione
			//che va fatto, oltre che per la codice ente creditore e codice fiscale, per anno-ruolo e codice entrata delle partite acquisite nel 750
			if (listaArticoli != null ) {
				String annoRuolo = "";
				String codEntrata = "";
				for (int k = 0; k < listaArticoli.size(); k++ ) {
					VArticolo articolo= listaArticoli.get(k);
					String annoRuoloArt = articolo.getChiavePartita().getAnnoRuolo();
					String codEntrataArt = articolo.getCodEntrata();
					if (!annoRuolo.equals(annoRuoloArt) || !codEntrata.equals(codEntrataArt))  {
						StatoRiscossioneSearchCriteria criteriaRisc = new StatoRiscossioneSearchCriteria();
						criteriaRisc.setEnteId(rc.getEnteId());
						criteriaRisc.setUserId(rc.getUserId());
						criteriaRisc.getChiaveRiscossione().setCodEnteCreditore(codEnteCreditore);
						criteriaRisc.setCodiceFiscale(rc.getCodFis());
						criteriaRisc.setCodiceTributo(codEntrataArt);
						criteriaRisc.getChiaveRiscossione().setAnnoRuolo(annoRuoloArt);
						List<SRiscossioni> listaRiscossioni = cncRiscService.getRiscossioniByAnnoComuneCF(criteriaRisc);
						//Ora verranno toalizzate tutte le righe degli importi riscossi relative all'anno del ruolo: 
						//il totale non può essere fatto per anno di rif. ma per anno del ruolo perché non è presente l'anno di riferimento in stato della riscossione
						if (listaRiscossioni != null ) {
							int indAnno=-1;
							//cerco l'indice relativo all'anno del ruolo
							for (int i = 0 ; i < listaVersamentiDaAggiornare.size(); i++){
								String annoRif = listaVersamentiDaAggiornare.get(i).getAnnoRif();
								if (articolo.getChiavePartita().getAnnoRuolo().equals(annoRif)) {
									indAnno=i;
								}
							}
							for (int j = 0; j < listaRiscossioni.size(); j++ ) {
								SRiscossioni risc= listaRiscossioni.get(j);
								BigDecimal impTotRisc =listaVersamenti.get(indAnno).getImpTotRiscosso();
								BigDecimal impRisc= risc.getImportoCaricoRisc();
								impTotRisc = impTotRisc.add(impRisc);
								listaVersamenti.get(indAnno).setImpTotRiscosso(impTotRisc);
							}
						}
						annoRuolo=annoRuoloArt;
						codEntrata=codEntrataArt;
					}
				}
			}
		}
		return listaVersamenti;
	}

	/*
	 * Aggiunge alla lista anno/tributo per anno passata a parametro, gli importi a ruolo e quelli riscossi per il cf e il codice tipo tributo
	 * INPUT:  
	 * --> RicercaCncDTO: codEnteCreditore, codiceTipoTributo (se null considera tutti i tributi), codFis  
	 */
	public List<DatiImportiCncDTO> getImportiCnc(RicercaCncDTO rc, List<DatiImportiCncDTO> listaDaAggiornare) {
		logger.debug("Inizio getImportiCnc()");
		List<DatiImportiCncDTO> listaImp=new ArrayList<DatiImportiCncDTO>();
		if (listaDaAggiornare !=null && listaDaAggiornare.size()>0) {
			logger.debug("listaDaAggiornare PRESENTE");
			for (DatiImportiCncDTO impCnc: listaDaAggiornare) {
				DatiImportiCncDTO impCnc1 = new DatiImportiCncDTO();
				impCnc1.setEnteId(impCnc.getEnteId());
				impCnc1.setUserId(impCnc.getUserId());
				impCnc1.setAnnoRif(impCnc.getAnnoRif());
				logger.debug("anno: " + impCnc.getAnnoRif());
				impCnc1.setCodTipoTributo(impCnc.getCodTipoTributo());
				impCnc1.setDesTipoTributo(impCnc.getDesTipoTributo());
				logger.debug("tribuo: " + impCnc.getCodTipoTributo() + "-" + impCnc.getDesTipoTributo());
				impCnc1.setImpTotRuolo(impCnc.getImpTotRuolo());
				impCnc1.setImpTotRiscosso(impCnc.getImpTotRuolo());
				impCnc1.setImpTotVer(impCnc.getImpTotVer());
				listaImp.add(impCnc1);
			}
		}else {
			return null; 
		}
		//DATI IMPORTO CARICO ISCRIZIONI A RUOLO CNC
		String codEnteCreditore="";
		//RECUPERA L'ENTE CREDITORE
		//RECUPERA L'ENTE CREDITORE
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rc.getEnteId());
		cet.setUserId(rc.getUserId());
		String codBelfioreEnte = rc.getCodEnteCreditore();
		if (codBelfioreEnte==null || codBelfioreEnte.equals(""))
			codBelfioreEnte= commonService.getEnte(cet).getCodent();
		CNCDTO cncDTO = new CNCDTO();
		cncDTO.setEnteId(rc.getEnteId());
		cncDTO.setUserId(rc.getUserId());
		cncDTO.setCodiceEnte(codBelfioreEnte);
		codEnteCreditore= cncCommonService.getCodiceEnte(cncDTO);
		Flusso750SearchCriteria criteria750 = new Flusso750SearchCriteria();
		criteria750.setEnteId(rc.getEnteId());
		criteria750.setUserId(rc.getUserId());
		criteria750.getChiaveRuolo().setCodEnteCreditore(codEnteCreditore);
		criteria750.setCodiceFiscale(rc.getCodFis());
		//
		if (rc.getCodiceTipoTributo()!= null && !rc.getCodiceTipoTributo().equals(""))
			criteria750.setCodiceTipoTributo(rc.getCodiceTipoTributo());
		List<VArticolo> listaArticoli= cnc750Service.getArticoliByComuneECF(criteria750);
		if (listaArticoli == null ) 
			logger.debug("lista articoli 750. NULL");
		if (listaArticoli != null ) {
			logger.debug("lista articoli 750. N.ele: "+ listaArticoli.size());
			for (int i = 0 ; i < listaDaAggiornare.size(); i++){
				String annoRif = listaDaAggiornare.get(i).getAnnoRif();
				String codTributo = listaDaAggiornare.get(i).getCodTipoTributo();
				for (int j = 0; j < listaArticoli.size(); j++ ) {
					VArticolo articolo= listaArticoli.get(j);
					String annoEle = articolo.getChiavePartita().getAnnoRiferimento();
					String codTributoEle = articolo.getTipoEntrata();
					if (annoEle != null && annoEle.equals(annoRif) && 
						codTributoEle!=null && codTributoEle.equals(codTributo)	) {
						BigDecimal imp= null;
						BigDecimal impRuolo= listaImp.get(i).getImpTotRuolo();
						try  {
							imp= new BigDecimal(articolo.getImportoCarico());
							impRuolo=impRuolo.add(imp);
							listaImp.get(i).setImpTotRuolo(impRuolo);
						}catch(NumberFormatException nfe) {	}
					}
				}
			}
		}
		
		//STATO DELLA RISCOSSIONE CNC: bisogna scorrere la lista per poter acquisire lo stato della riscossione
		//che va fatto, oltre che per la codice ente creditore e codice fiscale, per anno-ruolo e codice entrata delle partite acquisite nel 750
		if (listaArticoli != null ) {
			String annoRuolo = "";
			String codEntrata = "";
			for (int k = 0; k < listaArticoli.size(); k++ ) {
				VArticolo articolo= listaArticoli.get(k);
				String annoRuoloArt = articolo.getChiavePartita().getAnnoRuolo();
				String codEntrataArt = articolo.getCodEntrata();
				logger.debug("Cerca le riscossioni aventi annoRuolo: " + annoRuoloArt + " e codEntrataArt: "+codEntrataArt);
				if (!annoRuolo.equals(annoRuoloArt) || !codEntrata.equals(codEntrataArt))  {
					StatoRiscossioneSearchCriteria criteriaRisc = new StatoRiscossioneSearchCriteria();
					criteriaRisc.setEnteId(rc.getEnteId());
					criteriaRisc.setUserId(rc.getUserId());
					criteriaRisc.getChiaveRiscossione().setCodEnteCreditore(codEnteCreditore);
					criteriaRisc.setCodiceFiscale(rc.getCodFis());
					criteriaRisc.setCodiceTributo(codEntrataArt);
					criteriaRisc.getChiaveRiscossione().setAnnoRuolo(annoRuoloArt);
					List<SRiscossioni> listaRiscossioni = cncRiscService.getRiscossioniByAnnoComuneCF(criteriaRisc);
					//Ora verranno toalizzate tutte le righe degli importi riscossi relative all'anno del ruolo: 
					//il totale non può essere fatto per anno di rif. ma per anno del ruolo perché non è presente l'anno di riferimento in stato della riscossione
					if (listaRiscossioni != null ) {
						int indEleAgg=-1;
						String codTipoEntrataArt= articolo.getTipoEntrata();
						//cerco l'indice relativo all'anno del ruolo
						for (int i = 0 ; i < listaDaAggiornare.size(); i++){
							String annoRif = listaDaAggiornare.get(i).getAnnoRif();
							String codTipoTrib= listaDaAggiornare.get(i).getCodTipoTributo();//
							if (annoRuoloArt.equals(annoRif) &&
								codTipoEntrataArt.equals(codTipoTrib)	) {
								indEleAgg=i;
							}
						}
						if (indEleAgg==-1)//non dovrebbe accadere, ma ....
							continue;
						for (int j = 0; j < listaRiscossioni.size(); j++ ) {
							SRiscossioni risc= listaRiscossioni.get(j);
							BigDecimal impTotRisc =listaImp.get(indEleAgg).getImpTotRiscosso();
							BigDecimal impRisc= risc.getImportoCaricoRisc();
							impTotRisc = impTotRisc.add(impRisc);
							listaImp.get(indEleAgg).setImpTotRiscosso(impTotRisc);
						}
					}
					annoRuolo=annoRuoloArt;
					codEntrata=codEntrataArt;
				}
			}
		}
		
		return listaImp;
	}

}
