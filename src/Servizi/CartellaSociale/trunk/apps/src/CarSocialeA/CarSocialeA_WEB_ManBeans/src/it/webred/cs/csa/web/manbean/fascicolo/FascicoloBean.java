package it.webred.cs.csa.web.manbean.fascicolo;

import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableDiarioSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.RelSettCatsocEsclusivaDTO;
import it.webred.cs.csa.web.manbean.fascicolo.altri.AltriSoggettiBean;
import it.webred.cs.csa.web.manbean.fascicolo.colloquio.ColloquioBean;
import it.webred.cs.csa.web.manbean.fascicolo.docIndividuali.DocIndividualiBean;
import it.webred.cs.csa.web.manbean.fascicolo.interventi.InterventiBean;
import it.webred.cs.csa.web.manbean.fascicolo.isee.IseeBean;
import it.webred.cs.csa.web.manbean.fascicolo.presaincarico.PresaInCaricoBean;
import it.webred.cs.csa.web.manbean.fascicolo.relazioni.RelazioniBean;
import it.webred.cs.csa.web.manbean.fascicolo.schedaMultidimAnz.ManSchedaMultidimAnzBean;
import it.webred.cs.csa.web.manbean.fascicolo.schedeSegr.SchedeSegrBean;
import it.webred.cs.csa.web.manbean.fascicolo.scuola.DatiScuolaBean;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsCCategoriaSociale;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsRelSettCatsocEsclusiva;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ejb.utility.ClientUtility;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;


@ManagedBean
@SessionScoped
public class FascicoloBean extends CsUiCompBaseBean {
	
/*********** Start Generic ******************/	
	protected String username;
	protected CsASoggetto soggetto;
	protected CsOOperatoreSettore opSettore;
	protected boolean isResponsabile;
	
	protected String datiPresaCaricoName = "datiPresaCarico";
	protected String fogliAmmName = "fogliAmm";
	protected String colloquioName = "colloquio";
	protected String iseeName = "isee";
	protected String relazName = "relaz";
	protected String schedeSegrName = "schedeSegr";
	protected String docIndivName = "docIndiv";
	protected String schedaMultidimAnzName = "schedamultidimAnz";
	protected String datiScuolaName = "datiScuola";
	
	protected boolean renderVisualizzaFascicolo;
	protected boolean isDatiPresaCarico;
	protected boolean isFogliAmm;
	protected boolean isColloquio;
	protected boolean isIsee;
	protected boolean isRelaz;
	protected boolean isSchedeSegr;
	protected boolean isDocIndiv;
	protected boolean isSchedaMultidimAnz;
	protected boolean isDatiScuola;
	
	protected boolean isDatiPresaCaricoReadOnly;
	protected boolean isFogliAmmReadOnly;
	protected boolean isColloquioReadOnly;
	protected boolean isIseeReadOnly;
	protected boolean isRelazReadOnly;
	protected boolean isSchedeSegrReadOnly;
	protected boolean isDocIndivReadOnly;
	protected boolean isSchedaMultidimAnzReadOnly;
	protected boolean isDatiScuolaReadOnly;
	
	protected PresaInCaricoBean presaInCaricoBean;
	protected InterventiBean interventiBean;
	protected ColloquioBean colloquioBean;
	protected RelazioniBean relazioniBean;
	protected DocIndividualiBean docIndividualiBean;
	protected ManSchedaMultidimAnzBean schedaMultidimAnzBean;
	protected DatiScuolaBean datiScuolaBean;
	protected IseeBean iseeBean;
	protected SchedeSegrBean schedeSegrBean;
	
	protected boolean altroSoggetto;
	protected AltriSoggettiBean altriSoggettiBean;
	
	public void initializeFascicoloCartellaUtente(Object soggettoObj) {
		
		if(soggettoObj != null) {
			
			try {
				soggetto = (CsASoggetto) soggettoObj;
				getSession().setAttribute("soggetto", soggettoObj);
				
				
				opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
				
				caricaFascicolo();

				FacesContext.getCurrentInstance().getExternalContext().redirect("fascicoloCartellaUtente.faces");
			} catch (Exception e) {
				addError("Errore", "Errore durante il reindirizzamento al Fascicolo Cartella Utente");
			}
			
		} else 
			addWarningFromProperties("seleziona.warning");
		
	}
	
	private void caricaFascicolo() {
		
		try {
		
			resetTabPermission();
			
			//controllo responsabile
			isResponsabile = false;
			AccessTableCasoSessionBeanRemote casoSessionBean = getCasoSessioBean();
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(soggetto.getCsACaso().getId());
			CsACasoOpeTipoOpe coto = casoSessionBean.findResponsabile(dto);
			if(coto != null && coto.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore().getCsOOperatore().getUsername().equals(dto.getUserId()))
				isResponsabile = true;
			// se non esiste resp ma ho creato il caso
			if(coto == null && soggetto.getCsACaso().getUserIns().equals(dto.getUserId()))
				isResponsabile = true;
			
			initializePresaInCaricoTab(soggetto);
			initializeFogliAmmTab(soggetto);
			initializeColloquioTab(soggetto);
			initializeRelazioniTab(soggetto);
			initializeDocIndividualiTab(soggetto);
			initializeSchedaMultidimAnzTab(soggetto);
			initializeDatiScuolaTab(soggetto);
			initializeSchedeSegrTab(soggetto);
			initializeIseeTab(soggetto);
			
			initializeAltriSoggetti(soggetto);
			
			isDocIndivReadOnly = !checkPermesso(DataModelCostanti.PermessiDocIndividuali.UPLOAD_DOC_INDIVIDUALI);
			
			//TODO da gestire e inserire in checkTabPermission
			isIsee = true;
			isIseeReadOnly = false;
					
		}catch(Exception e){
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}
	

	public void onTabChange(TabChangeEvent tab) throws Exception {
		String tabName = tab.getTab().getId();

		String name = "";
		
		//se ho caricato un altro soggetto, al cambio tab inizializzo quello originale
		if (tabName.equals(datiPresaCaricoName + "Tab")) {
			name = datiPresaCaricoName;
			if(interventiBean.getCsASoggetto().getAnagraficaId() != soggetto.getAnagraficaId())
				initializePresaInCaricoTab(soggetto);
		}
		
		if (tabName.equals(fogliAmmName + "Tab")) {
			name = fogliAmmName;
			if(interventiBean.getCsASoggetto().getAnagraficaId() != soggetto.getAnagraficaId())
				initializeFogliAmmTab(soggetto);
		}
		
		if (tabName.equals(colloquioName + "Tab")) {
			name = colloquioName;
			if(colloquioBean.getCsASoggetto().getAnagraficaId() != soggetto.getAnagraficaId())
				initializeColloquioTab(soggetto);
		}
		
		if (tabName.equals(relazName + "Tab")) {
			name = relazName;
			if(relazioniBean.getCsASoggetto().getAnagraficaId() != soggetto.getAnagraficaId())
				initializeRelazioniTab(soggetto);
		}
		
		if (tabName.equals(docIndivName + "Tab")) {
			name = docIndivName;
			if(docIndividualiBean.getCsASoggetto().getAnagraficaId() != soggetto.getAnagraficaId())
				initializeDocIndividualiTab(soggetto);
		}
		
		if (tabName.equals(schedeSegrName + "Tab")) {
			name = schedeSegrName;
			if(schedeSegrBean.getCsASoggetto().getAnagraficaId() != soggetto.getAnagraficaId())
				initializeSchedeSegrTab(soggetto);
		}
		
		if(tabName.equals(schedaMultidimAnzName + "Tab")) {
			name = schedaMultidimAnzName;
			if(schedaMultidimAnzBean.getCsASoggetto().getAnagraficaId() != soggetto.getAnagraficaId())
				initializeSchedaMultidimAnzTab(soggetto);
		}
		
		if(tabName.equals(datiScuolaName + "Tab")) {
			name = datiScuolaName;
			if(datiScuolaBean.getCsASoggetto().getAnagraficaId() != soggetto.getAnagraficaId())
				initializeDatiScuolaTab(soggetto);
		}
		
		if(tabName.equals(iseeName + "Tab")) {
			name = iseeName;
			if(iseeBean.getCsASoggetto().getAnagraficaId() != soggetto.getAnagraficaId())
				initializeIseeTab(soggetto);
		}
		
		getAltriSoggettiBean().setLabelSoggetto(soggetto.getCsAAnagrafica().getCognome() + " " + soggetto.getCsAAnagrafica().getNome());
		String clientId = tab.getComponent().getClientId();
		RequestContext.getCurrentInstance().update(clientId + ":" + name + "Form");

	}
	
	public void initializePresaInCaricoTab(CsASoggetto s) throws Exception{
		presaInCaricoBean = new PresaInCaricoBean();
		presaInCaricoBean.initialize(s);
		isDatiPresaCarico = true;
		isDatiPresaCaricoReadOnly = false;
		presaInCaricoBean.setReadOnly(isDatiPresaCaricoReadOnly);
	}
	
	public void initializeFogliAmmTab(CsASoggetto s) throws Exception{
		interventiBean = new InterventiBean();
		interventiBean.initialize(s);
		checkTabPermission(DataModelCostanti.TipoDiario.FOGLIO_AMM_ID, s);
		interventiBean.setReadOnly(isFogliAmmReadOnly);
	}
	
	public void initializeColloquioTab(CsASoggetto s) throws Exception{
		colloquioBean = new ColloquioBean();
		colloquioBean.initialize(s);
		checkTabPermission(DataModelCostanti.TipoDiario.COLLOQUIO_ID, s);
		colloquioBean.setReadOnly(isColloquioReadOnly);
	}
	
	public void initializeRelazioniTab(CsASoggetto s) throws Exception{
		relazioniBean = new RelazioniBean();
		relazioniBean.initialize(s);
		checkTabPermission(DataModelCostanti.TipoDiario.RELAZIONE_ID, s);
		checkTabPermission(DataModelCostanti.TipoDiario.PAI_ID, s);
		relazioniBean.setReadOnly(isRelazReadOnly);
	}
	
	public void initializeDocIndividualiTab(CsASoggetto s) throws Exception{
		docIndividualiBean = new DocIndividualiBean();
		docIndividualiBean.initialize(s);
		checkTabPermission(DataModelCostanti.TipoDiario.DOC_INDIVIDUALE_ID, s);
		docIndividualiBean.setReadOnly(isDocIndivReadOnly);
	}
	
	public void initializeSchedaMultidimAnzTab(CsASoggetto s) throws Exception{
		schedaMultidimAnzBean = new ManSchedaMultidimAnzBean();
		schedaMultidimAnzBean.initialize(s);
		checkTabPermission(DataModelCostanti.TipoDiario.VALUTAZIONE_MDS_ID, s);
		schedaMultidimAnzBean.setReadOnly(isSchedaMultidimAnzReadOnly);
	}
	
	public void initializeIseeTab(CsASoggetto s) throws Exception{
		iseeBean = new IseeBean();
		iseeBean.initialize(s);
		checkTabPermission(DataModelCostanti.TipoDiario.ISEE_ID, s);
		iseeBean.setReadOnly(isIseeReadOnly);
	}
	
	public void initializeDatiScuolaTab(CsASoggetto s) throws Exception{
		datiScuolaBean = new DatiScuolaBean();
		datiScuolaBean.initialize(s);
		checkTabPermission(DataModelCostanti.TipoDiario.DATI_SCUOLA_ID, s);
		datiScuolaBean.setReadOnly(isDatiScuolaReadOnly);
	}
	
	public void initializeSchedeSegrTab(CsASoggetto s) throws Exception{
		schedeSegrBean = new SchedeSegrBean();
		schedeSegrBean.initialize(s);
		isSchedeSegr = true;
		isSchedeSegrReadOnly = false;
		schedeSegrBean.setReadOnly(isSchedeSegrReadOnly);
	}
	
	public void initializeAltriSoggetti(CsASoggetto s) {
		altriSoggettiBean = new AltriSoggettiBean();
		altriSoggettiBean.initialize(s);
	}
	
	private void resetTabPermission() {
		isFogliAmm = false;
		isColloquio = false;
		isRelaz = false;
		isDocIndiv = false;
		isSchedaMultidimAnz = false;
		isDatiScuola = false;
	}
	
	private void checkTabPermission(int tipoDiarioId, CsASoggetto sogg) {
		
		try {
			
			AccessTableDiarioSessionBeanRemote diarioSessionBean = getDiarioSessioBean();
			AccessTableSoggettoSessionBeanRemote soggSessionBean = getSoggettoSessioBean();
			
			BaseDTO dtoSogg = new BaseDTO();
			fillEnte(dtoSogg);
			dtoSogg.setObj(sogg.getAnagraficaId());
			CsCCategoriaSociale catsocCorrente = soggSessionBean.getCatSocAttualeBySoggetto(dtoSogg);
			
			RelSettCatsocEsclusivaDTO relDto = new RelSettCatsocEsclusivaDTO();
			fillEnte(relDto);
			relDto.setCategoriaSocialeId(catsocCorrente.getId());
			relDto.setSettoreId(opSettore.getCsOSettore().getId());
			relDto.setTipoDiarioId(new Long(tipoDiarioId));
			CsRelSettCatsocEsclusiva relEsclusivaIds = diarioSessionBean.findRelSettCatsocEsclusivaByIds(relDto);
			List<CsRelSettCatsocEsclusiva> listaRelEsclusivaTipoD = diarioSessionBean.findRelSettCatsocEsclusivaByTipoDiarioId(relDto);
			
			
			boolean funzionePresente = listaRelEsclusivaTipoD != null && !listaRelEsclusivaTipoD.isEmpty(); 
			boolean funzioneSettCatsocPresente = relEsclusivaIds != null;
			boolean canModifica = checkPermesso(DataModelCostanti.PermessiCaso.MODIFICA_CASI_SETTORE) || isResponsabile;
			boolean existsDatiStorici = false;
			
			//il Tab è visibile solo se non sono specificati permessi per il tipo di diario o sono già presenti dei dati
			//la modifica è abilitata solo se è presente il permesso composto da TipoDiario/CatSociale/Settore
			switch (tipoDiarioId) {
			case DataModelCostanti.TipoDiario.FOGLIO_AMM_ID:
				
				isFogliAmmReadOnly = true;
				existsDatiStorici = interventiBean.getListaInterventi() != null && !interventiBean.getListaInterventi().isEmpty();
				if(funzioneSettCatsocPresente || !funzionePresente) {
					isFogliAmm = true;
					if(canModifica)
						isFogliAmmReadOnly = false;
				} else if(funzionePresente && existsDatiStorici)
					isFogliAmm = true;

				break;
	
			case DataModelCostanti.TipoDiario.COLLOQUIO_ID:
				
				isColloquioReadOnly = true;
				existsDatiStorici = colloquioBean.getListaColloquios() != null && !colloquioBean.getListaColloquios().isEmpty();
				if(funzioneSettCatsocPresente || !funzionePresente) {
					isColloquio = true;
					if(canModifica)
						isColloquioReadOnly = false;
				} else if(funzionePresente && existsDatiStorici)
					isColloquio = true;
				
				break;
	
			case DataModelCostanti.TipoDiario.RELAZIONE_ID:
				
				isRelazReadOnly = true;
				existsDatiStorici = relazioniBean.getListaRelazioni() != null && !relazioniBean.getListaRelazioni().isEmpty();
				if(funzioneSettCatsocPresente || !funzionePresente) {
					isRelaz = true;
					if(canModifica)
						isRelazReadOnly = false;
				} else if(funzionePresente && existsDatiStorici)
					isRelaz = true;
				
				break;
				
			case DataModelCostanti.TipoDiario.PAI_ID:
				
				relazioniBean.setDisableNuovoPAI(true);
				if((funzioneSettCatsocPresente || !funzionePresente) && canModifica)
					relazioniBean.setDisableNuovoPAI(false);
				
				break;
	
			case DataModelCostanti.TipoDiario.DOC_INDIVIDUALE_ID:
				
				isDocIndivReadOnly = true;
				existsDatiStorici = (docIndividualiBean.getListaDocIndividualiPubblica() != null && !docIndividualiBean.getListaDocIndividualiPubblica().isEmpty()) ||
						(docIndividualiBean.getListaDocIndividualiPrivata() != null && !docIndividualiBean.getListaDocIndividualiPrivata().isEmpty()); 
				
				if(funzioneSettCatsocPresente || !funzionePresente) {
					isDocIndiv = true;
					if(canModifica)
						isDocIndivReadOnly = false;
				} else if(funzionePresente && existsDatiStorici)
					isDocIndiv = true;
				
				break;
	
			case DataModelCostanti.TipoDiario.VALUTAZIONE_MDS_ID:
				
				isSchedaMultidimAnzReadOnly = true;
				existsDatiStorici = schedaMultidimAnzBean.getListaSchedeMultidims() != null && !schedaMultidimAnzBean.getListaSchedeMultidims().isEmpty(); 
				if(funzioneSettCatsocPresente || !funzionePresente) {
					isSchedaMultidimAnz = true;
					if(canModifica)
						isSchedaMultidimAnzReadOnly = false;
				} else if(funzionePresente && existsDatiStorici)
					isSchedaMultidimAnz = true;
				
				break;
			
			case DataModelCostanti.TipoDiario.DATI_SCUOLA_ID:
				
				isDatiScuolaReadOnly = true;
				existsDatiStorici = (datiScuolaBean.getListaScuole() != null && !datiScuolaBean.getListaScuole().isEmpty()); 
				
				if(funzioneSettCatsocPresente || !funzionePresente) {
					isDatiScuola = true;
					if(canModifica)
						isDatiScuolaReadOnly = false;
				} else if(funzionePresente && existsDatiStorici)
					isDatiScuola = true;
				
				break;
		
			case DataModelCostanti.TipoDiario.ISEE_ID:
				
				isIseeReadOnly = true;
				existsDatiStorici = (iseeBean.getListaIsee() != null && !iseeBean.getListaIsee().isEmpty()); 
				
				if(funzioneSettCatsocPresente || !funzionePresente) {
					isIsee = true;
					if(canModifica)
						isIseeReadOnly = false;
				} else if(funzionePresente && existsDatiStorici)
					isIsee = true;
				
				break;
			}
			
		}catch(Exception e){
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}

	protected AccessTableDiarioSessionBeanRemote getDiarioSessioBean() throws NamingException {
		AccessTableDiarioSessionBeanRemote sessionBean = (AccessTableDiarioSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableDiarioSessionBean");
		return sessionBean;
	}
	
	protected AccessTableSoggettoSessionBeanRemote getSoggettoSessioBean() throws NamingException {
		AccessTableSoggettoSessionBeanRemote sessionBean = (AccessTableSoggettoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableSoggettoSessionBean");
		return sessionBean;
	}
	
	protected AccessTableCasoSessionBeanRemote getCasoSessioBean() throws NamingException {
		AccessTableCasoSessionBeanRemote sessionBean = (AccessTableCasoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableCasoSessionBean");
		return sessionBean;
	}

	protected AccessTableConfigurazioneSessionBeanRemote getConfigurazioneSessioBean() throws NamingException {
		AccessTableConfigurazioneSessionBeanRemote sessionBean = (AccessTableConfigurazioneSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableConfigurazioneSessionBean");
		return sessionBean;
	}
	
	public String getUsername() {
		return username;
	}

	public boolean isDatiPresaCarico() {
		return isDatiPresaCarico;
	}

	public boolean isFogliAmm() {
		return isFogliAmm;
	}

	public boolean isColloquio() {
		return isColloquio;
	}

	public boolean isIsee() {
		return isIsee;
	}

	public boolean isSchedeSegr() {
		return isSchedeSegr;
	}

	public boolean isDocIndiv() {
		return isDocIndiv;
	}
	
	public boolean isSchedaMultidimAnz() {
		return isSchedaMultidimAnz;
	}

	public boolean isDatiPresaCaricoReadOnly() {
		return isDatiPresaCaricoReadOnly;
	}

	public boolean isFogliAmmReadOnly() {
		return isFogliAmmReadOnly;
	}

	public boolean isColloquioReadOnly() {
		return isColloquioReadOnly;
	}

	public boolean isIseeReadOnly() {
		return isIseeReadOnly;
	}

	public boolean isSchedeSegrReadOnly() {
		return isSchedeSegrReadOnly;
	}

	public boolean isDocIndivReadOnly() {
		return isDocIndivReadOnly;
	}

	public boolean isSchedaMultidimAnzReadOnly() {
		return isSchedaMultidimAnzReadOnly;
	}
	
	public ColloquioBean getColloquioBean() {
		return colloquioBean;
	}
	
	public RelazioniBean getRelazioniBean() {
		return relazioniBean;
	}

	public void setRelazioniBean(RelazioniBean relazioniBean) {
		this.relazioniBean = relazioniBean;
	}

	public InterventiBean getInterventiBean() {
		return interventiBean;
	}

	public void setInterventiBean(InterventiBean interventiBean) {
		this.interventiBean = interventiBean;
	}

	public boolean isRelaz() {
		return isRelaz;
	}

	public boolean isRelazReadOnly() {
		return isRelazReadOnly;
	}

	public void setColloquioBean(ColloquioBean colloquioBean) {
		this.colloquioBean = colloquioBean;
	}

	public ManSchedaMultidimAnzBean getSchedaMultidimAnzBean() {
		return schedaMultidimAnzBean;
	}

	public void setSchedaMultidimAnzBean(ManSchedaMultidimAnzBean schedaMultidimAnzBean) {
		this.schedaMultidimAnzBean = schedaMultidimAnzBean;
	}

	public AltriSoggettiBean getAltriSoggettiBean() {
		return altriSoggettiBean;
	}

	public void setAltriSoggettiBean(AltriSoggettiBean altriSoggettiBean) {
		this.altriSoggettiBean = altriSoggettiBean;
	}

	public boolean isRenderVisualizzaFascicolo() {
		return checkPermesso(DataModelCostanti.PermessiFascicolo.VISUALIZZAZIONE_FASCICOLO);
	}

	public String getDatiPresaCaricoName() {
		return datiPresaCaricoName;
	}

	public String getFogliAmmName() {
		return fogliAmmName;
	}

	public String getColloquioName() {
		return colloquioName;
	}

	public String getIseeName() {
		return iseeName;
	}

	public String getRelazName() {
		return relazName;
	}

	public String getSchedeSegrName() {
		return schedeSegrName;
	}

	public String getDocIndivName() {
		return docIndivName;
	}

	public String getSchedaMultidimAnzName() {
		return schedaMultidimAnzName;
	}

	public DocIndividualiBean getDocIndividualiBean() {
		return docIndividualiBean;
	}

	public void setDocIndividualiBean(DocIndividualiBean docIndividualiBean) {
		this.docIndividualiBean = docIndividualiBean;
	}

	public boolean isAltroSoggetto() {
		return altroSoggetto;
	}

	public void setAltroSoggetto(boolean altroSoggetto) {
		this.altroSoggetto = altroSoggetto;
	}

	public String getDatiScuolaName() {
		return datiScuolaName;
	}

	public boolean isDatiScuola() {
		return isDatiScuola;
	}

	public boolean isDatiScuolaReadOnly() {
		return isDatiScuolaReadOnly;
	}

	public DatiScuolaBean getDatiScuolaBean() {
		return datiScuolaBean;
	}

	public PresaInCaricoBean getPresaInCaricoBean() {
		return presaInCaricoBean;
	}

	public void setPresaInCaricoBean(PresaInCaricoBean presaInCaricoBean) {
		this.presaInCaricoBean = presaInCaricoBean;
	}

	public SchedeSegrBean getSchedeSegrBean() {
		return schedeSegrBean;
	}

	public void setSchedeSegrBean(SchedeSegrBean schedeSegrBean) {
		this.schedeSegrBean = schedeSegrBean;
	}

	public IseeBean getIseeBean() {
		return iseeBean;
	}

	public void setIseeBean(IseeBean iseeBean) {
		this.iseeBean = iseeBean;
	}
	
/*********** End Generic ******************/

}
