package it.webred.ct.diagnostics.service.web.beans;

import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaEsecuzioneDTO;
import it.webred.ct.diagnostics.service.web.user.UserBean;
import it.webred.ct.rulengine.service.bean.RecuperaComandoService;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaDiaBean extends UserBean {
	
	private static final long serialVersionUID = 1L;			
	private List<DiaEsecuzioneDTO> listaDiaExecute = new ArrayList<DiaEsecuzioneDTO>();
	
	private DiagnosticheService diaService;	
	private String paginaDettaglio = "/jsp/protected/empty.xhtml";

	

	public List<DiaEsecuzioneDTO> getListaDiaExecute() {
		return listaDiaExecute;
	}


	public void setListaDiaExecute(List<DiaEsecuzioneDTO> listaDiaExecute) {
		this.listaDiaExecute = listaDiaExecute;
	}


	public DiagnosticheService getDiaService() {
		return diaService;
	}


	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}

	public VisualizzaDiaBean(){
		super();
	}
	
	
	public void doToro() {
		paginaDettaglio = "/jsp/protected/diagnostics/data/viewDiaTestata.xhtml";
	}
	
	public String goBackFromDetail(){
		doToro();
		return "menu.visualizzaDia";
	}

	public void doResetPage(){
		paginaDettaglio = "/jsp/protected/empty.xhtml";
	}

	public String getPaginaDettaglio() {
		return paginaDettaglio;
	}


	public void setPaginaDettaglio(String paginaDettaglio) {
		this.paginaDettaglio = paginaDettaglio;
	}

}
