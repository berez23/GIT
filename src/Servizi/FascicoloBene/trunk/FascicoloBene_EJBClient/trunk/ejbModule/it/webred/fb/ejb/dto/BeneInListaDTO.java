package it.webred.fb.ejb.dto;

import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmBIndirizzo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BeneInListaDTO implements Serializable {

	private DmBBene bene;
	private List<IndirizzoDTO> indirizzi;
	private List<String> mappali;
	private List<String> dirittiReali;
	
	public DmBBene getBene() {
		return bene;
	}
	public void setBene(DmBBene bene) {
		this.bene = bene;
	}
	public List<IndirizzoDTO> getIndirizzi() {
		return indirizzi;
	}
	public void setIndirizzi(List<IndirizzoDTO> indirizzi) {
		this.indirizzi = indirizzi;
	}
	public List<String> getMappali() {
		return mappali;
	}
	public void setMappali(List<String> mappali) {
		this.mappali = mappali;
	}
	public List<String> getDirittiReali() {
		return dirittiReali;
	}
	public void setDirittiReali(List<String> dirittiReali) {
		this.dirittiReali = dirittiReali;
	}
	
}
