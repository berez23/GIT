package it.webred.ct.data.access.basic.docfa.dto;

import it.webred.ct.data.access.basic.catasto.dto.CatastoBaseDTO;

import java.io.Serializable;
import java.util.Date;

public class RicercaOggettoDocfaDTO extends CatastoBaseDTO implements Serializable {
	
	private String sezione;
	private String foglio;
	private String particella;
	private String unimm;
	

	private Date fornitura;
	private String protocollo;
	private String nrProg;
	private String dataRegistrazione;
	
	public String getFoglio() {
		return foglio;
	}
	
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getUnimm() {
		return unimm;
	}

	public void setUnimm(String unimm) {
		this.unimm = unimm;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}	
	
	public Date getFornitura() {
		return fornitura;
	}

	public void setFornitura(Date fornitura) {
		this.fornitura = fornitura;
	}

	public String getProtocollo() {
		return protocollo;
	}

	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}

	public String getNrProg() {
		return nrProg;
	}

	public void setNrProg(String nrProg) {
		this.nrProg = nrProg;
	}

	public String getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(String dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}


}
