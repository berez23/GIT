package it.webred.cs.csa.web.manbean.fascicolo.presaincarico;

import it.webred.cs.csa.ejb.client.AccessTableIterStepSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.web.manbean.fascicolo.FascicoloCompBaseBean;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsItStep;
import it.webred.cs.jsf.interfaces.IPresaInCarico;
import it.webred.cs.jsf.manbean.IterDialogMan;
import it.webred.cs.jsf.manbean.IterInfoStatoMan;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;

import javax.faces.bean.ManagedProperty;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class PresaInCaricoBean extends FascicoloCompBaseBean implements IPresaInCarico {

	private IterInfoStatoMan casoInfo;
	
	@ManagedProperty( value="#{iterDialogMan}")
	private IterDialogMan iterDialogMan;	
	
	AccessTableIterStepSessionBeanRemote iterSessionBean = (AccessTableIterStepSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableIterStepSessionBean");
	
	@Override
	public void initializeData() {
		
		try{
			
			casoInfo = new IterInfoStatoMan();
			CsASoggetto csSogg = (CsASoggetto) getSession().getAttribute("soggetto");
			IterDTO itDto = new IterDTO();
			CsUiCompBaseBean.fillEnte(itDto);
			itDto.setIdCaso(csSogg.getCsACaso().getId());
			CsItStep itStep = iterSessionBean.getLastIterStepByCaso(itDto);
			
			if( itStep != null ) {
				casoInfo.initialize( itStep );
				iterDialogMan = new IterDialogMan();
			}
			
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public ActionListener getCloseDialog() {
	    return new ActionListener() {
	        @Override
	        public void processAction(ActionEvent event) throws AbortProcessingException {
	        	initializeData();
	        }
	    };
	}

	public IterInfoStatoMan getCasoInfo() {
		return casoInfo;
	}

	public void setCasoInfo(IterInfoStatoMan casoInfo) {
		this.casoInfo = casoInfo;
	}

	@Override
	public IterDialogMan getIterDialogMan() {
		return iterDialogMan;
	}

	public void setIterDialogMan(IterDialogMan iterDialogMan) {
		this.iterDialogMan = iterDialogMan;
	}
}
