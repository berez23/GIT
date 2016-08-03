package it.webred.cs.csa.web.manbean.scheda.anagrafica;

import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableIndirizzoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableIterStepSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableNazioniSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableParentiGitSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSchedaSegrSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.web.manbean.scheda.SchedaBean;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsAAnagrafica;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsAIndirizzo;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsASoggettoMedico;
import it.webred.cs.data.model.CsASoggettoMedicoPK;
import it.webred.cs.data.model.CsASoggettoStatoCivile;
import it.webred.cs.data.model.CsASoggettoStatoCivilePK;
import it.webred.cs.data.model.CsASoggettoStatus;
import it.webred.cs.data.model.CsASoggettoStatusPK;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsSsSchedaSegr;
import it.webred.cs.jsf.bean.DatiAnaBean;
import it.webred.cs.jsf.bean.ValiditaCompBaseBean;
import it.webred.cs.jsf.interfaces.IDatiAna;
import it.webred.cs.jsf.manbean.ComuneNazioneNascitaMan;
import it.webred.cs.jsf.manbean.ResidenzaCsaMan;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ct.config.luoghi.LuoghiService;
import it.webred.ct.config.model.AmTabNazioni;
import it.webred.ejb.utility.ClientUtility;
import it.webred.jsf.bean.ComuneBean;
import it.webred.ss.data.model.SsScheda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;

import org.primefaces.context.RequestContext;


@ManagedBean
@SessionScoped
public class AnagraficaBean extends CsUiCompBaseBean implements IDatiAna {
	
	protected AccessTableSoggettoSessionBeanRemote soggettoService = (AccessTableSoggettoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableSoggettoSessionBean");
	
	protected AccessTableCasoSessionBeanRemote casoService = (AccessTableCasoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableCasoSessionBean");

	protected AccessTableIndirizzoSessionBeanRemote indirizzoService = (AccessTableIndirizzoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableIndirizzoSessionBean");

	protected AccessTableIterStepSessionBeanRemote iterStepService = (AccessTableIterStepSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableIterStepSessionBean");
	
	protected AccessTableParentiGitSessionBeanRemote parentiService = (AccessTableParentiGitSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableParentiGitSessionBean");

	protected LuoghiService luoghiService = (LuoghiService) getEjb("CT_Service", "CT_Config_Manager", "LuoghiServiceBean");
	
	protected Long soggettoId = (Long) getSession().getAttribute("soggettoId");
	
	private DatiAnaBean datiAnaBean;
	
	private ComuneNazioneNascitaMan comuneNazioneNascitaMan;

	private ResidenzaCsaMan residenzaCsaMan;
	
	private MediciGestBean mediciGestBean;
	
	private StatusGestBean statusGestBean;
	
	private StatoCivileGestBean statoCivileGestBean;
	
	private SoggCatSocialeBean soggCatSocialeBean;
	
	public void initialize(Long sId) {
		
		soggettoId = sId;
		
		BaseDTO dto = new BaseDTO();
		fillEnte(dto);
		dto.setObj(soggettoId);

		datiAnaBean = new DatiAnaBean();
		comuneNazioneNascitaMan = new ComuneNazioneNascitaMan();
		residenzaCsaMan = new ResidenzaCsaMan();
		mediciGestBean = new MediciGestBean();
		statusGestBean = new StatusGestBean();
		statoCivileGestBean = new StatoCivileGestBean();
		if(soggCatSocialeBean == null)
			soggCatSocialeBean = new SoggCatSocialeBean();
		
		if(soggettoId != null){
			
			//carico dati anagrafici
			CsASoggetto csSogg = soggettoService.getSoggettoById(dto);
			CsAAnagrafica csAna = csSogg.getCsAAnagrafica();
			datiAnaBean.setCognome(csAna.getCognome());
			datiAnaBean.setNome(csAna.getNome());
			datiAnaBean.setDataNascita(csAna.getDataNascita());
			datiAnaBean.setCodiceFiscale(csAna.getCf());
			if(csAna.getComuneNascitaCod() != null) {
				ComuneBean comuneBean = new ComuneBean(csAna.getComuneNascitaCod(), csAna.getComuneNascitaDes(), csAna.getProvNascitaCod());
				comuneNazioneNascitaMan.getComuneNascitaMan().setComune(comuneBean);
			}
			if(csAna.getStatoNascitaCod() != null) {
				AmTabNazioni nazione = luoghiService.getNazioneBySigla(csAna.getStatoNascitaCod());
				comuneNazioneNascitaMan.getNazioneNascitaMan().setNazione(nazione);
				comuneNazioneNascitaMan.setValue(comuneNazioneNascitaMan.getNazioneValue());
			}
			datiAnaBean.setSesso(csAna.getSesso());
			datiAnaBean.setCittadinanza(csAna.getCittadinanza());
			datiAnaBean.setTelefono(csAna.getTel());
			datiAnaBean.setCellulare(csAna.getCell());
			datiAnaBean.seteMail(csAna.getEmail());
			
			//carico indirizzi
			List<CsAIndirizzo> listaIndirizzi = indirizzoService.getIndirizziBySoggetto(dto);
			residenzaCsaMan.setLstIndirizziOld(listaIndirizzi);
			residenzaCsaMan.setLstIndirizzi(residenzaCsaMan.resetListeIndirizzi(listaIndirizzi));
			
			//carico medici
			List<ValiditaCompBaseBean> lstMedici = new ArrayList<ValiditaCompBaseBean>();
			List<CsASoggettoMedico> lstSoggMedico = soggettoService.getSoggettoMedicoBySoggetto(dto);
			for(CsASoggettoMedico csSoggComp: lstSoggMedico) {
				ValiditaCompBaseBean comp = new ValiditaCompBaseBean();
				comp.setDataFine(csSoggComp.getId().getDataFineApp());
				comp.setDataInizio(csSoggComp.getDataInizioApp());
				String denominazione = (csSoggComp.getCsCMedico().getCognome() == null ? "" : csSoggComp.getCsCMedico().getCognome()).trim();
				String nome = (csSoggComp.getCsCMedico().getNome() == null ? "" : csSoggComp.getCsCMedico().getNome()).trim();
				if (!nome.equals("")) {
					if (!denominazione.equals("")) {
						denominazione += " ";
					}
					denominazione += nome;
				}	
				comp.setDescrizione(denominazione);
				comp.setId(csSoggComp.getId().getMedicoId());
				lstMedici.add(comp);
			}
			mediciGestBean.setLstComponents(lstMedici);		
			mediciGestBean.setLstComponentsActive(mediciGestBean.getActiveList());
			
			//carico status
			List<ValiditaCompBaseBean> lstStatus = new ArrayList<ValiditaCompBaseBean>();
			List<CsASoggettoStatus> lstSoggStatus = soggettoService.getSoggettoStatusBySoggetto(dto);
			for(CsASoggettoStatus csSoggComp: lstSoggStatus) {
				ValiditaCompBaseBean comp = new ValiditaCompBaseBean();
				comp.setDataFine(csSoggComp.getId().getDataFineApp());
				comp.setDataInizio(csSoggComp.getDataInizioApp());
				comp.setDescrizione(csSoggComp.getCsTbStatus().getDescrizione());
				comp.setId(csSoggComp.getId().getStatusId());
				lstStatus.add(comp);
			}
			statusGestBean.setLstComponents(lstStatus);
			statusGestBean.setLstComponentsActive(statusGestBean.getActiveList());
			
			//carico stato civile
			List<ValiditaCompBaseBean> lstStatoCivile = new ArrayList<ValiditaCompBaseBean>();
			List<CsASoggettoStatoCivile> lstSoggStatoCiv = soggettoService.getSoggettoStatoCivileBySoggetto(dto);
			for(CsASoggettoStatoCivile csSoggComp: lstSoggStatoCiv) {
				ValiditaCompBaseBean comp = new ValiditaCompBaseBean();
				comp.setDataFine(csSoggComp.getId().getDataFineApp());
				comp.setDataInizio(csSoggComp.getDataInizioApp());
				comp.setDescrizione(csSoggComp.getCsTbStatoCivile().getDescrizione());
				comp.setId(new Long(csSoggComp.getId().getStatoCivileCod()));
				lstStatoCivile.add(comp);
			}
			statoCivileGestBean.setLstComponents(lstStatoCivile);
			statoCivileGestBean.setLstComponentsActive(statoCivileGestBean.getActiveList());
		} else {
			//resetto la gestione soggetto/cat sociale solo per una nuova cartella
			soggCatSocialeBean.setDisableEsci(true);
		}
		
	}
	
	public void salva() {
		
		try{
			
			if(!validaAnagrafica())
				return;

			String username = getUser().getUsername();
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);	
			dto.setObj(soggettoId);
			
			//dati anagrafici
			CsASoggetto csSogg = new CsASoggetto();
			if(soggettoId == null) {
				IterDTO itDto = new IterDTO();
				fillEnte(itDto);
				csSogg.setCsAAnagrafica(new CsAAnagrafica());
				csSogg.setCsACaso(casoService.newCaso(itDto));
			} else 
				csSogg = soggettoService.getSoggettoById(dto);
			CsAAnagrafica csAna = csSogg.getCsAAnagrafica();
			CsACaso csCaso = csSogg.getCsACaso();
			
			csAna.setNome(datiAnaBean.getNome());
			csAna.setCognome(datiAnaBean.getCognome());
			csAna.setDataNascita(datiAnaBean.getDataNascita());
			csAna.setCf(datiAnaBean.getCodiceFiscale());
			if(comuneNazioneNascitaMan.getComuneNascitaMan().getComune() != null) {
				csAna.setProvNascitaCod(comuneNazioneNascitaMan.getComuneNascitaMan().getComune().getSiglaProv());
				csAna.setComuneNascitaCod(comuneNazioneNascitaMan.getComuneNascitaMan().getComune().getCodIstatComune());
				csAna.setComuneNascitaDes(comuneNazioneNascitaMan.getComuneNascitaMan().getComune().getDenominazione());
			}
			if(comuneNazioneNascitaMan.getNazioneNascitaMan().getNazione() != null) {
				csAna.setStatoNascitaCod(comuneNazioneNascitaMan.getNazioneNascitaMan().getNazione().getCodNazioneAnagrafe());
				csAna.setStatoNascitaDes(comuneNazioneNascitaMan.getNazioneNascitaMan().getNazione().getNazione());
			}
			csAna.setSesso(datiAnaBean.getSesso());
			csAna.setCittadinanza(datiAnaBean.getCittadinanza());
			csAna.setTel(datiAnaBean.getTelefono());
			csAna.setCell(datiAnaBean.getCellulare());
			csAna.setEmail(datiAnaBean.geteMail());
			
			dto.setObj(csSogg);
			if(soggettoId == null) {
				csSogg.setDtIns(new Date());
				csSogg.setUserIns(username);
				csSogg = soggettoService.saveSoggetto(dto);
			}
			else {
				csSogg.setDtMod(new Date());
				csSogg.setUsrMod(username);
				soggettoService.updateSoggetto(dto); 
			}
			
			//iterstep
			if(soggettoId == null) {
				IterDTO itDto = new IterDTO();
				fillEnte(itDto);
				itDto.setNomeOperatore(username);			
				itDto.setIdCaso(csCaso.getId());
				CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
				itDto.setIdSettore(opSettore.getCsOSettore().getId());
				itDto.setAlertUrl("");
				itDto.setNotificaSettoreSegnalante(true);
				iterStepService.newIter(itDto);
			}
			
			//indirizzi
			if(soggettoId != null) {
				dto.setObj(soggettoId);
				indirizzoService.eliminaIndirizziBySoggetto(dto);
			}
			for(CsAIndirizzo csInd: residenzaCsaMan.getLstIndirizzi()) {
				
				dto.setObj(csInd);
				csInd.setAnaIndirizzoId(null);
				csInd.getCsAAnaIndirizzo().setId(null);
				csInd.setDtIns(new Date());
				csInd.setUserIns(username);
				csInd.setDataInizioSys(new Date());
				csInd.setCsASoggetto(csSogg);
				if(csInd.getDataFineApp() != null && !csInd.getDataFineApp().equals(DataModelCostanti.END_DATE))
					csInd.setDataFineSys(new Date());
				indirizzoService.saveIndirizzo(dto);	
			}
			
			//medici
			if(soggettoId != null) {
				dto.setObj(soggettoId);
				soggettoService.eliminaSoggettoMedicoBySoggetto(dto);
			}
			for(ValiditaCompBaseBean comp: mediciGestBean.getLstComponents()) {
				CsASoggettoMedico cs = new CsASoggettoMedico();
				CsASoggettoMedicoPK csPK = new CsASoggettoMedicoPK();
				cs.setCsASoggetto(csSogg);
				cs.setDataInizioApp(comp.getDataInizio());
				cs.setDataInizioSys(new Date());
				cs.setDtIns(new Date());
				cs.setUserIns(username);
				csPK.setMedicoId(comp.getId());
				csPK.setDataFineApp(comp.getDataFine());
				csPK.setSoggettoAnagraficaId(csSogg.getAnagraficaId());
				cs.setId(csPK);
				
				dto.setObj(cs);
				soggettoService.saveSoggettoMedico(dto);
			}
			
			//status
			if(soggettoId != null) {
				dto.setObj(soggettoId);
				soggettoService.eliminaSoggettoStatusBySoggetto(dto);
			}
			for(ValiditaCompBaseBean comp: statusGestBean.getLstComponents()) {
				CsASoggettoStatus cs = new CsASoggettoStatus();
				CsASoggettoStatusPK csPK = new CsASoggettoStatusPK();
				cs.setCsASoggetto(csSogg);
				cs.setDataInizioApp(comp.getDataInizio());
				cs.setDataInizioSys(new Date());
				cs.setDtIns(new Date());
				cs.setUserIns(username);
				csPK.setStatusId(comp.getId());
				csPK.setDataFineApp(comp.getDataFine());
				csPK.setSoggettoAnagraficaId(csSogg.getAnagraficaId());
				cs.setId(csPK);
				
				dto.setObj(cs);
				soggettoService.saveSoggettoStatus(dto);
			}
			
			//stato civile
			if(soggettoId != null) {
				dto.setObj(soggettoId);
				soggettoService.eliminaSoggettoStatoCivileBySoggetto(dto);
			}
			for(ValiditaCompBaseBean comp: statoCivileGestBean.getLstComponents()) {
				CsASoggettoStatoCivile cs = new CsASoggettoStatoCivile();
				CsASoggettoStatoCivilePK csPK = new CsASoggettoStatoCivilePK();
				cs.setCsASoggetto(csSogg);
				cs.setDataInizioApp(comp.getDataInizio());
				cs.setDataInizioSys(new Date());
				cs.setDtIns(new Date());
				cs.setUserIns(username);
				
				csPK.setStatoCivileCod(comp.getId().toString());
				csPK.setDataFineApp(comp.getDataFine());
				csPK.setSoggettoAnagraficaId(csSogg.getAnagraficaId());
				cs.setId(csPK);
				
				dto.setObj(cs);
				soggettoService.saveSoggettoStatoCivile(dto);
			}
			
			SchedaBean schedaBean = (SchedaBean) getSession().getAttribute("schedaBean");
			
			//aggancio la scheda segretariato se sono partito da quella
			if(soggettoId == null && schedaBean.getSchedaSegrBean().getSsScheda() != null) {
				SsScheda ssScheda = schedaBean.getSchedaSegrBean().getSsScheda();
				soggCatSocialeBean.setSchedaSegr(ssScheda);
				
				AccessTableSchedaSegrSessionBeanRemote schedaSegrService = 
						(AccessTableSchedaSegrSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableSchedaSegrSessionBean");
				dto.setObj(ssScheda.getId());
				CsSsSchedaSegr csSsSchedaSegr = schedaSegrService.findSchedaSegrById(dto);
				csSsSchedaSegr.setCsASoggetto(csSogg);
				csSsSchedaSegr.setStato(DataModelCostanti.SchedaSegr.STATO_CREATA);
				csSsSchedaSegr.setDtMod(new Date());
				csSsSchedaSegr.setUsrMod(dto.getUserId());
				dto.setObj(csSsSchedaSegr);
				schedaSegrService.updateSchedaSegr(dto);
			}
			
			if(soggettoId == null) {
				//creo la famiglia GIT
				dto.setObj(csSogg);
				parentiService.createFamigliaGruppoGit(dto);
				
				//apro la gestione categoria sociale
				soggCatSocialeBean.carica(csSogg);
			}
			
			//riinizializzo la scheda
			schedaBean.initialize(csSogg);
			
			//refresh dei tab per farli renderizzare  
			RequestContext.getCurrentInstance().update(schedaBean.getIdTabScheda());
			
			addInfoFromProperties("salva.ok");
						
		} catch(Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	private boolean validaAnagrafica() {
		boolean ok = true;
		
		if(datiAnaBean.getCognome() == null || "".equals(datiAnaBean.getCognome().trim())) {
			addError("Cognome è un campo obbligatorio", "");
			ok = false;
		}
		
		if(datiAnaBean.getNome() == null || "".equals(datiAnaBean.getNome().trim())) {
			addError("Nome è un campo obbligatorio", "");
			ok = false;
		}
		
		if(datiAnaBean.getDataNascita() == null) {
			addError("Data di nascita è un campo obbligatorio", "");
			ok = false;
		}
		
		if(datiAnaBean.getSesso() == null || "".equals(datiAnaBean.getSesso().trim())) {
			addError("Sesso è un campo obbligatorio", "");
			ok = false;
		}
		
		if(datiAnaBean.getCodiceFiscale() == null || "".equals(datiAnaBean.getCodiceFiscale().trim())) {
			addError("Codice fiscale è un campo obbligatorio", "");
			ok = false;
		}
		
		if(datiAnaBean.getCittadinanza() == null || "".equals(datiAnaBean.getCittadinanza().trim())) {
			addError("Cittadinanza è un campo obbligatorio", "");
			ok = false;
		}
		
		if(comuneNazioneNascitaMan.getComuneNascitaMan().getComune() == null && comuneNazioneNascitaMan.getNazioneNascitaMan().getNazione() == null) {
			addError("Comune/Stato estero di nascita è un campo obbligatorio", "");
			ok = false;
		} 
		
		if(residenzaCsaMan.getIndirizzoResidenzaAttivo() == null || residenzaCsaMan.getIndirizzoResidenzaAttivo().getCsAAnaIndirizzo() == null) {
			addError("Residenza è un campo obbligatorio", "");
			ok = false;
		}
		
		if((datiAnaBean.getCellulare() == null || "".equals(datiAnaBean.getCellulare().trim())) && (datiAnaBean.getTelefono()==null || "".equals(datiAnaBean.getTelefono()))){
			addError("Inserire almeno un recapito telefonico","");
			ok = false;
		}
			
		
		return ok;
	}
	
	public DatiAnaBean getDatiAnaBean() {
		return datiAnaBean;
	}
	
	public void setDatiAnaBean(DatiAnaBean datiAnaBean) {
		this.datiAnaBean = datiAnaBean;
	}

	@Override
	public ComuneNazioneNascitaMan getComuneNazioneNascitaMan() {
		return comuneNazioneNascitaMan;
	}

	public void setComuneNazioneNascitaMan(ComuneNazioneNascitaMan comuneNazioneNascitaMan) {
		this.comuneNazioneNascitaMan = comuneNazioneNascitaMan;
	}

	public ResidenzaCsaMan getResidenzaCsaMan() {
		return residenzaCsaMan;
	}

	public void setResidenzaCsaMan(ResidenzaCsaMan residenzaCsaMan) {
		this.residenzaCsaMan = residenzaCsaMan;
	}

	
	@Override
	public ArrayList<SelectItem> getLstSessi() {
		ArrayList<SelectItem> lstSessi = new ArrayList<SelectItem>();
		lstSessi.add(new SelectItem("", "-->"));
		lstSessi.add(new SelectItem("M", "M"));
		lstSessi.add(new SelectItem("F", "F"));
		return lstSessi;
	}
	
	@Override
	public ArrayList<SelectItem> getLstCittadinanze() {
		ArrayList<SelectItem> lstCittadinanze = new ArrayList<SelectItem>();
		lstCittadinanze.add(new SelectItem("", "--> scegli"));
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
		return lstCittadinanze;
	}

	@Override
	public Long getSoggettoId() {
		return soggettoId;
	}

	public void setSoggettoId(Long soggettoId) {
		this.soggettoId = soggettoId;
	}

	public MediciGestBean getMediciGestBean() {
		return mediciGestBean;
	}

	public void setMediciGestBean(MediciGestBean mediciGestBean) {
		this.mediciGestBean = mediciGestBean;
	}

	public StatusGestBean getStatusGestBean() {
		return statusGestBean;
	}

	public void setStatusGestBean(StatusGestBean statusGestBean) {
		this.statusGestBean = statusGestBean;
	}

	public StatoCivileGestBean getStatoCivileGestBean() {
		return statoCivileGestBean;
	}

	public void setStatoCivileGestBean(StatoCivileGestBean statoCivileGestBean) {
		this.statoCivileGestBean = statoCivileGestBean;
	}

	public SoggCatSocialeBean getSoggCatSocialeBean() {
		return soggCatSocialeBean;
	}

	public void setSoggCatSocialeBean(SoggCatSocialeBean soggCatSocialeBean) {
		this.soggCatSocialeBean = soggCatSocialeBean;
	}
	
}
