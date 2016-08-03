package it.webred.fb.web.bean;

import java.util.ArrayList;
import java.util.List;

import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.data.model.DmConfDocDir;
import it.webred.fb.ejb.client.CaricatoreSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.InfoCarProvenienzaDTO;
import it.webred.fb.ejb.dto.StatoConfDoc;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.NamingException;

@ManagedBean
@ViewScoped
public class GestoreFlussoBean extends FascicoloBeneBaseBean {
	private String activeIndex;
	private List<InfoCarProvenienzaDTO> listaInfo;
	private List<StatoConfDoc> listaInfoDocs;
	
	@SuppressWarnings("static-access")
	@PostConstruct
    public void init() {
	 try{
		CaricatoreSessionBeanRemote caricatoreService = (CaricatoreSessionBeanRemote) ClientUtility.getEjbInterface(
					"FascicoloBene", "FascicoloBene_EJB", "CaricatoreSessionBean");
		
		BaseDTO b = new BaseDTO();
		this.fillUserData(b);
		
			try{
				listaInfo = caricatoreService.getInfoCaricamento(b);
			} catch (Exception e) {
				   logger.error(e.getMessage(),e);
				   addError("Errore - Info caricamento dati",e.getMessage());
			}
			try{
				listaInfoDocs = caricatoreService.getConfCaricamentoDocsAbilitati(b);
			} catch (Exception e) {
				   logger.error(e.getMessage(),e);
				   addError("Errore - Info caricamento documenti",e.getMessage());
			}	
			
		} catch (NamingException e) {
			   logger.error(e.getMessage(),e);
			   addError("Errore durante l'inizializzazione",e.getMessage());
		}	
	      
	 }

	
	@SuppressWarnings("static-access")
	public void caricaAggiornamento(String provenienza){
		
	 try {
			CaricatoreSessionBeanRemote caricatoreService = (CaricatoreSessionBeanRemote) ClientUtility.getEjbInterface(
						"FascicoloBene", "FascicoloBene_EJB", "CaricatoreSessionBean");
			
			BaseDTO b = new BaseDTO();
			this.fillUserData(b);
			b.setObj(provenienza);
			
			caricatoreService.caricaDatiBene(b);
			
			caricatoreService.caricaDerivatiBene(b);
			
			listaInfo = caricatoreService.getInfoCaricamento(b);
			
		   } catch (Exception e) {
			   addError("Errore durante il caricamento dati",e.getMessage());
			}
		
	}
	
	@SuppressWarnings("static-access")
	public void svuotaFonte(String provenienza){
		
	 try {
			CaricatoreSessionBeanRemote caricatoreService = (CaricatoreSessionBeanRemote) ClientUtility.getEjbInterface(
						"FascicoloBene", "FascicoloBene_EJB", "CaricatoreSessionBean");
			
			BaseDTO b = new BaseDTO();
			this.fillUserData(b);
			b.setObj(provenienza);
			
			caricatoreService.svuotaProvenienza(b);
			addWarning("Anche i documenti associati potrebbero essere stati rimossi. Ricaricarli...",null);
			
			listaInfo = caricatoreService.getInfoCaricamento(b);
			
		   } catch (Exception e) {
			   addError("Errore durante lo svuotamento della fonte dati",e.getMessage());
			}
		
	}
	
	@SuppressWarnings("static-access")
	public void caricaDocumenti(DmConfDocDir path){
		
		 try {
			CaricatoreSessionBeanRemote caricatoreService = (CaricatoreSessionBeanRemote) ClientUtility.getEjbInterface(
						"FascicoloBene", "FascicoloBene_EJB", "CaricatoreSessionBean");
			
			List<DmConfDocDir> lst = new ArrayList<DmConfDocDir>();
			if(path!=null)
				lst.add(path);
			else{
				lst = new ArrayList<DmConfDocDir>();
				for(StatoConfDoc s : this.listaInfoDocs)
					lst.add(s.getConfigurazione());
			}
			
			BaseDTO b = new BaseDTO();
			this.fillUserData(b);
			b.setObj(lst);
			
			caricatoreService.caricaDocumenti(b);
			listaInfoDocs = caricatoreService.getConfCaricamentoDocsAbilitati(b);
			
		   } catch (Exception e) {
			   addError("Errore durante l'elaborazione dei documenti", e.getMessage());
		   }
		
	}

	public List<InfoCarProvenienzaDTO> getListaInfo() {
		return listaInfo;
	}


	public void setListaInfo(List<InfoCarProvenienzaDTO> listaInfo) {
		this.listaInfo = listaInfo;
	}


	public List<StatoConfDoc> getListaInfoDocs() {
		return listaInfoDocs;
	}


	public void setListaInfoDocs(List<StatoConfDoc> listaInfoDocs) {
		this.listaInfoDocs = listaInfoDocs;
	}


	public String getActiveIndex() {
		return activeIndex;
	}


	public void setActiveIndex(String activeIndex) {
		this.activeIndex = activeIndex;
	}
	
}

