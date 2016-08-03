package it.bod.application.beans.switches;

import it.bod.application.common.MasterItem;


public class BodSegnalazioneSwitch extends MasterItem{

	private static final long serialVersionUID = -8158173654871698945L;
	
	private Boolean disabledButtonSave = false;
	private Boolean mostraLinkNuova = false;
	private Boolean mostraLinkAllegato = false;
	
	private Boolean mostraLinkAllegatoB = false;
	
	public Boolean getDisabledButtonSave() {
		return disabledButtonSave;
	}
	public void setDisabledButtonSave(Boolean disabledButtonSave) {
		this.disabledButtonSave = disabledButtonSave;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}	
	public Boolean getMostraLinkNuova() {
		return mostraLinkNuova;
	}
	public void setMostraLinkNuova(Boolean mostraLinkNuova) {
		this.mostraLinkNuova = mostraLinkNuova;
	}
	public Boolean getMostraLinkAllegato() {
		return mostraLinkAllegato;
	}
	public void setMostraLinkAllegato(Boolean mostraLinkAllegato) {
		this.mostraLinkAllegato = mostraLinkAllegato;
	}
	public Boolean getMostraLinkAllegatoB() {
		return mostraLinkAllegatoB;
	}
	public void setMostraLinkAllegatoB(Boolean mostraLinkAllegatoB) {
		this.mostraLinkAllegatoB = mostraLinkAllegatoB;
	}

}
