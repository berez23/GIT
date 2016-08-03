package it.webred.ct.service.cnc.web;

import java.io.Serializable;

public class MenuBean implements Serializable {

	public String doRicercaAnagVistati() {
		return "menu.listaAnagVistati";
	}
	
	public String doRicercaRuoliVistati() {
		return "menu.listaRuoliVistati";
	}
	
	public String doRicercaRiscossioni() {
		return "menu.listaRiscossioni";
	}
	
	public String doRicercaAnag290() {
		return "menu.listaAnag290";
	}
}
