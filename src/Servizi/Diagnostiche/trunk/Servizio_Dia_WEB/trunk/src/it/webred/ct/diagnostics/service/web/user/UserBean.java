package it.webred.ct.diagnostics.service.web.user;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.webred.cet.permission.CeTUser;
import it.webred.cet.permission.GestionePermessi;
import it.webred.ct.config.model.AmInstance;
import it.webred.ct.config.parameters.application.ApplicationService;
import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaLogAccessoDTO;
import it.webred.ct.diagnostics.service.data.model.DiaLogAccesso;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.web.DiaBaseBean;

@SuppressWarnings("serial")
public class UserBean extends DiaBaseBean  implements Serializable {
	
	//controllo permessi di visualizzazione
	private static final String DIA_APPLICATION_AM = "Diagnostics";
	private static final String DIA_ITEM_AM = "Diagnostiche";
	private static final String DIA_PERMISSION_AM = "Visualizzazione avanzata";
	private boolean authorized;
	
	protected ApplicationService applicationService;
	
	public boolean isAuthorized() {
		CeTUser u = getUser();
		getLogger().debug("DIA_APPLICATION_AM: " + super.getEnte());
		if (u != null) {
			AmInstance ist = applicationService.getInstanceByApplicationComune(DIA_APPLICATION_AM, super.getEnte());
			if(ist != null)
				authorized = GestionePermessi.autorizzato(u, ist.getName(), DIA_ITEM_AM, DIA_PERMISSION_AM);
		}	
		getLogger().debug("authorized: " + authorized);
		return authorized;
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}
	
	public void doLogout(){
		
		getRequest().getSession().invalidate();
		try {
			getResponse().sendRedirect(getRequest().getContextPath() + "/");
		} catch (IOException e) {
			getLogger().error("Eccezione: "+e.getMessage(),e);
		}		
	}
	
	protected void doSaveLog(String desOperazione,Long idDiaTestata, DiagnosticheService diaService){		
		getLogger().info("[doSaveLog] - Start");
		try {
			
			DiaLogAccesso dla = new DiaLogAccesso();
			DiaTestata dt = new DiaTestata();
			getLogger().debug("[doSaveLog] - DiaTestata:"+dt.toString());
			getLogger().debug("[doSaveLog] - idDiaTestata:"+idDiaTestata);
			getLogger().debug("[doSaveLog] - desOperazione:"+desOperazione);
			dt.setId(idDiaTestata);
			dla.setDiaTestata(dt);
			
			dla.setDataAccesso(new Date());
			dla.setDesOperazione(desOperazione);
			dla.setDesUtente(getUser().getName());
			
			DiaLogAccessoDTO logDto = new DiaLogAccessoDTO(dla,getUser().getCurrentEnte(),getUser().getName());
			diaService.insertLogAccesso(logDto);
			getLogger().info("[doSaveLog] - Log inserito");
			
		} catch (Exception e) {
			getLogger().error("Eccezione: "+e.getMessage(),e);
		}
		getLogger().info("[doSaveLog] - End");
	}
	
	protected String getStrDate(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		return sdf.format(d);
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

}
