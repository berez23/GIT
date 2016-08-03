package it.webred.fb.ejb.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DatoArricchito implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private  List<Alert> alerts =  new ArrayList<Alert>();
	private List<TabellaDatiCollegati> tabelleDatiCollegati = new LinkedList<TabellaDatiCollegati>();
	private boolean tabelleDatiCollegatiIsEmpty;
	
	public List<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}

	public void addAlert(String type, String description) {
		alerts.add(new Alert(type, description));
	}

	public void addTabellaDatiCollegati(TabellaDatiCollegati tabella) {
		tabelleDatiCollegati.add(tabella);
	}

	public void addAlert(List<Alert> alerts2) {
		for (Alert alert : alerts2)
			alerts.add(alert);
	}

	public TabellaDatiCollegati getTabellaDatiCollegati(String nome) {
		for (TabellaDatiCollegati t : tabelleDatiCollegati) {
			if (t.getNomeTabella().equals(nome))
				return t;
		}
		
		return null;
	}

	public List<TabellaDatiCollegati> getTabelleDatiCollegati() {
		return tabelleDatiCollegati;
	}

	public boolean isTabelleDatiCollegatiIsEmpty() {
		tabelleDatiCollegatiIsEmpty = false;
			for (TabellaDatiCollegati it : tabelleDatiCollegati) {
				if(it.getRighe().size() > 0){
					tabelleDatiCollegatiIsEmpty = true;
					return tabelleDatiCollegatiIsEmpty;
					}
			}
		
		return tabelleDatiCollegatiIsEmpty;
	}
	

}
