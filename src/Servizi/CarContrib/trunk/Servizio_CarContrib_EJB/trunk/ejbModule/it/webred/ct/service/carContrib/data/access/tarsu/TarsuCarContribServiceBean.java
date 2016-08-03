package it.webred.ct.service.carContrib.data.access.tarsu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import it.webred.ct.data.access.basic.anagrafe.AnagrafeService;
import it.webred.ct.data.access.basic.anagrafe.dto.IndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaSoggettoIciDTO;
import it.webred.ct.data.access.basic.indice.IndiceService;
import it.webred.ct.data.access.basic.indice.ricerca.KeyFabbricatoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.RicercaIndiceDTO;
import it.webred.ct.data.access.basic.tarsu.TarsuService;
import it.webred.ct.data.access.basic.tarsu.dto.OggettoTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuParCatDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaSoggettoTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaViaTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SoggettoTarsuDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.catasto.SiticonduzImmAll;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.ici.SitTIciSogg;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;
import it.webred.ct.data.model.tarsu.SitTTarVia;
import it.webred.ct.data.model.tarsu.VTTarCiviciAll;
import it.webred.ct.service.carContrib.data.access.cc.CarContribException;
import it.webred.ct.service.carContrib.data.access.cc.IndiceNonAllineatoException;
import it.webred.ct.service.carContrib.data.access.cnc.CncCarContribLocalService;
import it.webred.ct.service.carContrib.data.access.cnc.dto.DatiImportiCncDTO;
import it.webred.ct.service.carContrib.data.access.cnc.dto.RicercaCncDTO;
import it.webred.ct.service.carContrib.data.access.common.CarContribServiceBaseBean;
import it.webred.ct.service.carContrib.data.access.common.GeneralService;
import it.webred.ct.service.carContrib.data.access.common.dto.IndiciSoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.VersamentoDTO;
import it.webred.ct.service.carContrib.data.access.common.utility.Constants;
import it.webred.ct.service.carContrib.data.access.common.utility.DateUtility;
import it.webred.ct.service.carContrib.data.access.common.utility.StringUtility;
import it.webred.ct.service.carContrib.data.access.common.dto.IndirizzoIciTarsuDTO;
import it.webred.ct.service.carContrib.data.access.tarsu.dto.DatiTarsuDTO;
import it.webred.ct.support.datarouter.CeTBaseObject;

/**
 * Session Bean implementation class TarsuCarContribServiceBean
 */
@Stateless
public class TarsuCarContribServiceBean extends CarContribServiceBaseBean		implements TarsuCarContribService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CatastoServiceBean")
	private CatastoService catastoService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CommonServiceBean")
	private CommonService  commonService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/AnagrafeServiceBean")
	private AnagrafeService anagrafeService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/TarsuServiceBean")
	private TarsuService tarsuService;
	
	@EJB
	private CncCarContribLocalService  cncCCService;

	@EJB
	private GeneralService genService;

	
	//Recupera i dati anagrafici dichiarati in TARSU del soggetto - INPUT:
	//-->idSoggTarsu
	public SitTTarSogg getDatiTarsuSoggetto(RicercaSoggettoTarsuDTO rs) {
		SitTTarSogg datiSogg =tarsuService.getSoggettoById(rs); 
		if (datiSogg.getDesIndRes()==null || datiSogg.getDesIndRes().equals("")) {
			if (datiSogg.getIdExtViaRes() != null && !datiSogg.getIdExtViaRes().equals("")) {
				RicercaViaTarsuDTO rv = new RicercaViaTarsuDTO();
				rv.setEnteId(rs.getEnteId());
				rv.setUserId(rs.getUserId());
				SitTTarVia datiVia = new SitTTarVia();
				datiVia.setIdExt(datiSogg.getIdExtViaRes());
				rv.setDatiVia(datiVia);
				SitTTarVia via = tarsuService.getViaByIdExt(rv);
				String civico=(datiSogg.getNumCivExt() != null)?datiSogg.getNumCivExt(): "";
				if (via!=null) {
					datiSogg.setDesIndRes(via.getDescrizione()+", " + civico);
				}
			}
			
		}
		return datiSogg;

	}
	
	
	
	/*
     * Recupera i dati dichiarati per la sezione Tarsu della cartella.
     * INPUT - RicercaSoggettoTarsuDTO:
     * -->idSoggTarsu
     * -----per PF------
     * -->codFis
     * -->cognome
     * -->nome
     * -->dtNas
     * -->codComNas
     * -->desComNas
     * -----per PG--------
     * parIva
     * denom
     * INPUT -  IndiciSoggettoDTO :
     * -->listaIdSoggAnagGen
     * -->listaIdSoggAnagCat
    */
	public List<DatiTarsuDTO> getDatiTarsu(RicercaSoggettoTarsuDTO rs,IndiciSoggettoDTO indSogg) {
		List<DatiTarsuDTO> listaDatiTarsu=null;
		SitTTarSogg soggTarsu=null;
		try {
			CeTBaseObject cet = new CeTBaseObject();
			cet.setEnteId(rs.getEnteId());
			cet.setUserId(rs.getUserId());
			String codEnte = commonService.getEnte(cet).getCodent();
			//ricerca gli oggetti Tarsu
			List<OggettoTarsuDTO> listaOgg = tarsuService.getListaOggettiByIdSogg(rs);
			listaDatiTarsu = new ArrayList<DatiTarsuDTO> ();
			String key ="";String currKey="";
			DatiTarsuDTO datiTarsu=null;
			IndirizzoIciTarsuDTO indirizzo=null;
			RicercaOggettoTarsuDTO ro =null;
			List<SoggettoTarsuDTO> listaSoggetti = null;
			RicercaOggettoCatDTO roCat=null;
			String sezione="";String foglio=""; String numero=""; String sub ="";
			for (OggettoTarsuDTO ogg : listaOgg) {
				//SCARTO LE RIGHE CON DATA FINE POSSESSO ANTECEDENTE AL QUINQUENNIO CONSIDERATO
				Date dataSys = new Date();
				int anno=DateUtility.annoData(dataSys);
				int annoIniQuinquennio= anno - 4;
				int annoFinePosOgg = -10;
				if (ogg.getOggettoTarsu().getDatFin() != null )
					annoFinePosOgg = DateUtility.annoData(ogg.getOggettoTarsu().getDatFin());
				if (annoFinePosOgg > 0 && annoFinePosOgg < annoIniQuinquennio)	
					continue;
				//una sola riga nella lista per ciascuna coordinata
				sezione=ogg.getOggettoTarsu().getSez() !=null ? ogg.getOggettoTarsu().getSez().trim() : "" ;
				foglio=ogg.getOggettoTarsu().getFoglio() !=null ? ogg.getOggettoTarsu().getFoglio().trim() : "" ;
				numero=ogg.getOggettoTarsu().getNumero()!=null ? ogg.getOggettoTarsu().getNumero().trim() : "" ;
				sub=ogg.getOggettoTarsu().getSub()!=null ? ogg.getOggettoTarsu().getSub().trim() : "" ;
				currKey=sezione + "|"+ foglio + "|" + numero + "|" + sub;
				if (!key.equals(currKey)){
					//acquisizione lista soggetti coinvolti nella dichiarazione
					ro= new RicercaOggettoTarsuDTO();
					ro.setEnteId(rs.getEnteId());
					ro.setUserId(rs.getUserId());
					ro.setIdExtOgg(ogg.getOggettoTarsu().getIdExt());
					listaSoggetti= tarsuService.getListaSoggettiDichiarazioneTarsu(ro);
					datiTarsu =new DatiTarsuDTO();
					
					if (foglio.equals("") && numero.equals("") && sub.equals(""))
						// Se tutti e tre non ci sono si visualizza la string vuota 
						datiTarsu.setDescFPS("");
					else
						//datiTarsu.setDescFPS(StringUtility.removeLeadingZero(foglio) + "/" + StringUtility.removeLeadingZero(numero) + "/" + StringUtility.removeLeadingZero(sub));
						datiTarsu.setDescFPS(foglio + " / " + numero + " / " + sub);

					datiTarsu.valorizzaDatiDichiarazione(ogg.getOggettoTarsu(), listaSoggetti);
					//dati da reperire/calcolare al di fuori della banca dati ici
					//per ici tutte le considerazioni seguenti si fanno all'anno di riferimento della dichiarazione, che per Tarsu non c'è. Quindi le faccio alla data corrente
					if (true)  {
						Date dtRif = dataSys;
						if (anno!= -1 && !foglio.equals("") && !numero.equals("") && !sub.equals(""))  {
							//dai dati catastali immobile
							roCat = new RicercaOggettoCatDTO(codEnte,ogg.getOggettoTarsu().getFoglio(), 
									ogg.getOggettoTarsu().getNumero(), ogg.getOggettoTarsu().getSub(),dtRif);
							if (!sezione.equals(""))
								roCat.setSezione(sezione);
							roCat.setEnteId(rs.getEnteId());
							roCat.setUserId(rs.getUserId());
							Sitiuiu ui = catastoService.getDatiUiAllaData(roCat);
							//recupera i dati relativa alla titolarità catastale dell'immobile 
							BigDecimal idSoggCat=null;
							RicercaSoggettoCatDTO rsCat = new RicercaSoggettoCatDTO(rs.getEnteId(),dtRif);
							rsCat.setEnteId(rs.getEnteId());
							rsCat.setUserId(rs.getUserId());
							if (indSogg !=null && indSogg.getListaIdSoggAnagCat()!= null && indSogg.getListaIdSoggAnagCat().size() >0 ) {
								idSoggCat = indSogg.getListaIdSoggAnagCat().get(0); 
							}
							SiticonduzImmAll datiTitImm=null;
							if (idSoggCat != null) {
								rsCat.setIdSogg(idSoggCat);
								datiTitImm= catastoService.getDatiBySoggUiAllaData(roCat, rsCat);
							}
							//indirizzi catasto dell'immobile 
							List<IndirizzoIciTarsuDTO> listaIndirizziDaCatasto=null;
							List<IndirizzoDTO> listaInd = catastoService.getLocalizzazioneCatastale(roCat);//TODO: VEDI QUALE INDIRIZZO (DATA DI VALIDità) RECUPERA: QUELLO ATTUALE?
							//indirizzo SIT
							List<IndirizzoDTO> listaIndSIT=null;
							if (ui!=null){
								RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
								roc.setEnteId(rs.getEnteId());
								roc.setIdUiu(ui.getPkidUiu().toString());
								roc.setCodEnte(codEnte);
								listaIndSIT = catastoService.getListaIndirizziImmobile(roc);
							}
							//ora valorizza l'oggetto aggiungendo le info provenienti da catasto
							datiTarsu.valorizzaDatiCatasto(ui, datiTitImm, listaInd, listaIndSIT);
							//decodifica categoria
							if (ui != null && ui.getCategoria()!= null) {
								RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
								roc.setEnteId(rs.getEnteId());
								roc.setCodCategoria(ui.getCategoria());
								
								Sitideco sitideco = catastoService.getSitideco(roc);
								if (sitideco!=null)
									datiTarsu.setDesTipOggDaCatasto(sitideco.getDescription());
								else
									datiTarsu.setDesTipOggDaCatasto("");
							}
							//indirizzo da anagrafe
							String idSoggAna =null;
							if (indSogg !=null && indSogg.getListaIdSoggAnagGen()!= null && indSogg.getListaIdSoggAnagGen().size() >0 ) {
								idSoggAna = indSogg.getListaIdSoggAnagGen().get(0);
							}			
							if (idSoggAna != null ) {
								RicercaSoggettoAnagrafeDTO rsa = new RicercaSoggettoAnagrafeDTO();
								rsa.setIdVarSogg(idSoggAna);
								rsa.setEnteId(rs.getEnteId());
								rsa.setUserId(rs.getUserId());
								SitDPersona persona = anagrafeService.getPersonaById(rsa);
								if (persona != null) {
									logger.debug("IdExtSoggAna: " +persona.getIdExt());
									rsa.setIdSogg(persona.getIdExt());
									rsa.setIdVarSogg(null);
									rsa.setDtRif(dtRif);
									IndirizzoAnagrafeDTO indAna =anagrafeService.getIndirizzoPersona(rsa);
									if (indAna != null)
										logger.debug("indirizzo anagrafe: " + indAna.getSedimeVia() + " " + indAna.getDesVia()); 
									datiTarsu.valorizzaIndirizzoAnagrafe(indAna); 		
									logger.debug("indirizzo anagrafe in tarsu: " + datiTarsu.getIndirizzoDaAnagrafe().getDesIndirizzo());  
								}
							}
								
						}
					}
					listaDatiTarsu.add(datiTarsu );
					key=currKey;
				} 
			} 
		}catch (Throwable t) {
			logger.error("",t);
			throw new CarContribException(t);
		}
		return listaDatiTarsu;
	
	}
	//Recupera i  versamenti per la scheda TARSU (relativamente agli ultimi 5 anni a partire dalla data di riferimento)
	//INPUT 
	//-->codEnte 
	//-->codFis (valorizzare con la pi per le persone giuridiche)  
	//-->codTipiTributo (elenco dei codici COD_ENTRATA DA FLUSSO 750 da considerare)
	public List<VersamentoDTO> getVersamentiOLD(RicercaSoggettoTarsuDTO rs, int annoRif, String[] codTipiTributo) {
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rs.getEnteId());
		cet.setUserId(rs.getUserId());
		String codEnte = commonService.getEnte(cet).getCodent();
		List<VersamentoDTO> listaVersamenti=new ArrayList<VersamentoDTO>();
		VersamentoDTO ver= null;
		String[] anniVer = {annoRif +"" , annoRif-1+ "", annoRif-2+"", annoRif-3 +"", annoRif-4 +""};
		for (int i=0; i < anniVer.length; i++) {
			ver = new VersamentoDTO(anniVer[i]);
			listaVersamenti.add(ver);
		}
		RicercaCncDTO rc = new RicercaCncDTO();
		rc.setEnteId(rs.getEnteId());
		rc.setUserId(rs.getUserId());
		rc.setCodEnteCreditore(codEnte);
		rc.setCodFis(rs.getCodFis());
		for (int i=0; i < codTipiTributo.length; i++) {
			rc.setCodiceTipoTributo(codTipiTributo[i]);
			listaVersamenti=cncCCService.getVersamenti(rc, listaVersamenti);
		}
		
		return listaVersamenti;
	}

	public List<DatiImportiCncDTO> getVersamenti(RicercaSoggettoTarsuDTO rs,	int annoRif, String[] codTipiTributo) {
		List<DatiImportiCncDTO> listaVersamentiTot=null;
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rs.getEnteId());
		cet.setUserId(rs.getUserId());
		String codEnte = commonService.getEnte(cet).getCodent();
		
		List<DatiImportiCncDTO> listaVersamenti=new ArrayList<DatiImportiCncDTO>();
		DatiImportiCncDTO ver= null;
		String[] anniVer = {annoRif +"" , annoRif-1+ "", annoRif-2+"", annoRif-3 +"", annoRif-4 +""};
		
		for (int i=0; i < anniVer.length; i++) {
			for (int j=0; j < codTipiTributo.length; j++) {
				ver = new DatiImportiCncDTO();
				ver.setEnteId(rs.getEnteId());
				ver.setUserId(rs.getUserId());
				ver.setAnnoRif(anniVer[i]);
				ver.setCodTipoTributo(codTipiTributo[j]); 
				ver.setDesTipoTributo("TARSU");
				ver.setImpTotVer(new BigDecimal(0));
				ver.setImpTotRuolo(new BigDecimal(0));
				ver.setImpTotRiscosso(new BigDecimal(0));
				listaVersamenti.add(ver);
			}
		}
		
		RicercaCncDTO rc = new RicercaCncDTO();
		rc.setEnteId(rs.getEnteId());
		rc.setUserId(rs.getUserId());
		rc.setCodEnteCreditore(codEnte);
		rc.setCodFis(rs.getCodFis());
		for (int i=0; i < codTipiTributo.length; i++) {
			rc.setCodiceTipoTributo(codTipiTributo[i]);
			listaVersamenti=cncCCService.getImportiCnc(rc, listaVersamenti);
		}
		/* Nel 750 del CNC sono diversi i tipi tributi che corrispondeo a Tarsu. Nella pagina dei versamenti TARSU non mi interessa vedere i tipi distinti ma un unica somma di importi
		 * Per questo la listaVersamenti che viene qui creata non contiene gli importi per anno-entrata ma il totale	per anno	 
		 */
		String anno="";
		BigDecimal totImpVerAnno = new BigDecimal(0);
		BigDecimal totImpRuoloAnno = new BigDecimal(0);
		BigDecimal totImpRiscAnno = new BigDecimal(0);
		DatiImportiCncDTO eleVerTot;
		if (listaVersamenti != null && listaVersamenti.size() >0) {
			listaVersamentiTot = new ArrayList<DatiImportiCncDTO>();
			anno= listaVersamenti.get(0).getAnnoRif();
			for (int i=0; i < listaVersamenti.size(); i++) {
				DatiImportiCncDTO ele = listaVersamenti.get(i);
				if (!anno.equals(ele.getAnnoRif())){
					eleVerTot = new DatiImportiCncDTO();
					eleVerTot.setAnnoRif(anno);
					eleVerTot.setCodTipoTributo("TARSU"); 
					eleVerTot.setDesTipoTributo("TARSU");
					eleVerTot.setImpTotRuolo(totImpRuoloAnno);
					eleVerTot.setImpTotVer(totImpVerAnno);
					eleVerTot.setImpTotRiscosso(totImpRiscAnno);
					listaVersamentiTot.add(eleVerTot);
					anno= ele.getAnnoRif();
				}else  {
					if (ele.getImpTotVer() != null)
						totImpVerAnno = totImpVerAnno.add(ele.getImpTotVer());
					if (ele.getImpTotRuolo() != null)
						totImpRuoloAnno = totImpRuoloAnno.add(ele.getImpTotRuolo());
					if(ele.getImpTotRiscosso() != null)
						totImpRiscAnno = totImpRiscAnno.add(ele.getImpTotRiscosso());
					
				}
			}
			eleVerTot = new DatiImportiCncDTO();
			eleVerTot.setAnnoRif(anno);
			eleVerTot.setCodTipoTributo("TARSU"); 
			eleVerTot.setDesTipoTributo("TARSU");
			eleVerTot.setImpTotRuolo(totImpRuoloAnno);
			eleVerTot.setImpTotVer(totImpVerAnno);
			eleVerTot.setImpTotRiscosso(totImpRiscAnno);
			listaVersamentiTot.add(eleVerTot);
		}
		return listaVersamentiTot;
	}


	public List<SitTTarSogg> searchSoggettiCorrelatiTarsu(RicercaDTO dati) throws IndiceNonAllineatoException {
		List<SitTTarSogg> lista=new ArrayList<SitTTarSogg>();
		Object entitySogg =dati.getObjEntity();
		Object filtroSogg =dati.getObjFiltro();
		if (entitySogg == null && (filtroSogg instanceof RicercaSoggettoTarsuDTO )){
			lista= searchSoggettoTarsu((RicercaSoggettoTarsuDTO)filtroSogg);
			return lista;
		}
		if (entitySogg==null || 
		    (!(entitySogg instanceof SitDPersona) && !(entitySogg instanceof SitTTarSogg) && !(entitySogg instanceof SitTIciSogg)))
			return null;
		List<Object> listaIdSoggCorr = getSoggettiCorrelatiTarsu(dati);
		if (listaIdSoggCorr.size() ==0)
			return null;
		for (Object ele: listaIdSoggCorr) {
			logger.debug("scorro la lista");
			String id = ((SitTTarSogg)ele).getId();
			RicercaSoggettoTarsuDTO rs =  new RicercaSoggettoTarsuDTO();
			rs.setEnteId(dati.getEnteId());
			rs.setUserId(dati.getUserId());
			rs.setIdSoggTarsu(id);
			SitTTarSogg soggTar = tarsuService.getSoggettoById(rs);	
			if(soggTar==null)
				throw new IndiceNonAllineatoException("Indice non allineato per Soggetti Tarsu");
			
			//applico le condizioni di filtro, se ci sono
			boolean scarta= false; 
			if (filtroSogg != null) {
				String prov=((RicercaSoggettoTarsuDTO)filtroSogg).getProvenienza();
				if (prov!=null && prov.equals(""))
					if (!soggTar.getProvenienza().equalsIgnoreCase(prov))
						scarta = true;
			}
			if (!scarta && soggTar!=null)	
				lista.add(soggTar);
		}
		return lista;
	}

	
	private List<Object> getSoggettiCorrelatiTarsu(RicercaDTO dati) {
		String progEs = genService.getProgEs(dati); //recupera il progEs dalla tabella corrispondente all'entity
		return genService.getSoggettiCorrelati(dati,progEs, Constants.TRIBUTI_ENTE_SORGENTE,Constants.TRIBUTI_TARSU_TIPO_INFO_SOGG );
	}

	//Da utilizzare per CERCARE I SOGGETTI PER la sezione TARSU:
	//pf: cerca per cf e se non trova per nome-cognome-data nascita-comune nascita 
	//pg: cerca per pi e se non trova per denominazione
	public List<SitTTarSogg> searchSoggettoTarsu(RicercaSoggettoTarsuDTO rs) {
		List<SitTTarSogg> listaSogg=null;
		RicercaSoggettoTarsuDTO parms = new RicercaSoggettoTarsuDTO();
		parms.val(rs);
		parms.setTipoRicerca("all");
		if (rs.getTipoSogg().equals("F")) {
			//1. Ricerca del soggetto in banca dati: codice fiscale
			if(rs.getCodFis() != null &&  !rs.getCodFis().equals("")) {
				parms.forzaRicercaPerCFPI();
				listaSogg = tarsuService.searchSoggetto(parms);
				
			}
			//2. Se non trovo cerco nome-cognome-data nascita-comune nascita 
			if (listaSogg==null || listaSogg.size() ==0) {
				logger.debug("ricerca per cognome-nome-data e luogo nascita");
				parms.val(rs);
				parms.forzaRicercaPerDatiAna();
				listaSogg = tarsuService.searchSoggetto(parms);
			}
		}
		if (rs.getTipoSogg().equals("G")) {
			//1. Ricerca del soggetto in Tarsu: PARTITA IVA 
			parms.forzaRicercaPerPG();
			logger.debug("cerco soggetto pg Tarsu");
			listaSogg = tarsuService.searchSoggetto(parms);
			//2. se non trova, ricerca SOLO per PI -se significativa -
			if (listaSogg==null || listaSogg.size() ==0) {
				if(StringUtility.isANonZeroNumber(parms.getParIva())) {
					parms.val(rs);
					parms.forzaRicercaPerCFPI();
					listaSogg = tarsuService.searchSoggetto(parms);
				}	
			}
			//3. Se non trova, cerca per denominazione
			if (listaSogg==null || listaSogg.size() ==0) {
				parms.val(rs);
				parms.forzaRicercaPerDatiAna();
				listaSogg = tarsuService.searchSoggetto(parms);
			}
		}
		return listaSogg;
		
	}

}
