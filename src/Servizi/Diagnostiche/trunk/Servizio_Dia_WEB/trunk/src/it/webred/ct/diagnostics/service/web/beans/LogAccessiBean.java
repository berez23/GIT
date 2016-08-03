package it.webred.ct.diagnostics.service.web.beans;

import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaDettaglioDTO;
import it.webred.ct.diagnostics.service.data.model.DiaLogAccesso;
import it.webred.ct.diagnostics.service.web.beans.pagination.DataProviderImpl;
import it.webred.ct.diagnostics.service.web.beans.pagination.LogDataProviderImpl;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogAccessiBean extends UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<DiaLogAccesso> listaLog = new ArrayList<DiaLogAccesso>();
	private long idDiaTestata;
	private String logAccessiPage = "/jsp/protected/empty.xhtml";
		
	private DiagnosticheService diaService;
	
	public String getLogAccessiPage() {
		return logAccessiPage;
	}

	public void setLogAccessiPage(String logAccessiPage) {
		this.logAccessiPage = logAccessiPage;
	}

	public long getIdDiaTestata() {
		return idDiaTestata;
	}

	public void setIdDiaTestata(long idDiaTestata) {
		this.idDiaTestata = idDiaTestata;
	}
	
	public DiagnosticheService getDiaService() {
		return diaService;
	}

	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}
	
	public List<DiaLogAccesso> getListaLog() {
		return listaLog;
	}

	public void setListaLog(List<DiaLogAccesso> listaLog) {
		this.listaLog = listaLog;
	}

	public void doInit(){
		super.getLogger().debug("[LogAccessiBean.doInit] - Start");
				
//		DiaDettaglioDTO dd = new DiaDettaglioDTO(getUser().getCurrentEnte(),getUser().getName());
//		dd.setIdDiaTestata(idDiaTestata);
//		listaLog = diaService.getLogAccessiByDiaTestata(dd) ;
//		if (listaLog == null || listaLog.size() == 0) {
//			super.getLogger().debug("[LogAccessiBean.doInit] - Nessun accesso per la dia");
//		}
			
		//passaggio parametri ad dataProviderImpl
		LogDataProviderImpl ldp = (LogDataProviderImpl)super.getBeanReference("logDataProviderImpl");		
		ldp.setIdDiaTestata(idDiaTestata);
		
		super.getLogger().debug("[LogAccessiBean.doInit] - End");
	}
	
	public void resetPage() {
		setLogAccessiPage("/jsp/protected/empty.xhtml");			
	}
}
