package it.webred.ct.diagnostics.service.web.beans;

import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaTestataDTO;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.io.Serializable;

public class DettaglioSqlBean extends UserBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String sqlPage = "/jsp/protected/empty.xhtml";
	private String sqlCompleta;
	private String sqlParam;
		
	private Long idDiaTestata;
	private DiagnosticheService diaService;
	
	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getSqlCompleta() {
		return sqlCompleta;
	}

	public void setSqlCompleta(String sqlCompleta) {
		this.sqlCompleta = sqlCompleta;
	}

	public void doInit(){
		super.getLogger().debug("[DettaglioSqlBean.doInit] - START");
		
		/*
		if (sqlCompleta == null )
			super.getLogger().debug("[DettaglioSqlBean.doInit] - sqlCompleta non valorizzata");
		else
			super.getLogger().debug("[DettaglioSqlBean.doInit] - sqlCompleta valorizzata");
		*/
		
		DiaTestata dtObj = new DiaTestata();
		dtObj.setId(idDiaTestata);
		
		DiaTestata dt = diaService.getDiaTestata(new DiaTestataDTO(dtObj,getUser().getCurrentEnte(),getUser().getName()));
		sqlCompleta = dt.getDesSql();
	}
	
	public void resetPage() {
		setSqlPage("/jsp/protected/empty.xhtml");			
	}
	
	public String getSqlPage() {
		return sqlPage;
	}

	public void setSqlPage(String sqlPage) {
		this.sqlPage = sqlPage;
	}

	public DiagnosticheService getDiaService() {
		return diaService;
	}

	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}

	public Long getIdDiaTestata() {
		return idDiaTestata;
	}

	public void setIdDiaTestata(Long idDiaTestata) {
		this.idDiaTestata = idDiaTestata;
	}

}
