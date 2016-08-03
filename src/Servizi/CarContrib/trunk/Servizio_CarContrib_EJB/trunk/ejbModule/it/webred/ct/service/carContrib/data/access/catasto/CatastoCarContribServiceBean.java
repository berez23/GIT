package it.webred.ct.service.carContrib.data.access.catasto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.TerrenoPerSoggDTO;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.access.basic.tarsu.TarsuService;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuParCatDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SoggettoTarsuDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.Siticomu;
import it.webred.ct.data.model.catasto.SiticonduzImmAll;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;
import it.webred.ct.service.carContrib.data.access.common.CarContribServiceBaseBean;
import it.webred.ct.service.carContrib.data.access.common.GeneralService;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SitPatrimImmobileDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SitPatrimTerrenoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.utility.Constants;
import it.webred.ct.service.carContrib.data.access.common.utility.DateUtility;
import it.webred.ct.service.carContrib.data.access.common.utility.StringUtility;
import it.webred.ct.support.datarouter.CeTBaseObject;
@Stateless
public class CatastoCarContribServiceBean extends CarContribServiceBaseBean implements CatastoCarContribService, CatastoCarContribLocalService {
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CommonServiceBean")
	private CommonService  commonService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CatastoServiceBean")
	private CatastoService catastoService;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/TarsuServiceBean")
	private TarsuService tarsuService;
	
	@EJB
	private GeneralService generalService;
	
	
	//Recupera gli immobili posseduti alla dat di riferimento - INPUT:
	//-->RicercaSoggettoCatDTO: codEnte
	//-->RicercaSoggettoCatDTO: idSogg
	//-->RicercaSoggettoCatDTO: dtVal (opz.)  PER ICI DEVE ESSERE VALORIZZATA QUESTA
	//-->RicercaSoggettoCatDTO: dtRifDa - dtRifA  (opz.) (in alternativa con DtVal) PER TARSU DEVEONO ESSERE VALORIZZATE QUESTE 2 
	//-->visInScheda - valori possibili: ICI, TARSU (per ora) 
 	public List<SitPatrimImmobileDTO> getImmobiliPosseduti(RicercaSoggettoCatDTO rs, String visInScheda) {
		String codEnte =rs.getCodEnte();
		//RECUPERA L'ENTE 
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rs.getEnteId());
		cet.setUserId(rs.getUserId());
		if (codEnte==null || codEnte.equals(""))
			codEnte= commonService.getEnte(cet).getCodent();
		rs.setCodEnte(codEnte);
 		List<SitPatrimImmobileDTO> listaImm=null;
		SitPatrimImmobileDTO sitPatr=null;
		//Ricavo gli immobili posseduti
		// N.B.: getImmobiliByIdSogg() recupera gli immobili alla dtVal, se essa è valorizzata, 
		// gli immobile posseduti tra la dtRifDa e la dtRifA, se esse sono valorizzate 
		// tuyyi gli immobili posseduti nel corso del tempo se nessuna data è valorizzata 
		List<SiticonduzImmAll> listaTit = catastoService.getImmobiliByIdSogg(rs);
		
		RicercaOggettoCatDTO rod = new RicercaOggettoCatDTO();
		rod.setEnteId(codEnte);
		rod.setUserId(rs.getUserId());
		
		if (listaTit!= null){
			listaImm= new ArrayList<SitPatrimImmobileDTO>();
			for (SiticonduzImmAll imm: listaTit)  {
				sitPatr= new SitPatrimImmobileDTO(imm);
				
				//Recupero la descrizione della titolarità
				rod.setCodTipoDocumento(imm.getTipoDocumento());
				String descTitolo = catastoService.getDescConduzTipoDocumento(rod);
				sitPatr.setDescTitolo(descTitolo);
				
				//ricavo le informazioni inerenti la u.i.:
				//--alla data di validità in input, se la data di fine possesso non è valorizzata
				//--alla data di  fine possesso, se essa è valorizzata
				Date dtFinPos = imm.getId().getDataFine();
				Date dtFinPosFtz = DateUtility.faiParse(DateUtility.DT_FIN_VAL_CAT, DateUtility.FMT_DATE_VIS);
				Date dtVal=null;
				if (dtFinPos ==null || dtFinPosFtz.compareTo(dtFinPos)== 0){
					if (rs.getDtVal()!= null)
						dtVal =rs.getDtVal();
					else
						dtVal = new Date();
				}else
					dtVal = dtFinPos;
				RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
				ro.setEnteId(rs.getEnteId());
				ro.setUserId(rs.getUserId());
				ro.setCodEnte(codEnte);
				ro.setFoglio(imm.getId().getFoglio()+"");
				ro.setParticella(imm.getId().getParticella());
				ro.setUnimm(imm.getId().getUnimm()+"");
				ro.setDtVal(dtVal);
				Sitiuiu datiUICat= catastoService.getDatiUiAllaData(ro) ;
				String sezione=null;
				if (datiUICat !=null) {
					RicercaOggettoCatDTO roSitiComu = new RicercaOggettoCatDTO();
					roSitiComu.setEnteId(rs.getEnteId());
					roSitiComu.setUserId(rs.getUserId());
					roSitiComu.setCodEnte(datiUICat.getId().getCodNazionale());
					Siticomu siticomu=catastoService.getSitiComu(roSitiComu);
					if (siticomu != null){
						if(siticomu.getIdSezc()!=null && !siticomu.getIdSezc().equals(""))
							sezione=siticomu.getIdSezc();
					}
					//val. dati da Sitiuiu nel bean- sia per ici che per tarsu
					sitPatr.setSezione(sezione);
					sitPatr.setCategoria(datiUICat.getCategoria());
					sitPatr.setClasse(datiUICat.getClasse());
					sitPatr.setRendita(datiUICat.getRendita());
					if (datiUICat.getRendita()!=null)
						sitPatr.setRenditaF(StringUtility.DFEURO.format(datiUICat.getRendita()));
					else
						sitPatr.setRenditaF("");
					sitPatr.setSuperficieCat(datiUICat.getSupCat());
					if (datiUICat.getSupCat()!=null)
						sitPatr.setSuperficieCatF(StringUtility.DF.format(datiUICat.getSupCat()));
					else
						sitPatr.setSuperficieCatF("");
					//logger.debug("UI - F/P/S: " + sitPatr.getDatiTitImmobile().getId().getFoglio() + "/" + sitPatr.getDatiTitImmobile().getId().getParticella() + "/" + sitPatr.getDatiTitImmobile().getId().getUnimm() );
					//logger.debug("--> RENDITA: " + datiUICat.getRendita());
					//logger.debug("--> SUP CAT: " + datiUICat.getSupCat());
				}
				if (imm.getPercPoss()!=null)
					sitPatr.setPercPossF(StringUtility.DF.format(imm.getPercPoss()));
				else
					sitPatr.setPercPossF("");
				
				if (visInScheda != null && visInScheda.equalsIgnoreCase("TARSU")) {
					//calcolo superficie utile ai fini Tarsu
					BigDecimal supTarsuC340 = catastoService.calcolaSupUtileTarsuC30(ro);
					sitPatr.setSuperficieTarsu(supTarsuC340);
					if (supTarsuC340!=null)
						sitPatr.setSuperficieTarsuF(StringUtility.DF.format(supTarsuC340));
					else
						sitPatr.setSuperficieTarsuF("");
					logger.debug("--> SUP TARSU: " + sitPatr.getSuperficieTarsu());
					//acquisiSco i dichiaranti TARSU PER L'IMMOBILE
					RicercaOggettoTarsuParCatDTO parOggT = new RicercaOggettoTarsuParCatDTO();
					parOggT.setEnteId(rs.getEnteId());
					parOggT.setUserId(rs.getUserId());
					if (sezione != null)
						parOggT.setSezione(sezione);
					parOggT.setFoglio(imm.getId().getFoglio()+"");
					parOggT.setParticella(imm.getId().getParticella()+"");
					parOggT.setSubalterno(imm.getId().getUnimm()+"");
					List<SitTTarOggetto> listaOggTar= tarsuService.getListaDichiarazioniTarsu(parOggT);
					//mi interessa l'ultima dichiarazione
					SitTTarOggetto oggT = null;
					if (listaOggTar != null && listaOggTar.size()>0)
						oggT=listaOggTar.get(0);
					
					if (oggT != null) {
						logger.debug("RICERCA DICH. TARSU PER U.I.: " + oggT.getFoglio() + "-" + oggT.getNumero() + "-" + oggT.getSub() );
						RicercaOggettoTarsuDTO rot = new RicercaOggettoTarsuDTO();
						rot.setEnteId(rs.getEnteId());
						rot.setUserId(rs.getUserId());
						rot.setIdExtOgg(oggT.getIdExt());
						String desSoggT="";
						List<SoggettoTarsuDTO> listaSoggettiTarsu = tarsuService.getListaSoggettiDichiarazioneTarsu(rot);
						if (listaSoggettiTarsu != null && listaSoggettiTarsu.size() > 0 ) {
							List<String> listaDichiarantiTarsu= new ArrayList<String>();
							for (SoggettoTarsuDTO soggDTO : listaSoggettiTarsu)  {
								SitTTarSogg sogg = soggDTO.getSoggetto();
								desSoggT += sogg.getCogDenom() != null ? sogg.getCogDenom(): "";
								if (sogg.getTipSogg().equalsIgnoreCase("F")){
									desSoggT += sogg.getNome()!= null ? " " + sogg.getNome(): "";
									desSoggT += sogg.getCodFisc()!= null ? " CF: " + sogg.getCodFisc(): "";
								}else
									desSoggT += sogg.getPartIva()!= null ? " PI: " + sogg.getPartIva(): "";
								desSoggT +=" - " +soggDTO.getTitolo();  
								listaDichiarantiTarsu.add(desSoggT);
								logger.debug("DICH. TARSU : " + desSoggT);
							}
							sitPatr.setListaDichiarantiTarsu(listaDichiarantiTarsu);
						}
			
					}
				}
				
				//TODO: verificare come reperire le info mancanti: 
				sitPatr.setFlagCessato(null);
				sitPatr.setFlagCostituito(null);
				//TODO: PER IL MOMENTO NON è POSSIBILE perché NON ABBIAMO LE COORDINATE NELLE LOCAZIONI. SI PUO' VEDERE SE FARE PER INDIRIZZO .... UTILIZZANDO L'INDICE DI CORRELAZIONE???
				sitPatr.setFlagLocato(null);
				
				listaImm.add(sitPatr);
			}
		}
		return listaImm; 
	}

 	//Recupera i terreni posseduti alla data di riferimento - INPUT:
	//--> codEnte
	//--> idSogg
	//--> dtVal 
	public List<SitPatrimTerrenoDTO> getTerreniPosseduti(RicercaSoggettoCatDTO rs) {
		String codEnte =rs.getCodEnte();
		//RECUPERA L'ENTE 
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rs.getEnteId());
		cet.setUserId(rs.getUserId());
		if (codEnte==null || codEnte.equals(""))
			codEnte= commonService.getEnte(cet).getCodent();
		rs.setCodEnte(codEnte);
		List<SitPatrimTerrenoDTO> listaTerr=null;
		SitPatrimTerrenoDTO sitPatr=null;
		List<TerrenoPerSoggDTO> listaTit = catastoService.getTerreniByIdSogg(rs);
		if (listaTit!= null) {
			listaTerr= new ArrayList<SitPatrimTerrenoDTO>();
			//TODO: verificare come reperire le info mancanti per valorizzare i bean
			for (TerrenoPerSoggDTO terr: listaTit)  {
				sitPatr= new SitPatrimTerrenoDTO(terr);
				// REDDITO AGRARIO FORMATTATO
				sitPatr.setRedditoAgrarioF("-");
				if (terr.getRedditoAgrario()!=null)
					sitPatr.setRedditoAgrarioF(StringUtility.DFEURO.format(terr.getRedditoAgrario()));
				// REDDITO DOMENICALE FORMATTATO
				sitPatr.setRedditoDomenicaleF("-");
				if (terr.getRedditoDominicale()!=null)
					sitPatr.setRedditoDomenicaleF(StringUtility.DFEURO.format(terr.getRedditoDominicale()));
				// SUPERFICIE FORMATTATA
				sitPatr.setSuperficieF("-");
				if (terr.getSuperficie()!=null)
					sitPatr.setSuperficieF(StringUtility.DF.format(terr.getSuperficie()));
				
				sitPatr.setFlagCessato(null);
				sitPatr.setFlagCostituito(null);
				sitPatr.setFlagLocato(null);
				sitPatr.setFlagEdificabile(null);
				listaTerr.add(sitPatr);
			}
		}
		return listaTerr; 
	}

	//Recupera gli immobili ceduti nell'anno di riferimento - INPUT:
	//--> codEnte
	//--> idSogg
	//--> dtRifDa
	//--> dtRifA
	public List<SitPatrimImmobileDTO> getImmobiliCeduti(RicercaSoggettoCatDTO rs) {
		String codEnte =rs.getCodEnte();
		//RECUPERA L'ENTE 
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rs.getEnteId());
		cet.setUserId(rs.getUserId());
		if (codEnte==null || codEnte.equals(""))
			codEnte= commonService.getEnte(cet).getCodent();
		rs.setCodEnte(codEnte);
		List<SitPatrimImmobileDTO> listaImm=null;
		SitPatrimImmobileDTO sitPatr=null;
		List<SiticonduzImmAll> listaTit = catastoService.getImmobiliByIdSoggCedutiInRangeDate(rs);
		if (listaTit!= null) {
			listaImm= new ArrayList<SitPatrimImmobileDTO>();
			for (SiticonduzImmAll imm: listaTit)  {
				sitPatr= new SitPatrimImmobileDTO(imm);
				listaImm.add(sitPatr);
			}
		}
		return listaImm; 
	}

	//Recupera i terreni ceduti nell'anno di riferimento - INPUT:
	//--> codEnte
	//--> idSogg
	//--> dtRifDa
	//--> dtRifA
	public List<SitPatrimTerrenoDTO> getTerreniCeduti(RicercaSoggettoCatDTO rs) {
		String codEnte =rs.getCodEnte();
		//RECUPERA L'ENTE 
		CeTBaseObject cet = new CeTBaseObject();
		cet.setEnteId(rs.getEnteId());
		cet.setUserId(rs.getUserId());
		if (codEnte==null || codEnte.equals(""))
			codEnte= commonService.getEnte(cet).getCodent();
		rs.setCodEnte(codEnte);
		List<SitPatrimTerrenoDTO> listaTerr=null;
		SitPatrimTerrenoDTO sitPatr=null;
		List<TerrenoPerSoggDTO> listaTit = catastoService.getTerreniByIdSoggCedutiInRangeDate(rs);
		if (listaTit!= null) {
			listaTerr= new ArrayList<SitPatrimTerrenoDTO>();
			for (TerrenoPerSoggDTO terr: listaTit)  {
				sitPatr= new SitPatrimTerrenoDTO(terr);
				listaTerr.add(sitPatr);
			}
		}
		return listaTerr; 
	}
	
	
	private List<Object> getSoggettiCorrelatiCatasto(RicercaDTO dati) {
		return generalService.getSoggettiCorrelati(dati,generalService.getProgEs(dati), Constants.CATASTO_ENTE_SORGENTE,Constants.CATASTO_TIPO_INFO_CONDUZ);
	
	}	

}
