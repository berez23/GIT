package it.webred.cs.csa.web.manbean.scheda;

import it.webred.cs.csa.ejb.client.AccessTableSchedaSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.jsf.bean.ValiditaCompBaseBean;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.primefaces.context.RequestContext;

public abstract class SchedaValiditaBaseBean extends CsUiCompBaseBean {
	
	protected AccessTableSchedaSessionBeanRemote schedaService = (AccessTableSchedaSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableSchedaSessionBean");
	
	protected Long soggettoId;
	public int currentIndex = -1;
	protected List<ValiditaCompBaseBean> listaComponenti;
	
	public void initialize(Long sId) {
		
		soggettoId = sId;
		
		if(soggettoId != null){
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(soggettoId);
			dto.setObj2(getTypeClass());
			
			listaComponenti = new ArrayList();
			List<?> listaCs = schedaService.findCsBySoggettoId(dto);
			
			for(Object cs: listaCs){
				
				listaComponenti.add(getComponenteFromCs(cs));
				
			}
			
			if(listaComponenti.size() > 0)
				currentIndex = 0;
		}
		
	}
	
	public abstract Object getTypeClass();
			
	public void nuovo() {
		currentIndex = 0;
	}
	
	public void salva() {
		
		try{

			if(!validaComponenti())
				return;
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);			
			
			for(ValiditaCompBaseBean comp: listaComponenti) {
				
				if(!validaCs(comp))
					return;
				
				Object cs = getCsFromComponente(comp);
				
				dto.setObj(cs);
				if(comp.getId() != null)
					schedaService.updateCsA(dto);
				else schedaService.saveCsA(dto);
			}
			
			initialize(soggettoId);
			
			addInfoFromProperties("salva.ok");
		
		} catch(Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	public void salvaCs(Long id) {
		
		try{

			BaseDTO dto = new BaseDTO();
			fillEnte(dto);			
			
			ValiditaCompBaseBean comp = listaComponenti.get(currentIndex);
			
			Object cs = getCsFromComponente(comp);
			
			dto.setObj(cs);
			if(comp.getId() != null)
				schedaService.updateCsA(dto);
			else schedaService.saveCsA(dto);
			
			addInfoFromProperties("chiudi.ok");
		
		} catch(Exception e) {
			addErrorFromProperties("chiudi.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	public void elimina() {
		
		try{
			
			ValiditaCompBaseBean comp = listaComponenti.get(currentIndex);
			
			if(comp.getId() != null){
				BaseDTO dto = new BaseDTO();
				fillEnte(dto);
				dto.setObj(comp.getId());
				dto.setObj2(getTypeClass());
				
				schedaService.eliminaCsById(dto);
				
				initialize(soggettoId);
			} else {
				listaComponenti.remove(currentIndex);
			}
			
			addInfoFromProperties("elimina.ok");
			
		} catch(Exception e) {
			addErrorFromProperties("elimina.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	public void copia() {
		
		try{
			
			ValiditaCompBaseBean comp = listaComponenti.get(currentIndex);
			Object cs = getCsFromComponente(comp);
			ValiditaCompBaseBean newComp = getComponenteFromCs(cs);
			newComp.setDataInizio(new Date());
			newComp.setDataFine(null);
			newComp.setId(null);
			listaComponenti.add(0, newComp);
			currentIndex = 0;
			
			addInfoFromProperties("operazione.ok");
			
		} catch(Exception e) {
			addErrorFromProperties("operazione.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	public boolean validaCs(ValiditaCompBaseBean comp) {
		RequestContext.getCurrentInstance().addCallbackParam("canClose", true);
		return true;
	}
	
	public boolean validaComponenti() {
		return true;
	}
	
	public abstract Object getCsFromComponente(Object obj);
	
	public abstract ValiditaCompBaseBean getComponenteFromCs(Object obj);
			
	public List<ValiditaCompBaseBean> getListaComponenti() {
		return listaComponenti;
	}

	public void setListaComponenti(List<ValiditaCompBaseBean> listaComponenti) {
		this.listaComponenti = listaComponenti;
	}

	public Long getSoggettoId() {
		return soggettoId;
	}

	public void setSoggettoId(Long soggettoId) {
		this.soggettoId = soggettoId;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	
}
