package it.webred.ct.service.spclass.web.bean;

import java.util.ArrayList;
import java.util.Arrays;

import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.model.SelectItem;

public class ClassamentoTreBean extends ClassamentoBean {

	private static final long serialVersionUID = 1L;
	
	private boolean abilitaPostoAuto = false;
	private String flgPostoAuto;
	
	
	public String init(){
		
		
		String esito = "classamentoTre";
		
		super.init();
		
		//setto la lista delle categorie edilizie
		super.listaCategorieEdilizie = Arrays.asList(
				new SelectItem("A10", "A10 - Uffici e studi privati"), 
				new SelectItem("C01", "C01 - Negozi e botteghe"),
				new SelectItem("C02", "C02 - Magazzini e locali di deposito"),
				new SelectItem("C03", "C03 - Laboratori per arti e mestieri"),
				new SelectItem("C06", "C06 - Stalle, scuderie, rimesse e autorimesse"));
	  
		zonaDTO.setCodiceCategoriaEdilizia(listaCategorieEdilizie.get(0).getValue().toString());
	
		this.setFlgPostoAuto(common.getListaSiNo().get(0).getValue().toString());
		abilitaPostoAuto=false;
		
		getListaZC();
		getMicrozona();
		
		datiAttesiDTO.setClasseMaxFrequente("0");
		
		this.hideOutPanel();
		//resetForm();
		
		return esito;
	}
	
	
	public void resetForm(){
			
		zonaDTO.setFoglio("");
		zonaDTO.setStato("");
		this.listaZC = new ArrayList<SelectItem>();
		
		zonaDTO.setCodiceCategoriaEdilizia(listaCategorieEdilizie.get(0).getValue().toString());
		
		zonaDTO.setNewMicrozona("");
		zonaDTO.setOldMicrozona("");
			
		this.setFlgPostoAuto(common.getListaSiNo().get(0).getValue().toString());
		
					
	}
	
	
	public void selectFoglio(){
		
		getListaZC();
		getMicrozona();
		
		this.hideOutPanel();
	}
	
	public void selectCategoriaEdilizia(){
		
		String catEdilizia= zonaDTO.getCodiceCategoriaEdilizia();
		
		//se abitazioni civili disabilito cbxAscensore
		if (catEdilizia != null && !catEdilizia.equals("C06"))
			this.abilitaPostoAuto=false;
		else if (catEdilizia != null && catEdilizia.equals("C06"))
			this.abilitaPostoAuto=true;
		
		this.hideOutPanel();
	}
	

	public void calcola(){
		
		this.showOutPanel();
		datiAttesiDTO = calcoloValoriAttesiService.getCalcolo4(zonaDTO);
	}
	
	public boolean isAbilitaPostoAuto() {
		return abilitaPostoAuto;
	}


	public void setAbilitaPostoAuto(boolean abilitaPostoAuto) {
		this.abilitaPostoAuto = abilitaPostoAuto;
	}


	public void setFlgPostoAuto(String flgPostoAuto) {
		this.flgPostoAuto = flgPostoAuto;
		zonaDTO.setFlgPostoAuto("SI".equalsIgnoreCase(flgPostoAuto)? true : false);	
	}


	public String getFlgPostoAuto() {
		return flgPostoAuto;
	}


}
