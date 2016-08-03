package it.webred.ct.diagnostics.service.web.beans;

import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaCatalogoDTO;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DettaglioGroupBean extends EseguiDiaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<DiaCatalogo> listaDiaGrp = new ArrayList<DiaCatalogo>();
	private DiagnosticheService diaService;

	public DiagnosticheService getDiaService() {
		return diaService;
	}

	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}

	public List<DiaCatalogo> getListaDiaGrp() {				
		return listaDiaGrp;	
	}

	public void setListaDiaGrp(List<DiaCatalogo> listaDiaGrp) {
		this.listaDiaGrp = listaDiaGrp;
	}
	
	public void doInit(){
		super.getLogger().debug("[DettaglioGroupBean.doInit] - Codice Commando:" + super.getCodCommand());
				
		DiaCatalogoDTO dc = new DiaCatalogoDTO(null,getUser().getCurrentEnte(),getUser().getName());
		dc.setCodCommand(super.getCodCommand());
		listaDiaGrp = diaService.getDiagnosticheByCodCmdGrp(dc) ;
		if (listaDiaGrp != null)
			super.getLogger().debug("[GETLISTADIAGRP] - N. dia recuperate:" + listaDiaGrp.size());
		else
			super.getLogger().debug("[GETLISTADIAGRP] - N. dia recuperate: null");
	}
	
	public void resetPage() {
		setNewDiaPage("/jsp/protected/empty.xhtml");			
	}
	
}
