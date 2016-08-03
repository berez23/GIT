package it.webred.ct.service.comma340.web.common;

import it.webred.ct.service.comma340.web.catasto.CatastoDataModel;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

public class UIRicercaCatasto {

	private String descCategoria;
	private String descVia;
	private String descCivico;
	private String descCognome;
	private String descNome;
	private String descDenominazione;
	private String cf;
	private String piva;
	private String selectedPanelBar;
	private CatastoDataModel model;
	
	
	public CatastoDataModel getModel() {
		FacesContext context = FacesContext.getCurrentInstance();
		CatastoDataModel model = (CatastoDataModel)context.getApplication().getVariableResolver().resolveVariable(context,"catastoDataModel");
		return model;
	}

	public String getDescCategoria() {
		return descCategoria;
	}

	public void setDescCategoria(String descCategoria) {
		this.descCategoria = descCategoria;
	}

	public String getDescVia() {
		return descVia;
	}

	public void setDescVia(String descVia) {
		this.descVia = descVia;
		if(descVia!=null && descVia.isEmpty()){
			this.setDescCivico("");
			getModel().getCriteria().setIdVia("");
			
		}
			
	}

	public String getDescCivico() {
		return descCivico;
	}

	public void setDescCivico(String descCivico) {
		this.descCivico = descCivico;
		if(descCivico!=null && descCivico.isEmpty()){
			getModel().getCriteria().setIdCivico("");
		}
	}

	public String getDescCognome() {
		return descCognome;
	}

	public void setDescCognome(String descCognome) {
		this.descCognome = descCognome;
	}

	public String getDescNome() {
		return descNome;
	}

	public void setDescNome(String descNome) {
		this.descNome = descNome;
	}

	public String getDescDenominazione() {
		return descDenominazione;
	}

	public void setDescDenominazione(String descDenominazione) {
		this.descDenominazione = descDenominazione;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getPiva() {
		return piva;
	}

	public void setPiva(String piva) {
		this.piva = piva;
	}

	public String getSelectedPanelBar() {
		return selectedPanelBar;
	}

	public void setSelectedPanelBar(String selectedPanel) {
		this.selectedPanelBar = selectedPanel;
	}

	public void panelBarChanged(ActionEvent evt) {
		selectedPanelBar = evt.getComponent().getParent().getId();
		getModel().setResetData();
	}
	
}
