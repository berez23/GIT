package it.webred.ct.service.spprof.data.access.dto;

import it.webred.ct.service.spprof.data.model.SSpAreaPart;
import it.webred.ct.service.spprof.data.model.SSpCedificato;
import it.webred.ct.service.spprof.data.model.SSpEdificio;
import it.webred.ct.service.spprof.data.model.SSpEdificioMinore;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdi;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdiMin;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.List;

public class EdificioDTO extends CeTBaseObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SSpAreaPart particella;
	
	private SSpCedificato edificato;
	
	private SSpEdificio edificio;
	
	private SSpEdificioMinore edificioMinore;
	
	private SSpTipologiaEdi tipologia;
	
	private SSpTipologiaEdiMin tipologiaMin;
	
	private String destUrb;
	
	private String destUrbPrev;

	public SSpCedificato getEdificato() {
		return edificato;
	}

	public void setEdificato(SSpCedificato edificato) {
		this.edificato = edificato;
	}

	public SSpEdificio getEdificio() {
		return edificio;
	}

	public void setEdificio(SSpEdificio edificio) {
		this.edificio = edificio;
	}

	public SSpEdificioMinore getEdificioMinore() {
		return edificioMinore;
	}

	public void setEdificioMinore(SSpEdificioMinore edificioMinore) {
		this.edificioMinore = edificioMinore;
	}

	public SSpAreaPart getParticella() {
		return particella;
	}

	public void setParticella(SSpAreaPart particella) {
		this.particella = particella;
	}

	public SSpTipologiaEdi getTipologia() {
		return tipologia;
	}

	public void setTipologia(SSpTipologiaEdi tipologia) {
		this.tipologia = tipologia;
	}

	public SSpTipologiaEdiMin getTipologiaMin() {
		return tipologiaMin;
	}

	public void setTipologiaMin(SSpTipologiaEdiMin tipologiaMin) {
		this.tipologiaMin = tipologiaMin;
	}

	public String getDestUrb() {
		return destUrb;
	}

	public void setDestUrb(String destUrb) {
		this.destUrb = destUrb;
	}

	public String getDestUrbPrev() {
		return destUrbPrev;
	}

	public void setDestUrbPrev(String destUrbPrev) {
		this.destUrbPrev = destUrbPrev;
	}

}
