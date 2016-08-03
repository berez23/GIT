package it.webred.ct.service.spclass.web.bean;

import java.util.ArrayList;
import java.util.Arrays;

import javax.faces.model.SelectItem;

public class ClassamentoDueBean extends ClassamentoBean {

	private static final long serialVersionUID = 1L;

	public String initDue(){
		
		
		String esito = "classamentoDue";
		
		super.init();
		
		//setto la lista delle categorie edilizie
		super.listaCategorieEdilizie = Arrays.asList(
					new SelectItem("1", "Abitazioni Civili (A1,A8)"), 
					new SelectItem("2", "Abitazioni Civili (A2,A7)"),
					new SelectItem("3", "Abitazioni Economiche (A3)"));
			
		zonaDTO.setStato("Ottimo");
		zonaDTO.setCodiceCategoriaEdilizia(listaCategorieEdilizie.get(0).getValue().toString());
		
		getListaZC();
		getMicrozona();
			
		 numberOfRows=0;
		 
		 this.hideOutPanel();
		 
		// resetForm();
		
		return esito;
	}
	
	
	public String initTre(){
			
			String esito = "classamentoDue";
			
			super.init();
			
			//setto la lista delle categorie edilizie
			super.listaCategorieEdilizie = Arrays.asList(
					new SelectItem("1", "Abitazioni Civili (A1,A8)"), 
					new SelectItem("2", "Abitazioni Civili (A2,A7)"),
					new SelectItem("3", "Abitazioni Economiche (A3)"),
					new SelectItem("4", "Abitazioni Economiche (A4,A5)"));
			
			zonaDTO.setStato("Normale");
			zonaDTO.setCodiceCategoriaEdilizia(listaCategorieEdilizie.get(0).getValue().toString());
			
			
			getListaZC();
			getMicrozona();
			
			 numberOfRows=0;
			 
			this.hideOutPanel();
			 
		//	resetForm();
			
			return esito;
		}
		

	public void resetForm(){
		
		zonaDTO.setFoglio("");
		zonaDTO.setStato("");
		this.listaZC = new ArrayList<SelectItem>();

		zonaDTO.setCodiceCategoriaEdilizia(listaCategorieEdilizie.get(0).getValue().toString());
		
		zonaDTO.setNewMicrozona("");
		zonaDTO.setOldMicrozona("");
				
	}
	
	public void selectFoglio(){
		
		getListaZC();
		getMicrozona();
		this.hideOutPanel();
		
	}
	
	public void calcola(){
		
		this.showOutPanel();
		if("Ottimo".equalsIgnoreCase(zonaDTO.getStato()))
			this.datiAttesiDTO = calcoloValoriAttesiService.getCalcolo2(zonaDTO);
		else if("Normale".equalsIgnoreCase(zonaDTO.getStato()))
			this.datiAttesiDTO = calcoloValoriAttesiService.getCalcolo3(zonaDTO);
		
		this.numberOfRows = datiAttesiDTO.getClassamenti().size();
		
	}

}
