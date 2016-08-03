package it.webred.ct.diagnostics.service.web.beans;

import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.web.beans.pagination.StoricoEsecProviderImpl;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.io.Serializable;

public class StoricoEsecuzioniBean extends UserBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
	private DiagnosticheService diaService;
	private Long idDiaTestata;	
	private Long idCatalogoDia;	
	private String descrizione;
	
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodCmdOrCmdGrp() {
		return codCmdOrCmdGrp;
	}

	public void setCodCmdOrCmdGrp(String codCmdOrCmdGrp) {
		this.codCmdOrCmdGrp = codCmdOrCmdGrp;
	}

	private String codCmdOrCmdGrp;
	
	public Long getIdCatalogoDia() {
		return idCatalogoDia;
	}

	public void setIdCatalogoDia(Long idCatalogoDia) {
		this.idCatalogoDia = idCatalogoDia;
	}

	private String diaDescr;

	

	public DiagnosticheService getDiaService() {
		return diaService;
	}

	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}
	
	public String goStorico() {
		getLogger().debug("Recupero storico testata");
				
		//passaggio parametri ad dataProviderImpl
		StoricoEsecProviderImpl sep = (StoricoEsecProviderImpl)super.getBeanReference("storicoEsecProviderImpl");
		sep.setIdCatalogoDia(idCatalogoDia);		
		
		return "diagnostica.storicoTestata";
	}

	public Long getIdDiaTestata() {
		return idDiaTestata;
	}

	public void setIdDiaTestata(Long idDiaTestata) {
		this.idDiaTestata = idDiaTestata;
	}

	public String getDiaDescr() {
		return diaDescr;
	}
	
	public void setDiaDescr(String diaDescr) {
		this.diaDescr = diaDescr;
	}
}
