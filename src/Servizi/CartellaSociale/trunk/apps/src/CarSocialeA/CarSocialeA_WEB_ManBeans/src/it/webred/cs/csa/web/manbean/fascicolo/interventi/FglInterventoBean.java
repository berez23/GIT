package it.webred.cs.csa.web.manbean.fascicolo.interventi;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableIndirizzoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableNazioniSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSchedaSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.InterventoDTO;
import it.webred.cs.csa.web.manbean.fascicolo.FascicoloCompBaseBean;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsCCategoriaSociale;
import it.webred.cs.data.model.CsCTipoIntervento;
import it.webred.cs.data.model.CsDRelazione;
import it.webred.cs.data.model.CsFlgIntervento;
import it.webred.cs.data.model.CsIIntervento;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsOOperatoreTipoOperatore;
import it.webred.cs.data.model.CsOOrganizzazione;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.data.model.CsRelSettCsocTipoInterErPK;
import it.webred.cs.data.model.CsRelSettCsocTipoInterPK;
import it.webred.cs.data.model.CsTbInterventiUOL;
import it.webred.cs.data.model.CsTbMotivoChiusuraInt;
import it.webred.cs.data.model.CsTbTipoCirc4;
import it.webred.cs.data.model.CsTbTipoContributo;
import it.webred.cs.data.model.CsTbTipoProgetto;
import it.webred.cs.data.model.CsTbTipoRetta;
import it.webred.cs.data.model.CsTbTipoRientriFami;
import it.webred.cs.jsf.bean.DatiFglInterventoBean;
import it.webred.cs.jsf.bean.DatiInterventoBean;
import it.webred.cs.jsf.interfaces.IDatiFglIntervento;
import it.webred.cs.jsf.manbean.ComponenteAltroMan;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.ejb.utility.ClientUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;

import org.primefaces.context.RequestContext;

public class FglInterventoBean extends FascicoloCompBaseBean implements IDatiFglIntervento {
	
	protected AccessTableConfigurazioneSessionBeanRemote confService = (AccessTableConfigurazioneSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableConfigurazioneSessionBean");	
	
	protected AccessTableSchedaSessionBeanRemote schedaService = (AccessTableSchedaSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableSchedaSessionBean");
	
	protected AccessTableIndirizzoSessionBeanRemote indirizzoService = (AccessTableIndirizzoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableIndirizzoSessionBean");
	
	protected AccessTableOperatoreSessionBeanRemote operatoreService = (AccessTableOperatoreSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableOperatoreSessionBean");
	
	protected AnagraficaService amAnagraficaService = (AnagraficaService) getEjb("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");
	
	private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	
	protected Long settoreId;
	protected Long interventoId;
	protected Long diarioId;
	
	private DatiFglInterventoBean datiFglIntBean;
	private DatiInterventoBean datiInterventoBean; 
	
	private String codDialog;
	private String widgetVar;
	
	private CsASoggetto soggettoCorrente;
	
	@Override
	public void initializeData() {
		CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
		settoreId=opSettore.getCsOSettore().getId();
	}
	
	public void inizializzaDialog() {
		
		datiFglIntBean = new DatiFglInterventoBean();
		datiInterventoBean = new DatiInterventoBean(settoreId,idSoggetto);
		
		BaseDTO dto = new BaseDTO();
		fillEnte(dto);
		
		ComponenteAltroMan componente = new ComponenteAltroMan(idSoggetto);
		datiFglIntBean.setComponente(componente);
		
		if(diarioId != null && diarioId.intValue()>0){
			
			CsFlgIntervento cs;
			try {
				
				dto.setObj(diarioId);
				cs = interventoService.getFoglioInterventoById(dto);
				
				datiFglIntBean.valorizzaBean(cs);
								
				//Valorizzo i dati Intervento
				CsIIntervento ci = cs.getCsIIntervento();
				datiInterventoBean = new DatiInterventoBean(ci,idSoggetto);
				datiInterventoBean.setNuovoIntervento(false);
				interventoId = ci.getId();
				
			} catch (Exception e) {
				addErrorFromProperties("caricamento.error");
				logger.error(e.getMessage(),e);
			}
		}else{
			if(interventoId==null || interventoId.intValue()<=0){
				datiInterventoBean.setNuovoIntervento(true);
				if(this.getLstTipoIntervento().size()>0)
					datiInterventoBean.setTipoIntervento((Long)this.getLstTipoIntervento().get(0).getValue());
			}else{
				datiInterventoBean.setNuovoIntervento(false);
				
				dto.setObj(interventoId);
				try {
					CsIIntervento  ci = interventoService.getInterventoById(dto);
					datiInterventoBean = new DatiInterventoBean(ci,idSoggetto);
					datiInterventoBean.setNuovoIntervento(false);
				} catch (Exception e) {
					addErrorFromProperties("caricamento.error");
					logger.error(e.getMessage(),e);
				}
				
			}
		}
		
	}
	
	public void eliminaFoglio(){
		try{
			BaseDTO b = new BaseDTO();
			fillEnte(b);
			b.setObj(this.diarioId);
			
			logger.debug("Rimozione in corso del foglio amministrativo e relativo diario (id="+this.diarioId+")");
			
			interventoService.deleteFoglioAmministrativo(b);
			diarioService.deleteDiario(b);
			
			b.setObj(this.interventoId);
			CsIIntervento inter = interventoService.getInterventoById(b);
			if(inter.getCsFlgInterventos()==null || inter.getCsFlgInterventos().size()==0){
				logger.debug("Rimozione dell'intervento (id="+this.interventoId+")");
				interventoService.deleteIntervento(b);
			}
			
			RequestContext.getCurrentInstance().addCallbackParam("removed", true);
			
			addInfoFromProperties("elimina.ok");
			
		} catch (Exception e) {
			addErrorFromProperties("elimina.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	public void changeTipoIntervento(){
		datiInterventoBean.resetOnChangeTipoIntervento();
		datiFglIntBean.setIdRelazione(null);
	}
	
	public void reset() {
		datiFglIntBean = new DatiFglInterventoBean();
		datiInterventoBean  = new DatiInterventoBean(settoreId,idSoggetto);
		interventoId=null;
		diarioId=null;
	}
	
	
	public void salva() {
		
		try{

			BaseDTO dto = new BaseDTO();
			fillEnte(dto);	
			dto.setObj(diarioId);
			
			//dati anagrafici
			CsFlgIntervento cs = new CsFlgIntervento();
			
			if(diarioId!=null && diarioId.intValue()>0){
				cs = interventoService.getFoglioInterventoById(dto);
						
			}
			
			BaseDTO b = new BaseDTO();
			fillEnte(b);
			
			datiFglIntBean.valorizzaJpa(cs);
					
			CsIIntervento inter = new CsIIntervento();
			if(interventoId==null || interventoId.intValue()<=0){
				
				datiInterventoBean.valorizzaDettaglioIntervento(inter);
				
				if(datiInterventoBean.getSettore()!=null && datiInterventoBean.getTipoIntervento()!=null){
					
					CsRelSettCsocTipoInterPK pk = new CsRelSettCsocTipoInterPK();
					
					pk.setScsSettoreId(datiInterventoBean.getSettore());
					pk.setCstiTipoInterventoId(datiInterventoBean.getTipoIntervento());
					pk.setScsCategoriaSocialeId(this.catsocCorrente.getId());
					
					b.setObj(pk);
					inter.setCsRelSettCsocTipoInter(interventoService.findRelSettCsocTipoInterById(b));
				}
				
				if(datiInterventoBean.getErSettore()!=null && datiInterventoBean.getTipoIntervento()!=null){
					
					CsRelSettCsocTipoInterErPK pk = new CsRelSettCsocTipoInterErPK();
					pk.setScsSettoreId(datiInterventoBean.getErSettore());
					pk.setCstiTipoInterventoId(datiInterventoBean.getTipoIntervento());
					pk.setScsCategoriaSocialeId(this.catsocCorrente.getId());
					
					b.setObj(pk);
					inter.setCsRelSettCsocTipoInterEr(interventoService.findRelSettCsocTipoInterErById(b));
				}
			
				dto.setObj(inter);
				inter = interventoService.salvaIntervento(dto);
				
			}else{
				b.setObj(interventoId);
				inter = interventoService.getInterventoById(b);
			}
			
			cs.setCsIIntervento(inter);
			
			InterventoDTO foglioDTO = new InterventoDTO();
			fillEnte(foglioDTO);
			foglioDTO.setFoglio(cs);
			foglioDTO.setCasoId(idCaso);
			foglioDTO.setIdRelazione(datiFglIntBean.getIdRelazione());
			interventoService.salvaFoglioAmministrativo(foglioDTO);
			
		
			RequestContext.getCurrentInstance().addCallbackParam("saved", true);
			
		//	String lblClientId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pnlToUpdateId");
		//	RequestContext.getCurrentInstance().update(lblClientId);
					
			addInfoFromProperties("salva.ok");
		
		
		} catch(Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	
	public DatiFglInterventoBean getDatiFglIntBean() {
		return datiFglIntBean;
	}

	public void setDatiFglIntBean(DatiFglInterventoBean datiFglIntBean) {
		this.datiFglIntBean = datiFglIntBean;
	}

	private List<SelectItem> lstSessi;
	public List<SelectItem> getLstSessi() {
		if(lstSessi == null) {
			lstSessi = new ArrayList<SelectItem>();
			lstSessi.add(new SelectItem(null, "-->"));
			lstSessi.add(new SelectItem("M", "M"));
			lstSessi.add(new SelectItem("F", "F"));
		}
		return lstSessi;
	}
	
	private List<SelectItem> lstCittadinanze;
	public List<SelectItem> getLstCittadinanze() {
		if(lstCittadinanze == null) {
			lstCittadinanze = new ArrayList<SelectItem>();
			lstCittadinanze.add(new SelectItem(null, "--> scegli"));
			try {
				AccessTableNazioniSessionBeanRemote bean = (AccessTableNazioniSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableNazioniSessionBean");
				List<String> beanLstCittadinanze = bean.getCittadinanze();
				if (beanLstCittadinanze != null) {
					for (String cittadinanza : beanLstCittadinanze) {
						//in AM_TAB_NAZIONI il campo NAZIONALITA ha lunghezza 500, in CS_A_SOGGETTO il campo CITTADINANZA ha lunghezza 255
						if (cittadinanza.length() > 255) {
							cittadinanza = cittadinanza.substring(0, 252) + "...";
						}
						lstCittadinanze.add(new SelectItem(cittadinanza, cittadinanza));
					}
				}
			} catch (NamingException e) {
				addErrorFromProperties("caricamento.error");
				logger.error(e.getMessage(),e);
			}
		}
		return lstCittadinanze;
	}



	private List<SelectItem> lstMotiviChiusura;
	@Override
	public List<SelectItem> getLstMotiviChiusura() {
		if(lstMotiviChiusura == null) {
			lstMotiviChiusura = new ArrayList<SelectItem>();
			lstMotiviChiusura.add(new SelectItem(null, "--> scegli"));
			try {
				CeTBaseObject cet = new CeTBaseObject();
				fillEnte(cet);
				List<CsTbMotivoChiusuraInt> beanLst = confService.getMotiviChiusuraIntervento(cet);
				if (beanLst != null) {
					for (CsTbMotivoChiusuraInt s : beanLst) {
						lstMotiviChiusura.add(new SelectItem(s.getId(), s.getDescrizione()));
					}
				}
			} catch (Exception e) {
				addErrorFromProperties("caricamento.error");
				logger.error(e.getMessage(),e);
			}
		}
		return lstMotiviChiusura;
	}

	@Override
	public List<SelectItem> getLstSettoriEr() {
		ArrayList<SelectItem> lst = new ArrayList<SelectItem>();
		lst.add(new SelectItem(null, "--> scegli"));
		try {
			InterventoDTO idto = new InterventoDTO();
			fillEnte(idto);
			idto.setIdCatsoc(this.catsocCorrente.getId());
			idto.setIdTipoIntervento(datiInterventoBean.getTipoIntervento());
			List<CsOSettore> beanLst = interventoService.getListaSettoriEr(idto);
			if (beanLst != null) {
				for (CsOSettore s : beanLst) {
					lst.add(new SelectItem(s.getId(), s.getNome()));
				}
			}
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		return lst;
	}
	
	@Override
	public List<SelectItem> getLstTipoIntervento(){
		List<SelectItem> lstTipoIntervento = new ArrayList<SelectItem>();
		HashMap<String,DatiInterventoBean> mappaIAperti = new HashMap<String,DatiInterventoBean>();
		try {
			if(settoreId!=null && catsocCorrente!=null){
				InterventoDTO dto = new InterventoDTO();
				fillEnte(dto);
				dto.setIdSettore(settoreId);
				dto.setIdCatsoc(this.catsocCorrente.getId());
				List<CsCTipoIntervento> beanlst = interventoService.findTipiInterventoSettoreCatSoc(dto);
				
				//Controllare per ciascun tipo intervento non esistano interventi aperti (in tal caso omettere dalla lista)
					if(this.idCaso!=null && idCaso>0){
						BaseDTO bdto = new BaseDTO();
						fillEnte(bdto);
						bdto.setObj(idCaso);
						List<CsIIntervento> lsti = interventoService.getListaInterventiByCaso(bdto);
						for(CsIIntervento i : lsti){
							DatiInterventoBean dib = new DatiInterventoBean(i,idSoggetto);
							if(!dib.getInterventoChiuso().booleanValue())
								mappaIAperti.put(dib.getDescTipoIntervento(), dib);
						}
					}
				
				if (beanlst != null) {
					for (CsCTipoIntervento tipo : beanlst){ 
						if(!mappaIAperti.containsKey(tipo.getDescrizione()))
							lstTipoIntervento.add(new SelectItem(tipo.getId(), tipo.getDescrizione()));
					}
				}
			}else{
			  logger.warn("Impossibile attivare interventi per il soggetto: settore o categoria sociale non specificata.");
		  }
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		return lstTipoIntervento;
	}
	
	
	public DatiInterventoBean getDatiInterventoBean() {
		return datiInterventoBean;
	}

	public void setDatiInterventoBean(DatiInterventoBean datiInterventoBean) {
		this.datiInterventoBean = datiInterventoBean;
	}

	public Long getInterventoId() {
		return interventoId;
	}

	public void setInterventoId(Long interventoId) {
		this.interventoId = interventoId;
	}

	@Override
	public Long getDiarioId() {
		return diarioId;
	}

	public void setDiarioId(Long diarioId) {
		this.diarioId = diarioId;
	}
	
	public String getCodDialog() {
		return codDialog;
	}

	public void setCodDialog(String codDialog) {
		this.codDialog = codDialog;
	}

	public String getWidgetVar() {
		widgetVar = "fglAmmDialog";
		return widgetVar;
	}
	
	public CsASoggetto getSoggettoCorrente() {
		return soggettoCorrente;
	}

	public void setSoggettoCorrente(CsASoggetto soggettoCorrente) {
		this.soggettoCorrente = soggettoCorrente;
	}

	@Override
	public CsCCategoriaSociale getCatsocCorrente() {
		return catsocCorrente;
	}

	@Override
	public void setCatsocCorrente(CsCCategoriaSociale catsocCorrente) {
		this.catsocCorrente = catsocCorrente;
	}

	private List<SelectItem> lstTipoQuotaPasti;
	@Override
	public List<SelectItem> getLstTipoQuotaPasti() {
		if(lstTipoQuotaPasti == null) {
			lstTipoQuotaPasti = new ArrayList<SelectItem>();
			lstTipoQuotaPasti.add(new SelectItem("Nessuna", "Nessuna"));
			lstTipoQuotaPasti.add(new SelectItem("Totale", "Totale"));
			lstTipoQuotaPasti.add(new SelectItem("Parziale", "Parziale"));
		}
		return lstTipoQuotaPasti;
	}
	
	private List<SelectItem> lstTipoQuotaCentroD;
	@Override
	public List<SelectItem> getLstTipoQuotaCentroD() {
		if(lstTipoQuotaCentroD == null) {
			lstTipoQuotaCentroD = new ArrayList<SelectItem>();
			lstTipoQuotaCentroD.add(new SelectItem("Nessuna", "Nessuna"));
			lstTipoQuotaCentroD.add(new SelectItem("Totale", "Totale"));
			lstTipoQuotaCentroD.add(new SelectItem("Parziale", "Parziale"));
		}
		return lstTipoQuotaCentroD;
	}
	
	private List<SelectItem> lstTipoRiscossione;
	@Override
	public List<SelectItem> getLstTipoRiscossione() {
		if(lstTipoRiscossione == null) {
			lstTipoRiscossione = new ArrayList<SelectItem>();
			lstTipoRiscossione.add(new SelectItem("Riscossione diretta", "Riscossione diretta"));
			lstTipoRiscossione.add(new SelectItem("Delegato", "Delegato"));
			lstTipoRiscossione.add(new SelectItem("Accredito", "Accredito"));
		}
		return lstTipoRiscossione;
	}
	
	private List<SelectItem> lstTipoOreVoucher;
	@Override
	public List<SelectItem> getLstTipoOreVoucher() {
		if(lstTipoOreVoucher == null) {
			lstTipoOreVoucher = new ArrayList<SelectItem>();
			lstTipoOreVoucher.add(new SelectItem("Giornaliere", "Giornaliere"));
			lstTipoOreVoucher.add(new SelectItem("Settimanali", "Settimanali"));
			lstTipoOreVoucher.add(new SelectItem("Mensili", "Mensili"));
			lstTipoOreVoucher.add(new SelectItem("Annuali", "Annuali"));
		}
		return lstTipoOreVoucher;
	}
	
	private List<SelectItem> lstTipoPeriodoErogazione;
	@Override
	public List<SelectItem> getLstTipoPeriodoErogazione() {
		if(lstTipoPeriodoErogazione == null) {
			lstTipoPeriodoErogazione = new ArrayList<SelectItem>();
			lstTipoPeriodoErogazione.add(new SelectItem("Una tantum", "Una tantum"));
			lstTipoPeriodoErogazione.add(new SelectItem("Giornaliero", "Giornaliero"));
			lstTipoPeriodoErogazione.add(new SelectItem("Mensile", "Mensile"));
			lstTipoPeriodoErogazione.add(new SelectItem("Annuale", "Annuale"));
		}
		return lstTipoPeriodoErogazione;
	}
	
	private List<SelectItem> lstTipoAttivita;
	@Override
	public List<SelectItem> getLstTipoAttivita() {
		if(lstTipoAttivita == null) {
			lstTipoAttivita = new ArrayList<SelectItem>();
			lstTipoAttivita.add(new SelectItem(null, "-->"));
			lstTipoAttivita.add(new SelectItem("A", "Attivazione"));
			lstTipoAttivita.add(new SelectItem("S", "Sospensione"));
			lstTipoAttivita.add(new SelectItem("C", "Chiusura"));
		}
		return lstTipoAttivita;
	}

	private List<SelectItem> lstTipoSospensione;
	@Override
	public List<SelectItem> getLstTipoSospensione() {
		if(lstTipoSospensione == null) {
			lstTipoSospensione = new ArrayList<SelectItem>();
			lstTipoSospensione.add(new SelectItem(null, "-->"));
			lstTipoSospensione.add(new SelectItem("Temporanea", "Temporanea"));
			lstTipoSospensione.add(new SelectItem("Definitiva", "Definitiva"));
		}
		return lstTipoSospensione;
	}
	
	private List<SelectItem> lstTipoAttivazione;
	@Override
	public List<SelectItem> getLstTipoAttivazione() {
		lstTipoAttivazione = new ArrayList<SelectItem>();
		lstTipoAttivazione.add(new SelectItem(null, "-->"));
		lstTipoAttivazione.add(new SelectItem("Immediata", "Immediata"));
		lstTipoAttivazione.add(new SelectItem("Riattivazione", "Riattivazione"));
		lstTipoAttivazione.add(new SelectItem("Attivazione", "Attivazione"));
		return lstTipoAttivazione;
	}
	
	private List<SelectItem> lstModalitaIntervento;
	@Override
	public List<SelectItem> getLstModalitaIntervento() {
		if(lstModalitaIntervento == null) {
			lstModalitaIntervento = new ArrayList<SelectItem>();
			lstModalitaIntervento.add(new SelectItem("P", "Prima Erogazione"));
			lstModalitaIntervento.add(new SelectItem("R", "Rinnovo"));
		}
		return lstModalitaIntervento;
	}
	
	private List<SelectItem> lstTipoPeriodo;
	@Override
	public List<SelectItem> getLstTipoPeriodo() {
		if(lstTipoPeriodo == null) {
			lstTipoPeriodo = new ArrayList<SelectItem>();
			lstTipoPeriodo.add(new SelectItem("1", "Presunta"));
			lstTipoPeriodo.add(new SelectItem("2", "Certa"));
		}
		return lstTipoPeriodo;
	}

	private List<SelectItem> lstTipoGestione;
	@Override
	public List<SelectItem> getLstTipoGestione() {
		if(lstTipoGestione == null) {
			lstTipoGestione = new ArrayList<SelectItem>();
			lstTipoGestione.add(new SelectItem("Badante", "Badante"));
			lstTipoGestione.add(new SelectItem("Cooperativa", "Cooperativa"));
			lstTipoGestione.add(new SelectItem("Altro", "Altro"));
		}
		return lstTipoGestione;
	}

	private List<SelectItem> lstTipoDeroghe;
	@Override
	public List<SelectItem> getLstTipoDeroghe() {
		if(lstTipoDeroghe == null) {
			lstTipoDeroghe = new ArrayList<SelectItem>();
			lstTipoDeroghe.add(new SelectItem("Su Importo", "Su Importo"));
			lstTipoDeroghe.add(new SelectItem("Sulle Applicazioni", "Sulle Applicazioni"));
		}
		return lstTipoDeroghe;
	}

	private List<SelectItem> lstTipoRientriFam;
	@Override
	public List<SelectItem> getLstTipoRientriFam() {
		if(lstTipoRientriFam == null) {
			lstTipoRientriFam = new ArrayList<SelectItem>();
			try {
				CeTBaseObject cet = new CeTBaseObject();
				fillEnte(cet);
				List<CsTbTipoRientriFami> beanLst = confService.getTipiRientriFami(cet);
				if (beanLst != null) {
					for (CsTbTipoRientriFami s : beanLst) {
						lstTipoRientriFam.add(new SelectItem(s.getId(), s.getDescrizione()));
					}
				}
			} catch (Exception e) {
				addErrorFromProperties("caricamento.error");
				logger.error(e.getMessage(),e);
			}
		}
		return lstTipoRientriFam;
	}

	private List<SelectItem> lstTipoRetta;
	@Override
	public List<SelectItem> getLstTipoRetta() {
		if(lstTipoRetta == null) {
			lstTipoRetta = new ArrayList<SelectItem>();
			try {
				CeTBaseObject cet = new CeTBaseObject();
				fillEnte(cet);
				List<CsTbTipoRetta> beanLst = confService.getTipiRetta(cet);
				if (beanLst != null) {
					for (CsTbTipoRetta s : beanLst) {
						lstTipoRetta.add(new SelectItem(s.getId(), s.getDescrizione()));
					}
				}
			} catch (Exception e) {
				addErrorFromProperties("caricamento.error");
				logger.error(e.getMessage(),e);
			}
		}
		return lstTipoRetta;
	}

	
	@Override
	public List<SelectItem> getLstRelazioni() {
		List<SelectItem> lst = new ArrayList<SelectItem>();
		lst.add(new SelectItem(null, "--> scegli"));
		try {
			InterventoDTO i = new InterventoDTO();
			fillEnte(i);
			i.setCasoId(this.idCaso);
			i.setIdTipoIntervento(this.datiInterventoBean.getTipoIntervento());
			List<CsDRelazione> beanLst = diarioService.findRelazioniByCasoTipoIntervento(i);
			if (beanLst != null) {
				for (CsDRelazione s : beanLst) {
					String descrizione = SDF.format(s.getDataProposta())+" - "+s.getProposta();
					lst.add(new SelectItem(s.getDiarioId(), descrizione));
				}
			}
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		return lst;
	}

	private List<SelectItem> lstTipoAffido;
	@Override
	public List<SelectItem> getLstTipoAffido() {
		if(lstTipoAffido == null) {
			lstTipoAffido = new ArrayList<SelectItem>();
			lstTipoAffido.add(new SelectItem("ETERO_FAMILIARE", "Etero Familiare"));
			lstTipoAffido.add(new SelectItem("PARENTI", "A parenti"));
			lstTipoAffido.add(new SelectItem("DIURNO_MESE", "Diurno Mese"));
			lstTipoAffido.add(new SelectItem("DIURNO_GIORNALIERO", "Giornaliero diurno"));
		}
		return lstTipoAffido;
	}
	
	private List<SelectItem> lstSpeseExtra;
	@Override
	public List<SelectItem> getLstSpeseExtra() {
		if(lstSpeseExtra == null) {
			lstSpeseExtra = new ArrayList<SelectItem>();
			lstSpeseExtra.add(new SelectItem("SANITARIE", "Spese Sanitarie"));
			lstSpeseExtra.add(new SelectItem("VACANZE", "Spese vacanze"));
			lstSpeseExtra.add(new SelectItem("INCONTRI", "Incontri Protetti"));
			lstSpeseExtra.add(new SelectItem("INT_EDU", "Intervento Educativo"));
			lstSpeseExtra.add(new SelectItem("TESTI", "Rimborso testi scuola"));
			lstSpeseExtra.add(new SelectItem("ALTRO", "Altro"));
		}
		return lstSpeseExtra;
	}
	
	private List<SelectItem> lstSpeseExtraSRM;
	@Override
	public List<SelectItem> getLstSpeseExtraSRM() {
		if(lstTipoQuotaCentroD == null) {
			lstSpeseExtraSRM = new ArrayList<SelectItem>();
			lstSpeseExtraSRM.add(new SelectItem("PERNOTTAMENTO", "Pernottamento"));
			lstSpeseExtraSRM.add(new SelectItem("CENE", "Cene"));
			lstSpeseExtraSRM.add(new SelectItem("INT_EDU", "Intervento Educativo"));
			lstSpeseExtraSRM.add(new SelectItem("ALTRO", "Altro"));
		}
		return lstSpeseExtraSRM;
	}

	private List<SelectItem> lstTipoAdmAdh;
	@Override
	public List<SelectItem> getLstTipoAdmAdh() {
		if(lstTipoAdmAdh == null) {
			lstTipoAdmAdh = new ArrayList<SelectItem>();
			lstTipoAdmAdh.add(new SelectItem("ADM", "ADM"));
			lstTipoAdmAdh.add(new SelectItem("ADH", "ADH"));
			lstTipoAdmAdh.add(new SelectItem("POLO", "POLO"));
		}
		return lstTipoAdmAdh;
	}
	
	private List<SelectItem> lstEducatori;
	public List<SelectItem> getLstEducatori() {
		if(lstEducatori == null) {
			//Filtro per tipo = Educatore UOL e Organizzazione = utente loggato
			lstEducatori = new ArrayList<SelectItem>();
			Map<Long, Long> mapEducatori = new HashMap<Long, Long>();
			
			try{
				CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
				CsOOrganizzazione org = opSettore.getCsOSettore().getCsOOrganizzazione();
				
				BaseDTO dto = new BaseDTO();
				fillEnte(dto);
				dto.setObj(DataModelCostanti.Operatori.EDUCATORE_UOL_ID);
				List<CsOOperatoreTipoOperatore> lista = operatoreService.getOperatoriByTipoId(dto);
				for(CsOOperatoreTipoOperatore cs: lista) {
					CsOOperatoreSettore opSett = cs.getCsOOperatoreSettore();
					if(opSett.getCsOSettore().getCsOOrganizzazione().getId().equals(org.getId())
							&& !mapEducatori.containsKey(opSett.getCsOOperatore().getId())) {
						AmAnagrafica amAna = amAnagraficaService.findAnagraficaByUserName(opSett.getCsOOperatore().getUsername());
						if(amAna != null) {
							mapEducatori.put(opSett.getCsOOperatore().getId(), opSett.getCsOOperatore().getId());
							lstEducatori.add(new SelectItem(opSett.getCsOOperatore().getId(), amAna.getCognome() + " " + amAna.getNome()));
						}
					}
				}
			} catch (Exception e) {
				addErrorFromProperties("caricamento.error");
				logger.error(e.getMessage(),e);
			}
		}
		
		return lstEducatori;
	}
	
	private List<SelectItem> lstTipoCirc4;
	public List<SelectItem> getLstTipoCirc4() {
		if(lstTipoCirc4 == null) {
			lstTipoCirc4 = new ArrayList<SelectItem>();
			
			CeTBaseObject cet = new CeTBaseObject();
			fillEnte(cet);
			List<CsTbTipoCirc4> lista = confService.getTipiCirc4(cet);
			for(CsTbTipoCirc4 cs: lista) {
				lstTipoCirc4.add(new SelectItem(cs.getId(), cs.getDescrizione()));
			}
		}
		return lstTipoCirc4;
	}
	
	private List<SelectItem> lstTipoInterventi;
	public List<SelectItem> getLstTipoInterventi() {
		if(lstTipoInterventi == null) {
			lstTipoInterventi = new ArrayList<SelectItem>();
			
			CeTBaseObject cet = new CeTBaseObject();
			fillEnte(cet);
			List<CsTbInterventiUOL> lista = confService.getinterventiUOL(cet);
			for(CsTbInterventiUOL cs: lista) {
				lstTipoInterventi.add(new SelectItem(cs.getId(), cs.getDescrizione()));
			}
		}
		return lstTipoInterventi;
	}
	
	private List<SelectItem> lstTipoProgetto;
	public List<SelectItem> getLstTipoProgetto() {
		if(lstTipoProgetto == null) {
			lstTipoProgetto = new ArrayList<SelectItem>();
				
			CeTBaseObject cet = new CeTBaseObject();
			fillEnte(cet);
			List<CsTbTipoProgetto> lista = confService.getTipiProgetto(cet);
			for(CsTbTipoProgetto cs: lista) {
				lstTipoProgetto.add(new SelectItem(cs.getId(), cs.getDescrizione()));
			}
		}
		return lstTipoProgetto;
	}
	
	private List<SelectItem> lstTipoContributo;
	private List<CsTbTipoContributo> lstTipoContributoTooltip;
	public List<SelectItem> getLstTipoContributo() {
		if(lstTipoContributo == null) {
			lstTipoContributo = new ArrayList<SelectItem>();
				
			CeTBaseObject cet = new CeTBaseObject();
			fillEnte(cet);
			lstTipoContributoTooltip = confService.getTipoContributo(cet);
			for(CsTbTipoContributo cs: lstTipoContributoTooltip) {
				lstTipoContributo.add(new SelectItem(cs.getId(), cs.getDescrizione()));
			}
		}
		return lstTipoContributo;
	}
	
	public List<CsTbTipoContributo> getLstTipoContributoTooltip() {
		if(lstTipoContributoTooltip == null) {
			getLstTipoContributo();
		}
		return lstTipoContributoTooltip;
	}
}
