package it.webred.cs.csa.web.manbean.scheda.anagrafica;

import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableCatSocialeSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableIterStepSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.web.manbean.scheda.SchedaBean;
import it.webred.cs.csa.web.manbean.scheda.invalidita.DatiInvaliditaComp;
import it.webred.cs.csa.web.manbean.scheda.sociali.DatiSocialiComp;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsASoggettoCategoriaSoc;
import it.webred.cs.data.model.CsASoggettoCategoriaSocPK;
import it.webred.cs.data.model.CsCCategoriaSociale;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.data.model.CsRelSettoreCatsoc;
import it.webred.cs.jsf.bean.ValiditaCompBaseBean;
import it.webred.cs.jsf.interfaces.ISoggCatSociale;
import it.webred.cs.jsf.manbean.DatiValGestioneMan;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.dto.utility.KeyValuePairBean;
import it.webred.ejb.utility.ClientUtility;
import it.webred.ss.data.model.SsScheda;
import it.webred.ss.data.model.SsSchedaSegnalato;
import it.webred.ss.ejb.client.SsSchedaSessionBeanRemote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.NamingException;

import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class SoggCatSocialeBean extends DatiValGestioneMan implements ISoggCatSociale {
	
	private CsASoggetto soggetto;
	private SsScheda schedaSegr;
	private String ufficioSchedaSegr;
	private String modalWidgetVar = "wdgCasoCatSocModal";
	private boolean disableEsci;
	private boolean readOnly;
	
	private String settTo;
	private Long settIdTo;
	private Long orgIdTo;
		
	@Override
	public void carica(CsASoggetto soggetto) {
		if(soggetto != null) {
			this.soggetto = soggetto;
			itemSelezionato = null;
			maxActiveComponents = 10;
			
			try {
				List<ValiditaCompBaseBean> lstCatSociali = new ArrayList<ValiditaCompBaseBean>();
				
				AccessTableSoggettoSessionBeanRemote soggettoService = (AccessTableSoggettoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableSoggettoSessionBean");
				BaseDTO dto = new BaseDTO();
				fillEnte(dto);
				dto.setObj(soggetto.getAnagraficaId());
				List<CsASoggettoCategoriaSoc> lista = soggettoService.getSoggettoCategorieBySoggetto(dto);
				
				for(CsASoggettoCategoriaSoc csSoggCatSoc: lista) {
					ValiditaCompBaseBean comp = new ValiditaCompBaseBean();
					comp.setDataFine(csSoggCatSoc.getId().getDataFineApp());
					comp.setDataInizio(csSoggCatSoc.getDataInizioApp());
					comp.setDescrizione(csSoggCatSoc.getCsCCategoriaSociale().getTooltip());
					comp.setId(new Long(csSoggCatSoc.getId().getCategoriaSocialeId()));
					lstCatSociali.add(comp);
				}
				setLstComponents(lstCatSociali);
				setLstComponentsActive(getActiveList());
			
				gestisci();
				
				/* Gestione Read Only
				*   il caso è in modifica se:
				*	ho il permesso modifica tutti i casi settore
				*	se il caso non ha responsabile e l'operatore è il creatore del caso
				*	se sono presente nella lista degli operatori per quel caso
				*/
				readOnly = true;
				CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
				AccessTableCasoSessionBeanRemote casoService = (AccessTableCasoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableCasoSessionBean");
				dto.setObj(soggetto.getCsACaso().getId());
				List<CsACasoOpeTipoOpe> listaCasoOpeTipoOpe = casoService.getListaOperatoreTipoOpByCasoId(dto);
				boolean isCasoOperatore = false;
				boolean respExist = false;
				for(CsACasoOpeTipoOpe casoOpeTipoOpe: listaCasoOpeTipoOpe) {
					if(casoOpeTipoOpe.getId().getDataFineApp().after(new Date())) {
						if(casoOpeTipoOpe.getFlagResponsabile() != null && casoOpeTipoOpe.getFlagResponsabile().booleanValue())
							respExist = true;
						if(casoOpeTipoOpe.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore().getId() == opSettore.getId())
							isCasoOperatore = true;
					}
				}
				
				if(checkPermesso(DataModelCostanti.PermessiCaso.MODIFICA_CASI_SETTORE) 
						|| (!respExist && soggetto.getCsACaso().getUserIns().equals(opSettore.getCsOOperatore().getUsername())
						|| isCasoOperatore))
					readOnly = false;
				
				//carico ufficio scheda segr
				ufficioSchedaSegr = null;
				if(schedaSegr != null && schedaSegr.getAccesso().getSede() != null)
					ufficioSchedaSegr = schedaSegr.getAccesso().getSede().getNome();
				
				RequestContext.getCurrentInstance().execute(modalWidgetVar + ".show()");
			} catch (NamingException e) {
				addErrorFromProperties("caricamento.error");
				logger.error(e.getMessage(),e);
			}
		} else addWarningFromProperties("seleziona.warning");
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<KeyValuePairBean> getLstItems() {
		
		lstItems = new ArrayList<KeyValuePairBean>();
		try {
			CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
			AccessTableCatSocialeSessionBeanRemote bean = (AccessTableCatSocialeSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableCatSocialeSessionBean");
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(opSettore.getCsOSettore().getId());
			List<CsRelSettoreCatsoc> beanLstCatSociali = bean.findRelSettoreCatsocBySettore(dto);
			if (beanLstCatSociali != null) {
				for (CsRelSettoreCatsoc cat : beanLstCatSociali) {
					lstItems.add(new KeyValuePairBean(cat.getId().getCategoriaSocialeId(), cat.getCsCCategoriaSociale().getTooltip()));
				}
			}
		} catch (NamingException e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		
		return lstItems;
	}
	
	@Override
	public void salva() {
		
		try {
			itemSelezionato = null;
			List<ValiditaCompBaseBean> lstTempActive = getActiveList();
			
			boolean ok = true;
			if(lstTempActive.size() > maxActiveComponents) {
				addError("Superato il numero di elementi attivi, massimo: " + maxActiveComponents, null);
				ok = false;
			}
			
			if(settTo == null || "".equals(settTo)) {
				addWarning("E' necessaria almeno una categoria attiva", "");
				ok = false;
			}
			
			if(ok) {
				lstComponentsActive = lstTempActive;
			
				AccessTableSoggettoSessionBeanRemote soggettoService = (AccessTableSoggettoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableSoggettoSessionBean");
				AccessTableIterStepSessionBeanRemote iterService = (AccessTableIterStepSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableIterStepSessionBean");

				BaseDTO dto = new BaseDTO();
				fillEnte(dto);	
				if(soggetto != null) {
					dto.setObj(soggetto.getAnagraficaId());
					soggettoService.eliminaSoggettoCategorieBySoggetto(dto);
				}
				for(ValiditaCompBaseBean comp: getLstComponents()) {
					CsASoggettoCategoriaSoc cs = new CsASoggettoCategoriaSoc();
					CsASoggettoCategoriaSocPK csPK = new CsASoggettoCategoriaSocPK();
					cs.setCsASoggetto(soggetto);
					cs.setDataInizioApp(comp.getDataInizio());
					cs.setDataInizioSys(new Date());
					cs.setDtIns(new Date());
					cs.setUserIns(dto.getUserId());
					csPK.setCategoriaSocialeId(comp.getId());
					csPK.setDataFineApp(comp.getDataFine());
					cs.setId(csPK);
					
					dto.setObj(cs);
					soggettoService.saveSoggettoCategoria(dto);					
				}
				
				//segnalo al settore
				CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
				IterDTO itDto = new IterDTO();
				fillEnte(itDto);
				itDto.setIdCaso(soggetto.getCsACaso().getId());
				itDto.setIdStato(DataModelCostanti.IterStatoInfo.SEGNALATO);
				itDto.setNomeOperatore(itDto.getUserId());
				itDto.setIdSettore(opSettore.getCsOSettore().getId());
				itDto.setIdOpTo(null);
				itDto.setIdSettTo(settIdTo);
				itDto.setIdOrgTo(orgIdTo);
				itDto.setNota(null);
				itDto.setAttrNewStato(null);
				itDto.setAlertUrl(null);
				itDto.setNotificaSettoreSegnalante(true);
				boolean bSaved = iterService.addIterStep(itDto);
				
				addInfoFromProperties("salva.ok");
				
				//carico gli altri dati provenienenti da scheda segretariato
				if(soggetto != null && schedaSegr != null) {	
					SchedaBean schedaBean = (SchedaBean) getSession().getAttribute("schedaBean");
					SsSchedaSessionBeanRemote ssSchedaSegrService = (SsSchedaSessionBeanRemote) getEjb("SegretariatoSoc", "SegretariatoSoc_EJB", "SsSchedaSessionBean");
					it.webred.ss.ejb.dto.BaseDTO bDto = new it.webred.ss.ejb.dto.BaseDTO();
					fillEnte(bDto);
					bDto.setObj(schedaSegr.getSegnalato());
		    		SsSchedaSegnalato segnalato = ssSchedaSegrService.readSegnalatoById(bDto);
					
					//tipo familiare
					if(segnalato.getTipologia_familiare() != null) {
						try {
							BigDecimal tipoFam = new BigDecimal(segnalato.getTipologia_familiare());
							schedaBean.getDatiSocialiBean().setListaComponenti(new ArrayList());
							schedaBean.getDatiSocialiBean().setSoggettoId(soggetto.getAnagraficaId());
							schedaBean.getDatiSocialiBean().nuovo();
							DatiSocialiComp comp = (DatiSocialiComp) schedaBean.getDatiSocialiBean().getListaComponenti().get(0);
							comp.setIdTipologiaFam(tipoFam);
							addWarning("Dati aggiuntivi da Scheda Segretariato in Dati Sociali", "Salvare per confermare");
						} catch (Exception e) {
							logger.error("SOGGETTO SCHEDA SEGR: " + soggetto.getCsAAnagrafica().getCf() + " dato tipo tipologia familiare non valido, necessario ID");
						}
					}
					
					//invalidita
					if(segnalato.getInvalido() != null) {
						schedaBean.getDatiInvaliditaBean().setListaComponenti(new ArrayList());
						schedaBean.getDatiInvaliditaBean().setSoggettoId(soggetto.getAnagraficaId());
						schedaBean.getDatiInvaliditaBean().nuovo();
						DatiInvaliditaComp comp = (DatiInvaliditaComp) schedaBean.getDatiInvaliditaBean().getListaComponenti().get(0);
						comp.setInvaliditaInCorso(segnalato.getInvalido());
						if(segnalato.getPercentuale() != null)
							comp.setInvaliditaPerc(new BigDecimal(segnalato.getPercentuale()));
						addWarning("Dati aggiuntivi da Scheda Segretariato in Invalidità", "Salvare per confermare");
					}
					
					//refresh dei tab per renderizzare i dati caricati  
					RequestContext.getCurrentInstance().update(schedaBean.getIdTabScheda());
				}
			}
			
			RequestContext.getCurrentInstance().addCallbackParam("saved", ok);
			
		} catch (Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
	}

	@Override
	public String getModalWidgetVar() {
		return modalWidgetVar;
	}

	@Override
	public boolean isDisableEsci() {
		return disableEsci;
	}

	public void setDisableEsci(boolean disableEsci) {
		this.disableEsci = disableEsci;
	}

	public String getSettTo() {
		settTo = null;
		settIdTo = null;
		orgIdTo = null;
		
		try {
			AccessTableCatSocialeSessionBeanRemote catSocService = (AccessTableCatSocialeSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableCatSocialeSessionBean");
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			for (ValiditaCompBaseBean comp: getActiveList()) {
				dto.setObj(comp.getId());
				CsCCategoriaSociale catSociale = catSocService.getCategoriaSocialeById(dto);
				if(catSociale.getCsRelSettoreCatsocs() != null && !catSociale.getCsRelSettoreCatsocs().isEmpty()) {
					List<CsRelSettoreCatsoc> list = new ArrayList<CsRelSettoreCatsoc>(catSociale.getCsRelSettoreCatsocs());
					CsOSettore settore = list.get(0).getCsOSettore();
					settTo = settore.getNome();
					settIdTo = settore.getId();
					orgIdTo = settore.getCsOOrganizzazione().getId();
					return settTo;
				}
			}
		} catch (Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
		
		return "";
	}

	public boolean isReadOnly() {		
		return readOnly;
	}

	public SsScheda getSchedaSegr() {
		return schedaSegr;
	}

	public void setSchedaSegr(SsScheda schedaSegr) {
		this.schedaSegr = schedaSegr;
	}

	public String getUfficioSchedaSegr() {
		return ufficioSchedaSegr;
	}

	public void setUfficioSchedaSegr(String ufficioSchedaSegr) {
		this.ufficioSchedaSegr = ufficioSchedaSegr;
	}
	
}
