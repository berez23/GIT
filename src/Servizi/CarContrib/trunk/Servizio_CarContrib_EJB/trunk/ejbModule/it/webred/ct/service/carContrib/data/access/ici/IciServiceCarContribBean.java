package it.webred.ct.service.carContrib.data.access.ici;
import it.webred.ct.data.access.basic.anagrafe.AnagrafeService;
import it.webred.ct.data.access.basic.anagrafe.dto.IndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.data.access.basic.ici.IciService;
import it.webred.ct.data.access.basic.ici.dto.OggettoIciDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaOggettoIciDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaSoggettoIciDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaViaIciDTO;
import it.webred.ct.data.access.basic.ici.dto.VersamentoIciAnnoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.KeyFabbricatoDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.catasto.SiticonduzImmAll;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.ici.SitTIciOggetto;
import it.webred.ct.data.model.ici.SitTIciSogg;
import it.webred.ct.data.model.ici.SitTIciVia;
import it.webred.ct.data.model.ici.VTIciCiviciAll;
import it.webred.ct.data.model.ici.VTIciSoggAll;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;
import it.webred.ct.data.model.tarsu.VTTarCiviciAll;
import it.webred.ct.service.carContrib.data.access.cc.CarContribException;
import it.webred.ct.service.carContrib.data.access.cc.IndiceNonAllineatoException;
import it.webred.ct.service.carContrib.data.access.cnc.CncCarContribLocalService;
import it.webred.ct.service.carContrib.data.access.cnc.dto.DatiImportiCncDTO;
import it.webred.ct.service.carContrib.data.access.cnc.dto.RicercaCncDTO;
import it.webred.ct.service.carContrib.data.access.common.CarContribServiceBaseBean;
import it.webred.ct.service.carContrib.data.access.common.GeneralService;
import it.webred.ct.service.carContrib.data.access.common.dto.IndiciSoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.IndirizzoIciTarsuDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.VersamentoDTO;
import it.webred.ct.service.carContrib.data.access.common.utility.Constants;
import it.webred.ct.service.carContrib.data.access.common.utility.DateUtility;
import it.webred.ct.service.carContrib.data.access.common.utility.IciUtilsFunctions;
import it.webred.ct.service.carContrib.data.access.common.utility.StringUtility;
import it.webred.ct.service.carContrib.data.access.ici.dto.DatiIciDTO;
import it.webred.ct.service.carContrib.data.access.tarsu.dto.DatiTarsuDTO;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class IciServiceCarContribBean
 */
@Stateless
public class IciServiceCarContribBean extends CarContribServiceBaseBean implements IciServiceCarContrib{

	private static final long serialVersionUID = 1L;

	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CommonServiceBean")
	private CommonService  commonService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CatastoServiceBean")
	private CatastoService catastoService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/AnagrafeServiceBean")
	private AnagrafeService anagrafeService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/IciServiceBean")
	private IciService iciService;
		
	@EJB
	private CncCarContribLocalService cncCCService;
	
	@EJB
	private GeneralService genService;

	//Recupera i dati anagrafici dichiarati in ICI del soggetto - INPUT:
	//-->idSoggIci
	public SitTIciSogg getDatiIciSoggetto(RicercaSoggettoIciDTO rs) {
		SitTIciSogg datiSogg = iciService.getSoggettoById(rs);
		if (datiSogg.getDesIndRes()==null || datiSogg.getDesIndRes().equals("")) {
			if (datiSogg.getIdExtViaRes() != null && !datiSogg.getIdExtViaRes().equals("")) {
				RicercaViaIciDTO rv = new RicercaViaIciDTO();
				rv.setEnteId(rs.getEnteId());
				rv.setUserId(rs.getUserId());
				SitTIciVia datiVia = new SitTIciVia();
				datiVia.setIdExt(datiSogg.getIdExtViaRes());
				rv.setDatiVia(datiVia);
				SitTIciVia via = iciService.getViaByIdExt(rv);
				String civico=(datiSogg.getNumCivExt() != null)?datiSogg.getNumCivExt(): "";
				if (via!=null) {
					datiSogg.setDesIndRes(via.getDescrizione()+", " + civico);
				}
			}
			
		}
		return datiSogg;
	}

    /*
     * Recupera i dati dichiarati per la sezione Ici della cartella.
     * INPUT - RicercaSoggettoIciDTO:
     * -->idSoggIci
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
	public List<DatiIciDTO> getDatiIci(RicercaSoggettoIciDTO rs, IndiciSoggettoDTO indSogg ) {
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rs.getEnteId());
		cet.setUserId(rs.getUserId());
		String codEnte = commonService.getEnte(cet).getCodent();
		ArrayList<DatiIciDTO> listaDatiIci=null;
		List<DatiIciDTO> listaDatiIciOrdinata=null;
		//SitTIciSogg soggIci=null;
		try {
			//ricerca gli oggetti Ici
			List<OggettoIciDTO> listaOgg = iciService.getListaOggettiByIdSogg(rs);
			listaDatiIci = new ArrayList<DatiIciDTO> ();
			String key ="";String currKey="";String subKey=""; String subKeyPrec="";
			DatiIciDTO datiIci=null;
			//IndirizzoIciTarsuDTO indirizzo=null;
			RicercaOggettoIciDTO ro =null;
			List<VTIciSoggAll> listaSoggetti = null;
			RicercaOggettoCatDTO roCat=null;
			String sezione="";String foglio=""; String numero=""; String sub ="";
			String annoDen=""; String annoRif="";  
			for (OggettoIciDTO ogg : listaOgg) {
				
				
				sezione=ogg.getOggettoIci().getSez() !=null ? ogg.getOggettoIci().getSez().trim() : "" ;
				foglio=ogg.getOggettoIci().getFoglio() !=null ? ogg.getOggettoIci().getFoglio().trim() : "" ;
				numero=ogg.getOggettoIci().getNumero()!=null ? ogg.getOggettoIci().getNumero().trim() : "" ;
				sub=ogg.getOggettoIci().getSub()!=null ? ogg.getOggettoIci().getSub().trim() : "" ;
				annoRif = ogg.getOggettoIci().getYeaRif() != null ? ogg.getOggettoIci().getYeaRif() .toString(): "";
				annoDen = ogg.getOggettoIci().getYeaDen() != null ? ogg.getOggettoIci().getYeaDen() .toString(): "";
				currKey=sezione + "|"+ foglio + "|" + numero + "|" + sub;
				subKey= annoRif + "|" + annoDen;
				logger.debug("***");
				logger.debug("ICI key/keyPrec : " + currKey + "/" + key);
				logger.debug("ICI subKey/subKeyPrec : " +subKey + "/" + subKeyPrec);
				if (!key.equals(currKey) || (subKey.equals(subKeyPrec) ) ){
					logger.debug("elabora..." );
					//logger.debug("ICI subKey/subKeyPrec : " + currKey + "/" + subKeyPrec);
					//acquisizione lista soggetti coinvolti nella dichiarazione
					ro=new RicercaOggettoIciDTO(null,ogg.getOggettoIci().getIdExt());
					ro.setEnteId(rs.getEnteId());
					ro.setUserId(rs.getUserId());
					listaSoggetti=iciService.getListaSoggettiByOgg(ro);
					datiIci =new DatiIciDTO();
					datiIci.valorizzaDatiDichiarazione(ogg.getOggettoIci(), listaSoggetti);
					//dati da reperire/calcolare al di fuori della banca dati ici
					int anno=0;//anno di riferimento per la verifica dei dati
					try  {
						anno=Integer.parseInt(ogg.getOggettoIci().getYeaRif());
					}catch (NumberFormatException nfe){	}
					if (anno ==0) {
						try  {
							anno=Integer.parseInt(ogg.getOggettoIci().getYeaDen());
						}catch (NumberFormatException nfe){	}
					}
					if (anno==0){
						Date dataSys = new Date();
						anno=DateUtility.annoData(dataSys);
					}
					datiIci.setAnnoRifConfr(anno+"");
					Date dtRif = DateUtility.dataInizioFineAnno(anno, "F");
					Date dtIniAnnoRif = DateUtility.dataInizioFineAnno(anno, "I");
					if (!foglio.equals("") && !numero.equals("") && !sub.equals(""))  {
						//dai dati catastali immobile
						roCat = new RicercaOggettoCatDTO(codEnte,ogg.getOggettoIci().getFoglio(), 
								ogg.getOggettoIci().getNumero(), ogg.getOggettoIci().getSub(),dtRif);
						if (!sezione.equals(""))
							roCat.setSezione(sezione);
						roCat.setEnteId(rs.getEnteId());
						roCat.setUserId(rs.getUserId());
						Sitiuiu ui = catastoService.getDatiUiAllaData(roCat);
						//recupera i dati relativa alla titolarità catastale dell'immobile 
						BigDecimal idSoggCat=null;
						RicercaSoggettoCatDTO rsCat = new RicercaSoggettoCatDTO(codEnte,dtRif);
						rsCat.setEnteId(rs.getEnteId());
						rsCat.setUserId(rs.getUserId());
						if (indSogg !=null && indSogg.getListaIdSoggAnagCat()!= null && indSogg.getListaIdSoggAnagCat().size() >0 ) {
							idSoggCat = indSogg.getListaIdSoggAnagCat().get(0); 
						}
						SiticonduzImmAll datiTitImm= null;
						if (idSoggCat != null) {
							rsCat.setIdSogg(idSoggCat);
							datiTitImm= catastoService.getDatiBySoggUiAllaData(roCat, rsCat);
						}
						//indirizzi catasto
						List<IndirizzoIciTarsuDTO> listaIndirizziDaCatasto=null;
						List<IndirizzoDTO> listaInd = catastoService.getLocalizzazioneCatastale(roCat);
						//indirizzo SIT
						List<IndirizzoDTO> listaIndSIT=null;
						if (ui!=null){
							RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
							roc.setEnteId(rs.getEnteId());
							roc.setIdUiu(ui.getPkidUiu().toString());
							roc.setCodEnte(codEnte);
							listaIndSIT = catastoService.getListaIndirizziImmobile(roc);
						}
						//ora valorizza l'oggetto
						datiIci.valorizzaDatiCatasto(ui, datiTitImm, listaInd, listaIndSIT);
						if (datiIci.getListaIndirizziDaCatasto() != null)	
							logger.debug("indirizzo catasto in  Ici (primo elem): " + datiIci.getListaIndirizziDaCatasto().get(0).getDesIndirizzo());
						//decodifica categoria
						if (ui != null && ui.getCategoria()!= null) {
							RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
							roc.setEnteId(rs.getEnteId());
							roc.setCodCategoria(ui.getCategoria());
							Sitideco sitideco = catastoService.getSitideco(roc);
							if (sitideco!=null)
								datiIci.setDesCategoriaDaCatasto(sitideco.getDescription());
							else
								datiIci.setDesCategoriaDaCatasto("");
						}
						//indirizzo da anagrafe
						String idSoggAna =null;
						if (indSogg !=null && indSogg.getListaIdSoggAnagGen()!= null && indSogg.getListaIdSoggAnagGen().size() >0 ) {
							idSoggAna = indSogg.getListaIdSoggAnagGen().get(0);
							logger.debug("idSoggAna: " +idSoggAna);
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
								datiIci.valorizzaIndirizzoAnagrafe(indAna); 	
								logger.debug("indirizzo anagrafein Ici: " + datiIci.getIndirizzoDaAnagrafe().getDesIndirizzo());  
							}
								
						}
						
					}
					/*RIMANE DA IMPOSTARE
					datiici. setValoreIciCalcolato(metodoCheLoCalcola);
					*/
					//fine
					listaDatiIci.add(datiIci );
					key=currKey;
					subKeyPrec=subKey;
				}else
					logger.debug("scarta.." );
				
			}
		listaDatiIciOrdinata= riordina(listaDatiIci, false)	;
		}catch (Throwable t) {
			logger.error("",t);
			throw new CarContribException(t);
		} 
		return listaDatiIciOrdinata;
		
	
	}
	
		
	//Da utilizzare per CERCARE I SOGGETTI PER la sezione ICI:
	//pf: cerca per cf e se non trova per nome-cognome-data nascita-comune nascita 
	//pg: cerca per pi e se non trova per denominazione
	private List<SitTIciSogg> searchSoggettoIci(RicercaSoggettoIciDTO rs) {
		List<SitTIciSogg> listaSogg=null;
		RicercaSoggettoIciDTO parms = new RicercaSoggettoIciDTO();
		parms.val(rs);
		parms.setTipoRicerca("all");
		if (rs.getTipoSogg().equals("F")) {
			//1. Ricerca del soggetto in banca dati: codice fiscale
			if(rs.getCodFis() != null &&  !rs.getCodFis().equals("")) {
				parms.forzaRicercaPerCFPI();
				listaSogg = iciService.searchSoggetto(parms);
				
			}
			//2. Se non trovo cerco nome-cognome-data nascita-comune nascita 
			if (listaSogg==null || listaSogg.size() ==0) {
				parms.val(rs);
				parms.forzaRicercaPerDatiAna();
				listaSogg = iciService.searchSoggetto(parms);
			}
		}
		if (rs.getTipoSogg().equals("G")) {
			//1. Ricerca del soggetto per dati completi PG( PARTITA IVA +DENOMINAZIONE)
			parms.forzaRicercaPerPG();
			listaSogg = iciService.searchSoggetto(parms);
			//2. se non trova, ricerca SOLO per PI -se significativa -
			if (listaSogg==null || listaSogg.size() ==0) {
				if(StringUtility.isANonZeroNumber(parms.getParIva())) {
					parms.val(rs);
					parms.forzaRicercaPerCFPI();
					listaSogg = iciService.searchSoggetto(parms);
				}	
			}
			//3. Se non trovo cerco per denominazione
			if (listaSogg==null || listaSogg.size() ==0) {
				parms.val(rs);
				parms.forzaRicercaPerDatiAna();
				listaSogg = iciService.searchSoggetto(parms);
			}
			
		}
		return listaSogg;
		
	}

	public List<DatiImportiCncDTO> getVersamenti(RicercaSoggettoIciDTO rs, int annoRif) {
		List<DatiImportiCncDTO> listaVersamentiCompleta=null;
		String codiceTipoTributo="ICI";//TIPO_ENTRATA IN 750 - ARTICOLO
		//String codiceTipoTributo="116";//SOLO PER IL TEST!!!!!!!.......
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rs.getEnteId());
		cet.setUserId(rs.getUserId());
		String codEnte = commonService.getEnte(cet).getCodent();
		//SE NECESSARIO QUESTO METODO SI POTRA' GENERALIZZARE - OPPORTUNAMENTE MODIFICATO - PASSANDO IN INPUT IL CODICE TIPO TRIBUTO  
		List<DatiImportiCncDTO> listaVersamenti=new ArrayList<DatiImportiCncDTO>();
		DatiImportiCncDTO ver= null;
		String[] anniVer = {annoRif +"" , annoRif-1+ "", annoRif-2+"", annoRif-3 +"", annoRif-4 +""};
		for (int i=0; i < anniVer.length; i++) {
			ver = new DatiImportiCncDTO();
			ver.setEnteId(rs.getEnteId());
			ver.setUserId(rs.getUserId());
			ver.setAnnoRif(anniVer[i]);
			ver.setCodTipoTributo(codiceTipoTributo);
			ver.setDesTipoTributo("ICI");
			ver.setImpTotVer(new BigDecimal(0));
			ver.setImpTotRuolo(new BigDecimal(0));
			ver.setImpTotRiscosso(new BigDecimal(0));
			listaVersamenti.add(ver);
		}
		logger.debug("listaVersamenti. Ele: " + listaVersamenti.size());
		//versamenti ICI
		List<VersamentoIciAnnoDTO> listaVerIci = iciService.getSommaVersamenti(rs);
		int indAnnoRif=-1;
		if (listaVerIci != null) {
			for (int i = 0 ; i < anniVer.length; i++){
				for (int j = 0; j < listaVerIci.size(); j++ ) {
					VersamentoIciAnnoDTO verIci = listaVerIci.get(j);
					if (verIci.getYeaRif().equals(anniVer[i])) {
						listaVersamenti.get(i).setImpTotVer(verIci.getImpPagEu());
					}
				}
			}
		}
		for (int i = 0 ; i < listaVersamenti.size(); i++){
			DatiImportiCncDTO ele =  listaVersamenti.get(i);
			logger.debug(" -- ANNO -->" + ele.getAnnoRif());
			logger.debug(" -- importo -->" + ele.getImpTotVer());
		}
		RicercaCncDTO rc = new RicercaCncDTO();
		rc.setEnteId(rs.getEnteId());
		rc.setUserId(rs.getUserId());
		rc.setCodEnteCreditore(codEnte);
		rc.setCodFis(rs.getCodFis());
		//listaVersamentiCompleta=cncCCService.getImportiCnc(rc, null);//per test stato riscossione
		listaVersamentiCompleta=cncCCService.getImportiCnc(rc, listaVersamenti);
		return listaVersamentiCompleta;
	}
    //IN SOSTITUZIONE DI searchSoggettoIci()  
	public List<SitTIciSogg> searchSoggettiCorrelatiIci(RicercaDTO dati) throws IndiceNonAllineatoException {
		List<SitTIciSogg> lista=new ArrayList<SitTIciSogg>();
		Object entitySogg =dati.getObjEntity();
		Object filtroSogg =dati.getObjFiltro();
		if (entitySogg == null && (filtroSogg instanceof RicercaSoggettoIciDTO )){
			lista= searchSoggettoIci((RicercaSoggettoIciDTO)filtroSogg);
			return lista;
		}
		if (entitySogg==null || 
		    (!(entitySogg instanceof SitDPersona) && !(entitySogg instanceof SitTTarSogg) && !(entitySogg instanceof SitTIciSogg)))
			return null;
		List<Object> listaIdSoggCorr = getSoggettiCorrelatiIci(dati);
		if (listaIdSoggCorr.size() ==0)
			return null;
		for (Object ele: listaIdSoggCorr) {
			logger.debug("scorro la lista");
			String id = ((SitTIciSogg)ele).getId();
			RicercaSoggettoIciDTO rs =  new RicercaSoggettoIciDTO();
			rs.setEnteId(dati.getEnteId());
			rs.setUserId(dati.getUserId());
			rs.setIdSoggIci(id);
			SitTIciSogg soggIci = iciService.getSoggettoById(rs);	
			if(soggIci==null)
				throw new IndiceNonAllineatoException("Indice non allineato per Soggetti Ici");
			
			//applico le condizioni di filtro, se ci sono
			boolean scarta= false; 
			if (filtroSogg != null) {
				String prov=((RicercaSoggettoIciDTO)filtroSogg).getProvenienza();
				if (prov!=null && prov.equals(""))
					if (!soggIci.getProvenienza().equalsIgnoreCase(prov))
						scarta = true;
			}
			if (!scarta && soggIci!=null)	
				lista.add(soggIci);
		}
		return lista;
		
	}
	
	private List<Object> getSoggettiCorrelatiIci(RicercaDTO dati) {
		String progEs = genService.getProgEs(dati); //recupera il progEs dalla tabella corrispondente all'entity
		return genService.getSoggettiCorrelati(dati,progEs, Constants.TRIBUTI_ENTE_SORGENTE,Constants.TRIBUTI_ICI_TIPO_INFO_SOGG );
				
	}
	

	//Recupera i  versamenti per la scheda ICI (relativamente agli ultimi 5 anni a partire dalla data di riferimento)
	//INPUT 
	//-->codEnte 
	//-->idSoggIci 
	//-->codFis (valorizzare con la pi per le persone giuridiche)  
	public List<VersamentoDTO> getVersamentiOLD(RicercaSoggettoIciDTO rs, int annoRif) {
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rs.getEnteId());
		cet.setUserId(rs.getUserId());
		String codEnte = commonService.getEnte(cet).getCodent();
		List<VersamentoDTO> listaVersamentiCompleta=null;
		String codiceTipoTributo="ICI";//TIPO_ENTRATA IN 750 - ARTICOLO
		//String codiceTipoTributo="116";//SOLO PER IL TEST!!!!!!!.......
		//SE NECESSARIO QUESTO METODO SI POTRA' GENERALIZZARE - OPPORTUNAMENTE MODIFICATO - PASSANDO IN INPUT IL CODICE TIPO TRIBUTO  
		List<VersamentoDTO> listaVersamenti=new ArrayList<VersamentoDTO>();
		VersamentoDTO ver= null;
		String[] anniVer = {annoRif +"" , annoRif-1+ "", annoRif-2+"", annoRif-3 +"", annoRif-4 +""};
		for (int i=0; i < anniVer.length; i++) {
			ver = new VersamentoDTO(anniVer[i]);
			listaVersamenti.add(ver);
		}
		//versamenti ICI
		List<VersamentoIciAnnoDTO> listaVerIci = iciService.getSommaVersamenti(rs);
		int indAnnoRif=-1;
		if (listaVerIci != null) {
			for (int i = 0 ; i < anniVer.length; i++){
				for (int j = 0; j < listaVerIci.size(); j++ ) {
					VersamentoIciAnnoDTO verIci = listaVerIci.get(j);
					if (verIci.getYeaRif().equals(anniVer[i])) {
						listaVersamenti.get(i).setImpTotVer(verIci.getImpPagEu());
					}
				}
			}
		}
		RicercaCncDTO rc = new RicercaCncDTO();
		rc.setEnteId(rs.getEnteId());
		rc.setUserId(rs.getUserId());
		rc.setCodEnteCreditore(codEnte);
		rc.setCodFis(rs.getCodFis());
		listaVersamentiCompleta=cncCCService.getVersamenti(rc, listaVersamenti);
		return listaVersamentiCompleta;
		
	}
	
	private List<DatiIciDTO>  riordina(ArrayList<DatiIciDTO> lista, boolean ordinamentoCrescente) {
		if (lista ==null || lista.size() == 0)
			return lista;
		List<DatiIciDTO> listaOrdinata = new ArrayList<DatiIciDTO> ();
		DatiIciDTO[] arr = new  DatiIciDTO[lista.size()]; 
		DatiIciDTO[] arr1 = lista.toArray(arr);
		Arrays.sort(arr1,lista.get(0));
		listaOrdinata = Arrays.asList(arr1);
		if (!ordinamentoCrescente)	{
			listaOrdinata = new ArrayList<DatiIciDTO>();
			for(int i=arr1.length - 1; i >= 0; i-- ) {
				DatiIciDTO ele = arr1[i];
				listaOrdinata.add(ele);
			}
		}
		return listaOrdinata;	
	}
	
}


