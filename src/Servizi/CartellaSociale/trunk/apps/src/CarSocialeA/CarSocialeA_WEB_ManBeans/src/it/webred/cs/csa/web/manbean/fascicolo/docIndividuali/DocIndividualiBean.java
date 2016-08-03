package it.webred.cs.csa.web.manbean.fascicolo.docIndividuali;

import it.webred.cs.csa.ejb.client.AccessTableAlertSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableIterStepSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.ejb.dto.OperatoreDTO;
import it.webred.cs.csa.web.manbean.fascicolo.FascicoloCompBaseBean;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsDDocIndividuale;
import it.webred.cs.data.model.CsItStep;
import it.webred.cs.data.model.CsLoadDocumento;
import it.webred.cs.data.model.CsOOperatore;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsTbTipoDiario;
import it.webred.cs.jsf.interfaces.IDocIndividuali;
import it.webred.cs.jsf.manbean.DiarioDocsMan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class DocIndividualiBean extends FascicoloCompBaseBean implements IDocIndividuali {

	private List<CsDDocIndividuale> listaDocIndividualiPubblica;
	private List<CsDDocIndividuale> listaDocIndividualiPrivata;
	private int idxSelected;
	private String modalHeader;
	private boolean disableUpload;
	private CsDDocIndividuale docIndividuale;
	
	private DiarioDocsMan diarioDocsMan;
	
	AccessTableOperatoreSessionBeanRemote operatoreService = (AccessTableOperatoreSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableOperatoreSessionBean");
	AccessTableAlertSessionBeanRemote alertService = (AccessTableAlertSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableAlertSessionBean");
	AccessTableIterStepSessionBeanRemote iterStepService = (AccessTableIterStepSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableIterStepSessionBean");
	
	@PostConstruct
	public void init() {
		
		if(csASoggetto == null)
			csASoggetto = (CsASoggetto) getSession().getAttribute("soggetto");
		
		if(csASoggetto != null)
			this.initialize(csASoggetto);
	}
	
	public void initializeDocIndividuali(CsASoggetto soggetto) {

		if(soggetto != null) {
			getSession().setAttribute("soggetto", soggetto);
			
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("docIndividuali.faces");
			} catch (IOException e) {
				addError("Errore", "Errore durante il reindirizzamento ai Documenti Individuali");
			}
		} else 
			addWarningFromProperties("seleziona.warning");
	}
	
	@Override
	public void initializeData() {
		
		try{
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(idCaso);
			listaDocIndividualiPubblica = new ArrayList<CsDDocIndividuale>();
			listaDocIndividualiPrivata = new ArrayList<CsDDocIndividuale>();
			List<CsDDocIndividuale> lista = diarioService.findDocIndividualiByCaso(dto);
		
			for(CsDDocIndividuale docInd: lista) {
				if(docInd.getPrivato() == null || !docInd.getPrivato()) 
					listaDocIndividualiPubblica.add(docInd);
				else if(docInd.getUserIns().equals(dto.getUserId())) 
					listaDocIndividualiPrivata.add(docInd);
			}
			
			diarioDocsMan = new DiarioDocsMan();
			
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void nuovo() {
		
		modalHeader = "Nuovo Documento Individuale";
		docIndividuale = new CsDDocIndividuale();
		docIndividuale.setPrivato(false);
		
		diarioDocsMan = new DiarioDocsMan();
		diarioDocsMan.getuFileMan().setExternalSave(true);
		disableUpload = false;
	}
	
	@Override
	public void caricaPubblico() {
		
		modalHeader = "Modifica Documento Individuale";
		docIndividuale = listaDocIndividualiPubblica.get(idxSelected);
		disableUpload = true;
		
	}
	
	@Override
	public void caricaPrivato() {
		
		modalHeader = "Modifica Documento Individuale";
		docIndividuale = listaDocIndividualiPrivata.get(idxSelected);
		disableUpload = true;
		
	}
	
	@Override
	public void indicaLettura() {
		
		try{
			
			docIndividuale = listaDocIndividualiPubblica.get(idxSelected);
			docIndividuale.setLetto(true);
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(docIndividuale);
			diarioService.updateDocIndividuale(dto);
			
			initializeData();
			
		} catch (Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void salva() {
		
		try{
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(docIndividuale);
			
			if(docIndividuale.getDiarioId() != null) {
				
				//modifica
				docIndividuale.setUsrMod(dto.getUserId());
				docIndividuale.setDtMod(new Date());
				diarioService.updateDocIndividuale(dto);
			}
			else {
				
				//nuovo
				if(!validaDocIndividuale()) {
					return;
				}
				
				CsDDiario csd = new CsDDiario();
		        CsACaso csa = new CsACaso();
		        CsTbTipoDiario cstd = new CsTbTipoDiario(); 
		        csa.setId(idCaso);
		        cstd.setId(new Long(DataModelCostanti.TipoDiario.DOC_INDIVIDUALE_ID)); 
		        csd.setCsACaso(csa);
		        csd.setCsTbTipoDiario(cstd);
		        CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
		        csd.setCsOOperatoreSettore(opSettore);
		        docIndividuale.setCsDDiario(csd);
		        docIndividuale.setLetto(false);
		        docIndividuale.setUserIns(dto.getUserId());
		        docIndividuale.setDtIns(new Date());
				
				csd = diarioService.saveDocIndividuale(dto);
				
				//salvo il documento
				diarioDocsMan.getuFileMan().setIdDiario(csd.getId());
				for(CsLoadDocumento loadDoc: diarioDocsMan.getuFileMan().getDocumentiUploaded()) {
					diarioDocsMan.getuFileMan().salvaDocumento(loadDoc);
				}
				
				//notifica al responsabile caso
				if(!docIndividuale.getPrivato()) {
					
					IterDTO itDto = new IterDTO();
					fillEnte(itDto);
					itDto.setIdCaso(idCaso);
					CsACaso csACaso = casoService.findCasoById(itDto);
					dto.setObj(csACaso.getId());
					CsACasoOpeTipoOpe casoOpTipoOp = casoService.findResponsabile(dto);
					//from
					itDto.setIdOrganizzazione(opSettore.getCsOSettore().getCsOOrganizzazione().getId());
					itDto.setIdSettore(opSettore.getCsOSettore().getId());
					itDto.setNomeOperatore(itDto.getUserId());
					//to
					
					if(casoOpTipoOp != null) {
						//Responsabile
						CsOOperatore operatore = casoOpTipoOp.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore().getCsOOperatore();
						itDto.setIdOpTo(operatore.getId());
					} else {
						//creatore
						OperatoreDTO opDto = new OperatoreDTO();
						fillEnte(opDto);
						opDto.setUsername(csACaso.getUserIns());
						CsOOperatore operatore = operatoreService.findOperatoreByUsername(opDto);
						itDto.setIdOpTo(operatore.getId());
					}
					itDto.setTipo(DataModelCostanti.TipiAlert.DOC_IND);
					itDto.setLabelTipo(DataModelCostanti.TipiAlert.DOC_IND_DESC);
					String denominazione = csACaso.getCsASoggetto().getCsAAnagrafica().getCognome() 
							+ " " + csACaso.getCsASoggetto().getCsAAnagrafica().getNome()
							+ " (" + csACaso.getCsASoggetto().getCsAAnagrafica().getCf() + ")";
					String descrizione = "Il caso " + denominazione.toUpperCase() + " ha un nuovo Documento Individuale";
					itDto.setDescrizione(descrizione);
					itDto.setTitoloDescrizione(descrizione);
					alertService.addAlert(itDto);
	
					//notifica al responsabile settore
					//to
					CsItStep itStep = iterStepService.getLastIterStepByCaso(itDto);
					itDto.setIdOpTo(null);
					if(itStep.getCsOSettore2() != null) {
						itDto.setIdOrgTo(itStep.getCsOOrganizzazione2().getId());
						itDto.setIdSettTo(itStep.getCsOSettore2().getId());
						alertService.addAlert(itDto);
					} else if(itStep.getCsOSettore1() != null) {
						itDto.setIdOrgTo(itStep.getCsOOrganizzazione1().getId());
						itDto.setIdSettTo(itStep.getCsOSettore1().getId());
						alertService.addAlert(itDto);
					}
					
				}

			}
			
			initializeData();
			
			addInfoFromProperties("salva.ok");
			RequestContext.getCurrentInstance().addCallbackParam("saved", true);
			
		} catch (Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	@Override
	public void elimina() {
		
		try {
		
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(docIndividuale);
			diarioService.deleteDocIndividuale(dto);
		
			initializeData();
			
			addInfoFromProperties("elimina.ok");
		
		} catch (Exception e) {
			addErrorFromProperties("elimina.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	private boolean validaDocIndividuale() {
		
		boolean ok = true;
		
		if(diarioDocsMan.getuFileMan().getDocumentiUploaded().isEmpty()) {
			ok = false;
			addError("Aggiungere un documento", "");
		}
		
		if(docIndividuale.getDescrizione() == null || "".equals(docIndividuale.getDescrizione())) {
			ok = false;
			addError("Descrizione è un campo obbligatorio", "");
		}
		
		if(docIndividuale.getData() == null) {
			ok = false;
			addError("Data è un campo obbligatorio", "");
		}
		
		return ok;
	}

	@Override
	public Long getIdCaso() {
		return idCaso;
	}

	@Override
	public void setIdCaso(Long idCaso) {
		this.idCaso = idCaso;
	}

	@Override
	public int getIdxSelected() {
		return idxSelected;
	}

	public void setIdxSelected(int idxSelected) {
		this.idxSelected = idxSelected;
	}

	@Override
	public CsDDocIndividuale getDocIndividuale() {
		return docIndividuale;
	}

	public void setDocIndividuale(CsDDocIndividuale docIndividuale) {
		this.docIndividuale = docIndividuale;
	}

	@Override
	public DiarioDocsMan getDiarioDocsMan() {
		return diarioDocsMan;
	}

	public void setDiarioDocsMan(DiarioDocsMan diarioDocsMan) {
		this.diarioDocsMan = diarioDocsMan;
	}

	@Override
	public String getModalHeader() {
		return modalHeader;
	}

	public void setModalHeader(String modalHeader) {
		this.modalHeader = modalHeader;
	}

	@Override
	public List<CsDDocIndividuale> getListaDocIndividualiPubblica() {
		return listaDocIndividualiPubblica;
	}

	public void setListaDocIndividualiPubblica(
			List<CsDDocIndividuale> listaDocIndividualiPubblica) {
		this.listaDocIndividualiPubblica = listaDocIndividualiPubblica;
	}

	@Override
	public List<CsDDocIndividuale> getListaDocIndividualiPrivata() {
		return listaDocIndividualiPrivata;
	}

	public void setListaDocIndividualiPrivata(
			List<CsDDocIndividuale> listaDocIndividualiPrivata) {
		this.listaDocIndividualiPrivata = listaDocIndividualiPrivata;
	}

	@Override
	public boolean isDisableUpload() {
		return disableUpload;
	}

	public void setDisableUpload(boolean disableUpload) {
		this.disableUpload = disableUpload;
	}

	public boolean isRenderVisualizzaDocIndividuali() {
		return checkPermesso(DataModelCostanti.PermessiDocIndividuali.UPLOAD_DOC_INDIVIDUALI);
	}

}
